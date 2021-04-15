import React, {Key, useState} from 'react'
import {Button, Input, Tree, Typography} from 'antd';
import wcmatch from 'wildcard-match'
import {DataNode} from 'antd/lib/tree';
import {ExpandAltOutlined, ShrinkOutlined} from "@ant-design/icons";
import {EventDataNode} from "rc-tree/lib/interface";
import {useAppContext} from "../AppContext";
import {Event, EventKey, EventName, Prefix, Subscription, Subsystem} from "@tmtsoftware/esw-ts";
import {EventTreeData} from "../data/EventTreeData";
import {EventSubscription} from "../data/EventSubscription";


const treeData: Array<DataNode> = EventTreeData.allEvents.map(a => {
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
const {Search} = Input;
const {Text} = Typography;

export const EventTree = (): JSX.Element => {

  const [eventTreeFilter, setEventTreeFilter] = useState<string>('*.*.*')
  const [expandedKeys, setExpandedKeys] = useState<Array<string>>([])
  const isMatch = wcmatch(eventTreeFilter && eventTreeFilter.length != 0 ? eventTreeFilter + '*' : '*.*.*')
  const {eventService, subscriptions, setSubscriptions} = useAppContext()

  function onChange(e: React.ChangeEvent<HTMLInputElement>) {
    setEventTreeFilter(e.currentTarget.value)
    expandTree()
  }

  function onExpand(keys: Key[]) {
    setExpandedKeys(keys.map(k => k.toString()))
  }

  function filterTreeNode(node: DataNode): boolean {
    if (node.children)
      return node.children.filter(filterTreeNode).length > 0
    if (node.isLeaf)
      return isMatch(node.key.toString())
    return false
  }

  function filterTreeData(ar: Array<DataNode>): Array<DataNode> {
    if (eventTreeFilter.length == 0 || eventTreeFilter == '*.*.*')
      return ar
    return ar.filter(filterTreeNode).map(node => {
      return {
        key: node.key,
        title: node.title,
        children: node.children ? filterTreeData(node.children) : node.children,
        isLeaf: node.isLeaf
      }
    })
  }

  const onEventCallback = (event: Event) => {
    console.log(event)
    // make use of ${event} inside this callback function
  }

  function onDoubleClick(_: React.MouseEvent, node: EventDataNode) {
    console.log(`double-click ${node.key}`)
    const ar = node.key.toString().split('.')
    console.log(`XXX ar = ${ar}, len = ${ar.length}`)
    if (ar.length == 3) {
      if (eventService) {
        const [subsystemName, componentName, eventName] = ar
        const subsystem: Subsystem = subsystemName as Subsystem
        const sourcePrefix = new Prefix(subsystem, componentName)
        const eventKey = new EventKey(sourcePrefix, new EventName(eventName))
        const eventKeys = new Set([eventKey])
        const subscription: Subscription = eventService.subscribe(eventKeys, 1)(onEventCallback)
        const eventSubscription: EventSubscription = {
          subscription: subscription,
          subsystem: subsystemName,
          component: componentName,
          event: eventName
        }
        setSubscriptions(subscriptions.concat(eventSubscription))
      } else {
        console.log("No event service!")
      }
    }
  }

  function getAllKeys(data: Array<DataNode>): Array<string> {
    return data.flatMap(node => {
      return [node.key.toString()].concat(node.children ? node.children.map(n => n.key.toString()) : [])
    })
  }

  function expandTree() {
    setExpandedKeys(getAllKeys(filterTreeData(treeData)))
  }

  function collapseTree() {
    setExpandedKeys([])
  }

  const spacing = {margin: '0px 5px 15px 0'}

  return (
    <div>
      <div>
        <Text
          strong
          style={{margin: '0px 10px 15px 0'}}>
          Events
        </Text>
        <Button
          type="ghost"
          icon={<ExpandAltOutlined/>}
          size={"middle"}
          title={'Expand all'}
          onClick={expandTree}
          style={spacing}/>
        <Button
          type="ghost"
          icon={<ShrinkOutlined/>}
          size={"middle"}
          title={'Collapse all'}
          onClick={collapseTree}
          style={spacing}/>
        <Search
          placeholder="Filter events"
          value={eventTreeFilter}
          onChange={onChange}
          style={{margin: spacing.margin, width: 200}}
          title={'Filter event tree. Use wildcards like *, ? for subsystem, component, event names'}/>
      </div>
      <DirectoryTree
        multiple={false}
        onDoubleClick={onDoubleClick}
        expandedKeys={expandedKeys}
        onExpand={onExpand}
        treeData={filterTreeData(treeData)}>
      </DirectoryTree>
    </div>
  )
}
