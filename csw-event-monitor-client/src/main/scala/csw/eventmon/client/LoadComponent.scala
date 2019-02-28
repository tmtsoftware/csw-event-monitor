package csw.eventmon.client

import com.github.ahnfelt.react4s._
import LoadComponent._
import csw.eventmon.client.MainComponent.localStorageKey
import org.scalajs.dom.ext.LocalStorage

import scala.scalajs.js

object LoadComponent {
  private val id = "loadConfig"
  private val nameSelectId = "loadNameSelect"

  sealed trait LoadType {
    val displayName: String
  }
  case object LoadFromLocalStorage extends LoadType {
    override val displayName = "Load from Local Storage"
  }
  case object LoadFromFile extends LoadType {
    override val displayName = "Load from File"
  }
  case object LoadFromConfigService extends LoadType {
    override val displayName = "Load from Config Service"
  }
  val loadTypes: List[LoadType] = List(LoadFromLocalStorage, LoadFromFile, LoadFromConfigService)
//  case class LoadSettings(name: String, loadType: LoadType)
}

// A modal dialog for adding events to subscribe to
case class LoadComponent() extends Component[Set[EventFieldSelection]] {
  private val selectedName     = State("")
  private val selectedLoadType = State[Option[LoadType]](None)
  private val savedConfigs     = State[Map[String, Set[EventFieldSelection]]](Map.empty)

  private def makeNameItem(get: Get): Element = {
    val map           = get(savedConfigs)
    val maybeLoadType = get(selectedLoadType)
    if (map.isEmpty || maybeLoadType.isEmpty) {
      E.div()
    } else {
      val names    = map.keySet.toList
      val loadType = maybeLoadType.get
      val items    = names.map(name => E.option(A.value(name), Text(name)))
      // Note: M.FormSelect.init() is called from main.scala.html once, but needs to be called again for dynamic <select> updates!
      val select = E.select(A.id(nameSelectId), A.onChangeText(nameSelected), Tags(items)).withRef { _ =>
        val document = js.Dynamic.global.document
        val elem = document.getElementById(nameSelectId).asInstanceOf[org.scalajs.dom.Element]
        M.FormSelect.init(elem, js.Object())
      }
      E.div(
        A.className("row"),
        E.div(A.className("input-field col s6"), select)
      )
    }
  }

  private def nameSelected(name: String): Unit = {
    println(s"XXX selected $name")
    selectedName.set(name)
  }

  private def loadTypeSelected(loadTypeStr: String): Unit = {
    val maybeLoadType = loadTypes.find(_.displayName == loadTypeStr)
    selectedLoadType.set(maybeLoadType)
    loadSavedConfigsForLoadType(maybeLoadType)
  }

  private def loadFromLocalStorage(): Map[String, Set[EventFieldSelection]] = {
    import upickle.default._
    LocalStorage(localStorageKey) match {
      case Some(json) => read[Map[String, Set[EventFieldSelection]]](json)
      case None       => Map.empty
    }
  }

  private def loadSavedConfigsForLoadType(maybeLoadType: Option[LoadType]): Unit = {
    val map = maybeLoadType match {
      case Some(loadType) =>
        loadType match {
          case LoadFromLocalStorage => loadFromLocalStorage()
          // XXX TODO
          case x => Map.empty[String, Set[EventFieldSelection]]
        }
      case None => Map.empty[String, Set[EventFieldSelection]]
    }
    println(s"XXX set savedConfigs to $map")
    savedConfigs.set(map)
  }

  private def makeLoadtypeItem(): Element = {
    val defaultItem =
      E.option(A.value("-"), A.disabled(), Text("Load from ..."))
    val items = defaultItem :: loadTypes.map(t => E.option(A.value(t.displayName), Text(t.displayName)))
    E.div(A.className("row"),
          E.div(A.className("input-field col s6"), E.select(A.onChangeText(loadTypeSelected), A.value("-"), Tags(items))))
  }

  private def makeButtons(get: Get): Element = {
//    val disabled = get(selectedName).isEmpty.toString
    E.div(
      A.className("modal-footer"),
      E.a(A.href("#!"), A.className("modal-close waves-effect waves-green btn-flat"), Text("Cancel")),
      E.a(A.href("#!"),
          A.className("modal-close waves-effect waves-green btn-flat"),
//          A.disabled(disabled),
          A.onClick(okButtonClicked(get)),
          Text("OK"))
    )
  }

  private def okButtonClicked(get: Get)(ev: MouseEvent): Unit = {
    // XXX TODO FIXME: Test bug when only one name in list (one saved config)
    val name     = get(selectedName)
    val loadType = get(selectedLoadType)
    val map = get(savedConfigs)
    if (name.nonEmpty && loadType.nonEmpty && map.contains(name)) {
       emit(map(name))
    } else {
      // XXX TODO: display error message
      val msg = if (name.isEmpty || name.equals("-")) "Please choose a name to load" else "Please select where to load from"
      println(msg)
    }
  }

  private def makeDialogBody(get: Get): Element = {
    E.div(makeLoadtypeItem(), makeNameItem(get))
  }

  override def render(get: Get): Node = {
    val trigger = E.a(A.className("modal-trigger"), A.href(s"#$id"), Text("Load"))
    val body = E.div(
      A.id(id),
      A.className("modal modal-fixed-footer"),
      E.div(A.className("model-content"), makeDialogBody(get)),
      E.div(A.className("modal-footer"), makeButtons(get))
    )
    E.li(body, trigger)
  }
}
