import React from 'react'
import type {AltAzCoord, BaseKeyType, Key, SystemEvent} from "@tmtsoftware/esw-ts";
import {Line} from "@ant-design/charts";

type ParamValuesLineChartProps = {
  cswParamKey: BaseKeyType<Key>
  events: Array<SystemEvent> | undefined
}

export const ParamValuesLineChart = ({cswParamKey, events}: ParamValuesLineChartProps): JSX.Element => {

  function round(num: any): number | undefined {
    if (typeof num == "number")
      return Math.round(num * 1000) / 1000
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
    // const value = (values && values.length > 0) ? round(values[0]) : "undefined"
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
        const c: AltAzCoord = value as AltAzCoord
        return [{
          time: time,
          value: c.alt.toDegree(),
          category: 'alt'
        },{
          time: time,
          value: c.az.toDegree(),
          category: 'az'
        }]

      default:
        return []

      // case 'BooleanKey':
      // case 'UTCTimeKey':
      // case 'TAITimeKey':
      // case 'EqCoordKey':
      //   return this.formatEqCoords(values)
      // case 'SolarSystemCoordKey':
      //   return this.formatSolarSystemCoords(values)
      // case 'MinorPlanetCoordKey':
      //   return this.formatMinorPlanetCoords(values)
      // case 'CometCoordKey':
      //   return this.formatCometCoords(values)
      // case 'AltAzCoordKey':
      //   return this.formatAltAzCoords(values)
      // case 'CoordKey':
      //   return this.formatCoords(values)
    }
  }

  // const data = events ? events.flatMap(systemEvent => getData(systemEvent)) : []
  const data = events ? events.flatMap(getData) : []
  console.log('XXX data = ', data)

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
}
