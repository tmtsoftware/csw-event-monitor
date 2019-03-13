package csw.eventmon.client

import com.github.ahnfelt.react4s._
import ControlComponent._
import csw.eventmon.client.react4s.React4sUtil._

object ControlComponent {
  case class ControlOption(duration: Int = 60,
                           ttl: Int = 60,
                           delay: Int = 2000,
                           frameRate: Int = 30,
                           chartWidth: Int = 67,
                           chartHeight: Int = 22,
                           performance: Boolean = false)

  val chartControlsId = "chartControls"
  val id              = "control-popup"
  val iconName        = "settings"
}

/**
 * A popup window with chart controls
 */
case class ControlComponent(settings: P[ControlOption]) extends Component[ControlOption] {

  // Makes the content of the chart control tab
  private def makeChartControls(get: Get): Element = {
    val s = get(settings)
    E.div(
      A.id(chartControlsId),
      E.h6(Text("Below you can configure how the charts are displayed.")),
      Component(SliderComponent,
                "duration",
                s.duration,
                "s",
                5,
                600,
                1,
                s"Data in the past ${s.duration} seconds will be displayed")
        .withKey("duration")
        .withHandler(secs => emit(s.copy(duration = secs))),
      Component(SliderComponent, "ttl", s.ttl, "s", 5, 600, 1, s"Data will be automatically deleted after ${s.ttl} seconds")
        .withKey("ttl")
        .withHandler(secs => emit(s.copy(ttl = secs))),
      Component(SliderComponent,
                "delay",
                s.delay,
                "ms",
                0,
                5000,
                100,
                s"Delay of ${s.delay} ms, so upcoming values are known before plotting a line")
        .withKey("delay")
        .withHandler(ms => emit(s.copy(delay = ms))),
      Component(SliderComponent,
                "frame rate",
                s.frameRate,
                "x/s",
                1,
                60,
                1,
                s"Chart is drawn ${s.frameRate} times per second (or when new events arrive)")
        .withKey("frameRate")
        .withHandler(ms => emit(s.copy(frameRate = ms))),
      Component(SliderComponent,
                "chart width",
                s.chartWidth,
                "vw",
                30,
                200,
                1,
                s"Each chart is drawn with a width of ${s.chartWidth} vw")
        .withKey("chartWidth")
        .withHandler(vw => emit(s.copy(chartWidth = vw))),
      Component(SliderComponent,
                "chart height",
                s.chartHeight,
                "vh",
                10,
                100,
                1,
                s"Each chart is drawn with a height of ${s.chartHeight} vh")
        .withKey("chartHeight")
        .withHandler(vh => emit(s.copy(chartHeight = vh))),
      E.div(
        A.className("row"),
        E.div(
          A.className("col s4 offset-s1"),
          E.label(
            E.input(A.`type`("checkbox"), A.className("filled-in"), onChecked(p => emit(s.copy(performance = p.toBoolean)))),
            E.span(Text("Optimize for Performance"))
          )
        )
      )
    )
  }

  private def makeButtons(get: Get): Element = {
    E.div(
      A.className("modal-footer"),
      E.a(A.href("#!"),
          A.className("modal-close waves-effect waves-green btn-flat"),
          A.onClick(_ => emit(ControlOption())),
          Text("Reset")),
      E.a(A.href("#!"), A.className("modal-close waves-effect waves-green btn-flat"), Text("OK")),
    )
  }

  override def render(get: Get): Node = {
    val icon    = E.i(A.className("material-icons"), Text(iconName))
    val trigger = E.a(A.className("modal-trigger"), A.href(s"#$id"), icon)
    val body = E.div(
      A.id(id),
      A.className("modal modal-fixed-footer"),
      E.div(A.className("modal-content"), makeChartControls(get)),
      makeButtons(get)
    )
    E.li(trigger, body)
  }

}
