package csw.eventmon.client

import com.github.ahnfelt.react4s._
import csw.eventmon.client.Navbar._
import csw.eventmon.client.SaveComponent._
import org.scalajs.dom.ext.LocalStorage

import scala.concurrent.ExecutionContext.Implicits.global
import MainComponent._
import csw.eventmon.client.ControlComponent._
import csw.params.events.Event

import scala.scalajs.js

object MainComponent {
  val localStorageKey = "csw-event-monitor"
}

// This is the main component for the event monitor.
// The other components emit values that are handled here or delegated to other components.
case class MainComponent() extends Component[NoEmit] {

  // Gateway to server side
  private val gateway = new WebGateway()

  // Event service web client
  private val eventClient = new EventJsClient(gateway)

  // Set of currently selected events
  private val eventFieldSelections = State[Set[EventFieldSelection]](Set.empty)

  // Gives you the stream for reading each selected event
  private val eventStreamMap = State[Map[EventSelection, EventStream[Event]]](Map.empty)

  // If you are plotting multiple fields from the same event, this maps the basic event selection to the fields being plotted
  private val eventSelectionMap = State[Map[EventSelection, Set[EventFieldSelection]]](Map.empty)

  // Set to true to pause live updating of the charts
  private val paused = State[Boolean](false)

  // Contains the contents of browser local storage, which maps names to a set of events to plot
  private val localStorageMap = State[Map[String, Set[EventFieldSelection]]](loadFromLocalStorage())

  // Options that apply to all of the charts
  private val controlOptions = State[ControlOption](ControlOption())

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

  // Adds the current config to local browser storage
  private def saveToLocalStorage(get: Get, name: String): Unit = {
    import upickle.default._
    val map = LocalStorage(localStorageKey) match {
      case Some(json) =>
        read[Map[String, Set[EventFieldSelection]]](json) + (name -> get(eventFieldSelections))
      case None =>
        Map(name -> get(eventFieldSelections))
    }
    LocalStorage(localStorageKey) = write(map)
    localStorageMap.set(map)
  }

  // Loads the saved configs from local browser storage
  private def loadFromLocalStorage(): Map[String, Set[EventFieldSelection]] = {
    import upickle.default._
    LocalStorage(localStorageKey) match {
      case Some(json) => read[Map[String, Set[EventFieldSelection]]](json)
      case None       => Map.empty
    }
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

  // Handler for navbar items
  private def navbarHandler(get: Get)(cmd: NavbarCommand): Unit = {
    cmd match {
      case AddEventFieldSelection(eventFieldSelection) => addEvent(get)(eventFieldSelection)
      case SaveConfig(settings)                        => saveConfig(get, settings)
      case LoadConfig(events)                          => loadConfig(get, events)
      case Pause(p)                                    => paused.set(p)
      case UpdateControlOptions(opts)                  => controlOptions.set(opts)
      case x                                           => println(s"XXX Not implemented: $x")
    }
  }

  override def render(get: Get): Node = {
    println(s"MainComponent render")
    val charts = get(eventSelectionMap).keySet.toList.map { eventSelection =>
      val eventSelections = get(eventSelectionMap)(eventSelection).toList
      val eventStream     = get(eventStreamMap)(eventSelection)
      Component(SingleEventStreamChart, eventSelections, eventStream, get(controlOptions), get(paused))
    }
    val numPlots = get(eventFieldSelections).size
    E.div(
      A.className("container"),
      Component(Navbar, eventClient, numPlots, get(localStorageMap), get(controlOptions)).withHandler(navbarHandler(get)),
      E.p(),
      Tags(charts),
    )
  }

}
