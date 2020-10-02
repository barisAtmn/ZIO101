import zio._
import zio.console._
import zio.test._
import zio.test.Assertion._
import zio.test.environment._

object HelloWorld {
  def sayHello: URIO[Console, Unit] =
    console.putStrLn("Hello, World!")
}

/**
 *  fastest way to start writing tests is to extend DefaultRunnableSpec, which creates a Spec that is also an
 *  executable program you can run from within SBT using test:run or by using test with the SBT test runner.
 *
 *  If you ever do need to access the "live" environment just use the live method in the mock package or specify
 *  the live environment in your type signature like Live[Console].
 **/
object HelloWorldSpec extends DefaultRunnableSpec {
  import HelloWorld._
  def spec = suite("HelloWorldSpec")(
    testM("sayHello correctly displays output") {
      for {
        _      <- sayHello
        output <- TestConsole.output
      } yield assert(output)(equalTo(Vector("Hello, World!\n")))
    }
  )
}