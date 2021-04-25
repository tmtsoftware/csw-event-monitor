import React, {useEffect, useState} from 'react'

import './App.css'

import {Topbar} from './components/Topbar'
import {Layout} from "antd"
import {EventTreeDrawer} from "./components/EventTreeDrawer";
import {MainWindow} from "./components/MainWindow";
import {DataNode} from "antd/lib/tree";
import {EventsForSubsystem, IcdServerInfo} from "./data/EventTreeData";
import {Settings} from "./components/Settings";

const {Content} = Layout

// Main application
const App = (): JSX.Element => {
  const [eventTreeData, setEventTreeData] = useState<Array<DataNode>>([])

  useEffect(() => {
    // Gets the event tree data and puts it in the correct format
    function getData() {
      console.log("XXX Getting event tree data! ")
      fetch(`${IcdServerInfo.baseUri}/eventList`)
        .then((response) => response.json())
        .then((result) => {
          const allEvents: Array<EventsForSubsystem> = result
          const treeData: Array<DataNode> = allEvents.map(a => {
              const node: DataNode = {
                key: a.subsystem,
                title: a.subsystem,
                children: a.components.map(c => {
                    const child: DataNode = {
                      key: `${a.subsystem}.${c.component}`,
                      title: c.component,
                      children: c.events.map(e => {
                          const leaf: DataNode = {
                            key: `${a.subsystem}.${c.component}.${e.event}`,
                            title: e.event,
                            isLeaf: true
                          }
                          return leaf
                        }
                      )
                    }
                    return child
                  }
                )
              }
              return node
            }
          )
          setEventTreeData(treeData)
        })
    }

    getData()
  }, [])



  return (
      <Layout className='App'>
        <Topbar/>
        <Content>
          <MainWindow/>
          <EventTreeDrawer eventTreeData={eventTreeData}/>
          <Settings/>
        </Content>
      </Layout>
  )
}
export default App
