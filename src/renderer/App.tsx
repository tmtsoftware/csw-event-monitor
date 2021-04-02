import React, {useContext, useEffect, useState} from 'react'
import './App.css'
import {Topbar} from './components/Topbar'
import {Layout} from "antd"
import 'antd/dist/antd.css'
import {Sidebar} from "./components/Sidebar";
import {appContext, AppContextState} from './AppContext'

const {Content} = Layout

const App = (): JSX.Element => {

  function updateDisplay() {
    // XXX TODO
  }

  const appContextValues: AppContextState = {
    updateDisplay,
  }

    return (
      <appContext.Provider value={appContextValues}>
        <Layout className='App'>
          <Topbar/>
          <Layout>
            <Sidebar/>
            <Content>
              <div className='app-container'>
                <p>Hello</p>
              </div>
            </Content>
          </Layout>
        </Layout>
      </appContext.Provider>
    )
}
export default App
