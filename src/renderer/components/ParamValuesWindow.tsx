import React, {useState} from 'react'
import {ParamValuesTable} from "./ParamValuesTable";
import {ParamInfoModel} from "../data/EventTreeData";
import {BaseKey, Key, SystemEvent} from "@tmtsoftware/esw-ts";
import {Button, Menu, Typography} from "antd";
import {BarChartOutlined, CloseOutlined, ExpandOutlined, LineChartOutlined, TableOutlined} from "@ant-design/icons";
import {MenuInfo} from 'rc-menu/lib/interface';

const {Text} = Typography;

type ParamValuesWindowProps = {
  paramInfoModel: ParamInfoModel | undefined
  cswParamKey: BaseKey<Key> | undefined
  events: Array<SystemEvent> | undefined
}

export const ParamValuesWindow = ({paramInfoModel, cswParamKey, events}: ParamValuesWindowProps): JSX.Element => {
  const [viewMode, setViewMode] = useState<string>('table')
  const [descriptionVisible, setDescriptionVisible] = useState<boolean>(true)

  function menuItemSelected(info: MenuInfo) {
    switch (info.key) {
      case 'table':
        setViewMode('table')
        break
      case 'lineChart':
        setViewMode('lineChart')
        break
      case 'barChart':
        setViewMode('barChart')
        break
      case 'expand':
        break
      case 'collapse':
        break
    }
  }

  function makeTitle(): JSX.Element {
    return (
      <Text strong>
        {paramInfoModel!.parameterName}
      </Text>
    )
  }

  function makeTooltip() {
    return `Subsystem: ${paramInfoModel!.eventInfoModel.subsystem}\n`
      + `Component: ${paramInfoModel!.eventInfoModel.component}\n`
      + `Event: ${paramInfoModel!.parameterName}`
  }

  function makeMenu(): JSX.Element {
    return (
      <Menu
        defaultSelectedKeys={['1']}
        mode="horizontal"
        theme="dark"
        selectedKeys={[viewMode]}
        onClick={menuItemSelected}
      >
        <Menu.Item
          key="title"
          disabled={true}
          title={makeTooltip()}
        >
          {makeTitle()}
        </Menu.Item>
        <Menu.Item
          key="expand"
          icon={<ExpandOutlined />}
          title={'Expand this window'}
          style={{float: 'right'}}
        />
        <Menu.Item
          key="barChart"
          icon={<BarChartOutlined/>}
          title={'Display data in a bar chart'}
          style={{float: 'right'}}
        />
        <Menu.Item
          key="lineChart"
          icon={<LineChartOutlined/>}
          title={'Display data in a line chart'}
          style={{float: 'right'}}
        />
        <Menu.Item
          key="table"
          icon={<TableOutlined/>}
          title={'Display data in a table'}
          style={{float: 'right'}}
        />
      </Menu>
    )
  }

  // <ShrinkOutlined />

  function makeTable(): JSX.Element {
    return (
      <ParamValuesTable
        cswParamKey={cswParamKey!}
        events={events}
      />
    )
  }

  function makeLineChart(): JSX.Element {
    return (<div>LineChart</div>)
  }

  function makeBarChart(): JSX.Element {
    return (<div>BarChart</div>)
  }

  function makeParamsValueDisplay(): JSX.Element {
    switch (viewMode) {
      case 'lineChart':
        return makeLineChart()
      case 'barChart':
        return makeBarChart()
      default:
        return makeTable()
    }
  }

  function hideDescription() {
    setDescriptionVisible(false)
  }

  function makeDescription(): JSX.Element {
    if (descriptionVisible) {
      return (
        <div style={{position: 'relative', paddingLeft: '20px', paddingTop: '5px'}}>
          <div dangerouslySetInnerHTML={{__html: paramInfoModel!.description}}/>
          <Button
            style={{position: 'absolute', right: '5px', top: '5px'}}
            key='hideDescription'
            type='ghost'
            size={"small"}
            icon={<CloseOutlined/>}
            onClick={hideDescription}
          />
        </div>
      )
    } else return <div/>
  }

  return (
    <div>
      {(paramInfoModel && cswParamKey) ?
        <div>
          {makeMenu()}
          {makeDescription()}
          {makeParamsValueDisplay()}
        </div>
        : <div/>}
    </div>
  )
}
