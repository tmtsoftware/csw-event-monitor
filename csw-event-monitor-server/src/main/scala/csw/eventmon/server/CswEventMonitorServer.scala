package csw.eventmon.server

object CswEventMonitorServer extends App {
  // XXX TODO FIXME parse options
  val port = args.headOption.map(_.toInt)

  val wiring = new Wiring(port)

  wiring.server.start()
}
