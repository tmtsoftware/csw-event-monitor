package csw.eventmon.client

import com.github.ahnfelt.react4s._
import csw.eventmon.client.Navbar.{AddEventSelection, NavbarCommand, SaveConfig}
import csw.eventmon.client.SaveComponent.{SaveSettings, SaveToConfigService, SaveToFile, SaveToLocalStorage}
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
    println(s"XXX Saving to local storage: $map")
  }

  // Call when the user clicks Save and selects and name and type of save (file, config service, etc.)
  private def saveConfig(get: Get)(settings: SaveSettings): Unit = {
    settings.saveType match {
      case SaveToLocalStorage => saveToLocalStorage(get, settings.name)
      case SaveToFile =>
      case SaveToConfigService =>
    }
  }

  private def navbarHandler(get: Get)(cmd: NavbarCommand): Unit = {
    cmd match {
      case AddEventSelection(eventSelection) => addEvent(get)(eventSelection)
      case SaveConfig(settings) => saveConfig(get)(settings)
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
