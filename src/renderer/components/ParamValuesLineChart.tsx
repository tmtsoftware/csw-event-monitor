import React from 'react'
import {Key, SystemEvent} from "@tmtsoftware/esw-ts";
import {KeyType} from "@tmtsoftware/esw-ts/lib/dist/src/models/params/ParameterSetType";
import {useAppContext} from "../AppContext";
import {Line} from "@ant-design/charts";

type ParamValuesLineChartProps = {
  cswParamKey: KeyType<Key>
  events: Array<SystemEvent> | undefined
}

export const ParamValuesLineChart = ({cswParamKey, events}: ParamValuesLineChartProps): JSX.Element => {

  // const {expandedParamInfoModel} = useAppContext()

  function round(num: any): number | undefined {
    if (typeof num == "number")
      return Math.round(num * 1000)/1000
    return num
  }

  const data = events ? events.map(systemEvent => {
    const values = systemEvent.get(cswParamKey)?.values
    // XXX TODO: Handle multiple values
    const value = (values && values.length > 0) ? round(values[0]) : "undefined"
    return {
      time: systemEvent.eventTime.substring(11, 23),
      value: value,
      category: 'value'
    };
  }) : []

  const config = {
    data,
    height: 400,
    xField: 'time',
    yField: 'value',
    legend: false,
    seriesField: 'category',
    point: {
      size: 5,
      shape: 'diamond',
    },
    label: {
      style: {
        fill: '#aaa',
      }
    },
  }

  return <Line {...config} />;
  // return <div/>;
}
