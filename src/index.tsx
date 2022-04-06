import React from 'react'
import {render} from 'react-dom'
import './index.css'
import {AuthContextProvider, setAppName} from "@tmtsoftware/esw-ts";
import {ThemeSwitcherProvider} from "react-css-theme-switcher";
import {AppConfig} from "./AppConfig";
import App from "./App";
import {EventMonitorSettings} from "./data/EventMonitorSettings";

setAppName(AppConfig.applicationName)

const themes = {
  // dark: `${import.meta.env.PUBLIC_URL}/dark-theme.css`,
  // light: `${import.meta.env.PUBLIC_URL}/light-theme.css`,
  dark: `/dark-theme.css`,
  light: `/light-theme.css`,
}

const darkMode = EventMonitorSettings.getDarkMode()

render(
  <AuthContextProvider config={AppConfig}>
    <ThemeSwitcherProvider
      themeMap={themes}
      defaultTheme={darkMode ? "dark" : "light"}
    >
      <App/>
    </ThemeSwitcherProvider>
  </AuthContextProvider>,
  document.getElementById('root'))
