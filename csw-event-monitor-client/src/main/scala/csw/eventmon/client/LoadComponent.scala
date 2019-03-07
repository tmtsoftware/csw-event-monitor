package csw.eventmon.client

import com.github.ahnfelt.react4s._
import LoadComponent._
import csw.eventmon.client.MainComponent.localStorageKey
import org.scalajs.dom.{FileReader, UIEvent}
import org.scalajs.dom.ext.LocalStorage
import org.scalajs.dom.raw.HTMLInputElement

import scala.scalajs.js

object LoadComponent {
  private val id          = "loadConfig"
  private val fileInputId = "fileInput"
  val loadNameSelectId    = "loadNameSelect"

  sealed trait LoadType {
    val displayName: String
  }
  case object LoadFromLocalStorage extends LoadType {
    override val displayName = "Load from Local Storage"
  }
  case object LoadFromFile extends LoadType {
    override val displayName = "Load from File"
  }
  val loadTypes: List[LoadType] = List(LoadFromLocalStorage, LoadFromFile)
}

// A modal dialog for loading the current configuration from local storage or a file
case class LoadComponent() extends Component[Set[EventFieldSelection]] {
  private val selectedName     = State("")
  private val selectedLoadType = State[Option[LoadType]](None)
  private val savedConfigs     = State[Map[String, Set[EventFieldSelection]]](Map.empty)

  private def makeLocalStorageNameSelector(get: Get): Element = {
    val map         = get(savedConfigs)
    val names       = map.keySet.toList
    val defaultItem = E.option(A.value(""), A.disabled(), A.hidden(), Text("Choose one"))
    val items       = defaultItem :: names.map(name => E.option(A.value(name), Text(name)))
    val select = E
      .select(A.id(loadNameSelectId), A.onChangeText(selectedName.set), Attribute("defaultValue", ""), Tags(items))
      .withRef(Materialize.formSelect(loadNameSelectId))
    E.div(
      A.className("row"),
      E.div(A.className("input-field col s6"), select)
    )
  }

  // Called when the use selects a file to load
  private def fileSelected(get: Get)(event: SyntheticEvent): Unit = {
    import upickle.default._
    val reader = new FileReader()
    reader.onload = (_: UIEvent) => {
      val json = reader.result.asInstanceOf[String]
      val set  = read[Set[EventFieldSelection]](json)
      emit(set)
    }
    val document = js.Dynamic.global.document
    val files    = document.getElementById(fileInputId).asInstanceOf[HTMLInputElement].files
    reader.readAsText(files.item(0))
  }

  private def makeFileSelector(get: Get): Element = {
    E.div(
      A.className("row file-field input-field"),
      E.div(A.className("col s6 btn"),
            E.span(Text("Choose File")),
            E.input(A.id(fileInputId), A.`type`("file"), A.onChange(fileSelected(get)))),
      E.div(A.className("file-path-wrapper"), E.input(A.className("file-path validate"), A.`type`("text")))
    )
  }

  private def makeNameItem(get: Get): Element = {
    get(selectedLoadType) match {
      case None => E.div()
      case Some(LoadFromLocalStorage) =>
        makeLocalStorageNameSelector(get)
      case Some(LoadFromFile) =>
        makeFileSelector(get)
    }
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
    maybeLoadType match {
      case Some(loadType) =>
        loadType match {
          case LoadFromLocalStorage =>
            val map = loadFromLocalStorage()
            savedConfigs.set(map)
          case LoadFromFile =>
          // Need to render a file input field first
        }
      case None =>
    }
  }

//  private def makeLoadtypeItem(): Element = {
//    val defaultItem =
//      E.option(A.value("-"), A.disabled(), Text("Load from ..."))
//    val items = defaultItem :: loadTypes.map(t => E.option(A.value(t.displayName), Text(t.displayName)))
//    E.div(A.className("row"),
//          E.div(A.id("load-type-div"),
//                A.className("input-field col s6"),
//                E.select(A.onChangeText(loadTypeSelected), A.value("-"), Tags(items))))
//  }

//  private def makeButtons(get: Get): Element = {
//    val disabled = if (get(selectedName).isEmpty) "disabled" else ""
//    E.div(
//      A.className("modal-footer"),
//      E.a(A.href("#!"), A.className("modal-close waves-effect waves-green btn-flat"), Text("Cancel")),
//      E.a(A.href("#!"),
//          A.className(s"modal-close waves-effect waves-green btn-flat $disabled"),
//          A.onClick(okButtonClicked(get)),
//          Text("OK"))
//    )
//  }

//  private def okButtonClicked(get: Get)(ev: MouseEvent): Unit = {
//    val name     = get(selectedName)
//    val loadType = get(selectedLoadType)
//    val map      = get(savedConfigs)
//    if (name.nonEmpty && loadType.nonEmpty && map.contains(name)) {
//      emit(map(name))
//    }
//  }

//  private def makeDialogBody(get: Get): Element = {
//    E.div(makeLoadtypeItem(), makeNameItem(get))
//  }

//  <!-- Dropdown Trigger -->
//    <a class='dropdown-trigger btn' href='#' data-target='dropdown1'>Drop Me!</a>
//
//  <!-- Dropdown Structure -->
//    <ul id='dropdown1' class='dropdown-content'>
//      <li><a href="#!">one</a></li>
//      <li><a href="#!">two</a></li>
//      <li class="divider" tabindex="-1"></li>
//      <li><a href="#!">three</a></li>
//      <li><a href="#!"><i class="material-icons">view_module</i>four</a></li>
//      <li><a href="#!"><i class="material-icons">cloud</i>five</a></li>
//    </ul>

  override def render(get: Get): Node = {
    val trigger =
      E.a(A.className("dropdown-trigger"), A.href("#"), Attribute("data-target", id), Text("Load"), S.paddingRight("15em"))
//    val body = E.div(
//      A.id(id),
//      A.className("modal modal-fixed-footer"),
//      E.div(A.className("model-content"), makeDialogBody(get)),
//      E.div(A.className("modal-footer"), makeButtons(get))
//    )
//    E.li(body)
    val dropdown = E.ul(A.id(id),
                        A.className("dropdown-content"),
                        E.li(E.a(A.href("#!"), makeFileSelector(get))),
                        E.li(E.a(A.href("#!"), Text("From Local Storage"))))
//    val item     = E.li(dropdown)

    E.li(Fragment(trigger, dropdown))
  }
}
