package csw.eventmon.client
import com.github.ahnfelt.react4s._
import Navbar._
import csw.eventmon.client.LoadComponent.LoadSettings
import csw.eventmon.client.SaveComponent.SaveSettings

object Navbar {
  // The navbar emits commands that are handled in the main component
  sealed trait NavbarCommand
  case class AddEventSelection(eventSelection: EventSelection) extends NavbarCommand
  case class SaveConfig(s: SaveSettings) extends NavbarCommand
  case class LoadConfig(s: LoadSettings) extends NavbarCommand

//  val saveDropdownId = "saveDropdown"
//  val loadDropdownId = "loadDropdown"
}

case class Navbar() extends Component[NavbarCommand] {

  override def render(get: Get): Node = {
    val navbar = E.nav(
      E.div(
        A.className("nav-wrapper teal lighten-2"),
        E.a(A.href("#!"), A.className("brand-logo"), Text("CSW Event Monitor")),
        // List of items in the navbar
        E.ul(
          A.className("right"),
          // Add Event
          Component(EventSelectorComponent).withHandler(es => emit(AddEventSelection(es))),
          Component(SaveComponent).withHandler(s => emit(SaveConfig(s))),
          Component(LoadComponent).withHandler(s => emit(LoadConfig(s)))
//          // Save
//          E.li(
//            E.a(A.className("dropdown-trigger"),
//                A.href("#!"),
//                Attribute("data-target", saveDropdownId),
//                Text("Save"),
//                E.i(A.className("arrow_drop_down")))
//          ),
//          // Load
//          E.li(
//            E.a(A.className("dropdown-trigger"),
//                A.href("#!"),
//                Attribute("data-target", loadDropdownId),
//                Text("Load"),
//                E.i(A.className("arrow_drop_down")))
//          )
        )
      )
    )

//    val saveDropdown = E.ul(
//      A.id(saveDropdownId),
//      A.className("dropdown-content"),
//      E.li(E.a(A.href("#!"), Text("Save to File"))),
//      E.li(E.a(A.href("#!"), Text("Save to Config Service"))),
//    )
//
//    val loadDropdown = E.ul(
//      A.id(loadDropdownId),
//      A.className("dropdown-content"),
//      E.li(E.a(A.href("#!"), Text("Load to File"))),
//      E.li(E.a(A.href("#!"), Text("Load to Config Service"))),
//    )

//    Fragment(saveDropdown, loadDropdown, navbar)
    navbar
  }
}
