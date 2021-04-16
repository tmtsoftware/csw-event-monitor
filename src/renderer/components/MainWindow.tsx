import React, {useState} from 'react'
import {Row, Col} from 'antd';
import {useAppContext} from "../AppContext";
import {EventModelTabs} from "./EventModelTabs";
import {ParamValuesWindow} from "./ParamValuesWindow";

export const MainWindow = (): JSX.Element => {
  const {subscriptions, paramInfoModels} = useAppContext()
  const paramCount = paramInfoModels.length
  const numRows = paramCount == 0 ? 1 : Math.trunc(Math.sqrt(paramCount))
  const numCols = paramCount == 0 ? 1 : Math.round(paramCount/numRows)

  const colIndexes = [...Array.from(Array(numRows * numCols).keys())]
  const cols = colIndexes.map(index => {
    return (
      <Col span={24 / numCols} key={index}>
        <div style={{
          // background: '#0092ff',
          padding: '1vh 0',
          border: 'solid'
        }}>
          <ParamValuesWindow paramIndex={index}/>
        </div>
      </Col>
    )
  })

  return (
    <div style={{margin: '20px'}}>
      <Row gutter={[16, 24]}>
        {cols}
      </Row>
      <Row gutter={[16, 24]}>
        <Col span={24}>
          <div style={{
            padding: '1vh 0',
            margin: '20px 0 0 0',
            border: 'solid'
          }}>
            <EventModelTabs/>
          </div>
        </Col>
      </Row>
    </div>
  )
}
