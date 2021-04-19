import React, {createContext, useContext} from "react"
import {EventService, SystemEvent} from "@tmtsoftware/esw-ts";
import {EventSubscription} from "./data/EventSubscription";
import {EventInfoModel, ParamInfoModel} from "./data/EventTreeData";

// Application context: Holds values and functions that are shared by different components in the app
export type AppContextState = {
  // Signals to update the display after a change in the database
  setEventTreeDrawerOpen: (_: boolean) => void,
  eventTreeDrawerOpen: boolean,
  eventService: EventService | undefined,
  subscriptions: Array<EventSubscription>,
  setSubscriptions: (_: Array<EventSubscription>) => void,
  eventInfoModels: Array<EventInfoModel>,
  setEventInfoModels: (_: Array<EventInfoModel>) => void,
  systemEvents: Map<string, Array<SystemEvent>>,
  setSystemEvents: (_: Map<string, Array<SystemEvent>>) => void,
  paramInfoModels: Array<ParamInfoModel>,
  setParamInfoModels: (_: Array<ParamInfoModel>) => void
}

const appContextDefaultValue: AppContextState = {
  setEventTreeDrawerOpen: (_: boolean) => {},
  eventTreeDrawerOpen: false,
  eventService: undefined,
  subscriptions: [],
  setSubscriptions: (_: Array<EventSubscription>) => [],
  eventInfoModels: [],
  setEventInfoModels: (_: Array<EventInfoModel>) => {},
  systemEvents: new Map(),
  setSystemEvents: (_: Map<string, Array<SystemEvent>>) => {},
  paramInfoModels: [],
  setParamInfoModels: (_: Array<ParamInfoModel>) => {}
}

export const appContext = createContext<AppContextState>(appContextDefaultValue)
export const useAppContext = () => useContext(appContext)

