import React from 'react'
import {Table, Typography} from "antd"
import {ColumnsType} from "antd/es/table"
import {ParamInfoModel} from "../data/EventTreeData";
import {BaseKey, Key, SystemEvent} from "@tmtsoftware/esw-ts";

interface ParamValue {
  time: string,
  value: any,
}

type ParamValuesTableProps = {
  cswParamKey: BaseKey<Key>
  events: Array<SystemEvent> | undefined
}

export const ParamValuesTable = ({cswParamKey, events}: ParamValuesTableProps): JSX.Element => {

  function makeTable(): JSX.Element {
    const columns: ColumnsType<ParamValue> = [
      {
        title: 'Time',
        dataIndex: 'time',
        key: 'time'
      },
      {
        title: 'Value',
        dataIndex: 'value',
        key: 'value',
      },
    ];

    //2021-04-19T14:19:23.584906572Z

    const dataSource: Array<ParamValue> = events ? events.map((systemEvent) => {
      const values = systemEvent.get(cswParamKey)?.values
      // XXX TODO: Handle multiple values
      const value = (values && values.length > 0) ? values[0] : "undefined"
      return {
        key: systemEvent.eventId,
        time: systemEvent.eventTime.substring(11, 23),
        value: value,
      };
    }) : []

    return (
      <Table<ParamValue>
        size={'small'}
        dataSource={dataSource}
        columns={columns}
        pagination={false}
        scroll={{y: 200}}
      />
    )
  }

  return makeTable()
}
