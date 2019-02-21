package csw.eventmon.client

import com.github.ahnfelt.react4s._

case class StripChart(eventStreams: P[List[EventStreamInfo]]) extends Component[NoEmit] {

  override def render(get: Get): Element = {
    val charts = get(eventStreams).map { info =>
      val id = info.eventSelection.toString
      E.div(A.className("row"), A.id(info.eventSelection.toString + "-div"), Component(ChartComponent, info).withKey(id))
    }

    E.div(Tags(charts))
  }
}
