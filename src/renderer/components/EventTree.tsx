import React, {Key} from 'react'
import {useAppContext} from "../AppContext"
import {Tree} from 'antd';
import {DownOutlined} from '@ant-design/icons';

// type EventTreeProps = {
//   xxx: string
// }

import allEventsJson from '../data/events.json'
import {DataNode} from 'antd/lib/tree';

interface Event {
  event: string
}

interface EventsForComponent {
  component: string
  events: Array<Event>
}

interface EventsForSubsystem {
  subsystem: string
  components: Array<EventsForComponent>
}

const allEvents = allEventsJson as Array<EventsForSubsystem>

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
                  title: e.event
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


export const EventTree = (): JSX.Element => {

  // const {updateDisplay} = useAppContext()

  function onSelect(selectedKeys: Array<Key>, info: object) {
    console.log('selected', selectedKeys, info);
  }

  return (
    <Tree
      style={{height: '100%'}}
      showLine
      switcherIcon={<DownOutlined/>}
      onSelect={onSelect}
      treeData={treeData}>
    </Tree>
  )
}
