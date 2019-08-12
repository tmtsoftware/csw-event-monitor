package csw.eventmon.client

import csw.params.core.formats.JsonSupport
import csw.params.events.{Event, SystemEvent}
import org.scalajs.dom.EventSource
import play.api.libs.json.Json

// From esw-prototype
class EventStream(eventSource: EventSource) {

  var onNext: Event => Unit = { _ =>
    ()
  }

  eventSource.onmessage = { messageEvent =>
    val data = messageEvent.data.toString
    if (data.nonEmpty) {
      val event = JsonSupport.readEvent[SystemEvent](Json.parse(data))
      onNext(event)
    }
  }

  def close(): Unit = eventSource.close()
}
