import React from 'react'
import {Space, Table, Typography} from "antd"
import type {ColumnsType} from "antd/es/table"
import type {Key} from "antd/lib/table/interface";
import type {EventInfoModel, ParamInfoModel} from "../data/EventTreeData";
import {useAppContext} from "../AppContext";
import {ParameterUtil} from "../data/ParameterUtil";

const {Text} = Typography;

interface EventParameter {
  name: string,
  typeStr: string,
  units: string,
  description: string,
}

type EventParamTableProps = {
  eventInfoModel: EventInfoModel
}

export const EventParamTable = ({eventInfoModel}: EventParamTableProps): JSX.Element => {

  const {paramInfoModels, setParamInfoModels} = useAppContext()

  function makeTable(): JSX.Element {
    const columns: ColumnsType<EventParameter> = [
      {
        title: 'Name',
        dataIndex: 'name',
        key: 'name',
        width: 300
      },
      {
        title: 'Type',
        dataIndex: 'typeStr',
        key: 'typeStr',
        width: 150
      },
      {
        title: 'Units',
        dataIndex: 'units',
        key: 'units',
        render: units => (
          <div dangerouslySetInnerHTML={{__html: units}}/>
        ),
        width: 150
      },
      {
        title: 'Description',
        dataIndex: 'description',
        key: 'description',
        render: description => (
          <div dangerouslySetInnerHTML={{__html: description}}/>
        ),
      }
    ];

    function makeKey(eventInfoModel: EventInfoModel, paramName: string): string {
      const subsystem = eventInfoModel.subsystem
      const component = eventInfoModel.component
      const eventName = eventInfoModel.eventModel.name
      const parameterName = ParameterUtil.fixParamName(paramName)
      return `${subsystem}::${component}::${eventName}::${parameterName}`
    }

    const dataSource: Array<EventParameter> = eventInfoModel ? eventInfoModel.eventModel.parameterList.map((param) => {
      return {
        key:  makeKey(eventInfoModel, param.name),
        name: ParameterUtil.fixParamName(param.name),
        typeStr: param.typeStr,
        units: param.units,
        description: param.description,
      };
    }) : []

    const rowSelection = {
      onChange: (_: Key[], selectedRows: EventParameter[]) => {
        const a: Array<ParamInfoModel> = selectedRows.map(p => {
          return {
            eventInfoModel: eventInfoModel,
            parameterName: p.name,
            units: p.units,
            description: p.description
          }
        })
        const otherEventParams = paramInfoModels.filter((p) => p.eventInfoModel != eventInfoModel)
        setParamInfoModels(otherEventParams.concat(a))
      }
    }

    const title = <div>
      <Space direction={"vertical"} size={"small"}>
        <Space direction={'horizontal'} size={'large'}>
          <Text>Subsystem: {eventInfoModel.subsystem}</Text>
          <Text>Component: {eventInfoModel.component}</Text>
          <Text>Event: {eventInfoModel.eventModel.name}</Text>
          <Text>{eventInfoModel.eventModel.maybeMaxRate ? `Max Rate: ${eventInfoModel.eventModel.maybeMaxRate} Hz` : ""}</Text>
        </Space>
        <Text strong>Parameters</Text>
      </Space>
    </div>

    return (
      <Table<EventParameter>
        rowSelection={{
          type: 'checkbox',
          ...rowSelection,
          hideSelectAll: true,
          selectedRowKeys: paramInfoModels.map(p => makeKey(p.eventInfoModel, p.parameterName))
        }}
        title={() => title}
        size={'small'}
        dataSource={dataSource}
        columns={columns}
        pagination={false}
        scroll={{y: 200}}
      />
    )
  }

  return (
    <div>
      {makeTable()}
    </div>
  )
}
