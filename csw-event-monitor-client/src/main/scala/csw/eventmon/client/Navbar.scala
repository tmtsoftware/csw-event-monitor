package csw.eventmon.client
import com.github.ahnfelt.react4s._
import Navbar._
import csw.eventmon.client.SaveComponent.SaveSettings

object Navbar {
  // The navbar emits commands that are handled in the main component
  sealed trait NavbarCommand
  case class AddEventFieldSelection(eventFieldSelection: EventFieldSelection) extends NavbarCommand
  case class SaveConfig(settings: SaveSettings)                               extends NavbarCommand
  case class LoadConfig(events: Set[EventFieldSelection])                     extends NavbarCommand
}

case class Navbar(eventClient: P[EventJsClient]) extends Component[NavbarCommand] {

  override def render(get: Get): Node = {
    E.nav(
      E.div(
        A.className("nav-wrapper teal lighten-2"),
        E.a(A.href("#!"), A.className("brand-logo"), Text("CSW Event Monitor")),
        E.ul(
          A.className("right"),
          Component(EventSelectorComponent, get(eventClient)).withHandler(es => emit(AddEventFieldSelection(es))),
          Component(SaveComponent).withHandler(s => emit(SaveConfig(s))),
          Component(LoadComponent).withHandler(s => emit(LoadConfig(s)))
        )
      )
    )
  }
}
