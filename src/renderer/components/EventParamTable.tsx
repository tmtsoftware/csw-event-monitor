import React from 'react'
import {Table, Typography} from "antd"
import {ColumnsType} from "antd/es/table"
import {Key} from "antd/lib/table/interface";
import {EventInfoModel} from "../data/EventTreeData";

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

  function makeTable(): JSX.Element {
    const columns: ColumnsType<EventParameter> = [
      {
        title: 'Name',
        dataIndex: 'name',
        key: 'name'
      },
      {
        title: 'Type',
        dataIndex: 'typeStr',
        key: 'typeStr',
      },
      {
        title: 'Units',
        dataIndex: 'units',
        key: 'units',
        render: units => (
          <div dangerouslySetInnerHTML={{__html: units}}/>
        ),
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
      onChange: (selectedRowKeys: Key[], selectedRows: EventParameter[]) => {
        console.log(`selectedRowKeys: ${selectedRowKeys}`, 'selectedRows: ', selectedRows)
      }
    }

    const title = <Text
      strong>{eventInfoModel.subsystem}.{eventInfoModel.component}.{eventInfoModel.eventModel.name}
    </Text>

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
      />
    )
  }

  return (
    <div style={{flex: 'grow'}}>
      {makeTable()}
    </div>
  )
}
