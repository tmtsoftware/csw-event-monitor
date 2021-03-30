import React, {createContext, useContext} from "react"

// Application context: Holds values and functions that are shared by different components in the app

export type AppContextState = {
  // Signals to update the display after a change in the database
  updateDisplay: () => void
}

const appContextDefaultValue: AppContextState = {
  updateDisplay: () => {},
}

export const appContext = createContext<AppContextState>(appContextDefaultValue)
export const useAppContext = () => useContext(appContext)

