package csw.eventmon.client

import com.github.ahnfelt.react4s._

case class StripChart(eventStreams: P[List[EventStreamInfo]]) extends Component[NoEmit] {

  private var chartMap = Map[EventStreamInfo, Element]()

  override def componentWillRender(get: Get): Unit = {
    get(eventStreams).foreach { info =>
      if (!chartMap.contains(info)) {
        val id = info.eventSelection.toString
        println(s"XXX StripChart create chart for $id")
        chartMap = chartMap + (info -> E.div(A.className("row"), Component(ChartComponent, info)))
      }
    }
  }

  override def render(get: Get): Element = {
    val charts = get(eventStreams).map(chartMap)
    E.div(Tags(charts))
  }
}
