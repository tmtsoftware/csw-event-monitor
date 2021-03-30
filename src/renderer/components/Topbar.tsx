import React from 'react'
import {PageHeader} from "antd"

export const Topbar = (): JSX.Element => {

  return (
    <PageHeader
      style={{backgroundColor: '#b2c4db', height: '45px', paddingTop: '0'}}
      ghost={true}
      className={'topbarPageHeader'}
      title="CSW Event Monitor"
    >
    </PageHeader>
  )
}
