import React from 'react'
import {useAppContext} from "../AppContext"
import {EventTree} from "./EventTree";
import {Drawer, Input, Typography} from "antd"
import {Button} from 'antd';
import {ExpandAltOutlined, ShrinkOutlined} from "@ant-design/icons";

declare type EventType = React.KeyboardEvent<HTMLDivElement> | React.MouseEvent<HTMLDivElement | HTMLButtonElement>;

const {Search} = Input;
const {Text} = Typography;

export const EventTreeDrawer = (): JSX.Element => {
  const {eventTreeDrawerOpen, setEventTreeDrawerOpen, eventTreeFilter, setEventTreeFilter} = useAppContext()

  function onSearch(value: string) {
    console.log(`XXX Search ${value}`)
  }

  function onChange(e: React.ChangeEvent<HTMLInputElement>) {
    console.log(`XXX Change: ${e.currentTarget.value}`)
    setEventTreeFilter(e.currentTarget.value)
  }

  const spacing = {margin: '0px 5px 15px 0'}

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
      <div>
        <Text
          strong
          style={{margin: '0px 10px 15px 0'}}>
          Events
        </Text>
        <Button
          type="ghost"
          icon={<ExpandAltOutlined/>}
          size={"middle"}
          title={'Expand all'}
          style={spacing}/>
        <Button
          type="ghost"
          icon={<ShrinkOutlined/>}
          size={"middle"}
          title={'Collapse all'}
          style={spacing}/>
        <Search
          placeholder="Filter events"
          value={eventTreeFilter}
          onSearch={onSearch}
          onChange={onChange}
          style={{margin: spacing.margin, width: 200}}
          title={'Filter event tree'}/>
      </div>
      <EventTree/>
    </Drawer>
  )
}


