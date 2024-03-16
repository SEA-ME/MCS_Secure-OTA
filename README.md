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

Planning and Preparation (1 weeks)
System Architecture and Design (1 weeks)
Development and Integration (4 weeks)
Testing and Debugging (1 weeks)
Pilot / Proof of Concept Deployment (1 weeks)
Note that the timeline may vary depending on the complexity of the project, the availability of resources, and the expertise of the development team. However, the timeline provides a general idea of the phases and timeline involved in implementing a Secure OTA project.

<br>


## Self-Assessment
You can self-assess the security of your OTA update system using the following points as a guide.

__1) Ensure network security:__
- Implement TLS/SSL encryption for all data transmissions to protect against eavesdropping and man-in-the-middle attacks.
- Utilize VPN tunnels for secure communication channels between servers and vehicles.
Conduct regular network security assessments to identify and mitigate potential vulnerabilities.

__2) Mutual authentication between servers, new software, and vehicles:__
- Use a public key infrastructure (PKI) for strong mutual authentication to ensure that only trusted entities can participate in the OTA update process.
- Use digital signatures on software updates to verify the integrity and provenance of new software to ensure updates are genuine and untampered with.
- Use certificates and encryption keys that are securely stored on both the server and vehicle hardware for authentication purposes.

__3) Defense against rollback attacks:__
- Implement versioning checks to prevent the installation of outdated or previously used software versions, effectively blocking rollback attempts.
- Protect cryptographic hashes of software versions to ensure integrity and authenticity and prevent unauthorized downgrade attacks.

__4) Identify other attack scenarios:__
- Regularly update and patch OTA update systems to protect against known exploits and vulnerabilities.
- Implement an anomaly detection system to monitor for unusual behavior or unauthorized access attempts during the OTA update process.
- Conduct penetration testing and red team drills to identify and remediate potential attack vectors that you may not have previously considered.
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
2. Shin, Y., & Jeon, S. (2024). MQTree: Secure OTA Protocol Using MQTT and MerkleTree. Sensors, 24(5), 1447.
