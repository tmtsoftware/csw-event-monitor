import React from 'react'
import {PageHeader} from "antd"
import {Button} from 'antd';
import {FileAddOutlined, SettingOutlined} from "@ant-design/icons";
import {useAppContext} from "../AppContext"

export const Topbar = (): JSX.Element => {
  const {eventTreeDrawerOpen, setEventTreeDrawerOpen, settingsDrawerOpen, setSettingsDrawerOpen} = useAppContext()

  function addEvent() {
    if (!eventTreeDrawerOpen)
      setEventTreeDrawerOpen(true)
  }

  function showSettings() {
    console.log(`XXX showSettings ${settingsDrawerOpen}`)
    if (!settingsDrawerOpen)
      setSettingsDrawerOpen(true)
  }

  return (
    <PageHeader
      style={{backgroundColor: '#0c7499', height: '45px', paddingTop: '5px'}}
      // style={{backgroundColor: '#b5cddb', height: '45px', paddingTop: '5px'}}
      ghost={true}
      title="CSW Event Monitor"
      extra={[
        <Button
          key='addEvent'
          type='ghost'
          icon={<FileAddOutlined/>}
          size={"middle"}
          title={'Add Event'}
          onClick={addEvent}>
          Add Event
        </Button>,
        <Button
          key='settings'
          type='ghost'
          icon={<SettingOutlined />}
          size={"middle"}
          title={'Show the settings dialog'}
          onClick={showSettings}/>
      ]}
    >
    </PageHeader>
  )
}
