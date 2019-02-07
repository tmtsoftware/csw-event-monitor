package csw.eventmon.server

import akka.actor.ActorSystem
import akka.stream.{ActorMaterializer, Materializer}
import csw.event.api.scaladsl.EventService
import csw.event.client.EventServiceFactory
import csw.location.api.scaladsl.LocationService
import csw.location.client.ActorSystemFactory
import csw.location.client.scaladsl.HttpLocationServiceFactory

import scala.concurrent.ExecutionContext

class Wiring(port: Option[Int]) {

  lazy implicit val system: ActorSystem                = ActorSystemFactory.remote()
  lazy implicit val materializer: Materializer         = ActorMaterializer()
  lazy implicit val executionContext: ExecutionContext = system.dispatcher

  lazy val locationService: LocationService = HttpLocationServiceFactory.makeLocalClient
  lazy val eventService: EventService       = new EventServiceFactory().make(locationService)

  lazy val configs      = new Configs(port)
  lazy val eventMonitor = new EventMonitor(eventService)

  lazy val routes = new Routes(eventMonitor)
  lazy val server = new Server(configs, routes)
}
