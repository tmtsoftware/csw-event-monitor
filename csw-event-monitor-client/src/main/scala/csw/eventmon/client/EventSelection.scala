package csw.eventmon.client
import upickle.default.{ReadWriter => RW, macroRW}

case object EventSelection {
  implicit val rw: RW[EventSelection] = macroRW
}

/**
 * Represents a selected event
 */
case class EventSelection(subsystem: String, maybeComponent: Option[String], maybeName: Option[String],
                          maybeRateLimit: Option[Int] = None) {
  def id: String = s"$subsystem-${maybeComponent.getOrElse("")}-${maybeName.getOrElse("")}"
  def title: String = s"Subsystem: $subsystem, Component: ${maybeComponent.getOrElse("*")}, Event: ${maybeName.getOrElse("*")}"
}

case object EventFieldSelection {
  implicit val rw: RW[EventFieldSelection] = macroRW
}

/**
 * Represents an optional selected field in an event
 */
case class EventFieldSelection(eventSelection: EventSelection, maybeEventField: Option[String]) {
  def id: String = s"${eventSelection.id}-${maybeEventField.getOrElse("")}"
  def title: String = s"${eventSelection.title}, Field: ${maybeEventField.getOrElse("*")}, Rate Limit: ${eventSelection.maybeRateLimit.getOrElse("none")}"
}
