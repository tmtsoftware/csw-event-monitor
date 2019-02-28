package csw.eventmon.client

import com.github.ahnfelt.react4s._
import EventSelectorComponent._
import csw.params.core.models.Subsystem

object EventSelectorComponent {
//  private val subsystemList = Subsystem.values.map(_.name).toList
  private val subsystemList = List("tcs", "test")
  private val id = "addEvent"
}

// A modal dialog for adding events to subscribe to
case class EventSelectorComponent() extends Component[EventFieldSelection] {
  private val selectedSubsystem = State("")
  private val selectedComponent = State("")
  private val selectedEventName = State("")
  private val selectedEventField = State("")

  private def makeSubsystemItem(): Element = {
    val defaultItem =
      E.option(A.value("BAD"), A.disabled(), Text("Select subsystem that publishes the event"))
    val items = defaultItem :: subsystemList.map(s => E.option(A.value(s), Text(s)))
    E.div(A.className("row"),
          E.div(A.className("input-field col s6"), E.select(A.onChangeText(subsystemSelected), A.value("BAD"), Tags(items))))
  }

  private def makeComponentItem(): Element = {
    E.div(
      A.className("row"),
      E.div(
        A.className("input-field col s6"),
        A.onChangeText(selectedComponent.set),
        E.input(A.id("component"), A.`type`("text")),
        E.label(A.`for`("component"), Text("Component"))
      )
    )
  }

  private def makeEventNameItem(): Element = {
    E.div(
      A.className("row"),
      E.div(
        A.className("input-field col s6"),
        A.onChangeText(selectedEventName.set),
        E.input(A.id("eventName"), A.`type`("text")),
        E.label(A.`for`("eventName"), Text("Event Name"))
      )
    )
  }

  private def makeEventFieldItem(): Element = {
    E.div(
      A.className("row"),
      E.div(
        A.className("input-field col s6"),
        A.onChangeText(selectedEventField.set),
        E.input(A.id("eventField"), A.`type`("text")),
        E.label(A.`for`("eventField"), Text("Event Field"))
      )
    )
  }

  private def makeButtons(get: Get): Element = {
    E.div(
      A.className("modal-footer"),
      E.a(A.href("#!"), A.className("modal-close waves-effect waves-green btn-flat"), Text("Cancel")),
      E.a(A.href("#!"), A.className("modal-close waves-effect waves-green btn-flat"), A.onClick(okButtonClicked(get)), Text("OK"))
    )
  }

  // called when a subsystem item is selected
  private def subsystemSelected(subsystem: String): Unit = {
    println(s"Select subsystem: $subsystem")
    selectedSubsystem.set(subsystem)
  }

  private def okButtonClicked(get: Get)(ev: MouseEvent): Unit = {
    val subsystem    = get(selectedSubsystem)
    val component    = get(selectedComponent)
    val componentOpt = if (component.isEmpty) None else Some(component)
    val name         = get(selectedEventName)
    val nameOpt      = if (name.isEmpty) None else Some(name)
    val field         = get(selectedEventField)
    val fieldOpt      = if (field.isEmpty) None else Some(field)
    emit(EventFieldSelection(EventSelection(subsystem, componentOpt, nameOpt), fieldOpt))
  }

  private def makeDialogBody(get: Get): Element = {
    // XXX TODO: Add event value key!
    E.div(makeSubsystemItem(), makeComponentItem(), makeEventNameItem(), makeEventFieldItem())
  }

  override def render(get: Get): Node = {
    val trigger = E.a(A.className("modal-trigger"), A.href(s"#$id"), Text("Add Event"))
    val body    = E.div(A.id(id), A.className("modal modal-fixed-footer"),
      E.div(A.className("model-content"), makeDialogBody(get)),
      E.div(A.className("modal-footer"), makeButtons(get))
    )
    E.li(trigger, body)
  }
}
