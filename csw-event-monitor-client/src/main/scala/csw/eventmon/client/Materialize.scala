package csw.eventmon.client

import scala.scalajs.js
import org.scalajs.dom.Element

// A partial Materialize facade.
// Note: M.FormSelect.init() is called from main.scala.html once when the page loads,
// but needs to be called again for dynamic <select> updates!

@js.native
trait FormSelect extends js.Object {
  def init(elem: Element, options: js.Object): Unit = js.native
}

@js.native
object M extends js.Object {
  var FormSelect: FormSelect = js.native
}
