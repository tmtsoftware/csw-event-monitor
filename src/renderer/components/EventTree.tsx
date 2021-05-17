import React, {Key, useState} from 'react'
import {Button, Input, Tree, Typography} from 'antd';
import wcmatch from 'wildcard-match'
import {DataNode} from 'antd/lib/tree';
import {ExpandAltOutlined, ShrinkOutlined} from "@ant-design/icons";
import {EventDataNode} from "rc-tree/lib/interface";
import {useAppContext} from "../AppContext";
import {Event, EventKey, EventName, Prefix, Subscription, Subsystem, SystemEvent} from "@tmtsoftware/esw-ts";
import {EventSubscription} from "../data/EventSubscription";
import {EventInfoModel, EventModel, EventUtil, IcdServerInfo} from "../data/EventTreeData";

const {DirectoryTree} = Tree;
const {Search} = Input;
const {Text} = Typography;

type EventTreeProps = {
  eventTreeData: Array<DataNode>
}

// XXX issue with setSystemEvents(systemEvents.concat([systemEvent]))
let receivedSystemEvents: Map<string, Array<SystemEvent>> = new Map()

export const EventTree = ({eventTreeData}: EventTreeProps): JSX.Element => {

  const wildcardMatchAll = `*${EventUtil.eventKeySeparator}*${EventUtil.eventKeySeparator}*`
  const [eventTreeFilter, setEventTreeFilter] = useState<string>(wildcardMatchAll)
  const [expandedKeys, setExpandedKeys] = useState<Array<string>>([])
  const isMatch = wcmatch(eventTreeFilter && eventTreeFilter.length != 0 ? eventTreeFilter + '*' : wildcardMatchAll)
  const {
    eventService,
    subscriptions,
    setSubscriptions,
    eventInfoModels,
    setEventInfoModels,
    setSystemEvents
  } = useAppContext()

  // Temp: Max events to keep for a single key
  const maxEvents = 100

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
    if (eventTreeFilter.length == 0 || eventTreeFilter == wildcardMatchAll)
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

  function getEventModel(subsystem: string, component: string, event: string) {
    const encodedEvent = encodeURIComponent(event)
    fetch(`${IcdServerInfo.baseUri}/eventInfo/${subsystem}/${component}/${encodedEvent}`)
      .then((response) => response.json())
      .then((data) => {
        const eventModel: EventModel = data
        const eventInfoModel: EventInfoModel = {subsystem, component, eventModel}
        setEventInfoModels(eventInfoModels.filter((x) =>
          !(x.eventModel.name == eventModel.name && x.component == component && x.subsystem == subsystem)
        ).concat([eventInfoModel]))
      })
  }

  // Called when a SystemEvent is received from the event service
  const onEventCallback = (event: Event) => {
    const systemEvent = event as SystemEvent
    console.log('XXX received event: ', systemEvent)
    const sep = EventUtil.eventKeySeparator
    const key = `${systemEvent.source.subsystem.toString()}${sep}${systemEvent.source.componentName}${sep}${systemEvent.eventName.name}`
    const map = new Map(receivedSystemEvents)
    if (map.has(key)) {
      const ar = map.get(key)
      if (ar) {
        if (ar.length >= maxEvents)
          ar.shift()
        ar.push(systemEvent)
      }
    } else {
      map.set(key, [systemEvent])
    }
    receivedSystemEvents = map
    setSystemEvents(map)
  }

  // Double-click on an event name in the tree: Subscribe to the event and display the received values
  function onDoubleClick(_: React.MouseEvent, node: EventDataNode) {
    console.log(`double-click ${node.key}`)
    const ar = node.key.toString().split(EventUtil.eventKeySeparator)
    if (ar.length == 3) {
      if (eventService) {
        const [subsystemName, componentName, eventName] = ar
        const existingSubscription = subscriptions.find(s =>
          s.event == eventName && s.component == componentName && s.subsystem == subsystemName)
        if (!existingSubscription) {
          getEventModel(subsystemName, componentName, eventName)
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
        }
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
    setExpandedKeys(getAllKeys(filterTreeData(eventTreeData)))
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
        treeData={filterTreeData(eventTreeData)}>
      </DirectoryTree>
    </div>
  )
}
