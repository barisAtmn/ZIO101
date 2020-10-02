import zio.test.{DefaultRunnableSpec}
import zio._
import zio.console._
import zio.test._
import zio.duration._
import zio.test.TestAspect._

object HelloWorldSpecEffectsInterrup extends DefaultRunnableSpec{

  override def spec =
    testM("effects can be safely interrupted" ){
      for {
        _ <- ZIO.effectTotal(putStr("forever")).forever
      } yield assertCompletes
    } @@ timeout(3.second)

}
