# zephyr-overlay

OpenVR overlay which displays notifications inside VR.

## Disclaimer
This portion of the project, for now, is a quick hack. Things work, but aren't polished.
I have minimal access to a Windows development environment and an OpenVR HMD for the 
next couple of months, so updates to the overlay will be few and far between.

## Installation
Download and install the [latest version of the desktop application](https://github.com/ThomasGaubert/zephyr/releases/latest).

Updates are delivered automatically and are applied after restarting Zephyr.
 
## Dependencies
This portion of the project depends on quite a few things which are not initially included in the repository.

### Qt
 * Download and install [Qt](https://www.qt.io/).
 * Setup the rest of the dependencies and launch Qt Creator.
 * After building the project, you'll need to use Qt's [Windows Deployment Tool](http://doc.qt.io/qt-5/windows-deployment.html#the-windows-deployment-tool) to generate the necessary dynamic libaries.
 
### Boost
 * The SocketIO C++ library requires [Boost](http://www.boost.org/), so follow the directions [here](https://github.com/socketio/socket.io-client-cpp/blob/master/INSTALL.md#boost_setup).
 * For reference, the "Without CMake" steps (in the aforementioned install guide) were followed. Use them as a guide to determine where to place the Boost header and library files.
 * Check out the Qt Creator [project file](https://github.com/ThomasGaubert/openvr-notifications/blob/master/overlay/src/openvr-notifications.pro) file for more hints regarding project structure.
