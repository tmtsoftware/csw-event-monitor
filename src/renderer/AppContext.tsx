import React, {createContext, useContext} from "react"

// Application context: Holds values and functions that are shared by different components in the app

export type AppContextState = {
  // Signals to update the display after a change in the database
  updateDisplay: () => void
  setEventTreeDrawerOpen: (_: boolean) => void,
  eventTreeDrawerOpen: boolean,
  setEventTreeFilter: (_: string) => void
  eventTreeFilter: string
}

const appContextDefaultValue: AppContextState = {
  updateDisplay: () => {},
  setEventTreeDrawerOpen: (_: boolean) => {},
  eventTreeDrawerOpen: false,
  setEventTreeFilter: (_: string) => {},
  eventTreeFilter: ""
}

export const appContext = createContext<AppContextState>(appContextDefaultValue)
export const useAppContext = () => useContext(appContext)

