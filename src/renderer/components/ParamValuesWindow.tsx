import React, {useState} from 'react'
import {useAppContext} from "../AppContext";
import {ParamValuesTable} from "./ParamValuesTable";

type ParamValuesWindowProps = {
  paramIndex: number
}

export const ParamValuesWindow = ({paramIndex}: ParamValuesWindowProps): JSX.Element => {
  const {paramInfoModels} = useAppContext()
  const paramInfoModel = (paramInfoModels.length > paramIndex) ? paramInfoModels[paramIndex] : undefined
  const paramName = paramInfoModel ? paramInfoModel.parameterName : ""

  return (
    <div>
      {paramInfoModel ?
        <ParamValuesTable paramInfoModel={paramInfoModel}/>
        : <div/>}
    </div>
  )
}
