import paho.mqtt.client as mqtt
import hashlib
import json
import requests
import re
import os
import time
import subprocess
import shutil


class BinaryHashTree:
    def __init__(self):
        self.root_hash = None

    def compute_file_hash(self, file_path):
        sha256_hash = hashlib.sha256()
        with open(file_path, "rb") as file:
            for chunk in iter(lambda: file.read(4096), b""):
                sha256_hash.update(chunk)
        return sha256_hash.digest()

    def build_tree(self, directory_path):
        ino_files = self.get_ino_files(directory_path)
        data = [self.compute_file_hash(file) for file in ino_files]
        self.root_hash = self.build_binary_hash_tree(data)

    def get_ino_files(self, directory_path):
        ino_files = []
        for root, dirs, files in os.walk(directory_path):
            for file in files:
                if file.lower().endswith(".ino"):
                    ino_files.append(os.path.join(root, file))
        return ino_files

    def build_binary_hash_tree(self, data):
        if len(data) == 0:
            return None
        if len(data) == 1:
            return data[0]
        mid = len(data) // 2
        left_data = data[:mid]
        right_data = data[mid:]
        left_hash = self.build_binary_hash_tree(left_data)
        right_hash = self.build_binary_hash_tree(right_data)
        return hashlib.sha256(left_hash + right_hash).digest()

    def get_root_hash(self):
        return self.root_hash


# Set up the MQTT client callback function
def on_connect(client, userdata, flags, rc):
    print("Connected with result code " + str(rc))
    client.subscribe(subTopic)  # Subscribe to a topic


message_count = 0


def get_next_firmware_filename(directory):
    # Get a list of all files in a directory.
    files = os.listdir(directory)
    # Regular expression to extract numbers from file names
    pattern = re.compile(r'^(\d+). SecureOTA.ino$')
    max_number = 0
    for filename in files:
        match = pattern.match(filename)
        if match:
            number = int(match.group(1))
            if number > max_number:
                max_number = number
    # Return the next file number
    return f"{max_number + 1}.SecureOTA.ino"


def compute_file_hash(file_path):
    sha256_hash = hashlib.sha256()
    with open(file_path, "rb") as file:
        for chunk in iter(lambda: file.read(4096), b""):
            sha256_hash.update(chunk)
    return sha256_hash.hexdigest()


# this is the Main part
def on_message(client, userdata, msg):
    global message_count, root_hash  # Use root_hash as a global variable
    payload = json.loads(msg.payload)  # Message json payload received from MQTT
    firmwareURL = payload['URL']
    firmwareLocation = f"http://{brokerIp}{firmwareURL}"
    print(payload)
    print('=======================================================')
    print()

    # Compare motor versions to see whether an update is needed or not
    if float(payload['Version']) > moterVersion:
        # Ask the user if they want to proceed with the update.
        user_input = input("New firmware version available. Do you want to update? (Y/N): ")
        if user_input.lower() in ["y", "yes"]:
            print("Proceeding with firmware update...")
            time.sleep(1)
            # Firmware update logic
            try:
                response = requests.get(firmwareLocation)
                response.raise_for_status()  # Raise exception in case of error

                # Save the downloaded content to a file
                firmwareDirectory = './Firmware/'
                firmwareFilename = get_next_firmware_filename(firmwareDirectory)
                firmwarePath = os.path.join(firmwareDirectory, firmwareFilename)
                with open(firmwarePath, 'wb') as file:
                    file.write(response.content)

                time.sleep(1)
                print(f"Firmware downloaded and saved to {firmwarePath}")

                # Calculate hash after file is successfully saved
                downloaded_file_hash = compute_file_hash(firmwarePath)

                if payload["FileHash"] == downloaded_file_hash:
                    time.sleep(1)
                    print("FileHash match. Firmware is verified.")
                    print('=======================================================')
                    print()


                else:
                    print("FileHash do not match. Firmware might be tampered or updated.")

                with open(firmwarePath, 'wb') as file:
                    file.write(response.content)

                time.sleep(1)
                print(f"Firmware downloaded and saved to {firmwarePath}")
                print("Save and calculate root hash")
                print('=======================================================')
                print()

                # Rebuild binary hash tree and calculate root hash value
                bht = BinaryHashTree()
                bht.build_tree('./Firmware')
                root_hash = bht.get_root_hash().hex()
                time.sleep(1)
                print("New Root Hash:", root_hash)
                print('=======================================================')
                print()

                # Compare the root hash with the received hash
                if payload["RootHash"] == root_hash:
                    time.sleep(1)
                    print("RootHashes match. Firmware is verified.")
                    print('=======================================================')
                    print()
                    ##Now we just need to apply the Arduino CLI.##
                    # Execute command to get the list of connected boards

                    # Move to the arduino sketch directory
                    target_directory = './1.SecureOTA/'
                    target_path = os.path.join(target_directory, os.path.basename(firmwarePath))
                    shutil.move(firmwarePath, target_path)

                    print(f"File moved to {target_path}")

                    # Get the list of connected boards
                    result = subprocess.run(['arduino-cli', 'board', 'list'], stdout=subprocess.PIPE, text=True)
                    board_list_output = result.stdout

                    # regular expression to extract the exact port and FQBN of the first board
                    pattern = re.compile(
                        r'(\/dev\/ttyACM0)\s+serial\s+.*Arduino Mega or Mega 2560\s+(arduino:avr:mega)\s+arduino:avr')

                    # Use a regular expression to find the desired port and FQBN in the output
                    match = pattern.search(board_list_output)
                    if match:
                        port = match.group(1)  # Extract '/dev/ttyACM0'
                        fqbn = match.group(2)  # Extract 'arduino:avr:mega'
                        print(f"Found Arduino board at port: {port} with FQBN: {fqbn}")
                    else:
                        print("No matching Arduino board found.")
                        exit(1)

                    # Create a directory already sketched from setup_environment with the same name
                    sketch_path = "1.SecureOTA"

                    # Run the compile command
                    compile_command = ['sudo', 'arduino-cli', 'compile', '--fqbn', fqbn, sketch_path]
                    subprocess.run(compile_command)

                    # Execute the upload command
                    upload_command = ['sudo', 'arduino-cli', 'upload', '-p', port, '--fqbn', fqbn, sketch_path]
                    subprocess.run(upload_command)


                else:
                    print("RootHashes do not match. Firmware might be tampered or updated.")
            except requests.RequestException as e:
                print(f"Error downloading the firmware: {e}")

        else:
            print("Firmware update canceled.")
            return
    else:
        print("No new firmware update required.")


# Create and configure MQTT client instance
userId = YOUR_ID
userPw = YOUR_PW

client = mqtt.Client()
client.username_pw_set(userId, userPw)
client.on_connect = on_connect
client.on_message = on_message

# Connect to the MQTT broker
subTopic = YOUR_Topic
brokerIp = YOUR_IP
port = YOUR_PORT
client.connect(brokerIp, port, 60)

global moterVersion
moterVersion = 0.7

global lightVersion
lightVersion = 0.7

# Start the network loop
client.loop_forever()
