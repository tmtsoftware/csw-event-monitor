package csw.test

import akka.actor.typed.ActorSystem
import akka.actor.typed.scaladsl.ActorContext
import akka.util.Timeout
import com.typesafe.config.ConfigFactory
import csw.command.client.messages.TopLevelActorMessage
import csw.event.api.exceptions.PublishFailure
import csw.framework.deploy.containercmd.ContainerCmd
import csw.framework.models.CswContext
import csw.framework.scaladsl.{ComponentBehaviorFactory, ComponentHandlers}
import csw.location.api.models.TrackingEvent
import csw.params.commands.CommandResponse.{Completed, SubmitResponse, ValidateCommandResponse}
import csw.params.commands.{CommandResponse, ControlCommand}
import csw.params.core.formats.ParamCodecs
import csw.params.events.{Event, EventName, SystemEvent}
import csw.time.core.models.UTCTime
import csw.params.core.generics.{Key, KeyType}
import csw.params.core.models.Id
import csw.prefix.models.Subsystem.CSW

import scala.concurrent.duration._
import scala.concurrent.ExecutionContextExecutor
import scala.util.Random

private class TestHcdBehaviorFactory extends ComponentBehaviorFactory {
  override def handlers(ctx: ActorContext[TopLevelActorMessage], cswServices: CswContext): ComponentHandlers =
    new TestHcdHandlers(ctx, cswServices)
}

//noinspection DuplicatedCode
private class TestHcdHandlers(ctx: ActorContext[TopLevelActorMessage], cswCtx: CswContext)
    extends ComponentHandlers(ctx, cswCtx)
    with ParamCodecs {

  import cswCtx._

  private val log                           = loggerFactory.getLogger
  implicit val system: ActorSystem[Nothing] = ctx.system
  implicit val ec: ExecutionContextExecutor = ctx.executionContext
  implicit def timeout: Timeout             = new Timeout(20.seconds)

  // Dummy key for publishing events
  private val eventValueKey: Key[Int] = KeyType.IntKey.make("hcdEventValue")
  private val eventName               = EventName("myHcdEvent")
  private val eventName2              = EventName("myHcdEvent2")
  private val eventName3              = EventName("myHcdEvent3")
  private val eventValues             = Random
  private val baseEvent               = SystemEvent(componentInfo.prefix, eventName)
  private val baseEvent2              = SystemEvent(componentInfo.prefix, eventName2)
  private val baseEvent3              = SystemEvent(componentInfo.prefix, eventName3)

  override def initialize(): Unit = {
    log.debug("Initialize called")
    startPublishingEvents()
  }

  override def validateCommand(runId: Id, controlCommand: ControlCommand): ValidateCommandResponse = {
    CommandResponse.Accepted(runId)
  }

  override def onSubmit(runId: Id, controlCommand: ControlCommand): SubmitResponse = {
    log.debug(s"onSubmit called: $controlCommand")
    Completed(runId)
  }

  override def onOneway(runId: Id, controlCommand: ControlCommand): Unit = {
    log.debug(s"onOneway called: $controlCommand")
    Completed(runId)
  }

  override def onShutdown(): Unit = {
    log.debug("onShutdown called")
  }

  override def onGoOffline(): Unit = log.debug("onGoOffline called")

  override def onGoOnline(): Unit = log.debug("onGoOnline called")

  override def onLocationTrackingEvent(trackingEvent: TrackingEvent): Unit =
    log.debug(s"onLocationTrackingEvent called: $trackingEvent")

  private def startPublishingEvents(): Unit = {
    val publisher = eventService.defaultPublisher
    publisher.publish(eventGenerator(), 1.seconds, p => onError(p))
    publisher.publish(eventGenerator2(), 50.millis, p => onError(p))
    publisher.publish(eventGenerator3(), 5.seconds, p => onError(p))
  }

  // this holds the logic for event generation, could be based on some computation or current state of HCD
  private def eventGenerator(): Option[Event] = {
    val event = baseEvent
      .copy(eventId = Id(), eventTime = UTCTime.now())
      .add(eventValueKey.set(eventValues.nextInt(100)))
    Some(event)
  }
  private def eventGenerator2(): Option[Event] = {
    val event = baseEvent2
      .copy(eventId = Id(), eventTime = UTCTime.now())
      .add(eventValueKey.set(eventValues.nextInt(1000)))
    Some(event)
  }

  private def eventGenerator3(): Option[Event] = {
    val event = baseEvent3
      .copy(eventId = Id(), eventTime = UTCTime.now())
      .add(eventValueKey.set(eventValues.nextInt(10000)))
    Some(event)
  }

  private def onError(publishFailure: PublishFailure): Unit =
    log.error(
      s"Publish failed for event: [${publishFailure.event}]",
      ex = publishFailure.cause
    )

  override def onDiagnosticMode(startTime: UTCTime, hint: String): Unit = {}

  override def onOperationsMode(): Unit = {}
}

object TestHcdApp extends App {
  val defaultConfig = ConfigFactory.load("TestHcd.conf")
  ContainerCmd.start("testhcd", CSW, args, Some(defaultConfig))
}
