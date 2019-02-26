package csw.eventmon.client

import com.github.ahnfelt.react4s._
import csw.eventmon.client.Navbar.{AddEventSelection, LoadConfig, NavbarCommand, SaveConfig}
import csw.eventmon.client.SaveComponent.{SaveSettings, SaveToFile, SaveToLocalStorage}
import org.scalajs.dom.ext.LocalStorage

import scala.concurrent.ExecutionContext.Implicits.global
import MainComponent._

object MainComponent {
  val localStorageKey = "csw-event-monitor"
}

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

  private def saveToLocalStorage(get: Get, name: String): Unit = {
    import upickle.default._
    val map = LocalStorage(localStorageKey) match {
      case Some(json) =>
        read[Map[String, Set[EventSelection]]](json) + (name -> get(eventSelections))
      case None =>
        Map(name -> get(eventSelections))
    }
    LocalStorage(localStorageKey) = write(map)
  }

  // Call when the user clicks Save and selects and name and type of save (file, config service, etc.)
  private def saveConfig(get: Get, settings: SaveSettings): Unit = {
    settings.saveType match {
      case SaveToLocalStorage => saveToLocalStorage(get, settings.name)
      case SaveToFile =>
    }
  }

  // Called when the user selects a previously saved configuration (list of events) to load.
  // Unsubscribe to all current events and subscribe to the new ones.
  private def loadConfig(get: Get, events: Set[EventSelection]): Unit = {
    get(eventStreams).foreach(_.eventStream.close())
    eventStreams.set(Nil)
    eventSelections.set(Set.empty)
    events.foreach(addEvent(get))
  }

  private def navbarHandler(get: Get)(cmd: NavbarCommand): Unit = {
    cmd match {
      case AddEventSelection(eventSelection) => addEvent(get)(eventSelection)
      case SaveConfig(settings) => saveConfig(get, settings)
      case LoadConfig(events) => loadConfig(get, events)
      case x => println(s"XXX Not implemented: $x")
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
