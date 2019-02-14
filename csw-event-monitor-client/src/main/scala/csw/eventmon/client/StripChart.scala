package csw.eventmon.client

import com.github.ahnfelt.react4s._
import csw.eventmon.client.EventSelector.EventSelection
import csw.eventmon.client.MainComponent.EventStreamInfo
import csw.params.core.generics.KeyType
import csw.params.core.generics.KeyType._
import csw.params.events.SystemEvent
import org.scalajs.dom

import scala.scalajs.js
import js.JSConverters._

object StripChart {
  case class ChartInfo(chart: ECharts, div: Element)
}

case class StripChart(eventStreams: P[List[EventStreamInfo]], eventMap: P[Map[EventSelection, List[SystemEvent]]])
    extends Component[NoEmit] {

  import StripChart._

  private val chartInfoMap = State[Map[EventSelection, ChartInfo]](Map.empty)


  private def isNumericKey(keyType: KeyType[_]): Boolean = {
    keyType == IntKey || keyType == DoubleKey || keyType == FloatKey || keyType == ShortKey || keyType == ByteKey
  }

//  private def getParameterValue(p: Parameter[_]): Double = {
//    p.keyType match {
//      case IntKey => p.
//    }
//  }

  private def makeChart(id: String, info: EventSelection, events: Seq[SystemEvent]): ECharts = {
    val options = events.flatMap { e =>
      val eventTime = e.eventTime.value.toString
      val maybeParam = if (info.maybeName.nonEmpty) {
        e.paramSet.find(_.keyName == info.maybeName.get)
      } else {
        e.paramSet.find(p => isNumericKey(p.keyType))
      }
      val maybeEventValue = maybeParam.map(_.head.toString)
      println(s"XXX maybeParam = $maybeParam, eventValue = $maybeEventValue")
      maybeEventValue.map{ ev =>
        println(s"XXX Adding event value: name = $eventTime, value = $ev")
        js.Dynamic.literal(name = eventTime, value = ev)
      }
    }.toJSArray

    echarts.init(dom.document.getElementById(id), "light", options)
  }

  private def updateChart(chartInfo: ChartInfo, info: EventSelection, events: Seq[SystemEvent]): ChartInfo = {
    val options = events.flatMap { e =>
      val eventTime = e.eventTime.value.toString
      val maybeParam = if (info.maybeName.nonEmpty) {
        e.paramSet.find(_.keyName == info.maybeName.get)
      } else {
        e.paramSet.find(p => isNumericKey(p.keyType))
      }
      val eventValue = maybeParam.map(_.head.toString)
      eventValue.map(ev => js.Dynamic.literal(name = eventTime, value = ev))
    }.toJSArray

    chartInfo.chart.setOption(options)
    chartInfo
  }

  private def makeId(es: EventSelection): String = {
    s"${es.subsystem}.${es.maybeComponent.getOrElse("")}.${es.maybeName.getOrElse("")}}"
  }

  override def render(get: Get): Element = {
    val charts = get(eventStreams).map { info =>
      val ciMap = get(chartInfoMap)
      val evMap = get(eventMap)
      val events = evMap.getOrElse(info.eventSelection, Nil)
      val chartInfo = if (ciMap.contains(info.eventSelection)) {
        val ci = ciMap(info.eventSelection)
        updateChart(ci, info.eventSelection, events)
      } else {
        val id = makeId(info.eventSelection)
        println(s"XXX make chart with id $id")
        val div = E.div(A.id(id), A.className("row"))
        // FIXME XXX won't work, since div not in document yet?
        val chart = makeChart(id, info.eventSelection, events)
        ChartInfo(chart, div)
      }
      chartInfo.div
    }
    println(s"charts = $charts")
    E.div(Tags(charts))
  }

}
