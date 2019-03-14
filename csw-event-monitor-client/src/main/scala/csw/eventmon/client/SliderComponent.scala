package csw.eventmon.client

import com.github.ahnfelt.react4s._

/**
 * Displays a slider and emits the current value
 */
case class SliderComponent(label: P[String],
                           value: P[Int],
                           units: P[String],
                           minValue: P[Int],
                           maxValue: P[Int],
                           step: P[Int],
                           tooltip: P[String])
    extends Component[Int] {

  private def valueChanged(i: Int): Unit = {
    emit(i)
  }

  override def render(get: Get): Node = {
    val id = get(label).replace(" ", "-")
    E.div(
      A.id(id),
      A.className("row tooltipped"),
      S.marginBottom("0px"),
      Attribute("data-position", "left"),
      Attribute("data-tooltip", get(tooltip)),
      E.div(A.className("col s2 offset-s1"), Text(s"${get(label)}:"), S.textAlign("right"), S.paddingRight("2px")),
      E.div(A.className("col s1"), Text(get(value).toString + get(units)), S.textAlign("left"), S.paddingLeft("2px")),
      E.div(
        S.paddingLeft("0px"),
        A.className("col s4"),
        A.className("range-field"),
        E.input(
          A.`type`("range"),
          A.onChangeText(s => valueChanged(s.toInt)),
          A.min(get(minValue).toString),
          A.max(get(maxValue).toString),
          A.step(get(step).toString),
          A.value(get(value).toString),
        )
      )
    ).withRef(Materialize.tooltip(id))
  }
}
