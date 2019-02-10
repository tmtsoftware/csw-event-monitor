package csw.eventmon.client

import com.github.ahnfelt.react4s._
import csw.eventmon.client.react4s.facade.M

import scala.scalajs.js

object EventSelector {
  // materialize icon for adding an event
  val iconName      = "playlist_add"
  val subsystemList = List("TCS", "NFIRAOS", "IRIS")
}

/**
 */
case class EventSelector() extends Component[String] {

  import EventSelector._

  private val selectedSubsystem = State("")
  private val selectedComponent = State("")
  private val selectedEventName = State("")
  private var collapsibleElem   = E.ul()

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

  private def makeButtons(): Element = {
    E.div(
      A.className("row"),
      E.div(
        A.className("col s2 offset-s4"),
        E.button(A.className("btn waves-effect waves-light"), A.onClick(cancelButtonClicked), Text("Cancel")),
        Text(" "),
        E.button(A.className("btn waves-effect waves-light"), A.onClick(okButtonClicked), Text("OK"))
      )
    )
  }

  private def makeDialogBody(): Element = {
    E.div(makeSubsystemItem(), makeComponentItem(), makeEventNameItem(), makeButtons())
  }

  override def render(get: Get): Element = {
    val icon = E.i(A.className("material-icons"), Text(iconName))
    val collapsible = E.ul(
      A.className("collapsible popout"),
      E.li(
        E.div(A.className("collapsible-header col s6"), icon, Text("Add Event")),
        E.div(A.className("collapsible-body col s6"), makeDialogBody())
      )
    )
    collapsibleElem = collapsible
    E.div(collapsible)

//    val labelItem  = Text(label)
//    val labelDiv   = E.div(A.className("col s1"), labelItem)
//    val items      = choiceList.map(s => E.option(A.value(s), Text(s)))
//    val selectItem = E.select(A.className("input-field"), A.onChangeText(itemSelected), Tags(items))
//    val selectDiv  = E.div(A.className("col s3"), selectItem)
//
//    val i = choiceList.indexOf(currentValue)
//    val (iconName, iconLabel, iconColor) = get(commandResponse) match {
//      case Accepted(_) =>
//        (if (i == 0) "filter_none" else s"filter_$i", currentValue, normalColor)
//      case Completed(_)      => ("done", "", normalColor)
//      case Error(_, msg)     => ("error_outline", msg, errorColor)
//      case Invalid(_, issue) => ("error_outline", issue.reason, errorColor)
//      case x                 => ("error_outline", s"unexpected command response: $x", errorColor)
//    }
//
//    val selectStateIcon  = E.i(A.className(s"material-icons $iconColor"), Text(iconName))
//    val selectStateLabel = Text(s" $iconLabel")
//    val selectStateDiv = E.div(A.className("col s8"), selectStateIcon, selectStateLabel)
//
//    E.div(A.className("row valign-wrapper"), labelDiv, selectDiv, selectStateDiv)
  }

  // called when a subsystem item is selected XXXXXXXXXXXXXX
  private def subsystemSelected(subsystem: String): Unit = {
    println(s"Select subsystem: $subsystem")
    selectedSubsystem.set(subsystem)
  }

  private def okButtonClicked(ev: MouseEvent): Unit = {
    println("OK")
    val M = js.Dynamic.global.M
    M.Collapsible.getInstance(collapsibleElem).close()

    emit("XXX")
  }

  private def cancelButtonClicked(ev: MouseEvent): Unit = {
    println("cancel")
//    M.Collapsible.getInstance(collapsibleElem).close()
  }

}
