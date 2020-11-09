import zio._
import cats.implicits._
import cats._
import cats.derived._
import derived.auto.show._
import cats.implicits._, cats._, cats.derived._

object HelloWorldZIOWithKittens extends App{

  override def run(args: List[String]): URIO[zio.ZEnv, ExitCode] = program.tap(a => console.putStrLn(a)).exitCode

  case class Address[T](street: T, city: T, state: T)


  val program = for {
    mike <- ZIO.succeed(Address("1 Main ST", "Chicago", "IL"))
  } yield mike.show
}
