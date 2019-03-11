package csw.eventmon.client

import com.github.ahnfelt.react4s._
import LoadComponent._
import org.scalajs.dom.{FileReader, UIEvent}
import org.scalajs.dom.raw.HTMLInputElement

import scala.scalajs.js

object LoadComponent {
  private val id          = "loadConfig"
  private val fileInputId = "fileInput"
}

// Manages a dropdown menu for loading the current configuration from local storage or a file
case class LoadComponent(localStorageMap: P[Map[String, Set[EventFieldSelection]]]) extends Component[Set[EventFieldSelection]] {

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
      A.className("file-field input-field"),
      E.span(Text("Choose File ...")),
      E.input(A.id(fileInputId), A.`type`("file"), A.onChange(fileSelected(get)))
    )
  }

  override def render(get: Get): Node = {
    val localStorageItems = get(localStorageMap).map(p => E.li(E.a(A.href("#!"), Text(p._1), A.onClick(_ => emit(p._2))))).toList

    val dropdownTrigger =
      E.a(A.className("dropdown-trigger"), A.href("#"), Attribute("data-target", id), Text("Load"), S.paddingRight("15em"))
    val dropdown = E.ul(
      A.id(id),
      A.className("dropdown-content"),
      E.li(E.a(A.href("#!"), makeFileSelector(get))),
      E.li(A.className("divider"), A.tabIndex("-1")),
      Tags(localStorageItems)
    )

    E.li(Fragment(dropdownTrigger, dropdown))
  }
}
