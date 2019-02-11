//package csw.eventmon.client
//
//import com.github.ahnfelt.react4s._
//import csw.eventmon.client.EventSelector.EventSelection
//import csw.eventmon.client.MainComponent.EventStreamInfo
//import csw.params.events.SystemEvent
//import paths.high.Stock
//
//import scala.scalajs.js
////import japgolly.scalajs.react._
////import japgolly.scalajs.react.vdom.all.key
////import japgolly.scalajs.react.vdom.svg.all._
////import paths.high.Stock
////
////import demo.colors._
////
////
////object line {
////  case class Event(date: String, value: Double)
////
////  private val palette = mix(Color(130, 140, 210), Color(180, 205, 150))
////  private val months = List("Jan", "Feb", "Mar", "Apr", "May",
////    "Jun", "Jul","Aug", "Sep", "Oct", "Nov", "Dec")
////
////  private def parseDate(event: Event) = {
////    val date = new js.Date
////    val Array(month, year) = event.date split ' '
////    date.setFullYear(year.toInt)
////    date.setMonth(months.indexOf(month))
////
////    date.getTime
////  }
////
////  val LineChart = ReactComponentB[Seq[Seq[Event]]]("Stock chart")
////    .render(events => {
////      val stock = Stock[Event](
////        data = events,
////        xaccessor = parseDate,
////        yaccessor = _.value,
////        width = 420,
////        height = 360,
////        closed = true
////      )
////      val lines = stock.curves map { curve =>
////        g(transform := "translate(50,0)",
////          path(d := curve.area.path.print, fill := string(transparent(palette(curve.index))), stroke := "none"),
////          path(d := curve.line.path.print, fill := "none", stroke := string(palette(curve.index)))
////        )
////      }
////
////      svg(width := 480, height := 400,
////        lines
////      )
////    })
////    .build
////}
//
//class StripChart(eventStreams: P[List[EventStreamInfo]], eventMap: P[Map[EventSelection, List[SystemEvent]]])
//    extends Component[NoEmit] {
//
//  private def parseDate(event: SystemEvent): Int = {
//    // XXX FIXME
//    0
//  }
//  private def eventValue(event: SystemEvent): Int = {
//    // XXX FIXME
//    0
//  }
//
//  private def makeChart(info: EventSelection, events: Seq[SystemEvent]): Element = {
//    val stock = Stock[SystemEvent](
//      data = Seq(events),
//          xaccessor = parseDate,
//          yaccessor = eventValue,
//          width = 420,
//          height = 260,
//          closed = true
//        )
//
//        val lines = stock.curves map { curve =>
//          g(
//            transform := "translate(50,0)",
//            path(d := curve.area.path.print, fill := string(transparent(palette(curve.index))), stroke := "none"),
//            path(d := curve.line.path.print, fill := "none", stroke := string(palette(curve.index)))
//          )
//        }
//
//        svg(width := 480, height := 400, lines)
//
//    E.div()
//  }
//
//  override def render(get: Get): Element = {
//    val charts = get(eventStreams).map(s =>
//      makeChart(s.eventSelection, get(eventMap)(s.eventSelection)))
//
//  }
//
//}
