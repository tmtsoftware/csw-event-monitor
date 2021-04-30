import React, {createContext, useContext} from "react"
import {EventService, SystemEvent} from "@tmtsoftware/esw-ts";
import {EventSubscription} from "./data/EventSubscription";
import {EventInfoModel, ParamInfoModel} from "./data/EventTreeData";
import {EventMonitorSettings} from "./data/EventMonitorSettings";

// Application context: Holds values and functions that are shared by different components in the app
export type AppContextState = {
  // Signals to update the display after a change in the database
  eventTreeDrawerOpen: boolean,
  setEventTreeDrawerOpen: (_: boolean) => void,
  settingsDrawerOpen: boolean,
  setSettingsDrawerOpen: (_: boolean) => void,
  eventService: EventService | undefined,
  subscriptions: Array<EventSubscription>,
  setSubscriptions: (_: Array<EventSubscription>) => void,
  eventInfoModels: Array<EventInfoModel>,
  setEventInfoModels: (_: Array<EventInfoModel>) => void,
  systemEvents: Map<string, Array<SystemEvent>>,
  setSystemEvents: (_: Map<string, Array<SystemEvent>>) => void,
  paramInfoModels: Array<ParamInfoModel>,
  setParamInfoModels: (_: Array<ParamInfoModel>) => void
  expandedParamInfoModel: ParamInfoModel | undefined,
  setExpandedParamInfoModel: (_: ParamInfoModel | undefined) => void,
  appSettings: EventMonitorSettings,
  viewMode: Map<string, string>,
  setViewMode: (_: Map<string, string>) => void,
  darkMode: boolean,
  setDarkMode: (_: boolean) => void
}

const appContextDefaultValue: AppContextState = {
  settingsDrawerOpen: false,
  setSettingsDrawerOpen: (_: boolean) => {},
  eventTreeDrawerOpen: false,
  setEventTreeDrawerOpen: (_: boolean) => {},
  eventService: undefined,
  subscriptions: [],
  setSubscriptions: (_: Array<EventSubscription>) => [],
  eventInfoModels: [],
  setEventInfoModels: (_: Array<EventInfoModel>) => {},
  systemEvents: new Map(),
  setSystemEvents: (_: Map<string, Array<SystemEvent>>) => {},
  paramInfoModels: [],
  setParamInfoModels: (_: Array<ParamInfoModel>) => {},
  expandedParamInfoModel: undefined,
  setExpandedParamInfoModel: (_: ParamInfoModel | undefined) => {},
  appSettings: new EventMonitorSettings(),
  viewMode: new Map(),
  setViewMode: (_: Map<string, string>) => {},
  darkMode: true,
  setDarkMode: (_: boolean) => {}
}

export const appContext = createContext<AppContextState>(appContextDefaultValue)
export const useAppContext = () => useContext(appContext)

