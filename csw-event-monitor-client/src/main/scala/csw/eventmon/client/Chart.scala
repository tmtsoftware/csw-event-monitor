package csw.eventmon.client

import java.util.Date

import org.scalajs.dom.Element

import scala.scalajs.js
import scala.scalajs.js.JSConverters._
import scala.scalajs.js.annotation.{JSGlobal, JSImport}

// A partial scala.js facade for Chart.js

@js.native
trait ChartDataset extends js.Object {
  def label: String          = js.native
  def data: js.Array[Double] = js.native
  def fillColor: String      = js.native
  def strokeColor: String    = js.native
}

object ChartDataset {
  def apply(data: Seq[Double],
            label: String,
            fill: Boolean = true,
            backgroundColor: String = "#8080FF",
            borderColor: String = "#404080"): ChartDataset = {
    js.Dynamic
      .literal(
        data = data.toJSArray,
        label = label,
        fill = fill,
        backgroundColor = backgroundColor,
        borderColor = borderColor
      )
      .asInstanceOf[ChartDataset]
  }
}

@js.native
trait ChartData extends js.Object {
  def labels: js.Array[js.Date]        = js.native
  def datasets: js.Array[ChartDataset] = js.native
}

object ChartData {
  def apply(labels: Seq[String], datasets: Seq[ChartDataset]): ChartData = {
    js.Dynamic
      .literal(
        labels = labels.toJSArray,
        datasets = datasets.toJSArray
      )
      .asInstanceOf[ChartData]
  }
}

@js.native
trait LegendOptions extends js.Object {
  def display: Boolean = js.native
  def position: String = js.native
}

object LegendOptions {
  def apply(display: Boolean = true, position: String = "top"): LegendOptions = {
    js.Dynamic
      .literal(
        display = display,
        position = position
      )
      .asInstanceOf[LegendOptions]
  }
}

@js.native
trait TooltipStyle extends js.Object {
  def pointerEvents: String = js.native
}

object TooltipStyle {
  def apply(pointerEvents: String = "none"): TooltipStyle = {
    js.Dynamic
      .literal(
        pointerEvents = pointerEvents
      )
      .asInstanceOf[TooltipStyle]
  }
}

@js.native
trait TooltipOptions extends js.Object {
  def intersect: Boolean = js.native
  def position: String   = js.native
  def style: String      = js.native
}

object TooltipOptions {
  def apply(intersect: Boolean = true, position: String = "average", style: TooltipStyle = TooltipStyle()): TooltipOptions = {
    js.Dynamic
      .literal(
        intersect = intersect,
        position = position,
        style = style
      )
      .asInstanceOf[TooltipOptions]
  }
}

@js.native
trait DisplayFormats extends js.Object {
  def millisecond: String = js.native
  def second: String      = js.native
  def minute: String      = js.native
  def hour: String        = js.native
  def day: String         = js.native
  def week: String        = js.native
  def month: String       = js.native
  def quarter: String     = js.native
  def year: String        = js.native
}

object DisplayFormats {
  val fmt = "mm:ss.S"
  def apply(
      millisecond: String = fmt,
      second: String = fmt,
      minute: String = fmt,
      hour: String = fmt,
      day: String = fmt,
      week: String = fmt,
      month: String = fmt,
      quarter: String = fmt,
      year: String = fmt
  ): DisplayFormats = {
    js.Dynamic
      .literal(
        millisecond = millisecond,
        second = second,
        minute = minute,
        hour = hour,
        day = day,
        week = week,
        month = month,
        quarter = quarter,
        year = year
      )
      .asInstanceOf[DisplayFormats]
  }
}
@js.native
trait AxisTime extends js.Object {
  var min: js.Date                   = js.native
  var max: js.Date                   = js.native
  def displayFormats: DisplayFormats = js.native
}

object AxisTime {
  // Default to min X-axis
  def apply(min: js.Date = ChartUtil.minTime,
            max: js.Date = ChartUtil.maxTime,
            displayFormats: DisplayFormats = DisplayFormats()): AxisTime = {
    js.Dynamic
      .literal(
        min = min,
        max = max,
      )
      .asInstanceOf[AxisTime]
  }
}

@js.native
trait AxisOptions extends js.Object {
  def display: Boolean = js.native
  def `type`: String   = js.native
  def time: AxisTime   = js.native
}

object AxisOptions {
  def apply(display: Boolean = true, `type`: String = "time", time: AxisTime = AxisTime()): AxisOptions = {
    js.Dynamic
      .literal(
        display = display,
        `type` = `type`,
        time = time
      )
      .asInstanceOf[AxisOptions]
  }
}

@js.native
trait ScalesOptions extends js.Object {
  def xAxes: js.Array[AxisOptions] = js.native
}

object ScalesOptions {
  def apply(xAxes: Array[AxisOptions] = Array(AxisOptions())): ScalesOptions = {
    js.Dynamic
      .literal(
        xAxes = xAxes.toJSArray,
      )
      .asInstanceOf[ScalesOptions]
  }
}
@js.native
trait ChartOptions extends js.Object {
  def responsive: Boolean      = js.native
  def legend: LegendOptions    = js.native
  def tooltips: TooltipOptions = js.native
  def scales: ScalesOptions    = js.native
}

object ChartOptions {
  def apply(responsive: Boolean = true,
            legend: LegendOptions = LegendOptions(),
            tooltips: TooltipOptions = TooltipOptions(),
            scales: ScalesOptions = ScalesOptions()): ChartOptions = {
    js.Dynamic
      .literal(
        responsive = responsive,
        legend = legend,
        tooltips = tooltips,
        scales = scales
      )
      .asInstanceOf[ChartOptions]
  }
}

@js.native
trait ChartConfiguration extends js.Object {
  def `type`: String        = js.native
  def data: ChartData       = js.native
  def options: ChartOptions = js.native
}

object ChartConfiguration {
  def apply(`type`: String, data: ChartData, options: ChartOptions = ChartOptions()): ChartConfiguration = {
    js.Dynamic
      .literal(
        `type` = `type`,
        data = data,
        options = options
      )
      .asInstanceOf[ChartConfiguration]
  }
}

@js.native
@JSImport("chart.js", JSImport.Namespace)
class Chart(ctx: String, config: ChartConfiguration) extends js.Object {
  def update(): Unit        = js.native
  def data: ChartData       = js.native
  def options: ChartOptions = js.native
  def canvas: Element       = js.native
}

@js.native
@JSGlobal
object Chart extends js.Object {
  def instances: js.Dictionary[Chart] = js.native
}

object ChartUtil {
  // Keep this many seconds of data
  val keepSecs = 60
  // Min time for X time axis
  def minTime = new js.Date(new Date().getTime - keepSecs * 1000)
  // Max time for X time axis
  def maxTime = new js.Date(new Date().getTime)

  // Add a data point to the chart, keep 60 seconds worth of data
  def addData(chart: Chart, label: js.Date, data: Double): Unit = {
    chart.data.labels.push(label)
    chart.data.datasets.foreach(_.data.push(data))
    val t = minTime
    while (chart.data.labels.nonEmpty && chart.data.labels(0).getTime() < t.getTime()) {
      chart.data.labels.shift()
      chart.data.datasets.foreach(_.data.shift())
    }
    chart.update()
  }

  // Adjusts the min and max values of the X-axis to keep it in sync with the other charts
  def adjustChart(chart: Chart): Unit = {
    chart.options.scales.xAxes(0).time.min = minTime
    chart.options.scales.xAxes(0).time.max = maxTime
    chart.update()
  }

}
