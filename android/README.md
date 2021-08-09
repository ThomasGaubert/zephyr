# zephyr-android

[![Build Status](https://thomasgaubert.visualstudio.com/Zephyr/_apis/build/status/Android%20CI?branchName=master)](https://thomasgaubert.visualstudio.com/Zephyr/_build/latest?definitionId=1&branchName=master)

Android application which delivers notifications to the Zephyr desktop client.

## Installation
There are several ways to install Zephyr:

### Play Store
Zephyr is coming soon to the Play Store.

As you'd expect, updates are delivered through the Play Store.

### Build from source
See *Building* below for how to build Zephyr.

## Building
The latest stable version of Android Studio is recommended. Before you can build, you'll need to do a few more setup items.

### Firebase
Zephyr uses Firebase, so you need to create a new Firebase project for local development. 
Once you've created the project, get the `google-services.json` file associated with that project and put it in the `app` directory.
See [here](https://support.google.com/firebase/answer/7015592?hl=en) for more info.

**Note:** Zephyr uses a different package name for the dev build variant, so you'll want to make sure you have both package names registered to your Firebase project.

### API keys
Copy the `example.properties` file and rename it to `private.properties`. You'll need API keys for the following:

- `APP_CENTER_SECRET` - [AppCenter](https://appcenter.ms) API key for beta distribution.

### Code analysis
Zephyr uses several code analysis tools to maintain code quality. The tools include `checkstyle` and `pmd`.

You can run any of these tools in the terminal using `gradlew <tool-name>`.

Please see each tool's respective website for setup instructions.