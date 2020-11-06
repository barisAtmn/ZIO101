import zio._

object IntegrationExample extends scala.App {
  val runtime = Runtime.default

  runtime.unsafeRun(Task(println("Hello World!")))



}