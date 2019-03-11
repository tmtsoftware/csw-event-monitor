package csw.eventmon.client
import com.github.ahnfelt.react4s._

object ManageComponent {
  val id = "manageLocalStorage"
}

// Displays a dialog to manage the items saved in local storage
case class ManageComponent(localStorageMap: P[Map[String, Set[EventFieldSelection]]])
    extends Component[Map[String, Set[EventFieldSelection]]] {

  private val selectedItems = State[Set[String]](Set.empty)

  import ManageComponent._

  // Should be in React4s: Like A.onChangeText, but for a checkbox
  private def onChecked(onChange: String => Unit) = {
    A.onChange(e => onChange(e.target.checked.asInstanceOf[Boolean].toString))
  }

  // Called when a checkbox is clicked
  private def selectionChanged(name: String, selected: Boolean): Unit = {
    if (selected)
      selectedItems.modify(_ + name)
    else
      selectedItems.modify(_ - name)
  }

  private def makeDialogBody(get: Get): Element = {
    val map = get(localStorageMap)
    val items = map.keySet.toList.map { name =>
      E.div(
        A.className("row"),
        S.marginBottom("0px"),
        E.div(
          A.className("col s4 offset-s1"),
          E.label(E.input(A.`type`("checkbox"), A.className("filled-in"), onChecked(s => selectionChanged(name, s.toBoolean))),
                  E.span(Text(name)))
        )
      )
    }
    E.div(Tags(items))
  }

  private def deleteSelected(get: Get)(e: MouseEvent): Unit = {
    val toDelete = get(selectedItems)
    val map = get(localStorageMap).filterKeys(s => !toDelete.contains(s))
    emit(map)
  }

  private def makeButtons(get: Get): Element = {
    E.div(
      A.className("modal-footer"),
      E.a(A.href("#!"), A.className("modal-close waves-effect waves-green btn-flat"), Text("Cancel")),
      E.a(A.href("#!"),
          A.className("modal-close waves-effect waves-green btn-flat"),
          A.onClick(deleteSelected(get)),
          Text("Delete Selected"))
    )
  }

  override def render(get: Get): Node = {
    val trigger = E.a(A.className("modal-trigger"), A.href(s"#$id"), Text("Manage"))
    val body = E.div(
      A.id(id),
      A.className("modal modal-fixed-footer"),
      E.div(A.className("modal-content"), makeDialogBody(get)),
      E.div(makeButtons(get))
    )
    E.li(trigger, body)
  }

}
