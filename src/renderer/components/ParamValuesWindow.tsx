import React from 'react'
import {ParamValuesTable} from "./ParamValuesTable";
import {ParamInfoModel} from "../data/EventTreeData";
import {BaseKey, Key, SystemEvent} from "@tmtsoftware/esw-ts";

type ParamValuesWindowProps = {
  paramInfoModel: ParamInfoModel | undefined
  cswParamKey: BaseKey<Key> | undefined
  events: Array<SystemEvent> | undefined
}

export const ParamValuesWindow = ({paramInfoModel, cswParamKey, events}: ParamValuesWindowProps): JSX.Element => {
  return (
    <div>
      {(paramInfoModel  && cswParamKey) ?
        <ParamValuesTable
          paramInfoModel={paramInfoModel}
          cswParamKey={cswParamKey}
          events={events}
        />
        : <div/>}
    </div>
  )
}
