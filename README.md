![Banner](https://raw.githubusercontent.com/TebbeUbben/Telesto/master/Banner.png)

# Telesto
Telesto is an open source application for Android to remotely control an Accu-Chek Insight insulin pump. Keep in mind that this is still in an early state of development and should not be used on a productive system.

## What is working?
 - Pairing
 - Monitoring pump status
	 - Cartridge
	 - Battery
	 - Operating Mode
	 - Active basal rate
	 - Active boluses
	 - Active TBR
 - Delivering boluses
 - Setting TBRs
 - Changing operating mode
 - Receive and dismiss pump alerts
 - (Reading history: Records are read from the pump and saved into a database, but there is no UI yet.)

## Support
There is no commercial support available - all the work is done by volunteers! However, if you need help, feel free to submit an issue or join our [Gitter chat](https://gitter.im/InsightINVESTIGATIONS/Lobby).

## Roadmap
There are still a lot of things to do:
 - Integration for third-party applications like AndroidAPS
 - Displaying total daily dose
 - UI for showing history records
 - Editing basal profiles
 - Smart cartridge reminders which will connect to the pump at a specified time and check if there is enough insulin left in the cartridge for x hours of basal + puffer
 - Automatically connect to the pump in regular intervals to synchronize pump status and history
 - Improving stability
 - Improving UI
 - Challenges to confirm actions: Clicking buttons in the right order, PIN, fingerprint

## Disclaimer
This is not even alpha software, so use it at your own risk. No one, but you, is responsible for any damages on your phone, pump, health or whatever gets damaged if you use this software. There is no warranty that using this software is save or beneficial.
Neither this project, me or anybody who has worked on it is affiliated with or endorsed by Roche or any of its national or international subsidiaries.