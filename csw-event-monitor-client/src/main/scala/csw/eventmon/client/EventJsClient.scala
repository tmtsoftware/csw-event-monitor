package csw.eventmon.client

import csw.params.core.formats.JsonSupport
import csw.params.events.Event

// From esw-prototype
class EventJsClient(gateway: WebGateway) extends JsonSupport {
  def subscribe(subsystem: String, component: Option[String] = None, event: Option[String] = None): EventStream[Event] = {
    val componentAttr = component.map(c => s"component=$c")
    val eventAttr     = event.map(e => s"event=$e")
    val attrs         = (componentAttr ++ eventAttr).mkString("&")
    val attrStr       = if (attrs.isEmpty) "" else s"?$attrs"
    gateway.stream[Event](s"/events/subscribe/$subsystem$attrStr")
  }
}
