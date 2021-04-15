import React, {useEffect, useState} from 'react'

import 'antd/dist/antd.dark.css';
// import 'antd/dist/antd.css'

import './App.css'

import {Topbar} from './components/Topbar'
import {Layout, Typography} from "antd"
import {appContext, AppContextState} from './AppContext'
import {EventTreeDrawer} from "./components/EventTreeDrawer";
import {MainWindow} from "./components/MainWindow";
import {EventService} from "@tmtsoftware/esw-ts";
import {EventSubscription} from "./data/EventSubscription";

const {Content} = Layout
const {Text} = Typography;

const App = (): JSX.Element => {

  const [eventTreeDrawerOpen, setEventTreeDrawerOpen] = useState<boolean>(false)
  const [eventService, setEventService] = useState<EventService | undefined>(undefined)
  const [hasError, setHasError] = useState<String>("")
  const [subscriptions, setSubscriptions] = useState<Array<EventSubscription>>([])

  async function findEventService() {
    try {
      setEventService(await EventService())
    } catch {
      setHasError("Can't locate the Event Service.")
    }
  }

  useEffect(() => {
    findEventService()
  }, []);

  function updateDisplay() {
    // XXX TODO
  }

  const appContextValues: AppContextState = {
    updateDisplay,
    setEventTreeDrawerOpen,
    eventTreeDrawerOpen,
    eventService,
    subscriptions,
    setSubscriptions
  }

  return (
    <appContext.Provider value={appContextValues}>
      <Layout className='App'>
        <Topbar/>
        <Content>
          {hasError.length != 0 ?
            <Text strong={true} type="danger">{hasError}</Text>
            : <MainWindow/>
          }
          <EventTreeDrawer/>
        </Content>
      </Layout>
    </appContext.Provider>
  )
}
export default App
