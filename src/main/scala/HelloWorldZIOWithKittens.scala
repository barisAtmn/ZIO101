import zio._
import cats.implicits._
import cats._
import cats.derived._
import derived.auto.show._

object HelloWorldZIOWithKittens extends App{

  override def run(args: List[String]): URIO[zio.ZEnv, ExitCode] = program.tap(a => console.putStrLn(a)).exitCode

  case class Address(street: String, city: String, state: String)

  val program = for {
    mike <- ZIO.succeed(Address("1 Main ST", "Chicago", "IL"))
    a    = mike.show
  } yield a
}
