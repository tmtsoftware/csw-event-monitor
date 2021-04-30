import ElectronStore from "electron-store";

interface EventMonitorSettingsType {
  darkMode: boolean
}

export class EventMonitorSettings {
  private store: ElectronStore<EventMonitorSettingsType>;
  private defaultSettings: EventMonitorSettingsType = {
    darkMode: true
  }

  constructor() {
    this.store = new ElectronStore<EventMonitorSettingsType>({defaults: this.defaultSettings})
  }

  setDarkMode(b: boolean) {
    console.log(`XXX setDarkMode ${b}`)
    this.store.set('darkMode', b)
  }

  getDarkMode(): boolean {
    console.log(`XXX getDarkMode`)
    if (this.store.has('darkMode'))
      return this.store.get('darkMode')
    return true
  }
}
