package csw.eventmon.client

import com.github.ahnfelt.react4s._
import csw.eventmon.client.EventSelector.EventSelection

import scala.concurrent.ExecutionContext.Implicits.global

object MainComponent {
  val titleStr = "CSW Event Monitor"
}

case class MainComponent() extends Component[NoEmit] {
  import MainComponent._

  private val title       = E.div(A.className("row"), E.div(A.className("col s6  teal lighten-2"), Text(titleStr)))
  private val gateway     = new WebGateway()
  private val eventClient = new EventJsClient(gateway)
//  private val subsystem   = "test"
//  private val eventStream = eventClient.subscribe(subsystem)

//  // Handle events
//  eventStream.onNext = {
//    case event: SystemEvent =>
//      println(s"Received system event: $event")
//    case event =>
//      println(s"Received unexpected event: $event")
//  }

  override def render(get: Get): Element = {
    val eventSelector = Component(EventSelector).withHandler(e => addEvent(e))

    E.div(
      A.className("container"),
      title,
      eventSelector
    )
  }

  private def addEvent(e: EventSelection): Unit = {
    println(s"XXX Add event: $e")
  }

}
