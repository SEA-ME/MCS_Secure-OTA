# **MCS Project 1 - Secure OTA**

- [**MCS Project 1 - Secure OTA**](#mcs-project-1---secure-ota)
  - [Introduction](#introduction)
  - [Project Goals and Objectives](#project-goals-and-objectives)
  - [System Architecture](#system-architecture)
  - [Project Timeline](#project-timeline)
  - [Self-Assessment](#self-assessment)
  - [Submission](#submission)
  - [References](#references)

## Introduction

The modern vehicle is rapidly transforming into a "driving computer," necessitating frequent updates to its software and firmware to manage and process data efficiently. Over-the-air (OTA) updates emerge as a pivotal technology in this context, enabling the wireless transmission and installation of new software or firmware directly from a server to the vehicle's system.

However, the OTA update process is susceptible to various cybersecurity threats, including man-in-the-middle attacks, firmware spoofing, and unauthorized access, which pose significant risks to vehicle safety and user privacy. It is imperative to implement robust security mechanisms to mitigate these vulnerabilities.

This project focuses on identifying potential security threats during the OTA process and devising defence mechanisms from multiple perspectives, including network, system, and application levels. Our approach involves the application of advanced encryption techniques to secure data transmission, the use of digital signatures for firmware authentication, the implementation of secure bootloading processes to prevent unauthorized executions, and the enforcement of access control systems to deter unauthorized access.

"In the contemporary automotive industry, software acts as the vehicle's heartbeat. The challenge of OTA updates is to maintain software currency while safeguarding vehicles against nascent cyber threats." This initiative is crucial for enhancing vehicle and user protection by ensuring the secure deployment of software updates.

By undertaking this ambitious project, we aim to investigate the OTA update process's capacity to safely upgrade vehicle software and firmware, thereby effectively countering all pertinent security threats. This endeavor will offer valuable insights for vehicle manufacturers, software developers, and security specialists, ultimately equipping the automotive industry to navigate future security challenges more adeptly.

<br>


## Project Goals and Objectives
This project outlines a comprehensive approach to implementing secure OTA updates, crucial for modern vehicles that rely heavily on software for their functionality. By wirelessly updating vehicle software and firmware, OTA technology offers a seamless method for enhancing vehicle capabilities and security. Here's an improved structure for the project, focusing on security details, a clear step-by-step approach, testing, and clarification of technical terms:

### 1) OTA Server

__Web Server Implementation:__
- A web server will be configured to manage new software updates through a notice board-like interface, enabling easy access and organization.
- The server will feature the ability to upload both full and differential images. Full image upload functionality is standard, with differential image upload as an advanced option to minimize data transmission.
- Advanced user authentication will be implemented to ensure that only authorized users can download updates, enhancing the security of the distribution process.

__MQTT Server Configuration:__
- Utilizing the MQTT protocol, the server will broadcast the latest software version and type. This ensures that all connected vehicles are aware of new updates in real-time.
- Security protocols for MQTT communication will be detailed, emphasizing encryption and secure messaging to protect against eavesdropping and unauthorized access.

### 2) Virtual Vehicle

__MQTT Client:__
- As an MQTT client, the virtual vehicle will incorporate features to receive new software information and download updates from the web server. This simulates real-world interaction with the OTA update system.
- The central gateway within the virtual vehicle will verify the integrity of received software, employing cryptographic techniques to ensure the authenticity and integrity of the updates.
- Optionally, the process to generate a full image from differential updates will be outlined, providing a method to reconstruct the latest software version efficiently.

__Target ECU Development:__
- The project will include the development of an Automotive Ethernet to CAN module, facilitating the communication necessary for OTA updates within the vehicle's network.
- A simple sensor module will be implemented on the Target ECU to verify the outcome of OTA updates, ensuring that the updated software functions as intended within the vehicle's ecosystem.
<br>


## System Architecture
The architecture outlined below centers around the OTA Server, Central Gateway, and Update Target, forming the core of OTA system. Presently, reference code is optimized for ECUs designated as Update Targets. However, it's important to note that our architecture is versatile and can be applied to any module within the vehicle, including but not limited to the Cluster and IVI systems. This flexibility is a key aspect of our design, ensuring broad applicability across various vehicle components.

<img width="1153" alt="image" src="https://github.com/SEA-ME/MCS_Secure-OTA/assets/163559668/6c0fdcd8-e649-4a2a-b1bb-bd396b1c1b7e">
<br>
<br>


## Project Timeline
A tentative project timeline for the Secure OTA project would include the following phases:

- Planning and Preparation (1 weeks)
- System Architecture and Design (1 weeks)
- Development and Integration (4 weeks)
- Testing and Debugging (1 weeks)
- Pilot / Proof of Concept Deployment (1 weeks)
* Note that the timeline may vary depending on the complexity of the project, the availability of resources, and the expertise of the development team. However, the timeline provides a general idea of the phases and timeline involved in implementing a Secure OTA project.

<br>


## Self-Assessment
You can self-assess the security of your OTA update system using the following points as a guide.

__0) Identifying exploit scenarios:__
* OTA Server (update files and hash files)
  - damage the server (it can be cause prevent update).
  - update malwares (it can crash updating ECU or make problem when driving).
  - delete or crash update files or hashes (it can be cause prevent update).
  - hacker access or Steal privileges (hackers can delete files or update malware).
* Network:
  - sniffing download
  - Dos
  - sniffing private id or address in network
  - middle-in-the-man attack
* Central Gateway in Vehicle
	- Dos
	- pretend to be an official OTA server
	- rollback attack
* Target ECU in Vehicle
	- update malware
	- prevented essential update

__1) Ensure network security:__
- Implement TLS/SSL encryption for all data transmissions to protect against eavesdropping and man-in-the-middle attacks. [Level 3]
  - How much do you understand about TLS/SSL encryption? (1~5)
  - What TLS/SSL version did you apply for data transmission security?
    - [TSL 1.3 should be used for full protection against eavesdropping and man-in-the-middle attacks]
  - What is your cipher suit (write all)?
    - [ECDHE, RSA or ECDSA (only 1.2), up AES 256 and except ECB, up SHA-2]
  - How do you manage your data transmissions secure session ID?
    - [To deny untrusted entities from participating in the OTA update process with a trusted entity session ID, you must protect this ID.]

- Utilize VPN tunnels for secure communication channels between servers and vehicles.
Conduct regular network security assessments to identify and mitigate potential vulnerabilities. [Level 3]
  -	How much do you understand about VPN tunnels? (1~5)
  -	What protocol did you use for VPN tunnels for secure communication channels?
    - [should not use PPTP protocol]
  -	What encryption algorithm did you use for VPN tunnels? And is it appropriate for the OTA update process?
    - [should not use Ms-CHAPv2, and a long key bit makes communication slow]
  -	How often would you evaluate security and vulnerabilities? And is it appropriate?
    - [More evaluation increases the price, less makes vulnerability too dangerous]
  - What are checklist items for evaluation? And why did you choose them?

__2) Mutual authentication between servers, new software, and vehicles:__
- Use a public key infrastructure (PKI) for strong mutual authentication to ensure that only trusted entities can participate in the OTA update process. [Level 1]
  -	How much do you understand about PKI? (1~5)
  -	What PKI algorithm did you use?
    - [RSA, or ElGamal, Rabin, ECC]
  - Why did you choose that algorithm? What are the advantages of the algorithm?
    - [ex. RSA is a popular and strong algorithm in the world]
  - How long is the PKI key bit length? Is that appropriate?
    - [If you plan to use RSA encryption before the year 2030, make sure to choose a bit length that is longer than 2,048. This will help ensure the security of your data.]
  -	Does it verify the source of the public key?
    - [If the source cannot be verified, it may be vulnerable to a man-in-the-middle attack on the public key]
  -	Does it have any other authentication?
    - [authentication with Bluetooth devices or password encryption]

- Use digital signatures on software updates to verify the integrity and provenance of new software to ensure updates are genuine and untampered with. [Level 2]
  -	How much do you understand about digital signatures? (1~5)
  -	Is there any other integrity verification procedure?
    - [HASH algorithm should be used for check integrity]

- Use certificates and encryption keys that are securely stored on both the server and vehicle hardware for authentication purposes. [Level 2]
  -	How much do you understand about certificates with PKI? (1~5)
  -	How do you manage the certificate and encryption key? Is that enough for security? Why?
    - [if you don`t manage the certificate and encryption key, malicious hackers can disguise a trusted entity and download new software]

- Protect cryptographic hashes of software versions to ensure integrity and authenticity and prevent unauthorized downgrade attacks. [Level 3]
  -	Do you understand why HASH sever needs protection? (1~5)
  -	Does it protect administrator access?
    - [hackers can attack when an administrator accesses a hash server and still access]
  -	Does it have enough protection for hash sever safety?
    - [when it doesn`t have enough protection, hackers can update their firmware hash, so the client can`t check its authenticity when hackers update malware to the client]

__3) Defense against rollback attacks:__
- Implement versioning checks to prevent the installation of outdated or previously used software versions, effectively blocking rollback attempts. [Level 2]
  -	Do you understand why rollback attempts are dangerous? (1~5)
  -	Can it compare which version is the latest?
    - [Basic security from rollback attempts is to check the version which on is the latest]
  -	Can it determine whether it is an official downgrade or a rollback attempt?
    - [If you notice a major issue with new software, you will need to officially downgrade it, so if it's not classified, a client can reject the official downgrade]
  -	How do you manage noticing crucial problem version?
    - [Delete or deny accessing about problem version.]
  -	Protect cryptographic hashes of software versions to ensure integrity and authenticity and prevent unauthorized downgrade attacks.
    - [for example day can only download an official update hash file]

- Protect cryptographic hashes of software versions to ensure integrity and authenticity and prevent unauthorized downgrade attacks.

__4) Identify other attack scenarios:__
- Regularly update and patch OTA update systems to protect against known exploits and vulnerabilities. [Level 4]
  -	How often would you update to protect against known exploits and vulnerabilities?
    - [too many updates increase the price, but sometimes updates make vulnerability too dangerous]
  -	What will you do when a one-day attack that you can`t solve problem notice in OTA process?
    - [change sub protect process or add more authentication]
  -	What will you do when a zero-day attack notices?
    - [deactivate update process, or another way]

- Implement an anomaly detection system to monitor for unusual behavior or unauthorized access attempts during the OTA update process. [Level 4]
  -	Do you understand anomaly detection systems? (1~5)
  -	Does it detect hacker's intrusion?
    - [Intrusion is a highly dangerous activity as it can install malware on the server or even cause critical damage to it.]
  -	Does it check the integrity of the OTA update?
    - [hackers can make errors or vulnerabilities with a broken software update]

- Conduct penetration testing and red team drills to identify and remediate potential attack vectors that you may not have previously considered. [Level 5]
  -	Do you have enough scenarios for testing? 
    - [how many kinds of attack tools, how long, when]
  -	Does it detect anomalies when you test?
    - [if it doesn`t you must update your protection system]
  -	What will you do when it detects or protects failed?
    - [it`s very important for managing servers when you have problems. Please write your plan]
  -	What is the F1 score for the anomaly detection system? Do you think it is enough?

- By focusing on these security checkpoints, your OTA update system can provide robust protection against a wide range of cybersecurity threats, ensuring the integrity and security of your vehicle software updates.
<br>


## Submission
Submit the following artifacts to GitHub:

__1. Documentations:__
- Entire system architecture, data structures, and algorithms used.
- A comprehensive report detailing the methodology, challenges faced, and solutions implemented.
  
__2. Proof of Concept:__
- The source code.
- Test Cases demonstrating the reliability and accuracy of the system.
  
__3. Presentation:__
- A presentation summarizing the project, including an overview of the system architecture, technical specifications, user interface, and test results.
<br>


## References
Here are some open source references that could be useful in developing a Secure OTA project:
1. MQTT (2022), MQTT, https://mqtt.org/
2. Merkle Tree, Wikipedia, https://en.wikipedia.org/wiki/Merkle_tree
3. Shin, Y., & Jeon, S. (2024). MQTree: Secure OTA Protocol Using MQTT and MerkleTree. Sensors, 24(5), 1447.
