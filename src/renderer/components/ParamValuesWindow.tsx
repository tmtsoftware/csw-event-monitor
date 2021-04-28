import React, {useState} from 'react'
import {ParamValuesTable} from "./ParamValuesTable";
import {EventUtil, ParamInfoModel} from "../data/EventTreeData";
import {BaseKey, Key, SystemEvent} from "@tmtsoftware/esw-ts";
import {Button, Menu, Typography} from "antd";
import {
  BarChartOutlined,
  CloseOutlined,
  ExpandOutlined,
  LineChartOutlined,
  ShrinkOutlined,
  TableOutlined
} from "@ant-design/icons";
import {MenuInfo} from 'rc-menu/lib/interface';
import {useAppContext} from "../AppContext";
import {ParamValuesLineChart} from "./ParamValuesLineChart";
import {ParamValuesBarChart} from "./ParamValuesBarChart";

const {Text} = Typography;

type ParamValuesWindowProps = {
  paramInfoModel: ParamInfoModel | undefined
  cswParamKey: BaseKey<Key> | undefined
  events: Array<SystemEvent> | undefined
}

export const ParamValuesWindow = ({paramInfoModel, cswParamKey, events}: ParamValuesWindowProps): JSX.Element => {
  const [descriptionVisible, setDescriptionVisible] = useState<boolean>(true)
  const {expandedParamInfoModel, setExpandedParamInfoModel, viewMode, setViewMode, darkMode} = useAppContext()

  function setThisViewMode(mode: string) {
    const map = new Map(viewMode)
    if (paramInfoModel)
      map.set(EventUtil.getParamKey(paramInfoModel), mode)
    setViewMode(map)
  }

  function menuItemSelected(info: MenuInfo) {
    switch (info.key) {
      case 'table':
        setThisViewMode('table')
        break
      case 'lineChart':
        setThisViewMode('lineChart')
        break
      case 'barChart':
        setThisViewMode('barChart')
        break
      case 'expand':
        setExpandedParamInfoModel(paramInfoModel)
        break
      case 'collapse':
        setExpandedParamInfoModel(undefined)
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
    const selectedKey = paramInfoModel ? viewMode.get(EventUtil.getParamKey(paramInfoModel)) : undefined
    const selectedKeys = selectedKey ? [selectedKey] : []
    const theme = darkMode ? "dark" : "light"
    return (
      <Menu
        mode="horizontal"
        theme={theme}
        selectedKeys={selectedKeys}
        onClick={menuItemSelected}
      >
        <Menu.Item
          key="title"
          disabled={true}
          title={makeTooltip()}
        >
          {makeTitle()}
        </Menu.Item>
        {expandedParamInfoModel ?
          <Menu.Item
            key="collapse"
            icon={<ShrinkOutlined/>}
            title={'Collapse this window'}
            style={{float: 'right'}}
          />
          :
          <Menu.Item
            key="expand"
            icon={<ExpandOutlined/>}
            title={'Expand this window'}
            style={{float: 'right'}}
          />
        }
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

  function makeTable(): JSX.Element {
    return (
      <ParamValuesTable
        cswParamKey={cswParamKey!}
        events={events}
      />
    )
  }

  function makeLineChart(): JSX.Element {
    return (
      <ParamValuesLineChart
        cswParamKey={cswParamKey!}
        events={events}
      />
    )
  }

  function makeBarChart(): JSX.Element {
    return (
      <ParamValuesBarChart
        cswParamKey={cswParamKey!}
        events={events}
      />
    )
  }

  function makeParamsValueDisplay(): JSX.Element {
    switch (paramInfoModel ? viewMode.get(EventUtil.getParamKey(paramInfoModel)) : '') {
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
