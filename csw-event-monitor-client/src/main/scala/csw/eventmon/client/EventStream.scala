package csw.eventmon.client

import org.scalajs.dom.EventSource
import play.api.libs.json.{Json, Reads}

// From esw-prototype
class EventStream[T: Reads](eventSource: EventSource) {

  var onNext: T => Unit = { _ =>
    ()
  }

  eventSource.onmessage = { messageEvent =>
    println("XXX got an event!")
    val data = messageEvent.data.toString
    if (data.nonEmpty) {
      val event = Json.parse(data).as[T]
      onNext(event)
    }
  }

  def close(): Unit = eventSource.close()
}
