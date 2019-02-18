package csw.eventmon.client

import com.github.ahnfelt.react4s._
import csw.eventmon.client.EventSelector.EventSelection
import csw.eventmon.client.MainComponent.EventStreamInfo
import csw.params.core.generics.KeyType
import csw.params.core.generics.KeyType._
import csw.params.events.SystemEvent

case class ChartComponent(eventStreamInfo: P[EventStreamInfo]) extends Component[NoEmit] {

  println("XXX ChartComponent constructor")
  private val maybeDiv = State[Option[Element]](None)
  private val maybeChart = State[Option[Chart]](None)
  private val maybeEvent = State[Option[SystemEvent]](None)

  private def makeId(es: EventSelection): String = {
    s"${es.subsystem}-${es.maybeComponent.getOrElse("")}-${es.maybeName.getOrElse("")}"
  }

  private def receiveEvents(get: Get): Unit = {
    get(eventStreamInfo).eventStream.onNext = {
      case event: SystemEvent =>
//        println(s"XXX Received system event: $event")
        if (get(maybeChart).nonEmpty)
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
    val info = get(eventStreamInfo).eventSelection
    val maybeParam = if (info.maybeName.nonEmpty) {
      event.paramSet.find(_.keyName == info.maybeName.get)
    } else {
      event.paramSet.find(p => isNumericKey(p.keyType))
    }
    if (maybeParam.nonEmpty) {
      val eventValue = maybeParam.map(_.head.toString.toDouble)
      val chart = get(maybeChart).get
      eventValue.foreach(value => Chart.addData(chart, event.eventTime.toString, value))
    }
  }

  override def componentWillUnmount(get : Get) : Unit = {
    val id = makeId(get(eventStreamInfo).eventSelection)
    println(s"XXX ChartComponent will unmount: $id")
  }


  override def componentWillRender(get: Get): Unit = {
    // Note: The div has to be added to the dom first, since the chart will look for its id
    val id = makeId(get(eventStreamInfo).eventSelection)
    println(s"XXX ChartComponent will render: $id")
    if (get(maybeDiv).isEmpty) {
      println(s"XXX Create div for $id")
      val canvas = E.canvas(A.id(id), A.width("400"), A.height("50"))
      val div    = E.div(A.className("row"), canvas).withRef { _ =>
        if (get(maybeChart).isEmpty) {
          println(s"XXX calling makeChart with $id")
          val chart = makeChart(id, get(eventStreamInfo).eventSelection)
          maybeChart.set(Some(chart))
          receiveEvents(get)
        }
      }
      maybeDiv.set(Some(div))
    } else {
//      if (get(maybeChart).isEmpty) {
//        val chart = makeChart(id, get(eventStreamInfo).eventSelection)
//        maybeChart.set(Some(chart))
//      }
    }
  }

  override def render(get: Get): Element = {
    get(maybeDiv).get
  }
}
