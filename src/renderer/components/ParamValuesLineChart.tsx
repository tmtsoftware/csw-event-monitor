import React from 'react'
import type {AltAzCoord, EqCoord, BaseKeyType, Key, SystemEvent} from "@tmtsoftware/esw-ts";
import {Line} from "@ant-design/charts";
import {Angle} from "../data/Angle";

type ParamValuesLineChartProps = {
  cswParamKey: BaseKeyType<Key>
  events: Array<SystemEvent> | undefined
}

export const ParamValuesLineChart = ({cswParamKey, events}: ParamValuesLineChartProps): JSX.Element => {

  function round(num: any): number | undefined {
    if (typeof num == "number")
      return Math.round(num * 1000)/1000
    return num
  }

  type DataType = {
    time: string,
    value: number | undefined,
    category: string

  }

  function getData(systemEvent: SystemEvent): Array<DataType> {
    const values = systemEvent.get(cswParamKey)?.values
    // XXX TODO: Handle multiple values
    const value = (values && values.length > 0) ? values[0] : undefined
    const time = systemEvent.eventTime.substring(11, 23)
    switch (cswParamKey.keyTag) {
      case 'IntKey':
      case 'LongKey':
      case 'ShortKey':
      case 'FloatKey':
      case 'DoubleKey':
      case 'ByteKey':
        return [{
            time: time,
            value: round(value),
      category: 'value'
          }]

      case 'AltAzCoordKey':
        const altAzCoord: AltAzCoord = value as AltAzCoord
        return [{
          time: time,
          value: round(new Angle(altAzCoord.alt).toDegree()),
          category: 'Alt'
        },{
          time: time,
          value:  round(new Angle(altAzCoord.az).toDegree()),
          category: 'Az'
        }]

      case 'EqCoordKey':
        const eqCoord: EqCoord = value as EqCoord
        return [{
          time: time,
          value: new Angle(eqCoord.ra).toDegree(),
          category: 'RA'
        },{
          time: time,
          value:  new Angle(eqCoord.dec).toDegree(),
          category: 'Dec'
        }]

      default:
        return []

      // XXX TODO FIXME

      // case 'BooleanKey':
      // case 'UTCTimeKey':
      // case 'TAITimeKey':
      // case 'SolarSystemCoordKey':
      // case 'MinorPlanetCoordKey':
      // case 'CometCoordKey':
      // case 'CoordKey':
    }
  }

  // const data = events ? events.flatMap(systemEvent => getData(systemEvent)) : []
  const data = events ? events.flatMap(getData) : []

  const config = {
    data,
    height: 400,
    xField: 'time',
    yField: 'value',
    legend: false,
    seriesField: 'category',
    // point: {
    //   size: 5,
    //   shape: 'diamond',
    //   style: {
    //     fill: 'white',
    //     stroke: '#2593fc',
    //     lineWidth: 2,
    //   },
    // },
    label: {
      style: {
        fill: '#aaa',
      }
    },
  }

  return <Line {...config} />;
}
