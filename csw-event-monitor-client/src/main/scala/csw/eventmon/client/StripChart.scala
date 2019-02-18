package csw.eventmon.client

import com.github.ahnfelt.react4s._
import csw.eventmon.client.EventSelector.EventSelection
import csw.eventmon.client.MainComponent.EventStreamInfo

case class StripChart(eventStreams: P[List[EventStreamInfo]]) extends Component[NoEmit] {

//  private val chartMap = State[Map[EventSelection, ConstructorData[NoEmit]]](Map.empty)
  println("XXX StripChart constructor")

  // Make sure there is a div for each chart
//  override def componentWillRender(get: Get): Unit = {
//    get(eventStreams).foreach { info =>
//      if (!get(chartMap).contains(info.eventSelection)) {
//        println(s"XXX StripChart create chart for ${info.eventSelection.subsystem}")
//        val chart = Component(ChartComponent, info)
//        chartMap.modify(_ + (info.eventSelection -> chart))
//      }
//    }
//  }

  override def render(get: Get): Element = {
//    println(s"XXX StripChart render: eventStreams = ${get(eventStreams)}, chartMap keys = ${get(chartMap).keySet}")
//    val charts = get(eventStreams).map { info =>
//      get(chartMap)(info.eventSelection)
//    }

    val charts = get(eventStreams).map { info =>
        println(s"XXX StripChart create chart for ${info.eventSelection.subsystem}")
        Component(ChartComponent, info)
    }

    println(s"XXX num charts: ${charts.size}")
    E.div(Tags(charts))
  }
}
