package csw.eventmon.client

import scala.scalajs.js
import scala.scalajs.js.JSConverters._
import scala.scalajs.js.annotation.{JSGlobal, JSImport, JSName}

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
            backgroundColor: String = "#8080FF",
            borderColor: String = "#404080"): ChartDataset = {
    js.Dynamic
      .literal(
        label = label,
        data = data.toJSArray,
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
trait ChartOptions extends js.Object {
  def responsive: Boolean = js.native
}

object ChartOptions {
  def apply(responsive: Boolean = true): ChartOptions = {
    js.Dynamic
      .literal(
        responsive = responsive
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
  def apply(`type`: String, data: ChartData, options: ChartOptions = ChartOptions(false)): ChartConfiguration = {
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
}

object Chart {
  def addData(chart: Chart, label: String, data: Double): Unit = {
    chart.data.labels.push(label)
    chart.data.datasets.foreach(_.data.push(data))
    chart.update()
  }

  def removeData(chart: Chart): Unit = {
    chart.data.labels.pop()
    chart.data.datasets.foreach(_.data.pop())
    chart.update()
  }
}
