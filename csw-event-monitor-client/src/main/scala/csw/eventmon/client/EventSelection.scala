package csw.eventmon.client

case object EventSelection {
  import upickle.default.{ReadWriter => RW, macroRW}
  implicit val rw: RW[EventSelection] = macroRW
}

case class EventSelection(subsystem: String, maybeComponent: Option[String], maybeName: Option[String]) {
  override def toString: String = s"$subsystem-${maybeComponent.getOrElse("")}-${maybeName.getOrElse("")}"
}
