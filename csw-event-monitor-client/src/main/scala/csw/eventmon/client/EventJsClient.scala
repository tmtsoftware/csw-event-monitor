package csw.eventmon.client

import csw.params.core.formats.JsonSupport

class EventJsClient(gateway: WebGateway) extends JsonSupport {

  /**
  *  Subscribes to events based on the given arguments.
    * @param subsystem  the subsystem name
    * @param component the component name
    * @param event the event name
    * @param rateLimit optional rate limit
    * @return the event stream
    */
  def subscribe(subsystem: String,
                component: Option[String] = None,
                event: Option[String] = None,
                rateLimit: Option[Int] = None): EventStream = {
    val componentAttr = component.map(c => s"component=$c")
    val eventAttr     = event.map(e => s"event=$e")
    val rateLimitAttr = rateLimit.map(rl => s"rateLimit=$rl")
    val attrs         = (componentAttr ++ eventAttr ++ rateLimitAttr).mkString("&")
    val attrStr       = if (attrs.isEmpty) "" else s"?$attrs"

    gateway.stream(s"/events/subscribe/$subsystem$attrStr")
  }
}
