import React from 'react'
import {Table} from "antd"
import {ColumnsType} from "antd/es/table"
import {Key} from "antd/lib/table/interface";
import {EventModel} from "../data/EventTreeData";

interface EventParameter {
  name: string,
  typeStr: string,
  units: string,
  description: string,
}

type EventParamTableProps = {
  eventModel: EventModel
}

export const EventParamTable = ({eventModel}: EventParamTableProps): JSX.Element => {

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

    const dataSource: Array<EventParameter> = eventModel ? eventModel.parameterList.map((param) => {
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
      },
      getCheckboxProps: (param: EventParameter) => ({
        disabled: false,
        // Column configuration not to be checked
        name: param.name,
      }),
    }

    return (
      <Table<EventParameter>
        rowSelection={{
          type: 'checkbox',
          ...rowSelection,
        }}
        size={'small'}
        // title={() => eventModel.}
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