package csw.eventmon.client

import java.util.Date

import org.scalajs.dom.Element

import scala.scalajs.js
import scala.scalajs.js.JSConverters._
import scala.scalajs.js.annotation.{JSGlobal, JSImport}

// A partial scala.js facade for Chart.js

@js.native
trait DataPoint extends js.Object {
  def x: js.Date = js.native
  def y: Double  = js.native
}

object DataPoint {
  def apply(x: js.Date, y: Double): DataPoint = {
    js.Dynamic
      .literal(
        x = x,
        y = y,
      )
      .asInstanceOf[DataPoint]
  }
}
@js.native
trait ChartDataset extends js.Object {
  def label: String             = js.native
  def data: js.Array[DataPoint] = js.native
  def fillColor: String         = js.native
  def strokeColor: String       = js.native
}

object ChartDataset {
  def apply(data: Seq[DataPoint],
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
  def mode: String       = js.native
  def position: String   = js.native
  def style: String      = js.native
}

object TooltipOptions {
  def apply(intersect: Boolean = false,
            mode: String = "nearest",
            position: String = "average",
            style: TooltipStyle = TooltipStyle()): TooltipOptions = {
    js.Dynamic
      .literal(
        intersect = intersect,
        mode = mode,
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
  val fmt = "HH:mm:ss"
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
  def apply(
      displayFormats: DisplayFormats = DisplayFormats()
  ): AxisTime = {
    js.Dynamic
      .literal(
        displayFormats = displayFormats
      )
      .asInstanceOf[AxisTime]
  }
}

@js.native
trait Realtime extends js.Object {
  def duration: Int  = js.native
  def delay: Int     = js.native
  var pause: Boolean = js.native
}

object Realtime {
  def apply(duration: Int = 60000, delay: Int = 1000, pause: Boolean = false): Realtime = {
    js.Dynamic
      .literal(
        duration = duration,
        delay = delay,
        pause = pause
      )
      .asInstanceOf[Realtime]
  }
}

@js.native
trait XAxisOptions extends js.Object {
  def display: Boolean   = js.native
  def `type`: String     = js.native
  def time: AxisTime     = js.native
  def realtime: Realtime = js.native
}

object XAxisOptions {
  def apply(display: Boolean = true,
            `type`: String = "realtime",
            time: AxisTime = AxisTime(),
            realtime: Realtime = Realtime()): XAxisOptions = {
    js.Dynamic
      .literal(
        display = display,
        `type` = `type`,
        time = time,
        realtime = realtime
      )
      .asInstanceOf[XAxisOptions]
  }
}

@js.native
trait YAxisOptions extends js.Object {
  def display: Boolean = js.native
}

object YAxisOptions {
  def apply(display: Boolean = true): YAxisOptions = {
    js.Dynamic
      .literal(
        display = display
      )
      .asInstanceOf[YAxisOptions]
  }
}

@js.native
trait ScaleLabel extends js.Object {
  def display: Boolean = js.native
}

object ScaleLabel {
  def apply(display: Boolean = false): ScaleLabel = {
    js.Dynamic
      .literal(
        display = display
      )
      .asInstanceOf[ScaleLabel]
  }
}

@js.native
trait ScalesOptions extends js.Object {
  def xAxes: js.Array[XAxisOptions] = js.native
  def yAxes: js.Array[YAxisOptions] = js.native
}

object ScalesOptions {
  def apply(xAxes: Array[XAxisOptions] = Array(XAxisOptions()),
            yAxes: Array[YAxisOptions] = Array(YAxisOptions())): ScalesOptions = {
    js.Dynamic
      .literal(
        xAxes = xAxes.toJSArray,
        yAxes = yAxes.toJSArray
      )
      .asInstanceOf[ScalesOptions]
  }
}

@js.native
trait ChartTitle extends js.Object {
  def display: Boolean = js.native
  def text: String     = js.native
}

object ChartTitle {
  def apply(display: Boolean = true, text: String = "XXX ADD TITLE HERE"): ChartTitle = {
    js.Dynamic
      .literal(
        display = display,
        text = text
      )
      .asInstanceOf[ChartTitle]
  }
}
@js.native
trait ChartOptions extends js.Object {
  def title: ChartTitle        = js.native
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
trait UpdateOptions extends js.Object {
  def preservation: Boolean = js.native
}

object UpdateOptions {
  def apply(preservation: Boolean = true): UpdateOptions = {
    js.Dynamic
      .literal(
        preservation = preservation
      )
      .asInstanceOf[UpdateOptions]
  }
}
@js.native
@JSImport("chart.js", JSImport.Namespace)
class Chart(ctx: String, config: ChartConfiguration) extends js.Object {
  def update(options: UpdateOptions = UpdateOptions()): Unit = js.native
  def data: ChartData                                        = js.native
  def options: ChartOptions                                  = js.native
  def canvas: Element                                        = js.native
}

@js.native
@JSGlobal
object Chart extends js.Object {
  def instances: js.Dictionary[Chart] = js.native
}

//@js.native
//@JSImport("chartjs-plugin-streaming", JSImport.Namespace)
//class ChartPluginStreaming() extends js.Object {}
//
//@js.native
//@JSGlobal
//object ChartPluginStreaming extends js.Object {
//  def init(): Unit = {
//    println("XXX FIXME")
//  }
//}

/**
 * This object is not part of the facade. It contains utility methods.
 */
object ChartUtil {
  // Add a data point to the chart, keep 60 seconds worth of data
  def addData(chart: Chart, label: js.Date, data: Double): Unit = {
    chart.data.datasets(0).data.push(DataPoint(label, data))
    chart.update()
  }
}
