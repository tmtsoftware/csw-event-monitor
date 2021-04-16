import React, {useState} from 'react'
import {Row, Col} from 'antd';
import {useAppContext} from "../AppContext";
import {EventModelTabs} from "./EventModelTabs";
import {EventParamWindow} from "./EventParamWindow";

export const MainWindow = (): JSX.Element => {
  const [numRows, setNumRows] = useState<number>(2)
  const [numCols, setNumCols] = useState<number>(4)
  const {subscriptions} = useAppContext()

  console.log(`XXX main window: subscriptions: ${subscriptions} (${subscriptions.length})`)

  const colIndexes = [...Array.from(Array(numRows * numCols).keys())]
  const cols = colIndexes.map(index => {
    return (
      <Col span={24 / numCols} key={index}>
        <div style={{
          // background: '#0092ff',
          padding: '10vh 0',
          border: 'solid'
        }}>
          <EventParamWindow paramIndex={index}/>
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
