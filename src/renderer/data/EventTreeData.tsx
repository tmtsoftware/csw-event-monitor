import {DataNode} from "antd/lib/tree";

/**
 * An event name
 */
export interface Event {
  event: string
}

/**
 * The list of event names for a component
 */
export interface EventsForComponent {
  component: string
  events: Array<Event>
}

/**
 * List of all events for a subsystem, by component
 */
export interface EventsForSubsystem {
  subsystem: string
  components: Array<EventsForComponent>
}


/**
 * Defines the properties of a parameter
 *
 * @param name             name of the parameter
 * @param ref              if not empty, a reference to another parameter to copy missing values from
 *                         in the form component/section/name/paramSection/paramName (may be abbreviated, if in same scope)
 * @param refError contains an error message if ref is invalid (not stored in the db)
 * @param description      description of the parameter
 * @param maybeType        an optional string describing the type (either this or maybeEnum should be defined)
 * @param maybeEnum        an optional string describing the enum type (either this or maybeType should be defined)
 * @param maybeArrayType   if type is array, this should be the type of the array elements
 * @param maybeDimensions  if type is array, this should be the sizes of each dimension (optional)
 * @param units            description of the value's units
 * @param maxItems         optional max number of values in an array value (in string format)
 * @param minItems         optional min number of values in an array value (in string format)
 * @param minimum          optional max value (in string format)
 * @param maximum          optional min value (in string format)
 * @param exclusiveMinimum true if the min value in exclusive
 * @param exclusiveMaximum true if the max value in exclusive
 * @param defaultValue     default value (as a string, which may be empty)
 * @param typeStr          a generated text description of the type
 * @param parameterList   If type or array type is "struct", this should be a list of parameters in the struct
 */
export interface ParameterModel {
  name: string,
  ref: string,
  refError: string,
  description: string,
  maybeType?: string,
  maybeEnum?: Array<string>,
  maybeArrayType?: string,
  maybeDimensions?: Array<number>,
  units: string,
  maxItems?: number,
  minItems?: number,
  maxLength?: number,
  minLength?: number,
  minimum?: string,
  maximum?: string,
  exclusiveMinimum: boolean,
  exclusiveMaximum: boolean,
  allowNaN: boolean,
  defaultValue: string,
  typeStr: string,
  parameterList: Array<ParameterModel>
}


/**
 * Models the event published by a component
 *
 * @param name event name
 * @param ref if not empty, a reference to another event model in the
 *            form component/events/name, component/observeEvents/name, etc (may be abbreviated if in same component/section)
 * @param refError contains an error message if ref is invalid (not stored in the db)
 * @param description event description
 * @param requirements list of requirements that flow to this item
 * @param maybeMaxRate optional maximum rate of publishing in Hz
 * @param archive true if publisher recommends archiving this event
 * @param archiveDuration lifetime of the archiving (example: '2 years', '6 months'): Required if archive is true.
 * @param parameterList parameters for the event
 */
export interface EventModel {
  name: string,
  ref: string,
  refError: string,
  description: string,
  requirements: Array<string>,
  maybeMaxRate?: number,
  archive: boolean,
  archiveDuration: string,
  parameterList: Array<ParameterModel>
}

export interface EventInfoModel {
  subsystem: string,
  component: string,
  eventModel: EventModel
}

export interface ParamInfoModel {
  eventInfoModel: EventInfoModel,
  parameterName: string,
  units: string,
  description: string,
}

export class IcdServerInfo {
  static baseUri = import.meta.env.SNOWPACK_PUBLIC_API_URL
}

export class EventUtil {
  // Used to separate subsystem, component, event in keys for tree
  static eventKeySeparator = '::'

  static getEventKey(e: EventInfoModel): string {
    return `${e.subsystem}${EventUtil.eventKeySeparator}${e.component}${EventUtil.eventKeySeparator}${e.eventModel.name}`
  }

  static getParamKey(m: ParamInfoModel): string {
    return `${EventUtil.getEventKey(m.eventInfoModel)}${EventUtil.eventKeySeparator}${m.parameterName}`
  }

  static makeTreeData(allEvents: Array<EventsForSubsystem>): Array<DataNode> {
    return allEvents.map(a => {
        const node: DataNode = {
          key: a.subsystem,
          title: a.subsystem,
          children: a.components.map(c => {
              const child: DataNode = {
                key: `${a.subsystem}${EventUtil.eventKeySeparator}${c.component}`,
                title: c.component,
                children: c.events.map(e => {
                    const leaf: DataNode = {
                      key: `${a.subsystem}${EventUtil.eventKeySeparator}${c.component}${EventUtil.eventKeySeparator}${e.event}`,
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
  }

  static stripHtml(s: string): string {
    return s.replace(/(<([^>]+)>)/gi, "")
  }
}
