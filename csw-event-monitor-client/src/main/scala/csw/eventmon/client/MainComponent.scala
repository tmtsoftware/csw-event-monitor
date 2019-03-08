package csw.eventmon.client

import com.github.ahnfelt.react4s._
import csw.eventmon.client.Navbar._
import csw.eventmon.client.SaveComponent._
import org.scalajs.dom.ext.LocalStorage

import scala.concurrent.ExecutionContext.Implicits.global
import MainComponent._
import csw.params.events.Event

import scala.scalajs.js

object MainComponent {
  val localStorageKey = "csw-event-monitor"
}

case class MainComponent() extends Component[NoEmit] {

  private val gateway              = new WebGateway()
  private val eventClient          = new EventJsClient(gateway)
  private val eventFieldSelections = State[Set[EventFieldSelection]](Set.empty)
  private val eventStreamMap       = State[Map[EventSelection, EventStream[Event]]](Map.empty)
  private val eventSelectionMap    = State[Map[EventSelection, Set[EventFieldSelection]]](Map.empty)
  private val paused               = State[Boolean](false)

  // Call when the user adds an event subscription
  private def addEvent(get: Get)(eventFieldSelection: EventFieldSelection): Unit = {
    if (!get(eventFieldSelections).contains(eventFieldSelection)) {
      val eventSelection = eventFieldSelection.eventSelection
      if (!get(eventStreamMap).contains(eventSelection)) {
        val eventStream =
          eventClient.subscribe(eventSelection.subsystem.toLowerCase(), eventSelection.maybeComponent, eventSelection.maybeName)
        eventStreamMap.modify(_ + (eventSelection    -> eventStream))
        eventSelectionMap.modify(_ + (eventSelection -> Set(eventFieldSelection)))
      }
      val fields = get(eventSelectionMap)(eventSelection) + eventFieldSelection
      eventSelectionMap.modify(_ + (eventSelection -> fields))
      eventFieldSelections.modify(_ + eventFieldSelection)
    }
  }

  // Saves the current config to local browser storage
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

  // Downloads the json for the current config.
  // See https://stackoverflow.com/questions/19721439/download-json-object-as-a-file-from-browser/30800715#30800715
  private def saveToFile(get: Get, name: String): Unit = {
    import upickle.default._
    val json               = write(get(eventFieldSelections))
    val fileName           = s"$name.json"
    val dataStr            = s"data:text/json;charset=utf-8,$json"
    val document           = js.Dynamic.global.document
    val downloadAnchorNode = document.createElement("a")
    downloadAnchorNode.setAttribute("href", dataStr)
    downloadAnchorNode.setAttribute("download", fileName)
    document.body.appendChild(downloadAnchorNode) // required for firefox
    downloadAnchorNode.click()
    downloadAnchorNode.remove()
  }

  // Call when the user clicks Save and selects and name and type of save (file, config service, etc.)
  private def saveConfig(get: Get, settings: SaveSettings): Unit = {
    settings.saveType match {
      case SaveToLocalStorage => saveToLocalStorage(get, settings.name)
      case SaveToFile         => saveToFile(get, settings.name)
    }
  }

  // Called when the user selects a previously saved configuration (list of events) to load.
  // Unsubscribe to all current events and subscribe to the new ones.
  private def loadConfig(get: Get, events: Set[EventFieldSelection]): Unit = {
    eventSelectionMap.set(Map.empty)
    eventFieldSelections.set(Set.empty)
    get(eventStreamMap).values.foreach(_.close())
    eventStreamMap.set(Map.empty)
    events.foreach(addEvent(get))
  }

  private def navbarHandler(get: Get)(cmd: NavbarCommand): Unit = {
    cmd match {
      case AddEventFieldSelection(eventFieldSelection) => addEvent(get)(eventFieldSelection)
      case SaveConfig(settings)                        => saveConfig(get, settings)
      case LoadConfig(events)                          => loadConfig(get, events)
      case Pause(p)                                    => paused.set(p)
      case x                                           => println(s"XXX Not implemented: $x")
    }
  }

  override def render(get: Get): Node = {
    println(s"MainComponent render")
    val charts = get(eventSelectionMap).keySet.toList.map { eventSelection =>
      val eventSelections = get(eventSelectionMap)(eventSelection).toList
      val eventStream     = get(eventStreamMap)(eventSelection)
      Component(SingleEventStreamChart, eventSelections, eventStream, get(paused))
    }
    E.div(
      A.className("container"),
      Component(Navbar, eventClient, get(eventFieldSelections).size).withHandler(navbarHandler(get)),
      E.p(),
      Tags(charts)
    )
  }

}
