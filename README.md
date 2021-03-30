# CSW Event Monitor

This subproject contains the React/Typescript based CSW Event Monitor web application.
This version runs outside the browser as an [Electron app](https://www.electronjs.org/).

## Prerequisites Required for Running App

* The latest version of [Node.js](https://nodejs.org/en/download/package-manager/) must be installed.
* csw-services  (including the Database Service) and esw-services should be running

## Run the App in Local Environment

(Note: `npm` or `yarn` can be used in the following commands, but should not be mixed.)

Run following commands in the terminal.
```
npm install
npm run dev
```

This should open the application in a dedicated window.

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
