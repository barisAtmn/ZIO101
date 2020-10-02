import zio._

/**
 * bracket =>  try / finally
 * ZIO#bracket, which allows you to specify an acquire effect, which acquires a resource; a release effect, which releases it;
 * and a use effect, which uses the resource.
 * openFile(..)
 *      .bracket(_.close) { // always executed
 *       // use file
 *      }
 *
 * val resource = Zmanaged.make(connectRedis)(_.close)
 * resource.use {
 *  // use it
 * }
 **/
object HelloWorldResourceManagement extends App{

  override def run(args: List[String]): URIO[zio.ZEnv, ExitCode] = ???


  val lines = ???
}
