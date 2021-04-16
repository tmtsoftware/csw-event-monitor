import React, {useState} from 'react'
import {Tabs} from 'antd';
import {useAppContext} from "../AppContext";
import {EventParamTable} from "./EventParamTable";

const {TabPane} = Tabs;

export const EventModelTabs = (): JSX.Element => {
  const {eventModels} = useAppContext()
  const [selectedEventName, setSelectedEventName] = useState<string | undefined>(undefined)

  function onChange(activeKey: string) {
    setSelectedEventName(activeKey)
  }

  function onEdit(targetKey: any, _: string) {
    console.log(targetKey)
  }

  return (
    <Tabs
      hideAdd
      type="editable-card"
      onChange={onChange}
      activeKey={selectedEventName}
      onEdit={onEdit}
    >
      {eventModels.map(eventModel => {
        return (
          <TabPane tab={eventModel.name} key={eventModel.name} closable={true}>
            {<EventParamTable eventModel={eventModel}/>}
          </TabPane>
        )
      })}
    </Tabs>
  )
}
