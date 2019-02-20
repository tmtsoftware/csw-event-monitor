package csw.eventmon.client

case class EventSelection(subsystem: String, maybeComponent: Option[String], maybeName: Option[String]) {
  override def toString: String = s"$subsystem-${maybeComponent.getOrElse("")}-${maybeName.getOrElse("")}"
}
