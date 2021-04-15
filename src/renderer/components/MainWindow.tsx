import React, {useState} from 'react'
import {Row, Col} from 'antd';
import {EventParamWindow} from "./EventWindow";
import {useAppContext} from "../AppContext";
import {EventParamTable} from "./EventParamTable";

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
            border: 'solid'
          }}>
            <EventParamTable/>
          </div>
        </Col>
      </Row>
    </div>
  )
}
