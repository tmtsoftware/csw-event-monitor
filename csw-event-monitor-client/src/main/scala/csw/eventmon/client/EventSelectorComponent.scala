package csw.eventmon.client

import com.github.ahnfelt.react4s._
import EventSelectorComponent._
import csw.params.core.models.Subsystem

object EventSelectorComponent {
  // materialize icon for adding an event
  private val iconName      = "playlist_add"
//  private val subsystemList = Subsystem.values.map(_.name).toList
  private val subsystemList = List("tcs", "test")
  private val id = "addEvent"
}

// A modal dialog for adding events to subscribe to
case class EventSelectorComponent() extends Component[EventSelection] {
  private val selectedSubsystem = State("")
  private val selectedComponent = State("")
  private val selectedEventName = State("")

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

  private def makeButtons(get: Get): Element = {
    E.div(
      A.className("modal-footer"),
      E.a(A.href("#!"), A.className("modal-close waves-effect waves-green btn-flat"), Text("Cancel")),
      E.a(A.href("#!"), A.className("modal-close waves-effect waves-green btn-flat"), A.onClick(okButtonClicked(get)), Text("OK"))
    )
  }

  private def makeDialogBody(get: Get): Element = {
    E.div(makeSubsystemItem(), makeComponentItem(), makeEventNameItem(), makeButtons(get))
  }

  override def render(get: Get): Element = {
    val trigger = E.a(A.className("waves-effect waves-light btn modal-trigger"), A.href(s"#$id"), Text("Add Event"))
    val body    = E.div(A.id(id), A.className("modal"), E.div(A.className("model-content"), makeDialogBody(get)))
    E.div(trigger, body)
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
    emit(EventSelection(subsystem, componentOpt, nameOpt))
  }
}
