package csw.eventmon.client
import csw.params.events.Event

case class EventStreamInfo(eventSelection: EventSelection, eventStream: EventStream[Event])
