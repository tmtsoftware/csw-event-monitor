package csw.eventmon.client

import java.time.ZoneId
import java.time.format.{DateTimeFormatter, FormatStyle}

import com.github.ahnfelt.react4s._
import csw.params.core.generics.KeyType
import csw.params.core.generics.KeyType._
import csw.params.events.SystemEvent

case class ChartComponent(eventStreamInfo: P[EventStreamInfo]) extends Component[NoEmit] {

  private val maybeChart = State[Option[Chart]](None)
  private val maybeEvent = State[Option[SystemEvent]](None)

  //  private val timeFormatter = DateTimeFormatter.ofPattern("HHmmss")
//  private val timeFormatter = DateTimeFormatter
//    .ofLocalizedDateTime(FormatStyle.SHORT)
//    .withZone(ZoneId.of("UTC"))

  private def receiveEvents(get: Get): Unit = {
    val id = get(eventStreamInfo).eventSelection.toString

    get(eventStreamInfo).eventStream.onNext = {
      case event: SystemEvent =>
        updateChart(get, event)
        maybeEvent.set(Some(event))

      case _ =>
    }
  }

  private def makeChart(id: String, info: EventSelection): Chart = {
    val chartData = ChartData(List(id), List(ChartDataset(Nil, id)))
    val options   = ChartOptions(legend = LegendOptions(position = "right"), tooltips = TooltipOptions(intersect = false))
    val config    = ChartConfiguration("LineWithLine", chartData, options)
    new Chart(id, config)
  }

  private def isNumericKey(keyType: KeyType[_]): Boolean = {
    keyType == IntKey || keyType == DoubleKey || keyType == FloatKey || keyType == ShortKey || keyType == ByteKey
  }

  private def makeLabel(event: SystemEvent): String = {
//    timeFormatter.format(event.eventTime.value)
    ""
  }

  private def updateChart(get: Get, event: SystemEvent): Unit = {
    val id = get(eventStreamInfo).eventSelection.toString
    get(maybeChart).foreach { chart =>
      val info = get(eventStreamInfo).eventSelection
      val maybeParam = if (info.maybeName.nonEmpty) {
        event.paramSet.find(_.keyName == info.maybeName.get)
      } else {
        event.paramSet.find(p => isNumericKey(p.keyType))
      }
      if (maybeParam.nonEmpty) {
        val eventValue = maybeParam.map(_.head.toString.toDouble)
        eventValue.foreach(value => ChartUtil.addData(chart, makeLabel(event), value))
      }
    }
  }

  override def render(get: Get): Element = {
    val id = get(eventStreamInfo).eventSelection.toString
    E.canvas(A.id(id), A.width("400"), A.height("50")).withRef { _ =>
      if (get(maybeChart).isEmpty)
        maybeChart.set(Some(makeChart(id, get(eventStreamInfo).eventSelection)))
      receiveEvents(get)
    }
  }
}
