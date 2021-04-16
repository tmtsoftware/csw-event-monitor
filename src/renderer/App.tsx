import React, {useEffect, useState} from 'react'

// import 'antd/dist/antd.dark.css';
// // import 'antd/dist/antd.css'

import './App.css'

import {Topbar} from './components/Topbar'
import {Layout, Typography} from "antd"
import {appContext, AppContextState} from './AppContext'
import {EventTreeDrawer} from "./components/EventTreeDrawer";
import {MainWindow} from "./components/MainWindow";
import {EventService, SystemEvent} from "@tmtsoftware/esw-ts";
import {EventSubscription} from "./data/EventSubscription";
import {DataNode} from "antd/lib/tree";
import {EventModel, EventsForSubsystem, IcdServerInfo} from "./data/EventTreeData";

const {Content} = Layout
const {Text} = Typography;

const App = (): JSX.Element => {

  const [eventTreeData, setEventTreeData] = useState<Array<DataNode>>([])
  const [eventTreeDrawerOpen, setEventTreeDrawerOpen] = useState<boolean>(false)
  const [eventService, setEventService] = useState<EventService | undefined>(undefined)
  const [hasError, setHasError] = useState<String>("")
  const [subscriptions, setSubscriptions] = useState<Array<EventSubscription>>([])
  const [eventModels, setEventModels] = useState<Array<EventModel>>([])
  const [systemEvents, setSystemEvents] = useState<Array<SystemEvent>>([])

  async function findEventService() {
    try {
      setEventService(await EventService())
    } catch {
      setHasError("Can't locate the Event Service.")
    }
  }

  useEffect(() => {
    // noinspection JSIgnoredPromiseFromCall
    findEventService()
  }, []);

  useEffect(() => {
    // Gets the event tree data and puts it in the correct format
    function getData() {
      fetch(`${IcdServerInfo.baseUri}/eventList`)
        .then((response) => response.json())
        .then((result) => {
          const allEvents: Array<EventsForSubsystem> = result
          const treeData: Array<DataNode> = allEvents.map(a => {
              const node: DataNode = {
                key: a.subsystem,
                title: a.subsystem,
                children: a.components.map(c => {
                    const child: DataNode = {
                      key: `${a.subsystem}.${c.component}`,
                      title: c.component,
                      children: c.events.map(e => {
                          const leaf: DataNode = {
                            key: `${a.subsystem}.${c.component}.${e.event}`,
                            title: e.event,
                            isLeaf: true
                          }
                          return leaf
                        }
                      )
                    }
                    return child
                  }
                )
              }
              return node
            }
          )
          setEventTreeData(treeData)
        })
    }

    getData()
  }, [])

  function updateDisplay() {
    // XXX TODO
  }

  const appContextValues: AppContextState = {
    updateDisplay,
    setEventTreeDrawerOpen,
    eventTreeDrawerOpen,
    eventService,
    subscriptions,
    setSubscriptions,
    eventModels,
    setEventModels,
    systemEvents,
    setSystemEvents
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
          <EventTreeDrawer eventTreeData={eventTreeData}/>
        </Content>
      </Layout>
    </appContext.Provider>
  )
}
export default App
