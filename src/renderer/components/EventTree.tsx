import React, {Key} from 'react'
import {useAppContext} from "../AppContext"
import {Tree} from 'antd';
import wcmatch from 'wildcard-match'

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

const {DirectoryTree} = Tree;

export const EventTree = (): JSX.Element => {

  const {eventTreeFilter} = useAppContext()
  const isMatch = wcmatch(eventTreeFilter && eventTreeFilter.length != 0 ? eventTreeFilter : '*.*.*')

  function filterTreeNode(node: DataNode): boolean {
    if (node.children)
      return node.children.filter(filterTreeNode).length > 0
    if (node.isLeaf)
      return isMatch(node.key.toString())
    return false
  }

  function filterTreeData(ar: Array<DataNode>): Array<DataNode> {
    // return ar.filter(filterTreeNode)
    return ar.filter(filterTreeNode).map(node => {
      return {
        key: node.key,
        title: node.title,
        children: node.children ? filterTreeData(node.children) : node.children,
        isLeaf: node.isLeaf
      }
    })
  }

  function onSelect(selectedKeys: Array<Key>, info: object) {
    console.log('selected', selectedKeys, info);
  }

  return (
    <DirectoryTree
      onSelect={onSelect}
      treeData={filterTreeData(treeData)}>
    </DirectoryTree>
  )
}
