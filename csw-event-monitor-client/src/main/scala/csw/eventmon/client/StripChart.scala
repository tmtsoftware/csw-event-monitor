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
import js.Dynamic.literal
import scala.scalajs.js.Date

object StripChart {
  case class ChartInfo(chart: Chart, div: Element)
}

case class StripChart(eventStreams: P[List[EventStreamInfo]], eventMap: P[Map[EventSelection, List[SystemEvent]]])
    extends Component[NoEmit] {

  import StripChart._

  private val divInfoMap   = State[Map[EventSelection, Element]](Map.empty)
  private val chartInfoMap = State[Map[EventSelection, ChartInfo]](Map.empty)

  // Make sure there is a div for each chart
  override def componentWillRender(get: Get): Unit = {
    val ciMap  = get(chartInfoMap)
    val divMap = get(divInfoMap)
    val evMap  = get(eventMap)
    get(eventStreams).foreach { info =>
      val events = evMap.getOrElse(info.eventSelection, Nil)
      val id     = makeId(info.eventSelection)
      if (!divMap.contains(info.eventSelection)) {
        val canvas = E.canvas(A.id(id), A.width("400"), A.height("100"))
        val div    = E.div(A.className("row"), canvas)
        println(s"XXX Adding chart div with id $id")
        divInfoMap.set(divMap + (info.eventSelection -> div))
      } else {
        val div = divMap(info.eventSelection)
        if (ciMap.contains(info.eventSelection)) {
          println(s"XXX update chart with id $id")
          updateChart(ciMap(info.eventSelection), info.eventSelection, events)
        } else {
          println(s"XXX make chart with id $id")
          val chart = makeChart(id, info.eventSelection, events)
          chartInfoMap.set(ciMap + (info.eventSelection -> ChartInfo(chart, div)))
        }
      }
    }
  }

  private def isNumericKey(keyType: KeyType[_]): Boolean = {
    keyType == IntKey || keyType == DoubleKey || keyType == FloatKey || keyType == ShortKey || keyType == ByteKey
  }

//  private def getParameterValue(p: Parameter[_]): Double = {
//    p.keyType match {
//      case IntKey => p.
//    }
//  }

  //noinspection TypeAnnotation
  private def makeChart(id: String, info: EventSelection, events: Seq[SystemEvent]): Chart = {
    val chartData = ChartData(List("XXX"), List(ChartDataset(Nil, "xxx")))
    val options   = ChartOptions()
    val config    = ChartConfiguration("line", chartData, options)
    val chart     = Chart(id, config)
    println(s"XXX chart elem for $id = $chart")
    chart
  }

  private def updateChart(chartInfo: ChartInfo, info: EventSelection, events: Seq[SystemEvent]): Unit = {
//    val data = events.flatMap { e =>
//      val t = e.eventTime.value
//      val date = new Date(t.getEpochSecond * 1000 + t.getNano/1000000.0)
//      val maybeParam = if (info.maybeName.nonEmpty) {
//        e.paramSet.find(_.keyName == info.maybeName.get)
//      } else {
//        e.paramSet.find(p => isNumericKey(p.keyType))
//      }
//      val eventValue = maybeParam.map(_.head.toString)
//      val dateStr = s"${date.getFullYear()}/${date.getMonth()+1}/${date.getDate()}T${date.getHours()}:${date.getMinutes()}:${date.getSeconds()}"
//      eventValue.foreach(ev => println(s"XXX update event with name = $dateStr, value = ${ev.toInt}"))
//      eventValue.map(ev => literal(name = date.toDateString(), value = js.Array(dateStr, ev.toInt)))
//    }.toJSArray
//    val options = js.Object {
//      val series = js.Array(literal(data = data))
//    }
//
//    println(s"XXX update chart for options: $options")
//    chartInfo.chart.setOption(options)
    // XXX TODO FIXME
    val event = events.head
    val maybeParam = if (info.maybeName.nonEmpty) {
      event.paramSet.find(_.keyName == info.maybeName.get)
    } else {
      event.paramSet.find(p => isNumericKey(p.keyType))
    }
    val eventValue = maybeParam.map(_.head.toString.toDouble)
    eventValue.foreach(value => Chart.addData(chartInfo.chart, event.eventTime.toString, value))
  }

  private def makeId(es: EventSelection): String = {
    s"${es.subsystem}-${es.maybeComponent.getOrElse("")}-${es.maybeName.getOrElse("")}}"
  }

  override def render(get: Get): Element = {
    val charts = get(eventStreams).map { info =>
      get(divInfoMap)(info.eventSelection)
    }
    println(s"XXX num charts: ${charts.size}")
    E.div(A.className("row"), Tags(charts))
  }

}
