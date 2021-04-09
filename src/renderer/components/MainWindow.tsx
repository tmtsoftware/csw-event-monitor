import React, {useState} from 'react'
import {Row, Col} from 'antd';
import {EventWindow} from "./EventWindow";

export const MainWindow = (): JSX.Element => {
  const [numRows, setNumRows] = useState<number>(2)
  const [numCols, setNumCols] = useState<number>(4)

  const colIndexes = [...Array.from(Array(numRows * numCols).keys())]
  const cols = colIndexes.map(_ => {
    return (
      <Col span={24 / numCols}>
        <div style={{
          // background: '#0092ff',
          padding: '20vh 0',
          border: 'solid'
        }}>
          <EventWindow/>
        </div>
      </Col>
    )
  })

  return (
    <div style={{margin: '20px'}}>
      <Row gutter={[16, 24]}>
        {cols}
      </Row>
    </div>
  )
}
