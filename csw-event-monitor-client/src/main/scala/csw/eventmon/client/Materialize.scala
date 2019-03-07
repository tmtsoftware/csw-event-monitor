package csw.eventmon.client

import scala.scalajs.js
import org.scalajs.dom.Element

import scala.scalajs.js.annotation.{JSGlobal, JSImport}

// A partial Materialize facade.
// Note: M.FormSelect.init() is called from main.scala.html once when the page loads,
// but needs to be called again for dynamic <select> updates!

@JSImport("!style-loader!css-loader!materialize-css/dist/css/materialize.min.css", JSImport.Default )
@js.native
object MaterializeAssets extends js.Object {}

@js.native
trait FormSelect extends js.Object {
  def init(elem: Element, options: js.Object): Unit = js.native
}

@js.native
@JSImport("materialize-css", JSImport.Namespace)
object M extends js.Object {
  var FormSelect: FormSelect = js.native
}

// Contains some (non-native) utility methods
object Materialize {
  private val materializeAssets = MaterializeAssets

  // Calls M.FormSelect.init() on the select element with the given id
  // (Required after any dynamic update of a select element!)
  def formSelect(id: String)(a: Any): Unit = {
    val document = js.Dynamic.global.document
    val elem = document.getElementById(id).asInstanceOf[org.scalajs.dom.Element]
    M.FormSelect.init(elem, js.Object())
  }

}