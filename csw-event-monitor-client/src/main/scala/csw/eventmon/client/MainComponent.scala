package csw.eventmon.client

import com.github.ahnfelt.react4s._

import scala.concurrent.ExecutionContext.Implicits.global

object MainComponent {
  val titleStr = "CSW Event Monitor"
}

case class MainComponent() extends Component[NoEmit] {
  import MainComponent._

  private val title           = E.div(A.className("row"), E.div(A.className("col s6  teal lighten-2"), Text(titleStr)))
  private val gateway         = new WebGateway()
  private val eventClient     = new EventJsClient(gateway)
  private val eventSelections = State[Set[EventSelection]](Set.empty)
  private val eventStreams    = State[List[EventStreamInfo]](Nil)

  // Call when the user adds an event subscription
  private def addEvent(get: Get)(eventSelection: EventSelection): Unit = {
    if (!get(eventSelections).contains(eventSelection)) {
      println(s"XXX Add event: $eventSelection")
      eventSelections.modify(_ + eventSelection)
      val eventStream =
        eventClient.subscribe(eventSelection.subsystem.toLowerCase(), eventSelection.maybeComponent, eventSelection.maybeName)
      val eventStreamInfo = EventStreamInfo(eventSelection, eventStream)
      eventStreams.modify(es => eventStreamInfo :: es)
    }
  }

  override def render(get: Get): Element = {
    println(s"XXX MainComponent render: eventStreams = ${get(eventStreams)}")
    val eventSelector = Component(EventSelectorComponent).withHandler(e => addEvent(get)(e))
    val stripChart    = Component(StripChart, get(eventStreams))

    E.div(
      A.className("container"),
      title,
      eventSelector,
      stripChart
    )
  }

}
