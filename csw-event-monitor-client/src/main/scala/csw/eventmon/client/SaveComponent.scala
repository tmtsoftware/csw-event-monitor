package csw.eventmon.client

import com.github.ahnfelt.react4s._
import SaveComponent._

object SaveComponent {
  private val id = "saveConfig"

  sealed trait SaveType {
    val displayName: String
  }
  case object SaveToLocalStorage  extends SaveType {
    override val displayName = "Save to Local Storage"
  }
  case object SaveToFile          extends SaveType {
    override val displayName = "Save to File"
  }
  case object SaveToConfigService extends SaveType {
    override val displayName = "Save to Config Service"
  }
  val saveTypes = List(SaveToLocalStorage, SaveToFile, SaveToConfigService)
  case class SaveSettings(name: String, saveType: SaveType)
}

// A modal dialog for adding events to subscribe to
case class SaveComponent() extends Component[SaveSettings] {
  private val selectedName = State("")
  private val selectedSaveType = State[Option[SaveType]](None)

  private def makeNameItem(): Element = {
    E.div(
      A.className("row"),
      E.div(
        A.className("input-field col s6"),
        A.onChangeText(selectedName.set),
        E.input(A.id("name"), A.`type`("text")),
        E.label(A.`for`("name"), Text("Enter a name for this set of events:"))
      )
    )
  }

  // called when a save type item is selected
  private def saveTypeSelected(saveType: String): Unit = {
    val maybeSaveType = saveTypes.find(_.displayName == saveType)
    selectedSaveType.set(maybeSaveType)
  }

  private def makeSavetypeItem(): Element = {
    val defaultItem =
      E.option(A.value("-"), A.disabled(), Text("Save to ..."))
    val items = defaultItem :: saveTypes.map(t => E.option(A.value(t.displayName), Text(t.displayName)))
    E.div(A.className("row"),
      E.div(A.className("input-field col s6"), E.select(A.onChangeText(saveTypeSelected), A.value("-"), Tags(items))))

  }

  private def makeButtons(get: Get): Element = {
    E.div(
      A.className("modal-footer"),
      E.a(A.href("#!"), A.className("modal-close waves-effect waves-green btn-flat"), Text("Cancel")),
      E.a(A.href("#!"), A.className("modal-close waves-effect waves-green btn-flat"), A.onClick(okButtonClicked(get)), Text("OK"))
    )
  }

  private def okButtonClicked(get: Get)(ev: MouseEvent): Unit = {
    val name = get(selectedName)
    val saveType = get(selectedSaveType)
    if (name.nonEmpty && saveType.nonEmpty)
      emit(SaveSettings(name, saveType.get))
    else {
      // XXX TODO: display error message
      val msg = if (name.isEmpty || name.equals("-")) "Please enter a name to save under" else "Please select where to save to"
      println(msg)
    }
  }

  private def makeDialogBody(get: Get): Element = {
    E.div(makeNameItem(), makeSavetypeItem())
  }

  override def render(get: Get): Node = {
    val trigger = E.a(A.className("modal-trigger"), A.href(s"#$id"), Text("Save"))
    val body    = E.div(A.id(id), A.className("modal modal-fixed-footer"),
      E.div(A.className("model-content "), makeDialogBody(get)),
      E.div(A.className("modal-footer"), makeButtons(get))
    )
    E.li(trigger, body)
  }

}
