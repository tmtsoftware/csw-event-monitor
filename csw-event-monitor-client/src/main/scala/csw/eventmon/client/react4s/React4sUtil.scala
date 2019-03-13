package csw.eventmon.client.react4s
import com.github.ahnfelt.react4s.{A, EventHandler}

object React4sUtil {
  // Should be in React4s: Like A.onChangeText, but for a checkbox
  def onChecked(onChange: String => Unit): EventHandler = {
    A.onChange(e => onChange(e.target.checked.asInstanceOf[Boolean].toString))
  }
}
