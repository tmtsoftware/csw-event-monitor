# CSW Event Monitor

This project contains the React/Typescript based CSW Event Monitor web application.
This version runs outside the browser as an [Electron app](https://www.electronjs.org/).

## Prerequisites Required for Running App

* A recent version of [Node.js](https://nodejs.org/en/download/package-manager/) must be installed (tested with node: 6.8.0, npm: 7.21.0).
* Run `csw-services start` (from [csw](https://github.com/tmtsoftware/csw), version 4.0.0-RC2 or newer)
* Run `esw-services start` (from [esw](https://github.com/tmtsoftware/esw), version 0.3.0-RC2 or newer)
* Run `icdwebserver` (from [icd](https://github.com/tmtsoftware/icd))

## Run the App in Local Environment

Run following commands in the terminal.
```
npm install
npm run dev
```

This should open the application in a dedicated window where you can select a subsystem and event to subscribe to.
Once the subwindows for the subscribed events are displayed, you can click the checkboxes for parameter values to display.
Parameter values are displayed in tables by default. Numerical values can also be plotted in a graph or bar chart.

## Build the App for Production

Run following commands in the terminal:
```
npm install
npm run release
```

The release files are then found under ./dist (`snap` for Linux, `dmg` for MacOS).

Notes:

* You can only build a release for the OS that you are building on.
* To install the generated `snap` on a Linux system, you currently need to pass the `--dangerous` option to `snap`, since it is not signed.

## References
- ESW-TS Library - [Link](https://tmtsoftware/esw-ts/)
- ESW-TS Library Documentation - [Link](https://tmtsoftware.github.io/esw-ts/)
