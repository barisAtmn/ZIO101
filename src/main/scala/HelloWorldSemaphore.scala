import zio._

/**
 *  A counter we can use to limit access to a resource
 *  ex: just use 10 threads to access DB
 **/
object HelloWorldSemaphore extends App{
  override def run(args: List[String]): URIO[zio.ZEnv, ExitCode] = ???

  /**
   * without semaphore.
   *
   * ZIO.collectAllParN(10) {
   *   List.fill(50)(compute)
   * }
   *
   **/
//  val se = for {
//    permists <- Semaphore.make(10)
//    expensiveCompiutation = permists.withPermit{
//      // compute()
//    }
//    computations <- ZIO.collectAllPar(List.fill(50)(expensiveCompiutation))
//  } yield ()

}
