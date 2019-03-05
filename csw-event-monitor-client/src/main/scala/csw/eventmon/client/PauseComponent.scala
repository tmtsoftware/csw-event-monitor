package csw.eventmon.client
import com.github.ahnfelt.react4s._

case class PauseComponent(enabled: P[Boolean]) extends Component[Boolean] {

  private val paused = State[Boolean](false)

  private def onClicked(get: Get)(e: MouseEvent): Unit = {
    paused.set(!get(paused))
    emit(get(paused))
  }

  override def render(get: Get): Node = {
    if (get(enabled)) {
      val iconName = if (get(paused)) "play_arrow" else "pause"
      val icon     = E.i(A.className("material-icons"), Text(iconName))
      E.li(E.a(A.href("#!"), A.onClick(onClicked(get)), icon))
    } else {
      E.li()
    }
  }
}
