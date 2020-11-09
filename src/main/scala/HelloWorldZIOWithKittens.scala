import zio._
import cats.implicits._
import cats._
import cats.derived._
import derived.auto.show._
import cats.implicits._, cats._, cats.derived._
import lift._

object HelloWorldZIOWithKittens extends App{

  override def run(args: List[String]): URIO[zio.ZEnv, ExitCode] = program.tap(a => console.putStrLn(a.toString)).exitCode

  case class Address[T](street: T, city: T, state: T)

  case class Cat[T](food: T, foods: List[T])

  def foo(x: Int, y: String, z: Float) = s"$x - $y - $z"

  implicit val fc: Functor[Cat] = {
    import auto.functor._
    semiauto.functor
  }

  implicit val fc1: Functor[Address] = {
    import auto.functor._
    semiauto.functor
  }

  val lifted = Applicative[Option].liftA(foo _)

  val program = for {
    mike  <- ZIO.succeed(Address("1 Main ST", "Chicago", "IL"))
    kedi  <- ZIO.succeed(Cat(1, List(2, 3)))
    kedicik     = kedi.map(a => a + 1)
    mikecik     = mike.map(a => a + "asd")
    app        = lifted(Some(1), Some("a"), Some(3.2f))
  } yield (kedicik.show,mikecik.show, app.show)


}
