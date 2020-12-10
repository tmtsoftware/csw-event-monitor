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
import csw.eventmon.server.twirl.Implicits._

class Routes(
    eventMonitor: EventMonitor
)(implicit ec: ExecutionContext, val actorSystem: ActorSystem)
    extends PlayJsonSupport
    with JsonSupport {

  import akka.http.scaladsl.marshalling.sse.EventStreamMarshalling._

  val route: Route = cors() {
    RouteExceptionHandler.route {
      pathSingleSlash {
        get {
          complete {
            csw.eventmon.server.html.index.render()
          }
        }
      } ~
      get {
        pathPrefix("events") {
          path("subscribe" / Segment) { subsystem =>
            parameters("component".?, "event".?, "rateLimit".?) { (component, event, rateLimit) =>
              complete {
                eventMonitor
                  .subscribe(subsystem, component, event, rateLimit.map(_.toInt))
                  .map {evt =>
                    ServerSentEvent(Json.stringify(JsonSupport.writeEvent(evt)))
                  }
                  .keepAlive(10.second, () => ServerSentEvent.heartbeat)
              }
            }
          }
        }
      } ~
      pathPrefix("assets" / Remaining) { file =>
        // optionally compresses the response with Gzip or Deflate
        // if the client accepts compressed responses
        encodeResponse {
          getFromResource("public/" + file)
        }
      }
    }
  }
}
