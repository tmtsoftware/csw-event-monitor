package csw.eventmon.client
import com.github.ahnfelt.react4s._
import csw.eventmon.client.ControlComponent.ControlOption
import csw.params.events.{Event, SystemEvent}

case class SingleEventStreamChart(eventFieldSelections: P[List[EventFieldSelection]],
                                  eventStream: P[EventStream[Event]],
                                  controlOptions: P[ControlOption],
                                  paused: P[Boolean])
    extends Component[NoEmit] {

  private val maybeEvent = State[Option[SystemEvent]](None)

  private def receiveEvents(get: Get): Unit = {
    get(eventStream).onNext = {
      case event: SystemEvent =>
        maybeEvent.set(Some(event))
      case _ =>
    }
  }

  override def render(get: Get): Node = {
    val charts = get(eventFieldSelections).map { eventFieldSelection =>
      val id          = eventFieldSelection.toString
      val showXLabels = eventFieldSelection == get(eventFieldSelections).last
      Component(ChartComponent, eventFieldSelection, get(maybeEvent), showXLabels, get(controlOptions), get(paused))
        .withKey(id)
        .withRef(_ => receiveEvents(get))
    }

    E.div(Tags(charts))
  }
}
