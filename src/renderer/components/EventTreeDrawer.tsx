import React from 'react'
import {useAppContext} from "../AppContext"
import {EventTree} from "./EventTree";
import {Drawer} from "antd"

declare type EventType = React.KeyboardEvent<HTMLDivElement> | React.MouseEvent<HTMLDivElement | HTMLButtonElement>;

export const EventTreeDrawer = (): JSX.Element => {
  const {eventTreeDrawerOpen, setEventTreeDrawerOpen} = useAppContext()

  return (
    <Drawer
      // title="Subsystem Events"
      placement="right"
      closable={true}
      destroyOnClose
      visible={eventTreeDrawerOpen}
      width={500}
      onClose={(_: EventType) => setEventTreeDrawerOpen(false)}
    >
      <EventTree/>
    </Drawer>
  )
}


