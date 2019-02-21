package csw.eventmon.client

import com.github.ahnfelt.react4s._
import csw.eventmon.client.Navbar.{AddEventSelection, NavbarCommand}

import scala.concurrent.ExecutionContext.Implicits.global

case class MainComponent() extends Component[NoEmit] {

  private val gateway         = new WebGateway()
  private val eventClient     = new EventJsClient(gateway)
  private val eventSelections = State[Set[EventSelection]](Set.empty)
  private val eventStreams    = State[List[EventStreamInfo]](Nil)

  // Call when the user adds an event subscription
  private def addEvent(get: Get)(eventSelection: EventSelection): Unit = {
    if (!get(eventSelections).contains(eventSelection)) {
      eventSelections.modify(_ + eventSelection)
      val eventStream =
        eventClient.subscribe(eventSelection.subsystem.toLowerCase(), eventSelection.maybeComponent, eventSelection.maybeName)
      val eventStreamInfo = EventStreamInfo(eventSelection, eventStream)
      eventStreams.modify(es => eventStreamInfo :: es)
    }
  }

  private def navbarHandler(get: Get)(cmd: NavbarCommand): Unit = {
    cmd match {
      case AddEventSelection(es) => addEvent(get)(es)
    }
  }

  override def render(get: Get): Node = {
    E.div(
      A.className("container"),
      Component(Navbar).withHandler(navbarHandler(get)),
      E.p(),
      Component(StripChart, get(eventStreams))
    )
  }

}
