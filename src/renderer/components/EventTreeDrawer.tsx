import React from 'react'
import {useAppContext} from "../AppContext"
import {EventTree} from "./EventTree";
import {Drawer} from "antd"
import type {DataNode} from "antd/lib/tree";

declare type EventType = React.KeyboardEvent<HTMLDivElement> | React.MouseEvent<HTMLDivElement | HTMLButtonElement>;

type EventTreeDrawerProps = {
  eventTreeData: Array<DataNode>
}

export const EventTreeDrawer = ({eventTreeData}: EventTreeDrawerProps): JSX.Element => {
  const {eventTreeDrawerOpen, setEventTreeDrawerOpen} = useAppContext()

  return (
    <Drawer
      // title="Subsystem Events"
      placement="right"
      closable={true}
      // destroyOnClose
      width={500}
      visible={eventTreeDrawerOpen}
      onClose={(_: EventType) => setEventTreeDrawerOpen(false)}
    >
      <EventTree eventTreeData={eventTreeData}/>
    </Drawer>
  )
}


