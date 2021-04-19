import React from 'react'
import {Table, Typography} from "antd"
import {ColumnsType} from "antd/es/table"
import {Key} from "antd/lib/table/interface";
import {EventInfoModel, EventUtil, ParamInfoModel} from "../data/EventTreeData";
import {useAppContext} from "../AppContext";

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

    const dataSource: Array<EventParameter> = eventInfoModel ? eventInfoModel.eventModel.parameterList.map((param) => {
      return {
        key: param.name,
        name: param.name,
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
            description: p.description
          }
        })
        const otherEventParams = paramInfoModels.filter((p) => p.eventInfoModel != eventInfoModel)
        setParamInfoModels(otherEventParams.concat(a))
      }
    }

    const title = <Text strong>Event: {EventUtil.getEventKey(eventInfoModel)}</Text>

    return (
      <Table<EventParameter>
        rowSelection={{
          type: 'checkbox',
          ...rowSelection,
        }}
        title={() => title}
        size={'small'}
        dataSource={dataSource}
        columns={columns}
        pagination={false}
        scroll={{ y: 200 }}
      />
    )
  }

  return (
    <div>
      {makeTable()}
    </div>
  )
}
