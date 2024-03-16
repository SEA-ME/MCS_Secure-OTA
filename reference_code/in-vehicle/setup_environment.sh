#!/bin/bash

# Upgrade pip to its latest version
echo "Upgrading pip..."
python3 -m pip install --upgrade pip

# Install necessary Python packages from requirements.txt
echo "Installing required Python packages from requirements.txt..."
pip3 install -r requirements.txt

# Check if Arduino CLI is already installed
if ! command -v arduino-cli &> /dev/null
then
    echo "Arduino CLI not found. Installing..."
    # Install Arduino CLI
    curl -fsSL https://raw.githubusercontent.com/arduino/arduino-cli/master/install.sh | sh
else
    echo "Arduino CLI is already installed."
fi

# Check if 1.SecureOTA sketch directory already exists
if [ ! -d "1.SecureOTA" ]; then
    echo "Creating new sketch '1.SecureOTA'..."
    arduino-cli sketch new 1.SecureOTA
else
    echo "Sketch '1.SecureOTA' already exists."
fi

echo "Setup completed successfully."
