package csw.eventmon.client
import com.github.ahnfelt.react4s._
import csw.eventmon.client.ControlComponent.ControlOption
import csw.params.events.{Event, SystemEvent}

import scala.scalajs.js
import scala.scalajs.js.timers.SetIntervalHandle

case class SingleEventStreamChart(eventFieldSelections: P[List[EventFieldSelection]],
                                  eventStream: P[EventStream[Event]],
                                  controlOptions: P[ControlOption],
                                  paused: P[Boolean])
    extends Component[NoEmit] {

  // State containing the next event to plot
  private val maybeEvent = State[Option[SystemEvent]](None)

//  // Timer used to sync updates to the frame rate
//  var interval: Option[SetIntervalHandle] = None
//  var frameRateEvent: Option[SystemEvent] = None

  private def receiveEvents(get: Get): Unit = {
    get(eventStream).onNext = {
      case event: SystemEvent =>
        maybeEvent.set(Some(event))
      case _ =>
    }
  }

//  override def componentWillUnmount(get: Get): Unit = {
//    for (i <- interval) js.timers.clearInterval(i)
//  }

  override def render(get: Get): Node = {
    val charts = get(eventFieldSelections).map { eventFieldSelection =>
      val id          = eventFieldSelection.id
      val showXLabels = eventFieldSelection == get(eventFieldSelections).last
      Component(ChartComponent, eventFieldSelection, get(maybeEvent), showXLabels, get(controlOptions), get(paused))
        .withKey(id)
        .withRef(_ => receiveEvents(get))
    }

    E.div(Tags(charts))
  }
}
