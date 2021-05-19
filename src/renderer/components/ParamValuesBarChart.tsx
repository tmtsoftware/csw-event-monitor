import React from 'react'
import {Key, SystemEvent} from "@tmtsoftware/esw-ts";
// import {useAppContext} from "../AppContext";
import {Column} from "@ant-design/charts";
import {KeyType} from "@tmtsoftware/esw-ts/lib/dist/src/models/params/ParameterSetType";

type ParamValuesBarChartProps = {
  cswParamKey: KeyType<Key>
  events: Array<SystemEvent> | undefined
}

export const ParamValuesBarChart = ({cswParamKey, events}: ParamValuesBarChartProps): JSX.Element => {

  // const {expandedParamInfoModel} = useAppContext()

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
    yField: 'value'
  }

  return <Column {...config} />;
}
