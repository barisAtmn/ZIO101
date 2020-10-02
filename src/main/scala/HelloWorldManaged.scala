import java.io.Console

import zio._

/**
 * Managed is a data structure that encapsulates the acquisition and the release of a resource.
 * A Managed[E, A] is a managed resource of type A, which may be used by invoking the use method of the resource.
 **/
object HelloWorldManaged extends App{
  override def run(args: List[String]): URIO[zio.ZEnv, ExitCode] = program.tap(data => console.putStr(data.map(_.toString).mkString("[",",","]"))).exitCode


  val managedResource = Managed.make(Queue.unbounded[Int])(_.shutdown)

  def usedResource(data: Int) = managedResource.use {
    queue => for {
      _ <- queue.offer(data)
      _ <- queue.offer(5)
      _ <- queue.offer(6)
      a <- queue.takeAll
    } yield a
  }

  val program = for {
    data <- console.getStrLn
    intData <- ZIO(data.toInt).orElse(ZIO(3))
    a <- usedResource(intData)
  } yield a

  val zManagedResource: ZManaged[Console, Nothing, Queue[Int]] = ZManaged.make(Queue.unbounded[Int])(_.shutdown)

  val combined = for {
    queue <- managedResource
    queue2  <- zManagedResource
  } yield (queue, queue2)

  val usedCombinedRes = combined.use { case (queue, queue2) => for {
    _ <- queue.offer(1)
    a <- queue.take
    _ <- queue2.offer(a)
  } yield()
  }

}
