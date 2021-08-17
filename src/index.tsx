import React from 'react'
import {render} from 'react-dom'
import './index.css'
import {AuthContextProvider} from "@tmtsoftware/esw-ts";
import {AppConfig} from "./AppConfig";
import LightApp from "./LightApp";
import DarkApp from "./DarkApp";

render(
  <AuthContextProvider config={AppConfig}>
    {/*<LightApp/>*/}
    <DarkApp/>
  </AuthContextProvider>,
  document.getElementById('root'))
