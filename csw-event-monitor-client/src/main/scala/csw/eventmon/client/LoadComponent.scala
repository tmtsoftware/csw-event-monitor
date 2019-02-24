package csw.eventmon.client

import com.github.ahnfelt.react4s._
import LoadComponent._
import csw.eventmon.client.MainComponent.localStorageKey
import org.scalajs.dom.ext.LocalStorage

object LoadComponent {
  private val id = "loadConfig"

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
  val loadTypes = List(LoadFromLocalStorage, LoadFromFile, LoadFromConfigService)
  case class LoadSettings(name: String, loadType: LoadType)
}

// A modal dialog for adding events to subscribe to
case class LoadComponent() extends Component[LoadSettings] {
  private val selectedName     = State("")
  private val selectedLoadType = State[Option[LoadType]](None)
  private val savedConfigs     = State[Map[String, Set[EventSelection]]](Map.empty)

  private def makeNameItem(get: Get): Element = {
    val map   = get(savedConfigs)
    val names = map.keySet.toList
//    val names = if (map.nonEmpty) map.keySet.toList else List("A", "B", "C")
    val defaultItem = E.option(A.value("-"), A.disabled(), Text("Choose ..."))
    val items       = defaultItem :: names.map(name => E.option(A.value(name), Text(name)))
    println(s"XXX get savedConfigs: $map, names = $names, items = $items")
    E.div(A.className("row"),
          E.div(A.className("input-field col s6"), E.select(A.onChangeText(nameSelected), A.value("-"), Tags(items))))
  }

  private def nameSelected(name: String): Unit = {
    selectedName.set(name)
  }

  private def loadTypeSelected(loadType: String): Unit = {
    import upickle.default._
    val maybeLoadType = loadTypes.find(_.displayName == loadType)
    selectedLoadType.set(maybeLoadType)
    val configs: Map[String, Set[EventSelection]] = LocalStorage(localStorageKey) match {
      case Some(json) => read[Map[String, Set[EventSelection]]](json)
      case None       => Map.empty
    }
    savedConfigs.set(configs)
    println(s"XXX set savedConfigs to $configs")
  }

  private def makeLoadtypeItem(): Element = {
    val defaultItem =
      E.option(A.value("-"), A.disabled(), Text("Load from ..."))
    val items = defaultItem :: loadTypes.map(t => E.option(A.value(t.displayName), Text(t.displayName)))
    E.div(A.className("row"),
          E.div(A.className("input-field col s6"), E.select(A.onChangeText(loadTypeSelected), A.value("-"), Tags(items))))
  }

  private def makeButtons(get: Get): Element = {
    E.div(
      A.className("modal-footer"),
      E.a(A.href("#!"), A.className("modal-close waves-effect waves-green btn-flat"), Text("Cancel")),
      E.a(A.href("#!"), A.className("modal-close waves-effect waves-green btn-flat"), A.onClick(okButtonClicked(get)), Text("OK"))
    )
  }

  private def okButtonClicked(get: Get)(ev: MouseEvent): Unit = {
    val name     = get(selectedName)
    val loadType = get(selectedLoadType)
    if (name.nonEmpty && loadType.nonEmpty)
      emit(LoadSettings(name, loadType.get))
    else {
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
    E.li(trigger, body)
  }
}
