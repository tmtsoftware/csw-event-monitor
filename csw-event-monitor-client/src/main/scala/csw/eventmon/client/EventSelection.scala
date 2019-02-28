package csw.eventmon.client
import upickle.default.{ReadWriter => RW, macroRW}

case object EventSelection {
  implicit val rw: RW[EventSelection] = macroRW
}

/**
 * Represents a selected event
 */
case class EventSelection(subsystem: String, maybeComponent: Option[String], maybeName: Option[String]) {
  override def toString: String = s"$subsystem-${maybeComponent.getOrElse("")}-${maybeName.getOrElse("")}"
}

case object EventFieldSelection {
  implicit val rw: RW[EventFieldSelection] = macroRW
}

/**
 * Represents an optional selected field in an event
 */
case class EventFieldSelection(eventSelection: EventSelection, maybeEventField: Option[String]) {
  override def toString: String = s"$eventSelection-${maybeEventField.getOrElse("")}"
}
