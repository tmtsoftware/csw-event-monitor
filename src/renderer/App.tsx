import React, {useEffect, useState} from 'react'
import 'antd/dist/antd.css';
import './App.css'
import {Topbar} from './components/Topbar'
import {Layout, Typography} from "antd"
import {EventTreeDrawer} from "./components/EventTreeDrawer";
import {MainWindow} from "./components/MainWindow";
import {DataNode} from "antd/lib/tree";
import {EventInfoModel, EventsForSubsystem, EventUtil, IcdServerInfo, ParamInfoModel} from "./data/EventTreeData";
import {Settings} from "./components/Settings";
import {EventService, SystemEvent} from "@tmtsoftware/esw-ts";
import {EventSubscription} from "./data/EventSubscription";
import {appContext, AppContextState} from "./AppContext";

const {Content} = Layout
const {Text} = Typography;

// Main application
const App = (): JSX.Element => {
  const [eventTreeData, setEventTreeData] = useState<Array<DataNode>>([])
  const [eventTreeDrawerOpen, setEventTreeDrawerOpen] = useState<boolean>(true)
  const [settingsDrawerOpen, setSettingsDrawerOpen] = useState<boolean>(false)
  const [eventService, setEventService] = useState<EventService | undefined>(undefined)
  const [subscriptions, setSubscriptions] = useState<Array<EventSubscription>>([])
  const [eventInfoModels, setEventInfoModels] = useState<Array<EventInfoModel>>([])
  const [systemEvents, setSystemEvents] = useState<Map<string, Array<SystemEvent>>>(new Map())
  const [paramInfoModels, setParamInfoModels] = useState<Array<ParamInfoModel>>([])
  const [expandedParamInfoModel, setExpandedParamInfoModel] = useState<ParamInfoModel | undefined>(undefined)
  const [darkMode, setDarkMode] = useState<boolean>(true)
  const [viewMode, setViewMode] = useState<Map<string, string>>(new Map())
  const [hasError, setHasError] = useState<string>("")

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

  const appContextValues: AppContextState = {
    eventTreeDrawerOpen,
    setEventTreeDrawerOpen,
    settingsDrawerOpen,
    setSettingsDrawerOpen,
    eventService,
    subscriptions,
    setSubscriptions,
    eventInfoModels,
    setEventInfoModels,
    systemEvents,
    setSystemEvents,
    paramInfoModels,
    setParamInfoModels,
    expandedParamInfoModel,
    setExpandedParamInfoModel,
    viewMode,
    setViewMode,
    darkMode,
    setDarkMode
  }

  useEffect(() => {
    // Gets the event tree data and puts it in the correct format.
    // Do it here instead of EventTree, so that it is only done once.
    function getData() {
      console.log("XXX Getting event tree data! ")
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
                      key: `${a.subsystem}${EventUtil.eventKeySeparator}${c.component}`,
                      title: c.component,
                      children: c.events.map(e => {
                          const leaf: DataNode = {
                            key: `${a.subsystem}${EventUtil.eventKeySeparator}${c.component}${EventUtil.eventKeySeparator}${e.event}`,
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


  if (hasError.length != 0) return (
    <Text strong={true} type="danger">{hasError}</Text>
  )

  return (
    <appContext.Provider value={appContextValues}>
      <Layout className='App'>
        <Topbar/>
        <Content>
          <MainWindow/>
          <EventTreeDrawer eventTreeData={eventTreeData}/>
          <Settings/>
        </Content>
      </Layout>
    </appContext.Provider>
  )
}
export default App
