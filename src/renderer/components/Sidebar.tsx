import React from 'react'
import {Layout} from "antd";
import {useAppContext} from "../AppContext"
import {EventTree} from "./EventTree";

const {Sider} = Layout;

export const Sidebar = (): JSX.Element => {

  const {updateDisplay} = useAppContext()

  return (
    <Sider style={{height: '100%'}}>
      <EventTree/>
    </Sider>
  )
}
