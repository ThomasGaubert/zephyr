# Changelog

## 2.0.0 - WIP
Zephyr 2.0.0 represents a complete rewrite of all aspects of the project. Everything from the pairing process to displaying notifications within VR has been updated to ensure a polished experience.

### Android

#### Added
- **Pairing** - new ways to quickly pair with the Zephyr deskop client.
    - Join Code - short codes replace the need to type an entire IP address
    - QR code - scan a QR code instead of entering your computer's IP address
    - Discovery - computers running Zephyr are automatically listed in pairing menu
- **Notification settings**
    - Added the ability to search for apps.
- **Privacy**
    - Added privacy menu to review and expose privacy settings.
- **Other**
    - Added support for foldable devices like Surface Duo.

#### Changed
- Updated UI to conform with Material Design 2.
- Updated to latest Android SDK.

#### Removed
- Cloud syncing of Zephyr server IP address has been removed in favor of new pairing features.
- Removed Smart Connect and start on boot.

#### Fixed
- Improved connection reliability.

### Desktop

#### Added
- **Steam**
    - Zephyr is now distributed on Steam.
    - Deep integration with SteamVR, including auto-launch.
- **Notifications**
    - Notifications now include icons.
    - Review recent notifications in VR or via the desktop app.
- **Pairing** - new ways to quickly pair with the Zephyr deskop client.
    - Join Code - short codes replace the need to type an entire IP address
    - QR code - scan a QR code instead of entering your computer's IP address
    - Discovery - computers running Zephyr are automatically listed in pairing menu

#### Changed
- Updated UI to conform with Material Design 2 and Windows 10.
- API has been completely redesigned.
- Overlay is now tightly coupled with the desktop client.

#### Removed
- Cloud syncing of Zephyr server IP address has been removed in favor of new pairing features.
- Standalone (non-Steam) builds are currently not provided.
- Auto updates outside of Steam have been removed and are currently not supported.
- Telemetry.

#### Fixed
- Improved overall stability and reliability of desktop client and overlay.

## 1.1.5 - 2016-10-07

<details>
  <summary>Click to display</summary>

  ## Desktop
  #### Added
  - Support for CORS for APIs

  #### Fixed
  - Issue where VirtualBox would result in the wrong IP address being displayed.
</details>

## 1.1.4 - 2016-08-17

<details>
  <summary>Click to display</summary>
  
  ## Desktop
  #### Updated
  - Updated overlay with Zephyr branding! Future releases will further improve the overlay.

  #### Fixed
  - API inconsistency.
</details>

## 1.1.3 - 2016-07-16

<details>
  <summary>Click to display</summary>
  
  ## Desktop
  #### Changed
  - Quit confirmation to show before windows are closed
</details>

## 1.1.2 - 2016-07-16

<details>
  <summary>Click to display</summary>
  
  ## Desktop
  #### Added
  - "Close to tray" button
  - Confirmation dialog when quitting

  #### Changed
  - Restored original close button behavior

  #### Fixed
  - Prevent multiple instances of Zephyr (to avoid nasty "address in use" errors)
  - Increased reliability of showing changelogs
</details>

## 1.1.1 - 2016-07-4

<details>
  <summary>Click to display</summary>

  ## Desktop
  #### Fixed
  - Bug where server address wouldn't sync
</details>

## 1.1.0 - 2016-07-4

<details>
  <summary>Click to display</summary>

  ## Desktop
  #### Added
  - Login to instantly sync devices (account and Zephyr Android 1.1.0 required)
  - Ability to minimize to tray

  #### Changed
  - Improved error handling and metrics
</details>

## 1.0.3 - 2016-06-28

<details>
  <summary>Click to display</summary>

  ## Desktop
  #### Added
  - Ability to view update changelogs
  - Mixpanel analytics
</details>

## 1.0.2 - 2016-06-27

<details>
  <summary>Click to display</summary>

  ## Desktop
  #### Added
  - Logging

  #### Changed
  - Improved error reporting if the Zephyr overlay encounters an issue
</details>

## 1.0.1 - 2016-06-24

<details>
  <summary>Click to display</summary>

  ## Desktop
  #### Fixed
  - Included Visual Studio 2015 redistributables to resolve issue where overlay wouldn't start
</details>

## 1.0.0 - 2016-06-19

<details>
  <summary>Click to display</summary>

  ## Desktop
  #### Added
  - First stable release of Zephyr
</details>