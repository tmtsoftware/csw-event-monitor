import React from 'react'
import {render} from 'react-dom'
import './index.css'
import {AuthContextProvider} from "@tmtsoftware/esw-ts";
import {setAppConfigPath} from '@tmtsoftware/esw-ts'
import {ThemeSwitcherProvider} from "react-css-theme-switcher";
import {AppConfig} from "./AppConfig";
import App from "./App";

setAppConfigPath('/dist/AppConfig.js')

const themes = {
  dark: `${import.meta.env.PUBLIC_URL}/dark-theme.css`,
  light: `${import.meta.env.PUBLIC_URL}/light-theme.css`,
};

render(
  <AuthContextProvider config={AppConfig}>
    <ThemeSwitcherProvider
      themeMap={themes}
      defaultTheme="dark"
      insertionPoint="styles-insertion-point"
    >
      <App/>
    </ThemeSwitcherProvider>
  </AuthContextProvider>,
  document.getElementById('root'))
