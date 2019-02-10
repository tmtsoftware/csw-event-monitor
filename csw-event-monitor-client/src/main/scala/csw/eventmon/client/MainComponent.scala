package csw.eventmon.client

import com.github.ahnfelt.react4s._
import csw.eventmon.client.EventSelector.EventSelection
import csw.params.events.{Event, SystemEvent}

import scala.concurrent.ExecutionContext.Implicits.global

object MainComponent {
  val titleStr = "CSW Event Monitor"

  case class EventStreamInfo(eventSelection: EventSelection, eventStream: EventStream[Event])
}

case class MainComponent() extends Component[NoEmit] {
  import MainComponent._

  private val title       = E.div(A.className("row"), E.div(A.className("col s6  teal lighten-2"), Text(titleStr)))
  private val gateway     = new WebGateway()
  private val eventClient = new EventJsClient(gateway)
  private val eventStreams = State[List[EventStreamInfo]](Nil)

  override def render(get: Get): Element = {
    val eventSelector = Component(EventSelector).withHandler(e => addEvent(get)(e))

    E.div(
      A.className("container"),
      title,
      eventSelector
    )
  }

  private def addEvent(get: Get)(eventSelection: EventSelection): Unit = {
    println(s"XXX Add event: $eventSelection")
    val eventStream = eventClient.subscribe(eventSelection.subsystem.toLowerCase(), eventSelection.maybeComponent, eventSelection.maybeName)
    val eventStreamInfo = EventStreamInfo(eventSelection, eventStream)
    eventStreams.set(eventStreamInfo :: get(eventStreams))
    // Handle events
    eventStream.onNext = {
      case event: SystemEvent =>
        println(s"Received system event: $event")
      case event =>
        println(s"Received unexpected event: $event")
    }
  }

}
