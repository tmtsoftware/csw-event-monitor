package csw.eventmon.client

import com.github.ahnfelt.react4s._
import csw.params.events.SystemEvent
import ChartComponent._

import scala.scalajs.js
import scala.scalajs.js.timers.SetIntervalHandle

object ChartComponent {
  // Time in ms for syncing the chart x-axes
  val updateIntervalMs = 1000
}

case class ChartComponent(eventFieldSelection: P[EventFieldSelection],
                          maybeEvent: P[Option[SystemEvent]],
                          showXLabels: P[Boolean],
                          paused: P[Boolean])
    extends Component[NoEmit] {

  private val maybeChart = State[Option[Chart]](None)
//  private val maybeCanvas = State[Option[Element]](None)
  var interval: Option[SetIntervalHandle] = None

  private def makeChart(get: Get, id: String): Chart = {
    val legend   = LegendOptions(position = "bottom")
    val tooltips = TooltipOptions(intersect = false)
    val scales   = ScalesOptions()
    // XXX TODO: Add prop for color, different for each chart
//    val chartData = ChartData(defaultLabels, List(ChartDataset(defaultData, id, fill = false, borderColor = "#404080")))
    val chartData = ChartData(Nil, List(ChartDataset(Nil, id, fill = false, borderColor = "#404080")))
    val options   = ChartOptions(legend = legend, tooltips = tooltips, scales = scales)
    val config    = ChartConfiguration("LineWithLine", chartData, options)
//    val config    = ChartConfiguration("line", chartData, options)
    new Chart(id, config)
  }

  // Makes the label for the X-axis
  private def makeLabel(get: Get, event: SystemEvent): js.Date = {
    val instant = event.eventTime.value
    new js.Date(instant.getEpochSecond * 1000 + instant.getNano / 1000000)
  }

  private def updateChart(get: Get): Unit = {
    if (!get(paused)) {
      get(maybeChart).foreach { chart =>
        get(maybeEvent).foreach { event =>
          val info = get(eventFieldSelection)
          val maybeParam = if (info.maybeEventField.nonEmpty) {
            event.paramSet.find(_.keyName == info.maybeEventField.get)
          } else {
            event.paramSet.find(p => EventSelectorComponent.isNumericKey(p.keyType))
          }
          if (maybeParam.nonEmpty) {
            // XXX TODO FIXME: Don't use head: plot all values in same chart?
            val eventValue = maybeParam.map(_.head.toString.toDouble)
            eventValue.foreach(value => ChartUtil.addData(chart, makeLabel(get, event), value))
          }
        }
      }
    }
  }

  override def componentWillRender(get: Get): Unit = {
    if (interval.isEmpty)
      interval = Some(js.timers.setInterval(updateIntervalMs) {
        if (!get(paused))
          get(maybeChart).foreach(ChartUtil.adjustChart)
      })
  }

  override def componentWillUnmount(get: Get): Unit = {
    for (i <- interval) js.timers.clearInterval(i)
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
