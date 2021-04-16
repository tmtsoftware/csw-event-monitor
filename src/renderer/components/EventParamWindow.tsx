import React, {useState} from 'react'
import {useAppContext} from "../AppContext";

type EventParamWindowProps = {
  paramIndex: number
}

export const EventParamWindow = ({paramIndex}: EventParamWindowProps): JSX.Element => {
  const {paramInfoModels} = useAppContext()
  const paramInfoModel = (paramInfoModels.length > paramIndex) ? paramInfoModels[paramIndex] : undefined
  const paramName = paramInfoModel ? paramInfoModel.parameterName : ""

  return (
    <div>
      {paramName}
    </div>
  )
}
