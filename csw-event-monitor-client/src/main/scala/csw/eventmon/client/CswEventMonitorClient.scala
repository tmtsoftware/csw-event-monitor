package csw.eventmon.client

import com.github.ahnfelt.react4s.Component
import csw.eventmon.client.react4s.facade.NpmReactBridge

object CswEventMonitorClient {
  def main(args: Array[String]): Unit = {
    NpmReactBridge.renderToDomById(Component(MainComponent), "main")
  }
}
