import zio._

import zio.duration._

object HelloWorldRef extends App{
  override def run(args: List[String]): URIO[zio.ZEnv, ExitCode] = result.tap(value => console.putStrLn(value.toString)).exitCode

  val result = for{
    counter     <- Ref.make(0)
    incrementer <- counter.update(_ + 1)
                          .repeat(Schedule.spaced(1.second))
                          .fork
    _           <- ZIO.sleep(6.seconds)
    result      <- counter.get
  } yield result

}
