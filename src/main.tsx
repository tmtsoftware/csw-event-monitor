import React from 'react'
import ReactDOM from 'react-dom'
import './index.css'
import App from './App'
import './index.css'
import {AuthContextProvider, setAppName} from "@tmtsoftware/esw-ts";
import {ThemeSwitcherProvider} from "react-css-theme-switcher";
import {AppConfig} from "./AppConfig";
import {EventMonitorSettings} from "./data/EventMonitorSettings";

setAppName(AppConfig.applicationName)

const themes = {
    dark: `/dark-theme.css`,
    light: `/light-theme.css`,
}

const darkMode = EventMonitorSettings.getDarkMode()

ReactDOM.render(
  <React.StrictMode>
      <AuthContextProvider config={AppConfig}>
          <ThemeSwitcherProvider
              themeMap={themes}
              defaultTheme={darkMode ? "dark" : "light"}
          >
              <App/>
          </ThemeSwitcherProvider>
      </AuthContextProvider>,
  </React.StrictMode>,
  document.getElementById('root')
)
