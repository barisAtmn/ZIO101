import zio._
import zio.logging._

object HelloWorldLogging extends App {

  val env =
    Logging.console(
      logLevel = LogLevel.Info,
      format = LogFormat.ColoredLogFormat()
    ) >>> Logging.withRootLoggerName("my-component")

  override def run(args: List[String]) =
    log.info("Hello from ZIO logger").provideCustomLayer(env).as(ExitCode.success)
}
