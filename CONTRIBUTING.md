# Contributing
Contributions are gladly accepted for all components of Zephyr!
This document covers guidelines to follow when contributing to the project.

## Getting started
1. [Create a GitHub account](https://github.com/join) if you don't already have one.
2. [Fork Zephyr](https://github.com/ThomasGaubert/zephyr/fork)
3. [Create a new branch](https://help.github.com/en/articles/creating-and-deleting-branches-within-your-repository) for your contribution
4. [Create a pull request](https://github.com/ThomasGaubert/zephyr/compare)

Be sure to fully describe your changes in your pull request.
If your pull request happens to address a GitHub issue, please mention this too.

## Guidelines
This list isn't exhaustive, but covers some basic things you should keep in mind.

* Keep commit messages simple
* Keep pull requests focused on one feature or bug fix
* Try to conform with the existing code style and architecture
* Ask questions if you have any

## Project structure
Zephyr is a monorepo containing both the desktop and Android clients.
The README files within each client's directories contains a brief overview of the respective project and detail steps to get the project up and running.

## Build pipelines
Zephyr utilizes [Azure DevOps](https://dev.azure.com/thomasgaubert/Zephyr) for continuous integration and delivery.

New builds are spawned when creating a pull request and when a change is added to the master branch.
Only the appropriate builds for a given commit or PR will run (for example, Android changes will only run the Android build).
In general, you can expect the builds to do the following:

- Perform static analysis, linting, etc.
- Build the necessary components
- Run tests
- Publish reports of the above items

Builds from the master branch will kick off the release pipeline for the given component.

## Licensing
Zephyr is [licensed](https://github.com/ThomasGaubert/zephyr/blob/master/LICENSE) under the MIT License. 
By contributing to Zephyr, you certify and agree to the following:

* Your contributions are either created solely by you or are properly licensed to be included in the Zephyr project.
* You release your contributions to the project under the MIT License.

## Additional resources
* [Socket.IO](https://socket.io)
* [Android developer resources](https://developer.android.com/develop/index.html)
* [Electron](https://electronjs.org)
* [React](https://reactjs.org)
* More questions? Don't hesitate to [create an issue](https://github.com/ThomasGaubert/zephyr/issues/new).