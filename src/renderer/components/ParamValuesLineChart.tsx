import React from 'react'
import {BaseKey, Key, SystemEvent} from "@tmtsoftware/esw-ts";
import {useAppContext} from "../AppContext";
// import {Line} from "@ant-design/charts";

type ParamValuesLineChartProps = {
  cswParamKey: BaseKey<Key>
  events: Array<SystemEvent> | undefined
}

export const ParamValuesLineChart = ({cswParamKey, events}: ParamValuesLineChartProps): JSX.Element => {

  const {expandedParamInfoModel} = useAppContext()

  const data = events ? events.map(systemEvent => {
    const values = systemEvent.get(cswParamKey)?.values
    // XXX TODO: Handle multiple values
    const value = (values && values.length > 0) ? values[0] : "undefined"
    return {
      time: systemEvent.eventTime.substring(11, 23),
      value: value,
    };
  }) : []

  const config = {
    data,
    height: 400,
    xField: 'time',
    yField: 'value',
    point: {
      size: 5,
      shape: 'diamond',
    },
    label: {
      style: {
        fill: '#aaa',
      },
    },
  }

  // return <Line {...config} />;
  return <div/>;
}
