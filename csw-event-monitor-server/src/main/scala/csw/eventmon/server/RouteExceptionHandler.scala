package csw.eventmon.server

import akka.http.scaladsl.model._
import akka.http.scaladsl.server._

import scala.util.control.NonFatal

object RouteExceptionHandler extends Directives {

  def route: Directive[Unit] = handleExceptions(jsonExceptionHandler) & handleRejections(RejectionHandler.default)

  private val jsonExceptionHandler: ExceptionHandler = ExceptionHandler {
    case NonFatal(ex) => {
      complete(HttpResponse(StatusCodes.InternalServerError, entity = ex.getMessage))
    }
  }
}
