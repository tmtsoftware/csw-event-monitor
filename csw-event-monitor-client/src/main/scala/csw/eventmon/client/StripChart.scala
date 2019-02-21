package csw.eventmon.client

import com.github.ahnfelt.react4s._

case class StripChart(eventStreams: P[List[EventStreamInfo]]) extends Component[NoEmit] {

  override def render(get: Get): Node = {
    val list = get(eventStreams)
    val charts = list.map { info =>
      val id = info.eventSelection.toString
      val showXLabels = info == list.last
      Component(ChartComponent, info, showXLabels).withKey(id)
    }

    E.div(Tags(charts))
  }
}
