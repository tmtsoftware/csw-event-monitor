package csw.eventmon.client

import com.github.ahnfelt.react4s._
import csw.eventmon.client.EventSelector.EventSelection
import csw.eventmon.client.MainComponent.EventStreamInfo

case class StripChart(eventStreams: P[List[EventStreamInfo]]) extends Component[NoEmit] {

  private def makeId(es: EventSelection): String = {
    s"${es.subsystem}-${es.maybeComponent.getOrElse("")}-${es.maybeName.getOrElse("")}"
  }

  override def render(get: Get): Element = {
    val charts = get(eventStreams).map { info =>
      val id = makeId(info.eventSelection)
      println(s"XXX StripChart create chart for $id")
      E.div(A.className("row"), E.p(Text(id)), Component(ChartComponent, info))
    }

    println(s"XXX num charts: ${charts.size}")
    E.div(Tags(charts))
  }
}
