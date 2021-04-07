import React, {ChangeEvent} from 'react'
import {Button, Divider, Input, Layout} from "antd";
import {useAppContext} from "../AppContext"
import {EventTree} from "./EventTree";
import {Typography} from 'antd';
import {NodeCollapseOutlined, NodeExpandOutlined} from "@ant-design/icons";

const {Text} = Typography;
const {Sider} = Layout;
// const {Search} = Input;

export const Sidebar = (): JSX.Element => {

  const {updateDisplay} = useAppContext()


  // function onSearch(value: string) {
  //   console.log(`XXX Search ${value}`)
  // }
  //
  // function onChange(e: React.ChangeEvent<HTMLInputElement>) {
  //   console.log(`XXX Change: ${e.currentTarget.value}`)
  // }

  return (
    <Sider theme={"light"} width={345}>
      {/*<div style={{margin: '10px'}}>*/}
      {/* <span>*/}
      {/*  <Text strong={true} style={{margin: '10px'}}>Browse Events</Text>*/}
      {/*  <Button type="text" icon={<NodeExpandOutlined/>} size={"small"}/>*/}
      {/*  <Button type="text" icon={<NodeCollapseOutlined/>} size={"small"}/>*/}
      {/*  <Search placeholder="Filter events" onSearch={onSearch} onChange={onChange} style={{width: 150}}/>*/}
      {/*</span>*/}
      {/*</div>*/}
      <Divider style={{margin: '0'}}/>
      <EventTree/>
    </Sider>
  )
}
