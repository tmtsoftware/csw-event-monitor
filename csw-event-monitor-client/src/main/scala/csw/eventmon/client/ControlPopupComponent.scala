package csw.eventmon.client

import com.github.ahnfelt.react4s._
import ControlPopupComponent._
import csw.eventmon.client.ControlComponent.ControlOption

object ControlPopupComponent {
  val id       = "control-popup"
  val iconName = "settings"
}

/**
 * A popup window with chart controls
 */
case class ControlPopupComponent(controlOptions: P[ControlOption], localStorageMap: P[Map[String, Set[EventFieldSelection]]])
    extends Component[ControlOption] {

  override def render(get: Get): Node = {
    val icon     = E.i(A.className("material-icons"), Text(iconName))
    val trigger  = E.a(A.className("modal-trigger"), A.href(s"#$id"), icon)
    val controls = Component(ControlComponent, get(controlOptions), get(localStorageMap)).withHandler(emit)
    val body     = E.div(A.id(id), A.className("modal"), controls)
    E.li(trigger, body)
  }

}
