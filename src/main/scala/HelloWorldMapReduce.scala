import zio._
import zio.blocking._
import java.nio.file._
import scala.collection.JavaConverters._
/**
 * Imports a synchronous effect that does blocking IO into a pure value.
 * def effectBlocking[A](effect: => A): Task[A] =
 *      blocking(ZIO.effect(effect))
 **/
object HelloWorldMapReduce extends App{
  override def run(args: List[String]): URIO[zio.ZEnv, ExitCode] = readAllfiles("src/main/resources/mapReduce").tap(e => ZIO.foreach(e)(file => console.putStrLn(file.getFileName.toString))).exitCode


  def readAllfiles(dir: String) = effectBlocking{
      Files.list(Paths.get(dir)).iterator().asScala.toList
  }

}
