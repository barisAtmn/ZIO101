import zio._
import zio.clock.Clock
import zio.duration._

/**
 * .fork should be avoided for forever-running fibers.
 *
 * ZIO provides many operations for performing effects in parallel. These methods are all named with a Par suffix that helps you
 * identify opportunities to parallelize your code.
 *
 * Zips two effects into one	ZIO#zip	ZIO#zipPar
 * Effectfully loop over values	ZIO.foreach	ZIO.foreachPar
 *
 **/
object HelloWorldConcurrency extends App{

  override def run(args: List[String]): URIO[zio.ZEnv, ExitCode] = program4.tap(e => console.putStrLn(e.toString)).exitCode


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
  val program:ZIO[Any,Nothing,(Int,Int)] = for{
    fiber1  <- sumTillN(1000,0).fork
    fiber2  <- sumTillN(2000,0).fork
    fiber   = fiber1.zip(fiber2)
    result <- fiber.join
  } yield result

  /**
   * ZIO lets you race multiple effects in parallel, returning the first successful result
   **/
  val program2:ZIO[Any,Nothing,Int] = for{
    fiber   <- sumTillN(1000,0).race(sumTillN(2000,0))
  } yield fiber

  /**
   * ZIO lets you timeout any effect using the ZIO#timeout method, which returns a new effect that succeeds with an Option.
   * A value of None indicates the timeout elapsed before the effect completed.
   **/
  val program3:ZIO[Clock,Nothing,Option[Int]] = for{
    fiber   <- sumTillN(1000,0).timeout(10.seconds)
  } yield fiber

  /**
   *  Awaits the fiber, which suspends the awaiting fiber until the result of the fiber has been determined.
   **/
  val program4:ZIO[Any, Nothing, Exit[Nothing, String]] = for {
    fiber <- IO.succeed("Hi!").fork
    exit  <- fiber.await
  } yield exit

  /**
   *  A fiber whose result is no longer needed may be interrupted, which immediately terminates the fiber,
   *  safely releasing all resources and running all finalizers.
   **/
  val program5:ZIO[Any, Nothing, Exit[Nothing, Nothing]] = for {
    fiber <- IO.succeed("Hi!").forever.fork
    exit  <- fiber.interrupt
  } yield exit

  /**
   * Returns an effect that will timeout this effect, returning `None` if the
   * timeout elapses before the effect has produced a value; and returning
   * `Some` of the produced value otherwise.
   **/
  val program6:ZIO[Any with Clock, Nothing, Option[String]] = IO.succeed("Hello").timeout(10.seconds)

    /**
   * These methods combine two fibers into a single fiber that produces the results of both.
   * If either fiber fails, then the composed fiber will fail.
   **/
  val program7:ZIO[Any, Nothing, Fiber.Synthetic[Nothing, (Int, Int)]] = for{
    fiber1         <- sumTillN(1000,0).fork
    fiber2         <- sumTillN(2000,0).fork
    combined       =  fiber1.zip(fiber2)
    a              <- ZIO.succeed(combined)
  } yield a

}
