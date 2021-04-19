import React from 'react'
import {Row, Col, Typography} from 'antd';
import {useAppContext} from "../AppContext";
import {EventModelTabs} from "./EventModelTabs";
import {ParamValuesWindow} from "./ParamValuesWindow";
import {ParameterUtil} from "../data/ParameterUtil";
import {EventUtil} from "../data/EventTreeData";
import {ArrowRightOutlined} from "@ant-design/icons";

const {Title} = Typography;

export const MainWindow = (): JSX.Element => {
  const {expandedParamInfoModel, paramInfoModels, systemEvents, subscriptions} = useAppContext()
  const paramCount = paramInfoModels.length
  const numRows = expandedParamInfoModel ? 1 : Math.trunc(Math.sqrt(paramCount))
  const numCols = expandedParamInfoModel ? 1 : (paramCount == 0 ? 0 : Math.round(paramCount / numRows))
  const colIndexes = [...Array.from(Array(numRows * numCols).keys())]

  const cols = colIndexes.map(paramIndex => {
    const paramInfoModel = expandedParamInfoModel ? expandedParamInfoModel :
      ((paramInfoModels.length > paramIndex) ? paramInfoModels[paramIndex] : undefined)
    const cswParamKey = paramInfoModel ? ParameterUtil.getCswKey(paramInfoModel) : undefined
    const eventInfoModel = paramInfoModel?.eventInfoModel
    const eventKey = eventInfoModel ? EventUtil.getEventKey(eventInfoModel) : undefined
    const events = eventKey ? systemEvents.get(eventKey) : []
    return (
      <Col span={24 / numCols} key={paramIndex}>
        <div style={{
          height: '100%',
          padding: '1vh 0',
          border: 'thick double #32a1ce'
        }}>
          <ParamValuesWindow
            paramInfoModel={paramInfoModel}
            cswParamKey={cswParamKey}
            events={events}
          />
        </div>
      </Col>
    )
  })


  return (
    <div style={{margin: '20px'}}>
      <Row gutter={[16, 24]}>
        <Col span={24}>
          <div style={{
            padding: '1vh 0',
            margin: '20px 0 0 0',
            border: 'solid',
          }}>
            {
              (subscriptions.length == 0) ?
                <div style={{margin: '100px'}}>
                  <Title level={3}>Select events to monitor from the tree at right <ArrowRightOutlined/></Title>
                </div>
                : <div/>
            }
            <EventModelTabs/>
          </div>
        </Col>
      </Row>
      <Row gutter={[16, 24]}>
        {cols}
      </Row>
    </div>
  )
}
