# zephyr

[![Build status](https://ci.appveyor.com/api/projects/status/n8cwsdbepynkqe4i?svg=true)](https://ci.appveyor.com/project/ThomasGaubert/openvr-notifications)

Mirror Android notifications to VR.

[<img alt="Get it on Google Play" height="45px" src="https://play.google.com/intl/en_us/badges/images/generic/en_badge_web_generic.png" />](https://play.google.com/store/apps/details?id=com.texasgamer.zephyr)
 
## Disclaimer
This project, for now, is a quick hack. Things work, but aren't polished.
I will have minimal (if any) access to a Windows development environment 
and an OpenVR HMD for the next couple of months, so certain updates 
(especially relating to the overlay) will be few and far between.

## Motivation
This project was created out of a desire to learn more about OpenVR's SDK
and to address a glaring omission from the Vive's phone service. Currently
the Vive will display incoming texts, calls, and calendar events over Bluetooth
but does not mirror other notifications (such as those from other chat apps).

As an added bonus, the project is open source and works with all OpenVR HMDs!

## Usage
This project is still under development, but works in its current state (check out
the disclaimer above). To get things up and running:

 1. Download and install SteamVR
 2. Connect and setup your OpenVR HMD (Vive, Oculus Rift, etc.)
 3. Clone this repo
 4. Run `npm run dev` within the `desktop` directory
 5. Download the Android app from the [Play Store](https://play.google.com/store/apps/details?id=com.texasgamer.zephyr)
 6. Follow directions within the desktop application to connect the app

Expect this process to be streamlined in the future.

*More documentation coming soon...*
