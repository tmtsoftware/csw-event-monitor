import React from 'react'
import {render} from 'react-dom'
import './index.css'
import {AuthContextProvider} from "@tmtsoftware/esw-ts";
import {setAppConfigPath} from '@tmtsoftware/esw-ts'
import {AppConfig} from "./AppConfig";
import AppWrapper from "./AppWrapper";

setAppConfigPath('/dist/AppConfig.js')

render(
  <AuthContextProvider config={AppConfig}>
    <AppWrapper/>
  </AuthContextProvider>,
  document.getElementById('root'))
