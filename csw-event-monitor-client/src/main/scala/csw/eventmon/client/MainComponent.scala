package csw.eventmon.client

import com.github.ahnfelt.react4s._
import csw.eventmon.client.Navbar.{AddEventFieldSelection, LoadConfig, NavbarCommand, SaveConfig}
import csw.eventmon.client.SaveComponent.{SaveSettings, SaveToFile, SaveToLocalStorage}
import org.scalajs.dom.ext.LocalStorage

import scala.concurrent.ExecutionContext.Implicits.global
import MainComponent._
import csw.params.events.Event

object MainComponent {
  val localStorageKey = "csw-event-monitor"
}

case class MainComponent() extends Component[NoEmit] {

  private val gateway              = new WebGateway()
  private val eventClient          = new EventJsClient(gateway)
  private val eventFieldSelections = State[Set[EventFieldSelection]](Set.empty)
  private val eventStreamMap       = State[Map[EventSelection, EventStream[Event]]](Map.empty)
  private val eventSelectionMap    = State[Map[EventSelection, Set[EventFieldSelection]]](Map.empty)

  // Call when the user adds an event subscription
  private def addEvent(get: Get)(eventFieldSelection: EventFieldSelection): Unit = {
    if (!get(eventFieldSelections).contains(eventFieldSelection)) {
      val eventSelection = eventFieldSelection.eventSelection
      val eventStream = if (!get(eventStreamMap).contains(eventSelection)) {
        val es =
          eventClient.subscribe(eventSelection.subsystem.toLowerCase(), eventSelection.maybeComponent, eventSelection.maybeName)
        eventStreamMap.modify(_ + (eventSelection    -> es))
        eventSelectionMap.modify(_ + (eventSelection -> Set(eventFieldSelection)))
        es
      } else {
        get(eventStreamMap)(eventFieldSelection.eventSelection)
      }
      val fields = get(eventSelectionMap)(eventSelection) + eventFieldSelection
      eventSelectionMap.modify(_ + (eventSelection -> fields))
      eventFieldSelections.modify(_ + eventFieldSelection)
    }
  }

  private def saveToLocalStorage(get: Get, name: String): Unit = {
    import upickle.default._
    val map = LocalStorage(localStorageKey) match {
      case Some(json) =>
        read[Map[String, Set[EventFieldSelection]]](json) + (name -> get(eventFieldSelections))
      case None =>
        Map(name -> get(eventFieldSelections))
    }
    LocalStorage(localStorageKey) = write(map)
  }

  // Call when the user clicks Save and selects and name and type of save (file, config service, etc.)
  private def saveConfig(get: Get, settings: SaveSettings): Unit = {
    settings.saveType match {
      case SaveToLocalStorage => saveToLocalStorage(get, settings.name)
      case SaveToFile         => // XXX FIXME TODO
    }
  }

  // Called when the user selects a previously saved configuration (list of events) to load.
  // Unsubscribe to all current events and subscribe to the new ones.
  private def loadConfig(get: Get, events: Set[EventFieldSelection]): Unit = {
    get(eventStreamMap).values.foreach(_.close())
    eventStreamMap.set(Map.empty)
    eventSelectionMap.set(Map.empty)
    eventFieldSelections.set(Set.empty)
    events.foreach(addEvent(get))
  }

  private def navbarHandler(get: Get)(cmd: NavbarCommand): Unit = {
    cmd match {
      case AddEventFieldSelection(eventFieldSelection) => addEvent(get)(eventFieldSelection)
      case SaveConfig(settings)                        => saveConfig(get, settings)
      case LoadConfig(events)                          => loadConfig(get, events)
      case x                                           => println(s"XXX Not implemented: $x")
    }
  }

  override def render(get: Get): Node = {
    val charts = get(eventSelectionMap).keySet.toList.map { eventSelection =>
      val eventSelections = get(eventSelectionMap)(eventSelection).toList
      val eventStream     = get(eventStreamMap)(eventSelection)
      Component(SingleEventStreamChart, eventSelections, eventStream)
    }
    E.div(
      A.className("container"),
      Component(Navbar).withHandler(navbarHandler(get)),
      E.p(),
//      Component(StripChart, get(eventStreamInfoList))
      Tags(charts)
    )
  }

}
