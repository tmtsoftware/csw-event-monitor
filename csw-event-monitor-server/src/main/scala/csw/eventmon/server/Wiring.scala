package csw.eventmon.server

import akka.actor
import akka.actor.typed.{ActorSystem, SpawnProtocol}
import csw.event.api.scaladsl.EventService
import csw.event.client.EventServiceFactory
import csw.location.api.scaladsl.LocationService
import csw.location.client.scaladsl.HttpLocationServiceFactory
import akka.actor.typed.scaladsl.adapter.TypedActorSystemOps
import akka.stream.Materializer

import scala.concurrent.ExecutionContextExecutor

class Wiring(port: Option[Int]) {

  implicit val typedSystem: ActorSystem[SpawnProtocol.Command] = ActorSystem(SpawnProtocol(), "eventmon")
  implicit lazy val untypedSystem: actor.ActorSystem           = typedSystem.toClassic
  implicit lazy val mat: Materializer                          = Materializer(typedSystem)
  implicit lazy val ec: ExecutionContextExecutor               = untypedSystem.dispatcher

  lazy val locationService: LocationService = HttpLocationServiceFactory.makeLocalClient
  lazy val eventService: EventService       = new EventServiceFactory().make(locationService)

  lazy val configs      = new Configs(port)
  lazy val eventMonitor = new EventMonitor(eventService)

  lazy val routes = new Routes(eventMonitor)
  lazy val server = new Server(configs, routes)
}
