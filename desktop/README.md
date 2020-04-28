# zephyr-desktop

[![Build Status](https://thomasgaubert.visualstudio.com/Zephyr/_apis/build/status/Desktop%20CI?branchName=master)](https://thomasgaubert.visualstudio.com/Zephyr/_build/latest?definitionId=3&branchName=master)

Desktop application built on [Electron](https://electronjs.org/) which provides a user interface, runs a server instance, and renders the OpenVR overlay.

## Installation
There are several ways to install Zephyr:

### Steam
Zephyr is coming soon to Steam.

Updates are delivered through the Steam client.

### Build from source
See *Building* below for how to build Zephyr.

## Building
[Yarn](https://yarnpkg.com) is the recommended package manager for Zephyr. These steps also assume you're using a 64-bit installation of Windows 10 and NodeJS 10.9.0.

Zephyr uses git submodules for the [node-openvr](https://github.com/ZephyrVR/node-openvr) depdenceny. Be sure to clone with submodules or run the following post-clone:

```
git submodule update --init --recursive
```

To get all dependencies, just run:

```
yarn
```

To build and run Zephyr:

```
yarn start
```

Enable hot reloading for rapid iteration (run each command in a separate terminal):
```
yarn start
yarn watch
```

### Build types
There are three build types:

* dev - default when building from source, enables dev tools
* prod - standalone release for GitHub, enables auto-updates, etc.
* steam - release for Steam

There are variants of the `build` and `dist` scripts for each build type. See `package.json` for the full list of scripts.

### Code analysis
Yarn will run a linter when building with `yarn start`. You can fix issues manually or use `yarn lintautofix` to automatically fix common errors.

### OpenVR bindings
Zephyr uses a fork of [node-openvr](https://github.com/ZephyrVR/node-openvr) to interact with OpenVR. When editing the bindings, run `yarn update-bindings` to ensure the latest changes are used by the desktop client.

Note that these bindings are in a submodule, so any changes to the bindings need to be checked into that project's repository first.
