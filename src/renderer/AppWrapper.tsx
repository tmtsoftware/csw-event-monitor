import React, {useEffect, useState} from 'react'
import LightApp from "./LightApp";
import DarkApp from "./DarkApp";
import {EventService, SystemEvent} from "@tmtsoftware/esw-ts";
import {EventSubscription} from "./data/EventSubscription";
import {EventInfoModel, ParamInfoModel} from "./data/EventTreeData";
import {appContext, AppContextState} from "./AppContext";
import {Typography} from "antd";

const {Text} = Typography;

// A wrapper used to allow switching between dark and light modes.
// This class also initializes the shared AppContext values.
const AppWrapper = (): JSX.Element => {
  const [eventTreeDrawerOpen, setEventTreeDrawerOpen] = useState<boolean>(true)
  const [settingsDrawerOpen, setSettingsDrawerOpen] = useState<boolean>(false)
  const [eventService, setEventService] = useState<EventService | undefined>(undefined)
  const [subscriptions, setSubscriptions] = useState<Array<EventSubscription>>([])
  const [eventInfoModels, setEventInfoModels] = useState<Array<EventInfoModel>>([])
  const [systemEvents, setSystemEvents] = useState<Map<string, Array<SystemEvent>>>(new Map())
  const [paramInfoModels, setParamInfoModels] = useState<Array<ParamInfoModel>>([])
  const [expandedParamInfoModel, setExpandedParamInfoModel] = useState<ParamInfoModel | undefined>(undefined)
  const [darkMode, setDarkMode] = useState<boolean>(true)
  const [viewMode, setViewMode] = useState<Map<ParamInfoModel, string>>(new Map())
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

  if (hasError.length != 0) return (
    <Text strong={true} type="danger">{hasError}</Text>
  )

  return (
    <appContext.Provider value={appContextValues}>
      {darkMode ? <DarkApp/> : <LightApp/>}
    </appContext.Provider>
  )
}

export default AppWrapper
