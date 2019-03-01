package csw.eventmon.client

//import java.time.ZoneId
//import java.time.format.{DateTimeFormatter, FormatStyle}

import com.github.ahnfelt.react4s._
import csw.params.core.generics.KeyType
import csw.params.core.generics.KeyType._
import csw.params.events.SystemEvent
import ChartComponent._

object ChartComponent {
  private val maxDatasetSize = 60
  private val defaultData    = (0 until maxDatasetSize).map(_ => 0.0)
  private val defaultLabels  = (0 until maxDatasetSize).map(_ => "")
}

case class ChartComponent(eventFieldSelection: P[EventFieldSelection],
                          maybeEvent: P[Option[SystemEvent]],
                          showXLabels: P[Boolean])
    extends Component[NoEmit] {

  private val maybeChart = State[Option[Chart]](None)
//  private val maybeEvent = State[Option[SystemEvent]](None)

//    private val timeFormatter = DateTimeFormatter.ofPattern("mm:ss")
//  private val timeFormatter = DateTimeFormatter
//    .ofLocalizedDateTime(FormatStyle.SHORT)
//    .withZone(ZoneId.of("UTC"))

//  private def receiveEvents(get: Get): Unit = {
//    get(eventStreamInfo).eventStream.onNext = {
//      case event: SystemEvent =>
//        updateChart(get, event)
//        maybeEvent.set(Some(event))
//
//      case _ =>
//    }
//  }

  private def makeChart(get: Get, id: String): Chart = {
    val legend   = LegendOptions(position = "bottom")
    val tooltips = TooltipOptions(intersect = false)
    // XXX TODO: How to keep grid lines but hide x labels?
//    val scales   = ScalesOptions(xAxes = Array(TicksOptions(display = get(showXLabels))))
    val scales = ScalesOptions()
    // XXX TODO: Add prop for color, different for each chart
    val chartData = ChartData(defaultLabels, List(ChartDataset(defaultData, id, fill = false, borderColor = "#404080")))
    val options   = ChartOptions(legend = legend, tooltips = tooltips, scales = scales)
    val config    = ChartConfiguration("LineWithLine", chartData, options)
    new Chart(id, config)
  }

  private def makeLabel(get: Get, event: SystemEvent): String = {
    (event.eventTime.value.getEpochSecond % maxDatasetSize).toString
  }

  private def updateChart(get: Get): Unit = {
    get(maybeChart).foreach { chart =>
      get(maybeEvent).foreach { event =>
        val info = get(eventFieldSelection)
        val maybeParam = if (info.maybeEventField.nonEmpty) {
          event.paramSet.find(_.keyName == info.maybeEventField.get)
        } else {
          event.paramSet.find(p => EventSelectorComponent.isNumericKey(p.keyType))
        }
        if (maybeParam.nonEmpty) {
          val eventValue = maybeParam.map(_.head.toString.toDouble)
          eventValue.foreach(value => ChartUtil.addData(chart, makeLabel(get, event), value))
        }
      }
    }
  }

  override def render(get: Get): Node = {
    val id = get(eventFieldSelection).toString
    E.canvas(A.id(id), A.width("400"), A.height("50")).withRef { _ =>
      if (get(maybeChart).isEmpty)
        maybeChart.set(Some(makeChart(get, id)))
      updateChart(get)
    }
  }
}
