package csw.eventmon.server

import akka.actor.ActorSystem
import akka.http.scaladsl.Http

import scala.concurrent.Future
import scala.util.{Failure, Success}

class Server(configs: Configs, routes: Routes)(implicit system: ActorSystem) {
  import system._

  def start(): Future[Http.ServerBinding] = {
    val f = Http().bindAndHandle(routes.route, interface = "0.0.0.0", port = configs.port)
    f.onComplete {
      case Success(b) =>
        println(s"Server online at http://${b.localAddress.getHostName}:${b.localAddress.getPort}")
      case Failure(ex) =>
        ex.printStackTrace()
    }
    f
  }
}
