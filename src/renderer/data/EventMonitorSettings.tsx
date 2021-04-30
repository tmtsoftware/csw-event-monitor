import Cookies from 'universal-cookie';

interface EventMonitorSettingsType {
  darkMode: boolean
}

export class EventMonitorSettings {
  static cookies = new Cookies()
  static key = "EventMonitorSettings"

  static defaultSettings: EventMonitorSettingsType = {
    darkMode: true
  }

  static getSettings(): EventMonitorSettingsType {
    const value = this.cookies.get(this.key)
      console.log('XXX getSettings(): ', value)
    return value ? value : this.defaultSettings
  }

  static setSettings(settings: EventMonitorSettingsType) {
    this.cookies.set(this.key, settings)
  }

  static setDarkMode(b: boolean) {
    console.log(`XXX setDarkMode ${b}`)
    const settings = this.getSettings()
    settings.darkMode = b
    this.setSettings(settings)
  }

  static getDarkMode(): boolean {
    console.log(`XXX getDarkMode`, this.getSettings().darkMode)
    return this.getSettings().darkMode
  }

  static revert() {
    this.setSettings(this.defaultSettings)
  }
}
