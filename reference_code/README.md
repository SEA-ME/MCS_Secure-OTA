# Reference Code from MQTree
MQTree: Secure OTA Protocol using MQTT and MerkleTree

- [**Reference Code from MQTree**](#reference_code_from_mqtree)
  - [Install Mosquitto](#install_mosquitto)
  - [Arduino CLI Setup](#arduino_cli_setup)
  - [Clone Project and Navigate](#clone_project_and_navigate)
  - [Start](#start)
  
## Install Mosquitto
```bash
sudo apt-get update
sudo apt-get install mosquitto
sudo apt-get install mosquitto-clients
sudo systemctl start mosquitto
```
<br>


## Arduino CLI Setup
**Start by downloading and installing the latest version of Arduino CLI with the following command:**
```bash
curl -fsSL https://raw.githubusercontent.com/arduino/arduino-cli/master/install.sh | sh
```

**After installation, add Arduino CLI to your system's path to make it accessible from any terminal session:**
```bash
cd bin/
sudo cp arduino-cli /usr/local/bin/
```

**Verify Installation: To ensure that Arduino CLI is installed correctly, run the following command:**
```bash
arduino-cli version
```

**Initialization: Initialize Arduino CLI to create a default configuration file. This step is essential for first-time setup:**
```bash
arduino-cli config init
```

**Install the necessary core to use common boards like Uno or Mega:**
```bash
arduino-cli core install arduino:avr
```

**This command updates the list of available boards and cores:**
```bash
arduino-cli core update-index
```

**For projects that require the megaAVR architecture (e.g., "Arduino Uno WiFi Rev2"), install the megaAVR core:**
```bash
arduino-cli core install arduino:megaavr
```

**Verify that the core installation was successful:**
```bash
arduino-cli core list
```
<br>


## Clone Project and Navigate

**Clone your project repository and navigate to the relevant directory:**
```bash
git clone https://github.com/SYunje/MQTree.git
cd ./MQTree/InVehicle
```

**Give execution permissions to your environment setup script and run it:**
```bash
chmod +x setup_environment.sh
./setup_environment.sh
```

**If you encounter errors, it might be due to Windows line endings. Correct this issue by running:**
```bash
sed -i 's/\r$//' setup_environment.sh
```
<br>


## Start
```bash
python3 MQTree.py
```
