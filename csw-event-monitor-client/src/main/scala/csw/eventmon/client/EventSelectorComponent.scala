package csw.eventmon.client

import com.github.ahnfelt.react4s._
import EventSelectorComponent._
import csw.eventmon.client.react4s.React4sUtil.onChecked
import csw.prefix.models.Subsystem
import csw.params.events.SystemEvent
import csw.params.core.generics.KeyType
import csw.params.core.generics.KeyType._

object EventSelectorComponent {
  private val subsystemList = Subsystem.values.map(_.name).toList
  private val id            = "addEvent"

  private[client] def isNumericKey(keyType: KeyType[_]): Boolean = {
    keyType == IntKey || keyType == DoubleKey || keyType == FloatKey || keyType == ShortKey || keyType == ByteKey
  }

  // Rate limit used for discovering events inside this dialog
  private val rateLimitMs = Some(200)
}

// A modal dialog for adding events to subscribe to
case class EventSelectorComponent(eventClient: P[EventJsClient]) extends Component[EventFieldSelection] {
  // Selected states
  private val selectedSubsystem   = State("")
  private val selectedComponent   = State[Option[String]](None)
  private val selectedEventName   = State[Option[String]](None)
  private val selectedEventField  = State[Option[String]](None)
  private val selectedRateLimiter = State[Int](200)
  private val rateLimitEnabled    = State[Boolean](false)

  // Temp event stream used to get component names, event names, fields
  private val maybeEventStream       = State[Option[EventStream]](None)
  private val componentsForSubsystem = State[Map[String, Set[String]]](Map.empty)
  private val eventsForComponent     = State[Map[String, Set[String]]](Map.empty)
  private val fieldsForEvent         = State[Map[String, Set[String]]](Map.empty)

  private def makeSubsystemItem(get: Get): Element = {
    val defaultItem =
      E.option(A.value(""), A.disabled(), Text("Select subsystem that publishes the event"))
    val items = defaultItem :: subsystemList.map(s => E.option(A.value(s), Text(s)))
    E.div(
      A.className("row"),
      E.div(
        A.className("input-field col s6"),
        E.select(A.onChangeText(subsystemSelected(get)), A.value(""), Tags(items))
      )
    )
  }

  private def makeComponentItem(get: Get): Element = {
    val defaultItem =
      E.option(A.value(""), A.disabled(), Text("Select component that publishes the event"))
    val subsystem    = get(selectedSubsystem)
    val components   = get(componentsForSubsystem).getOrElse(subsystem, Set.empty).toList
    val items        = defaultItem :: components.map(s => E.option(A.value(s), Text(s)))
    val id           = "componentSelect"
    val currentValue = get(selectedComponent).getOrElse("")
    E.div(
      A.className("row"),
      E.div(
        A.className("input-field col s6"),
        E.select(A.id(id), A.onChangeText(componentSelected(get)), A.value(currentValue), Tags(items))
          .withRef(Materialize.formSelect(id))
      )
    )
  }

  private def makeEventNameItem(get: Get): Element = {
    val defaultItem =
      E.option(A.value(""), A.disabled(), Text("Select an event name"))
    val component    = get(selectedComponent)
    val eventNames   = get(eventsForComponent).getOrElse(component.getOrElse(""), Set.empty).toList
    val items        = defaultItem :: eventNames.map(s => E.option(A.value(s), Text(s)))
    val id           = "eventNameSelect"
    val currentValue = get(selectedEventName).getOrElse("")
    E.div(
      A.className("row"),
      E.div(
        A.className("input-field col s6"),
        E.select(A.id(id), A.onChangeText(eventNameSelected(get)), A.value(currentValue), Tags(items))
          .withRef(Materialize.formSelect(id))
      )
    )
  }

  private def makeEventFieldItem(get: Get): Element = {
    val defaultItem =
      E.option(A.value(""), A.disabled(), Text("Select an event field to plot"))
    val eventName    = get(selectedEventName)
    val eventFields  = get(fieldsForEvent).getOrElse(eventName.getOrElse(""), Set.empty).toList
    val items        = defaultItem :: eventFields.map(s => E.option(A.value(s), Text(s)))
    val id           = "eventFieldNameSelect"
    val currentValue = get(selectedEventField).getOrElse("")
    E.div(
      A.className("row"),
      E.div(
        A.className("input-field col s6"),
        E.select(A.id(id), A.onChangeText(eventFieldSelected(get)), A.value(currentValue), Tags(items))
          .withRef(Materialize.formSelect(id))
      )
    )
  }

  private def makeRateLimitItem(get: Get): Element = {
    val canUseRateLimit = get(selectedComponent).isDefined && get(selectedEventName).isDefined
    E.div(
      A.className("row"),
      E.div(
        A.className("col s2"),
        E.label(
          E.input(A.`type`("checkbox"),
                  Tags(if (!canUseRateLimit) Some(A.disabled()) else None),
                  A.className("filled-in"),
                  onChecked(p => rateLimitEnabled.set(p.toBoolean))),
          E.span(Text("Rate Limit"))
        )
      ),
      E.div(
        E.div(A.className("col s1"), Text(s"${get(selectedRateLimiter)}ms"), S.textAlign("left"), S.paddingLeft("2px")),
        E.div(
          S.paddingLeft("0px"),
          A.className("col s3"),
          A.className("range-field"),
          E.input(
            A.`type`("range"),
            A.onChangeText(s => selectedRateLimiter.set(s.toInt)),
            A.min("100"),
            A.max("1000"),
            A.step("1"),
            A.value(get(selectedRateLimiter).toString),
            Tags(if (!get(rateLimitEnabled)) Some(A.disabled()) else None)
          )
        )
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

  // Temp subscribe to all events from this subsystem in order to fill the menus with the component name, event name and fields
  private def tempSubscribeEvents(get: Get,
                                  subsystem: String,
                                  maybeComponent: Option[String],
                                  maybeEvent: Option[String]): Unit = {
    get(maybeEventStream).foreach(_.close())
    maybeEventStream.set(Some(get(eventClient).subscribe(subsystem, maybeComponent, maybeEvent, rateLimitMs)))
    get(maybeEventStream).get.onNext = {
      case event: SystemEvent =>
        val component  = event.source.componentName
        val eventName  = event.eventName.name
        val fields     = event.paramSet.filter(p => isNumericKey(p.keyType)).map(_.keyName)
        val components = get(componentsForSubsystem).getOrElse(subsystem, Set.empty) + component
        componentsForSubsystem.modify(_ + (subsystem -> components))
        val events = get(eventsForComponent).getOrElse(component, Set.empty) + eventName
        eventsForComponent.modify(_ + (component -> events))
        val eventFields = get(fieldsForEvent).getOrElse(eventName, Set.empty) ++ fields
        fieldsForEvent.modify(_ + (eventName -> eventFields))
      case _ =>
    }
  }

  // called when a subsystem item is selected
  private def subsystemSelected(get: Get)(subsystem: String): Unit = {
    selectedEventField.set(None)
    selectedEventName.set(None)
    selectedComponent.set(None)
    selectedSubsystem.set(subsystem)
    tempSubscribeEvents(get, subsystem, None, None)
  }

  // called when a component item is selected
  private def componentSelected(get: Get)(component: String): Unit = {
    selectedEventField.set(None)
    selectedEventName.set(None)
    selectedComponent.set(Some(component))
    tempSubscribeEvents(get, get(selectedSubsystem), Some(component), None)
  }

  // called when an event name item is selected
  private def eventNameSelected(get: Get)(eventName: String): Unit = {
    selectedEventField.set(None)
    selectedEventName.set(Some(eventName))
    tempSubscribeEvents(get, get(selectedSubsystem), get(selectedComponent), get(selectedEventName))
  }

  // called when an event field name item is selected
  private def eventFieldSelected(get: Get)(eventField: String): Unit = {
    selectedEventField.set(Some(eventField))
    get(maybeEventStream).foreach(_.close())
  }

  private def okButtonClicked(get: Get)(ev: MouseEvent): Unit = {
    get(maybeEventStream).foreach(_.close())
    val subsystem        = get(selectedSubsystem)
    val maybeComponent   = get(selectedComponent)
    val maybeEventName   = get(selectedEventName)
    val maybeEventField  = get(selectedEventField)
    val maybeRateLimiter = if (get(rateLimitEnabled)) Some(get(selectedRateLimiter)) else None
    emit(EventFieldSelection(EventSelection(subsystem, maybeComponent, maybeEventName, maybeRateLimiter), maybeEventField))
  }

  private def makeDialogBody(get: Get): Element = {
    E.div(
      E.p(E.em(Text("Note: The menus below are filled by temporarily subscribing to the Event Service using wildcards."))),
      makeSubsystemItem(get),
      makeComponentItem(get),
      makeEventNameItem(get),
      makeEventFieldItem(get),
      makeRateLimitItem(get)
    )
  }

  override def render(get: Get): Node = {
    val trigger = E.a(A.className("modal-trigger"), A.href(s"#$id"), Text("Add Event"))
    val body = E.div(
      A.id(id),
      A.className("modal modal-fixed-footer"),
      E.div(A.className("modal-content"), makeDialogBody(get)),
      makeButtons(get)
    )
    E.li(trigger, body)
  }
}
