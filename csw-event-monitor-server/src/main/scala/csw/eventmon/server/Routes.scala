package csw.eventmon.server

import akka.actor.ActorSystem
import akka.http.scaladsl.model.sse.ServerSentEvent
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import de.heikoseeberger.akkahttpplayjson.PlayJsonSupport
import play.api.libs.json.Json
import ch.megard.akka.http.cors.scaladsl.CorsDirectives._
import csw.params.core.formats.JsonSupport

import scala.concurrent.duration.DurationInt
import scala.concurrent.ExecutionContext

class Routes(
    eventMonitor: EventMonitor
)(implicit ec: ExecutionContext, val actorSystem: ActorSystem)
    extends PlayJsonSupport
    with JsonSupport {

  import akka.http.scaladsl.marshalling.sse.EventStreamMarshalling._

  val route: Route = cors() {
    RouteExceptionHandler.route {
      get {
        pathPrefix("events") {
          path("subscribe" / Segment) { subsystem =>
            parameters(("component".?, "event".?)) { (component, event) =>
              complete {
                eventMonitor
                  .subscribe(subsystem, component, event)
                  .map(evt => ServerSentEvent(Json.stringify(Json.toJson(evt))))
                  .keepAlive(10.second, () => ServerSentEvent.heartbeat)
              }
            }
          }
        }
      }
    }
  }
}
