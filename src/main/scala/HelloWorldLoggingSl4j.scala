import zio._
import zio.logging.{LogAnnotation, log}
import zio.logging.slf4j.Slf4jLogger

object HelloWorldLoggingSl4j extends App {

  val correlationId = LogAnnotation[String](
    name = "correlationId",
    initialValue = "undefined-correlation-id",
    combine = (_, newValue) => newValue,
    render = identity
  )

  val logFormat = "[correlation-id = %s] %s"

  val env =
    Slf4jLogger.make((context, message) => logFormat.format(context(correlationId), message))

  import zio.duration._
  override def run(args: List[String]) =
    (for {
      fiber <- log.locally(correlationId("1234"))(ZIO.unit).fork.interruptStatus(InterruptStatus.Uninterruptible)
      _     <- log.info("info message with correlation id from modifyLogger")
      _     <- log.info("info message with correlation id from modifyLogger")
      _     <- log.info("info message with correlation id from modifyLogger")
      _     <- fiber.join
      _     <- ZIO.succeed(1).ensuring(log.info("asd"))
      _     <- log.locally(correlationId("1234111").andThen(LogAnnotation.Name("other-logger" :: Nil))) {
        log.info("info message with correlation id") *>
          log.info("info message with correlation id").delay(5.seconds)
      }
    } yield ExitCode.success).provideCustomLayer(env)
}
