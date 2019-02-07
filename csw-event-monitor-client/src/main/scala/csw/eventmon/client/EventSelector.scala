package csw.eventmon.client

import com.github.ahnfelt.react4s._

object EventSelector {
  // materialize icon for adding an event
  val iconName      = "playlist_add"
  val subsystemList = List("TCS", "NFIRAOS", "IRIS")
}

//    <ul class="collapsible">
//      <li>
//        <div class="collapsible-header"><i class="material-icons">filter_drama</i>First</div>
//        <div class="collapsible-body"><span>Lorem ipsum dolor sit amet.</span></div>
//      </li>
//      <li>
//        <div class="collapsible-header"><i class="material-icons">place</i>Second</div>
//        <div class="collapsible-body"><span>Lorem ipsum dolor sit amet.</span></div>
//      </li>
//      <li>
//        <div class="collapsible-header"><i class="material-icons">whatshot</i>Third</div>
//        <div class="collapsible-body"><span>Lorem ipsum dolor sit amet.</span></div>
//      </li>
//    </ul>

/**
 */
case class EventSelector() extends Component[String] {

  import EventSelector._

  val targetState = State("")

  private def makeSubsystemItem(): Element = {
    val defaultItem = E.option(A.value(""), A.disabled(), A.value(), Text("Subsystem that publishes the event"))
    val items       = defaultItem :: subsystemList.map(s => E.option(A.value(s), Text(s)))
    E.div(E.select(A.className("input-field"), A.onChangeText(subsystemSelected), Tags(items)))
  }

  private def makeComponentItem(): Element = {
    E.div(A.className("input-field"),
          E.input(A.id("component"), A.`type`("text")),
          E.label(A.`for`("component"), Text("Component")))
  }

  private def makeEventNameItem(): Element = {
    E.div(A.className("input-field"),
          E.input(A.id("eventName"), A.`type`("text")),
          E.label(A.`for`("eventName"), Text("Event Name")))
  }

  override def render(get: Get): Element = {

    //A.className("row"),
    val body = E.div(makeSubsystemItem(), makeComponentItem(), makeEventNameItem())
//    val body = E.span(Text("Hi there"))
    val icon = E.i(A.className("material-icons"), Text(iconName))
    val collapsible = E.ul(A.className("collapsible popout"),
                           E.li(
                             E.div(A.className("collapsible-header"), icon, Text("Add Event")),
                             E.div(A.className("collapsible-body"), body)
                           ))
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

  // called when a subsystem item is selected
  private def subsystemSelected(value: String): Unit = {
//    targetState.set(value)
//
//    // This allows the main component to be notified when an item is selected
//    emit(value)
  }

}
