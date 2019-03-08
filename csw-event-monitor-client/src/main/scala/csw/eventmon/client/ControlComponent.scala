package csw.eventmon.client

import com.github.ahnfelt.react4s._

//<div>
//<span class="label">duration:</span>
//<span id="durationValue" class="value">20000</span>
//<span><input type="range" min="1000" max="60000" step="100" value="20000" id="duration" class="control"></span>
//</div>
//<div>
//<span class="label">ttl:</span>
//<span id="ttlValue" class="value">60000</span>
//<span><input type="range" min="1000" max="60000" step="100" value="60000" id="ttl" class="control"></span>
//</div>
//<div>
//<span class="label">refresh:</span>
//<span id="refreshValue" class="value">1000</span>
//<span><input type="range" min="50" max="3000" step="50" value="1000" id="refresh" class="control"></span>
//</div>
//<div>
//<span class="label">delay:</span>
//<span id="delayValue" class="value">2000</span>
//<span><input type="range" min="0" max="5000" step="100" value="2000" id="delay" class="control"></span>
//</div>
//<div>
//<span class="label">frameRate:</span>
//<span id="frameRateValue" class="value">30</span>
//<span><input type="range" min="1" max="60" step="1" value="30" id="frameRate" class="control"></span>
//</div>

//<form action="#">
//<p class="range-field">
//<input type="range" id="test5" min="0" max="100" />
//</p>
//</form>

case class ControlComponent() extends Component[NoEmit] {

  private val duration = State[Int](60)

  override def render(get: Get): Node = {
    E.div(
      A.className("row"),
      E.div(A.className("col s2 offset-s1"), Text("duration"), S.textAlign("right"), S.paddingRight("1em")),
      E.div(A.className("col s1"), Text(get(duration).toString), S.textAlign("left"), S.paddingLeft("1em")),
      E.div(
        S.paddingLeft("0px"),
        A.className("col s4"),
        A.className("range-field"),
        E.input(
          A.`type`("range"),
          A.onChangeText(t => duration.set(t.toInt)),
          A.min("20"),
          A.max("3600"),
          A.step("1"),
          A.value(get(duration).toString)
        )
      )
    )
  }
}
