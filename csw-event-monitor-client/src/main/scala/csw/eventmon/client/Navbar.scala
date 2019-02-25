package csw.eventmon.client
import com.github.ahnfelt.react4s._
import Navbar._
import csw.eventmon.client.LoadComponent.LoadSettings
import csw.eventmon.client.SaveComponent.SaveSettings

object Navbar {
  // The navbar emits commands that are handled in the main component
  sealed trait NavbarCommand
  case class AddEventSelection(eventSelection: EventSelection) extends NavbarCommand
  case class SaveConfig(s: SaveSettings)                       extends NavbarCommand
  case class LoadConfig(s: LoadSettings)                       extends NavbarCommand
}

case class Navbar() extends Component[NavbarCommand] {

  //    <a href="#" data-activates="slide-out" class="button-collapse hide-on-large-only"><i class="material-icons">menu</i></a>
//  <a href="#" data-target="slide-out" class="sidenav-trigger"><i class="material-icons">menu</i></a>

//  <ul id="slide-out" class="sidenav">
//    <li><div class="user-view">
//      <div class="background">
//        <img src="images/office.jpg">
//        </div>
//        <a href="#user"><img class="circle" src="images/yuna.jpg"></a>
//          <a href="#name"><span class="white-text name">John Doe</span></a>
//          <a href="#email"><span class="white-text email">jdandturk@gmail.com</span></a>
//        </div></li>
//      <li><a href="#!"><i class="material-icons">cloud</i>First Link With Icon</a></li>
//      <li><a href="#!">Second Link</a></li>
//      <li><div class="divider"></div></li>
//      <li><a class="subheader">Subheader</a></li>
//      <li><a class="waves-effect" href="#!">Third Link With Waves</a></li>
//    </ul>

  override def render(get: Get): Node = {
    val navbar = E.nav(
      E.div(
        A.className("nav-wrapper teal lighten-2"),
        E.a(A.href("#!"), A.className("brand-logo"), Text("CSW Event Monitor")),
        E.ul(
          A.className("right"),
          Component(EventSelectorComponent).withHandler(es => emit(AddEventSelection(es))),
          Component(SaveComponent).withHandler(s => emit(SaveConfig(s))),
          Component(LoadComponent).withHandler(s => emit(LoadConfig(s))),
          E.a(A.href("#"),
              Attribute("data-target", "slide-out"),
              A.className("sidenav-trigger"),
              E.i(A.className("material-icons"), Text("menu"))),
        )
      )
    )

    val slideOut = E.ul(A.id("slide-out"), A.className("sidenav"), E.li(E.p(Text("Hello"))))

    Fragment(navbar, slideOut)
  }
}
