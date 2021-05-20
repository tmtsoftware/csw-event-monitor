import React from 'react'
import {Table} from "antd"
import {ColumnsType} from "antd/es/table"
import {BaseKeyType, Key, SystemEvent} from "@tmtsoftware/esw-ts";
import {useAppContext} from "../AppContext";
import {EventUtil, ParamInfoModel} from "../data/EventTreeData";
import {ParameterUtil} from "../data/ParameterUtil";

interface ParamValue {
  time: string,
  value: any,
}

type ParamValuesTableProps = {
  paramInfoModel: ParamInfoModel | undefined
  cswParamKey: BaseKeyType<Key>
  events: Array<SystemEvent> | undefined
}

export const ParamValuesTable = ({paramInfoModel, cswParamKey, events}: ParamValuesTableProps): JSX.Element => {

  const {expandedParamInfoModel} = useAppContext()
  const unitsStr = paramInfoModel?.units ? ` (${EventUtil.stripHtml(paramInfoModel.units)})` : ""

  function makeTable(): JSX.Element {
    const columns: ColumnsType<ParamValue> = [
      {
        title: 'Time',
        dataIndex: 'time',
        key: 'time'
      },
      {
        title: `Value${unitsStr}`,
        dataIndex: 'value',
        key: 'value',
      },
    ];

    const dataSource: Array<ParamValue> = events ? events.slice().reverse().map((systemEvent) => {
      return {
        key: systemEvent.eventId,
        time: systemEvent.eventTime.substring(11, 23),
        value: ParameterUtil.formatValues(systemEvent, cswParamKey),
      };
    }) : []

    return (
      <Table<ParamValue>
        size={'small'}
        dataSource={dataSource}
        columns={columns}
        pagination={false}
        scroll={{y: expandedParamInfoModel ? 500 : 200}}
      />
    )
  }

  return makeTable()
}
