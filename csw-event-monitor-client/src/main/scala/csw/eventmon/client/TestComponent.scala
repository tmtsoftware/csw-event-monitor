//package csw.eventmon.client
//import com.github.ahnfelt.react4s._
//import TestComponent._
//
//object TestComponent {
//  private val id = "test"
//}
//
//class TestComponent extends Component[NoEmit] {
//  override def render(get: Get): Node = {
//    val trigger = E.a(A.className("modal-trigger"), A.href(s"#$id"), Text("Load"))
//    val body = E.div(
//      A.id(id),
//      A.className("modal modal-fixed-footer"),
//      E.div(A.className("model-content"), makeDialogBody(get)),
//      E.div(A.className("modal-footer"), makeButtons(get))
//    )
//    E.li(trigger, body)
//  }
//
//}
