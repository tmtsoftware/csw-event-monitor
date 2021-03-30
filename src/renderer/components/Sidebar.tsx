import React from 'react'
import {Layout} from "antd";
import {useAppContext} from "../AppContext"

const {Sider} = Layout;

export const Sidebar = (): JSX.Element => {

  const {updateDisplay} = useAppContext()

  return (
    <Sider>

    </Sider>
  )
}
