package csw.eventmon.client

import com.github.ahnfelt.react4s._
import csw.eventmon.client.EventSelector.EventSelection
import csw.eventmon.client.MainComponent.EventStreamInfo
import csw.params.core.generics.KeyType
import csw.params.core.generics.KeyType._
import csw.params.events.SystemEvent

case class ChartComponent(eventStreamInfo: P[EventStreamInfo]) extends Component[NoEmit] {

  println("XXX ChartComponent constructor")
  private var canvasMap  = Map[String, Element]()
  private var chartMap   = Map[String, Chart]()
  private val maybeEvent = State[Option[SystemEvent]](None)

  private def makeId(es: EventSelection): String = {
    s"${es.subsystem}-${es.maybeComponent.getOrElse("")}-${es.maybeName.getOrElse("")}"
  }

  private def receiveEvents(get: Get): Unit = {
    get(eventStreamInfo).eventStream.onNext = {
      case event: SystemEvent =>
        println(s"XXX Received event $event")
        updateChart(get, event)
        maybeEvent.set(Some(event))

      case _ =>
    }
  }

  private def makeChart(id: String, info: EventSelection): Chart = {
    val chartData = ChartData(List(id), List(ChartDataset(Nil, id)))
    val options   = ChartOptions()
    val config    = ChartConfiguration("line", chartData, options)
    val chart     = new Chart(id, config)

    println(s"XXX chart elem for $id = $chart")
    chart
  }

  private def isNumericKey(keyType: KeyType[_]): Boolean = {
    keyType == IntKey || keyType == DoubleKey || keyType == FloatKey || keyType == ShortKey || keyType == ByteKey
  }

  private def updateChart(get: Get, event: SystemEvent): Unit = {
    val id = makeId(get(eventStreamInfo).eventSelection)
    if (chartMap.contains(id)) {
      val info = get(eventStreamInfo).eventSelection
      val maybeParam = if (info.maybeName.nonEmpty) {
        event.paramSet.find(_.keyName == info.maybeName.get)
      } else {
        event.paramSet.find(p => isNumericKey(p.keyType))
      }
      if (maybeParam.nonEmpty) {
        val eventValue = maybeParam.map(_.head.toString.toDouble)
        eventValue.foreach(value => Chart.addData(chartMap(id), event.eventTime.toString, value))
      }
    }
  }

  override def componentWillRender(get: Get): Unit = {
    // Note: The div has to be added to the dom first, since the chart will look for its id
    val id = makeId(get(eventStreamInfo).eventSelection)
    println(s"XXX componentWillRender: $id")
    if (!canvasMap.contains(id)) {
      println(s"XXX ChartComponent will render: $id")
      canvasMap = canvasMap + (id -> E.canvas(A.id(id), A.width("400"), A.height("50")))
      receiveEvents(get)
    } else if (!chartMap.contains(id)) {
      println(s"XXX calling makeChart with $id")
      chartMap = chartMap + (id -> makeChart(id, get(eventStreamInfo).eventSelection))
    }
  }

  override def render(get: Get): Element = {
    val id = makeId(get(eventStreamInfo).eventSelection)
    canvasMap(id)
  }
}
