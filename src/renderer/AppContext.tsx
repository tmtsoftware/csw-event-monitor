import React, {createContext, useContext} from "react"
import {EventService, SystemEvent} from "@tmtsoftware/esw-ts";
import {EventSubscription} from "./data/EventSubscription";
import {EventModel} from "./data/EventTreeData";

// Application context: Holds values and functions that are shared by different components in the app
export type AppContextState = {
  // Signals to update the display after a change in the database
  updateDisplay: () => void
  setEventTreeDrawerOpen: (_: boolean) => void,
  eventTreeDrawerOpen: boolean,
  eventService: EventService | undefined,
  subscriptions: Array<EventSubscription>,
  setSubscriptions: (a: Array<EventSubscription>) => void,
  eventModel?: EventModel,
  setEventModel: (eventModel: EventModel) => void,
  systemEvents: Array<SystemEvent>,
  setSystemEvents: (systemEvents: Array<SystemEvent>) => void
}

const appContextDefaultValue: AppContextState = {
  updateDisplay: () => {},
  setEventTreeDrawerOpen: (_: boolean) => {},
  eventTreeDrawerOpen: false,
  eventService: undefined,
  subscriptions: [],
  setSubscriptions: (_: Array<EventSubscription>) => [],
  eventModel: undefined,
  setEventModel: (_: EventModel) => {},
  systemEvents: [],
  setSystemEvents: (_: Array<SystemEvent>) => {}
}

export const appContext = createContext<AppContextState>(appContextDefaultValue)
export const useAppContext = () => useContext(appContext)

