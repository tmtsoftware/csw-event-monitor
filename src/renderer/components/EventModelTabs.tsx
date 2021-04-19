import React, {useState} from 'react'
import {Tabs} from 'antd';
import {useAppContext} from "../AppContext";
import {EventParamTable} from "./EventParamTable";
import {EventUtil} from "../data/EventTreeData";

const {TabPane} = Tabs;

export const EventModelTabs = (): JSX.Element => {
  const {
    eventInfoModels,
    setEventInfoModels,
    subscriptions,
    setSubscriptions,
    paramInfoModels,
    setParamInfoModels
  } = useAppContext()
  const [selectedEventName, setSelectedEventName] = useState<string | undefined>(undefined)

  function onChange(activeKey: string) {
    setSelectedEventName(activeKey)
  }

  function onEdit(targetKey: any, action: string) {
    if (action == "remove") {
      const removed = eventInfoModels.find(eventInfoModel => EventUtil.getEventKey(eventInfoModel) == targetKey)
      if (removed) {
        setEventInfoModels(eventInfoModels.filter(m => m != removed))
        const eventSubscription = subscriptions.find(s =>
          s.event == removed.eventModel.name &&
          s.component == removed.component &&
          s.subsystem == removed.subsystem
        )
        if (eventSubscription) {
          eventSubscription.subscription.cancel()
          setSubscriptions(subscriptions.filter(s => s != eventSubscription))
        }
        setParamInfoModels(paramInfoModels.filter(p => p.eventInfoModel != removed))
      }
    }
  }

  return (
    <Tabs
      hideAdd
      type="editable-card"
      onChange={onChange}
      activeKey={selectedEventName}
      onEdit={onEdit}
    >
      {eventInfoModels.map(eventInfoModel => {
        const eventKey = EventUtil.getEventKey(eventInfoModel)
        return (
          <TabPane tab={eventInfoModel.eventModel.name} key={eventKey} closable={true}>
            {<EventParamTable eventInfoModel={eventInfoModel}/>}
          </TabPane>
        )
      })}
    </Tabs>
  )
}
