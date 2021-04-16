import React, {useState} from 'react'
import {useAppContext} from "../AppContext";
import {ParamValuesTable} from "./ParamValuesTable";
import {ParameterUtil} from "../data/ParameterUtil";

type ParamValuesWindowProps = {
  paramIndex: number
}

export const ParamValuesWindow = ({paramIndex}: ParamValuesWindowProps): JSX.Element => {
  const {paramInfoModels} = useAppContext()
  const paramInfoModel = (paramInfoModels.length > paramIndex) ? paramInfoModels[paramIndex] : undefined
  const cswParamKey = paramInfoModel ? ParameterUtil.getCswKey(paramInfoModel) : undefined

  return (
    <div>
      {(paramInfoModel  && cswParamKey) ?
        <ParamValuesTable
          paramInfoModel={paramInfoModel}
          cswParamKey={cswParamKey}
        />
        : <div/>}
    </div>
  )
}
