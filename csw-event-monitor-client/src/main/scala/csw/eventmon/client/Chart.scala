package csw.eventmon.client

import org.scalajs.dom.Element

import scala.scalajs.js
import scala.scalajs.js.JSConverters._
import scala.scalajs.js.annotation.JSImport

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
  def labels: js.Array[String]         = js.native
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
trait TooltipOptions extends js.Object {
  def intersect: Boolean = js.native
}

object TooltipOptions {
  def apply(intersect: Boolean = true): TooltipOptions = {
    js.Dynamic
      .literal(
        intersect = intersect
      )
      .asInstanceOf[TooltipOptions]
  }
}

@js.native
trait TicksOptions extends js.Object {
  def display: Boolean = js.native
}

object TicksOptions {
  def apply(display: Boolean = true): TicksOptions = {
    js.Dynamic
      .literal(
        display = display,
      )
      .asInstanceOf[TicksOptions]
  }
}

@js.native
trait ScalesOptions extends js.Object {
  def xAxes: js.Array[TicksOptions] = js.native
}

object ScalesOptions {
  def apply(xAxes: Array[TicksOptions] = Array(TicksOptions())): ScalesOptions = {
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
  def update(): Unit  = js.native
  def data: ChartData = js.native
  def canvas: Element = js.native
}

@js.native
object Chart extends js.Object {
  def instances: js.Dictionary[Chart] = js.native
}

object ChartUtil {
  def addData(chart: Chart, label: String, data: Double, keep: Int = 60): Unit = {
    chart.data.labels.push(label)
    chart.data.datasets.foreach(_.data.push(data))
    if (chart.data.labels.size > keep) {
      chart.data.labels.shift()
      chart.data.datasets.foreach(_.data.shift())
    }
    chart.update()
  }

}
