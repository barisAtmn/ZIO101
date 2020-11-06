
import zio.Runtime
import zio.clock.Clock
import zio.console.Console
import zio.internal.Platform
import zio._
import zio.duration._
object HelloWorldZIOApp {

  def main(args: Array[String]): Unit = {
    val runtime = Runtime.default

    runtime.unsafeRun(program.provideLayer(Clock.live ++ Console.live).as(0))

  }

  def sumTillN(value: Int, agg: Int):ZIO[Any,Nothing,Int] =
    if(value == 0)
      ZIO.succeed(agg)
    else{
      sumTillN(value - 1, agg + value - 1)
    }

  /**
   * These methods combine two fibers into a single fiber that produces the results of both.
   * If either fiber fails, then the composed fiber will fail.
   **/
  val program:ZIO[Clock with Console,Nothing,(Int,Int)] = for{
    _                     <- console.putStr("asd")
    _                     <- clock.sleep(80.seconds)
    fiber1  <- sumTillN(1000,0).fork
    fiber2  <- sumTillN(2000,0).fork
    fiber   = fiber1.zip(fiber2)
    result <- fiber.join
  } yield result
}
