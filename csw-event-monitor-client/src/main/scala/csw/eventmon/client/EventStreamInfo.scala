package csw.eventmon.client
import csw.params.events.Event

case class EventStreamInfo(eventSelection: EventSelection, eventStream: EventStream[Event]) {
  override def equals(obj: Any): Boolean = eventSelection == obj.asInstanceOf[EventStreamInfo].eventSelection
}
