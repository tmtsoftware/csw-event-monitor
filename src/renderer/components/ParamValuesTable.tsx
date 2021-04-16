import React from 'react'
import {Table, Typography} from "antd"
import {ColumnsType} from "antd/es/table"
import {ParamInfoModel} from "../data/EventTreeData";
import {useAppContext} from "../AppContext";
import {BaseKey, Key} from "@tmtsoftware/esw-ts";

const {Text} = Typography;

interface ParamValue {
  time: string,
  value: any,
}

type ParamValuesTableProps = {
  paramInfoModel: ParamInfoModel
  cswParamKey: BaseKey<Key>
}

export const ParamValuesTable = ({paramInfoModel, cswParamKey}: ParamValuesTableProps): JSX.Element => {
  const {systemEvents} = useAppContext()

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

    const dataSource: Array<ParamValue> = systemEvents.map((systemEvent) => {
      const values = systemEvent.get(cswParamKey)?.values
      // XXX TODO: Handle multiple values
      const value = (values && values.length > 0) ? values[0] : "undefined"
      return {
        key: systemEvent.eventId,
        time: systemEvent.eventTime,
        value: value,
      };
    })


    const title = <Text strong>
      {paramInfoModel.parameterName}
    </Text>

    return (
      <Table<ParamValue>
        title={() => title}
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
