package csw.eventmon.client
import org.scalajs.dom.{Blob, BlobPropertyBag, Element}

import scala.scalajs.js
import scala.scalajs.js.annotation._

@js.native
@JSGlobal
object echarts extends js.Object {
//  val echarts: js.Dynamic = js.Dynamic.global.require("echarts")

  def init(dom: Element, theme: String, opts: js.Any): ECharts = js.native
}

@js.native
trait ECharts extends js.Object {
  def setOption(option: js.Any, notMerge: Boolean = false, lazyUpdate: Boolean = false): Unit = js.native
}

