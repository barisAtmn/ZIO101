import zio.{App, ExitCode, Queue, UIO, URIO, ZIO, ZQueue}

/**
 * mapM ==> Transforms elements with an effectful function.
 **/
object HelloWorldQueue extends App {

  override def run(args: List[String]): URIO[Any, ExitCode] = annotatedOut.as(ExitCode.success)

  import java.util.concurrent.TimeUnit
  import zio.clock._

  val currentTimeMillis = currentTime(TimeUnit.MILLISECONDS)

  val annotatedOut: UIO[ZQueue[Any, Clock, Nothing, Nothing, String, (Long, String)]] =
    for {
      queue <- Queue.bounded[String](3)
      mapped = queue.mapM { el =>
        currentTimeMillis.map((_, el))
      }
    } yield mapped



  /**
   * Queue == ZQueue[Any, Any, Nothing, Nothing, A, A]
   **/
  val fiber:UIO[Queue[Int]] =
    for {
      queueI <- Queue.bounded[Int](16)
      queueO <- Queue.bounded[Int](16)
      _      <- queueI.offerAll(List.fill(16)(10))
      fiber  <- (
        for{
          element <- queueI.take
          _       <- queueO.offer(element)
        } yield()).forever.fork
      _      <- fiber.interrupt
      result = queueO
    } yield result
}
