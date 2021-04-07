import React from 'react'
import {Input, PageHeader} from "antd"
import {Button} from 'antd';
import {NodeCollapseOutlined, NodeExpandOutlined} from "@ant-design/icons";

const {Search} = Input;

export const Topbar = (): JSX.Element => {

  function onSearch(value: string) {
    console.log(`XXX Search ${value}`)
  }

  function onChange(e: React.ChangeEvent<HTMLInputElement>) {
    console.log(`XXX Change: ${e.currentTarget.value}`)
  }

  return (
    <PageHeader
      style={{backgroundColor: '#b2c4db', height: '45px', paddingTop: '0'}}
      ghost={true}
      className={'topbarPageHeader'}
      title="CSW Event Monitor"
      extra={[
        <Button type="ghost" icon={<NodeExpandOutlined/>} size={"middle"} title={'Expand all'}/>,
        <Button type="ghost" icon={<NodeCollapseOutlined/>} size={"middle"} title={'Collapse all'}/>,
        <Search placeholder="Filter events" onSearch={onSearch} onChange={onChange} style={{width: 200}} title={'Filter event tree'}/>
      ]}
    >
    </PageHeader>
  )
}
