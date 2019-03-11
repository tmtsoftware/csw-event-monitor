package csw.eventmon.client

import com.github.ahnfelt.react4s._
import csw.eventmon.client.ControlComponent._

object ControlComponent {
  case class ControlOption(duration: Int = 60,
                           ttl: Int = 60,
                           delay: Int = 2000,
                           frameRate: Int = 30,
                           chartWidth: Int = 67,
                           chartHeight: Int = 22)

  val chartControlsId = "chartControls"
  val manageSavedId   = "editSaved"
}

/**
 * Displays a set of controls for changing the plot views
 */
case class ControlComponent(settings: P[ControlOption], localStorageMap: P[Map[String, Set[EventFieldSelection]]])
    extends Component[ControlOption] {

  // Makes the content of the chart control tab
  private def makeChartControls(get: Get): Element = {
    val s = get(settings)
    E.div(
      A.id(chartControlsId),
      Component(SliderComponent,
                "duration",
                s.duration,
                "s",
                20,
                600,
                1,
                s"Data in the past ${s.duration} seconds will be displayed")
        .withKey("duration")
        .withHandler(secs => emit(s.copy(duration = secs))),
      Component(SliderComponent, "ttl", s.ttl, "s", 20, 600, 1, s"Data will be automatically deleted after ${s.ttl} seconds")
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
                "ms",
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
          A.className("col s4 offset-s2"),
          E.a(
            A.className("waves-effect waves-light btn"),
            Text("Reset to Default Settings"),
            A.onClick(_ => emit(ControlOption())),
            S.paddingBottom("2vh")
          ),
        )
      )
    )
  }

  // Makes the content of the tab to manage local storage
  private def makeManageLocalStorageItem(get: Get): Element = {
    val map = get(localStorageMap)
    val items = map.keySet.toList.map { name =>
      E.div(
        A.className("row"),
        S.marginBottom("0px"),
        E.div(A.className("col s4 offset-s1"),
              E.label(E.input(A.`type`("checkbox"), A.className("filled-in")), E.span(Text(name))))
      )
    }

    val button =
      E.div(
        A.className("row"),
        E.div(
          A.className("col s4 offset-s2"),
          E.a(
            A.className("waves-effect waves-light btn"),
            Text("Delete Selected"),
//          A.onClick(_ => emit(ControlOption())),
            S.paddingBottom("2vh")
          )
        )
      )

    E.div(A.id(manageSavedId), Tags(items), button)
  }

  override def render(get: Get): Node = {
    E.div(
      A.className("row"),
      E.div(
        A.className("col s12"),
        E.ul(
          A.className("tabs"),
          E.li(A.className("tab col s3"), E.a(A.href(s"#$chartControlsId"), Text("Chart Controls"))),
          E.li(A.className("tab col s3"), E.a(A.href(s"#$manageSavedId"), Text("Manage Local Storage")))
        )
      ),
      makeChartControls(get),
      makeManageLocalStorageItem(get)
    )
  }
}
