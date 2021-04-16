import React from 'react'
import {Table, Typography} from "antd"
import {ColumnsType} from "antd/es/table"
import {ParamInfoModel} from "../data/EventTreeData";
import {useAppContext} from "../AppContext";

const {Text} = Typography;

interface ParamValue {
  time: string,
  value: string,
}

type ParamValuesTableProps = {
  paramInfoModel: ParamInfoModel
}

export const ParamValuesTable = ({paramInfoModel}: ParamValuesTableProps): JSX.Element => {
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

    console.log(`XXX ParamValueTable: SystemEvent count: ${systemEvents.length}`)
    const dataSource: Array<ParamValue> = systemEvents.map((systemEvent) => {
      return {
        key: systemEvent.eventId,
        time: systemEvent.eventTime,
        // value: systemEvent.get(),
        value: "xxx",
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
        scroll={{ y: 200 }}
      />
    )
  }

  return makeTable()
}
