import React, {useState} from 'react'

import 'antd/dist/antd.dark.css';
// import 'antd/dist/antd.css'
import './App.css'

import {Topbar} from './components/Topbar'
import {Layout} from "antd"
import {appContext, AppContextState} from './AppContext'
import {EventTreeDrawer} from "./components/EventTreeDrawer";
import {MainWindow} from "./components/MainWindow";

const {Content} = Layout

const App = (): JSX.Element => {

  const [eventTreeDrawerOpen, setEventTreeDrawerOpen] = useState<boolean>(false)

  function updateDisplay() {
    // XXX TODO
  }

  const appContextValues: AppContextState = {
    updateDisplay,
    setEventTreeDrawerOpen,
    eventTreeDrawerOpen,
  }

  return (
    <appContext.Provider value={appContextValues}>
      <Layout className='App'>
        <Topbar/>
        <Content>
          <MainWindow/>
          <EventTreeDrawer/>
        </Content>
      </Layout>
    </appContext.Provider>
  )
}
export default App
