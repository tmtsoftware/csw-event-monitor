package csw.eventmon.client

import com.github.ahnfelt.react4s._
import csw.eventmon.client.ControlComponent.ControlOption
import csw.params.events.SystemEvent

import scala.scalajs.js
import scala.scalajs.js.timers.SetIntervalHandle

case class ChartComponent(eventFieldSelection: P[EventFieldSelection],
                          maybeEvent: P[Option[SystemEvent]],
                          showXLabels: P[Boolean],
                          controlOptions: P[ControlOption],
                          paused: P[Boolean])
    extends Component[NoEmit] {

  private val maybeChart                  = State[Option[Chart]](None)
  var interval: Option[SetIntervalHandle] = None

  private def makeChart(get: Get, id: String): Chart = {
    val legend   = LegendOptions(position = "bottom")
    val tooltips = TooltipOptions()
    val scales   = ScalesOptions()
    // XXX TODO: Add prop for color, different for each chart?
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

  // Update the chart with the latest options, data, etc.
  private def updateChart(get: Get): Unit = {
    val opts = get(controlOptions)
    get(maybeChart).foreach { chart =>
      val rt = chart.options.scales.xAxes(0).realtime
      rt.pause = get(paused)
      rt.duration = opts.duration * 1000
      rt.ttl = opts.ttl * 1000
      rt.delay = opts.delay
      chart.options.plugins.streaming.frameRate = opts.frameRate

      if (opts.performance) {
        // disable animation
        chart.options.animation.duration = 0
        chart.options.hover.animationDuration = 0
        chart.options.responsiveAnimationDuration = 0
        // disables bezier curves
        chart.options.elements.line.tension = 0
        // disable for all datasets
//        chart.options.showLines = false

      }

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

  override def componentWillUnmount(get: Get): Unit = {
    for (i <- interval) js.timers.clearInterval(i)
  }

  override def render(get: Get): Node = {
    val id     = get(eventFieldSelection).toString
    val width = get(controlOptions).chartWidth.toString
    val height = get(controlOptions).chartHeight.toString
    // The div is used for resizing the charts: See https://www.chartjs.org/docs/latest/general/responsive.html#important-note
    E.div(
      A.className("chart-container"),
      S.position("relative"),
      S.width(s"${width}vw"),
      S.height(s"${height}vh"),
      E.canvas(A.id(id)).withRef { _ =>
        if (get(maybeChart).isEmpty)
          maybeChart.set(Some(makeChart(get, id)))
        updateChart(get)
      }
    )
  }
}
