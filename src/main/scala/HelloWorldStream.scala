import java.nio.file.{Files, Paths}

import zio.stream.{ZStream, ZTransducer}
import zio.{App, ExitCode, URIO, ZIO}
import zio.console._

/**
 *
 *  runCollect => ZStream to ZIO
 *  drain => ZStream[-R,+E, Nothing]
 *  tap ==> Adds an effect to consumption of every element of the stream.
 *  runDrain ==>
 *  transduce ==>
 *  interruptAfter(15 seconds)
 *  runHead ==> to get result to Option
 **/
object HelloWorldStream extends App {

  override def run(args: List[String]): URIO[zio.ZEnv, ExitCode] = zstreamTap.runCollect.foldM(e=> putStrLn(e.getMessage) *> URIO(ExitCode.failure), s=> putStrLn(s.toString()) *> URIO(ExitCode.success))


  val zstream:ZStream[Any, RuntimeException, Int] = ZStream(1,2) ++ ZStream.fail(new RuntimeException("hello world")).catchAll(e => {putStrLn(e.getMessage);ZStream.succeed(1)})

  val zstreamInfinite:ZStream[Any, RuntimeException, Int] = ZStream.iterate(0)(_ + 1).takeUntil(_ / 127 == 1) ++ ZStream.fail(new RuntimeException("hello world")).catchAll(e => {putStrLn(e.getMessage);ZStream.succeed(1)})

  val zstreamTap:ZStream[Any, RuntimeException, Int] = ZStream.iterate(0)(_ + 1).take(5).mapM(i => if(i==2) ZIO.succeed(i*5) else  ZIO.succeed(i*6)) ++ ZStream.fail(new RuntimeException("hello world")).catchAll(e => {putStrLn(e.getMessage);ZStream.succeed(1)})

}

object HelloWorldStream2 extends App {

  override def run(args: List[String]): URIO[zio.ZEnv, ExitCode] = stream.exitCode

  val csvPath = "src/main/resources/mlb_players.csv"

  val stream = ZStream.fromFile(Paths.get(csvPath))
    .transduce(ZTransducer.utf8Decode >>> ZTransducer.splitLines)
    .drop(1)
    .take(100)
    .tap(putStrLn(_))
    .runDrain

}
