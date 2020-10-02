import zio._
import zio.duration._
/**
 * task.retry(
 *    ZSchedule.exponential(50.millis).jittered &&
 *    ZSchedule.recurs(5)
 * )
 **/
object HelloWorldRetry extends App{
  override def run(args: List[String]): URIO[zio.ZEnv, ExitCode] = mayFail.tap(args => console.putStr(args)).exitCode

  /**
   * ZIO[Any, Throwable, A]
   * Imports a synchronous side-effect
   * Produce value which has not been computed yet.
   * It may be failed.
   **/
  val mayFail = ZIO.effect(scala.io.Source.fromFile("file.txt").mkString).
    retry(Schedule.exponential(1.second).jittered && Schedule.recurs(3)) orElse(ZIO.effect(scala.io.Source.fromFile("src/main/resources/mapReduce/a.txt").mkString))
}
