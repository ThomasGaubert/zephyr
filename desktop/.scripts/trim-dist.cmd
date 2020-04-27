:: Script to trim size of unpacked build output.
:: Removes extraneous contents not required to run Zephyr.
:: Expected to be run from `.scripts` directory.

@ECHO off
set WORKING_DIRECTORY=..\dist\win-unpacked\resources\app.asar.unpacked\node_modules\node-openvr

:: Check that working directory exists
IF NOT EXIST %WORKING_DIRECTORY% (
    echo Unable to find directory: %WORKING_DIRECTORY%
    exit /b 1
)

:: Naive check that script hasn't already run
IF NOT EXIST %WORKING_DIRECTORY%\overlay (
    echo Already trimmed!
    exit
)

echo Cleaning up...

:: Cleanup OpenVR directory
rmdir /s /q %WORKING_DIRECTORY%\lib\openvr\bin
rmdir /s /q %WORKING_DIRECTORY%\lib\openvr\controller_callouts
rmdir /s /q %WORKING_DIRECTORY%\lib\openvr\lib
rmdir /s /q %WORKING_DIRECTORY%\lib\openvr\samples
rmdir /s /q %WORKING_DIRECTORY%\lib\openvr\src
del %WORKING_DIRECTORY%\lib\openvr\CMakeLists.txt
del %WORKING_DIRECTORY%\lib\openvr\README.md
del %WORKING_DIRECTORY%\lib\openvr\Toolchain-clang.cmake

:: Remove other directories/files
rmdir /s /q %WORKING_DIRECTORY%\overlay
rmdir /s /q %WORKING_DIRECTORY%\samples
rmdir /s /q %WORKING_DIRECTORY%\src
rmdir /s /q %WORKING_DIRECTORY%\tools
del %WORKING_DIRECTORY%\.gitmodules

echo Done!