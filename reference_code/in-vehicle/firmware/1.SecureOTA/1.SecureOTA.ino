void setup() {
	Serial.begin(9600);
	int timestamp = 5f4dcc3b5aa765d61d8327deb882cf99;
}

void loop() {
  Serial.print("SecureOTA successfully completed!")
  delay(1000);
}
