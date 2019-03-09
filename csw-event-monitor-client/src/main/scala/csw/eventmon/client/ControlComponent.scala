package csw.eventmon.client

import com.github.ahnfelt.react4s._
import csw.eventmon.client.ControlComponent._

object ControlComponent {
  case class ControlOption(duration: Int = 60, ttl: Int = 60, delay: Int = 2000, frameRate: Int = 30)
}

/**
 * Displays a set of controls for changing the plot views
 */
case class ControlComponent(settings: P[ControlOption]) extends Component[ControlOption] {

  override def render(get: Get): Node = {
    val s = get(settings)
    E.div(
      Component(SliderComponent, "duration", s.duration, "s", 20, 600, 1, s"data in the past ${s.duration} seconds will be displayed")
        .withKey("duration")
        .withHandler(secs => emit(s.copy(duration = secs))),
      Component(SliderComponent, "ttl", s.ttl, "s", 20, 600, 1, s"data will be automatically deleted after ${s.ttl} seconds")
        .withKey("ttl")
        .withHandler(secs => emit(s.copy(ttl = secs))),
      Component(SliderComponent, "delay", s.delay, "ms", 0, 5000, 100, s"delay of ${s.delay} ms, so upcoming values are known before plotting a line")
        .withKey("delay")
        .withHandler(ms => emit(s.copy(delay = ms))),
      Component(SliderComponent, "frame rate", s.frameRate, "ms", 1, 60, 1, s"chart is drawn ${s.frameRate} times every second")
        .withKey("frameRate")
        .withHandler(ms => emit(s.copy(frameRate = ms))),
    )
  }
}
