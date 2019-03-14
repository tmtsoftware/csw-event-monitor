package csw.eventmon.client

import com.github.ahnfelt.react4s._
import csw.eventmon.client.Navbar._
import csw.eventmon.client.SaveComponent._
import org.scalajs.dom.ext.LocalStorage

import scala.concurrent.ExecutionContext.Implicits.global
import MainComponent._
import csw.eventmon.client.ControlComponent.ControlOption
import csw.params.events.Event
import upickle.default.{ReadWriter => RW, macroRW}

import scala.scalajs.js

object MainComponent {
  val localStorageKey = "csw-event-monitor"

  // Used to save chart config to file or local storage
  case class SaveInfo(events: Set[EventFieldSelection], settings: ControlOption)
  case object SaveInfo {
    implicit val rw: RW[SaveInfo] = macroRW
  }
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
  private val localStorageMap = State[Map[String, SaveInfo]](loadFromLocalStorage())

  // Options that apply to all of the charts
  private val controlOptions = State[ControlOption](ControlOption())

  // Call when the user adds an event subscription
  private def addEvent(get: Get)(eventFieldSelection: EventFieldSelection): Unit = {
    if (!get(eventFieldSelections).contains(eventFieldSelection)) {
      val eventSelection = eventFieldSelection.eventSelection
      if (!get(eventStreamMap).contains(eventSelection)) {
        val eventStream =
          eventClient.subscribe(eventSelection.subsystem.toLowerCase(),
                                eventSelection.maybeComponent,
                                eventSelection.maybeName,
                                eventSelection.maybeRateLimit)
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
    val map = get(localStorageMap) + (name -> SaveInfo(get(eventFieldSelections), get(controlOptions)))
    LocalStorage(localStorageKey) = write(map)
    localStorageMap.set(map)
  }

  // Loads the saved configs from local browser storage
  private def loadFromLocalStorage(): Map[String, SaveInfo] = {
    import upickle.default._
    LocalStorage(localStorageKey) match {
      case Some(json) =>
        try {
          read[Map[String, SaveInfo]](json)
        } catch {
          case ex: Exception =>
            ex.printStackTrace()
            Map.empty
        }
      case None => Map.empty
    }
  }

  // Downloads the json for the current config.
  // See https://stackoverflow.com/questions/19721439/download-json-object-as-a-file-from-browser/30800715#30800715
  private def saveToFile(get: Get, name: String): Unit = {
    import upickle.default._
    val json               = write(SaveInfo(get(eventFieldSelections), get(controlOptions)))
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

  // Called when the user clicks Save and selects and name and type of save (file, config service, etc.)
  private def saveConfig(get: Get, settings: SaveSettings): Unit = {
    settings.saveType match {
      case SaveToLocalStorage => saveToLocalStorage(get, settings.name)
      case SaveToFile         => saveToFile(get, settings.name)
    }
  }

  // Called when the user selects saved local storage (config) items to delete (resulting in a new map of saved items)
  private def manageConfig(get: Get, map: Map[String, SaveInfo]): Unit = {
    import upickle.default._
    LocalStorage(localStorageKey) = write(map)
    localStorageMap.set(map)
  }

  // Called when the user selects a previously saved configuration (list of events) to load.
  // Unsubscribe to all current events and subscribe to the new ones.
  private def loadConfig(get: Get, saveInfo: SaveInfo): Unit = {
    eventSelectionMap.set(Map.empty)
    eventFieldSelections.set(Set.empty)
    get(eventStreamMap).values.foreach(_.close())
    eventStreamMap.set(Map.empty)
    controlOptions.set(saveInfo.settings)
    saveInfo.events.foreach(addEvent(get))
  }

  // Handler for navbar items
  private def navbarHandler(get: Get)(cmd: NavbarCommand): Unit = {
    cmd match {
      case AddEventFieldSelection(eventFieldSelection) => addEvent(get)(eventFieldSelection)
      case SaveConfig(settings)                        => saveConfig(get, settings)
      case ManageConfig(map)                           => manageConfig(get, map)
      case LoadConfig(saveInfo)                        => loadConfig(get, saveInfo)
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
