import zio._

import scala.concurrent.Future
import scala.util.Try

object HelloWordZIO extends App{
  override def run(args: List[String]): URIO[zio.ZEnv, ExitCode] = ???

  /**
   * ZIO[Any, Nothing, A]
   * Produce value which is already computed.
   **/
  val constant = ZIO.succeed(1)

  /**
   * ZIO[Any, Nothing, A]
   * Produce value which has not been computed yet.
   * It cant be failed.
   **/
  val neverFails = ZIO.effectTotal(System.nanoTime())

  /**
   * ZIO[Any, Throwable, A]
   * Imports a synchronous side-effect
   * Produce value which has not been computed yet.
   * It may be failed.
   **/
  val mayFail = ZIO.effect(scala.io.Source.fromFile("file.txt").mkString)


  /**
   * ZIO[Any, Throwable, A]
   * Produce value which has not been computed yet.
   * It may be failed. As try has Success and Failure
   **/
  val fromTry = ZIO.fromTry(Try(3))


  /**
   * ZIO[Any, E, A]
   * Produce value which has not been computed yet.
   * It may be failed. As Either has Right and Left
   **/
  val fromEither = ZIO.fromEither(Right(3))


  /**
   * ZIO[Any, Throwable, A]
   * Produce value which has not been computed yet.
   * It may be failed. As Future has Success and Failure
   **/
  val fromFuture = ZIO.fromFuture{ implicit ec =>
    Future.successful(3)
  }

  case class User(name:String, age:Int)

  val user:ZIO[Any,Throwable,User] = ZIO.fromFuture{ implicit ec =>
    Future(User("baris",29))
  }

  /**
   * Transform error type to another
   **/
  val mapError:ZIO[Any,String,User] = user.mapError(error => error.getMessage)

  /**
   * Remove error type to EITHER
   **/
  val removeError: ZIO[Any,Nothing,Either[Throwable,User]] = user.either


  val recovered:ZIO[Any,Throwable,Some[User]] = user.map(user => Some(user))

  /**
   * Recovers from all errors.
   **/
  val recoveredCatch:ZIO[Any,Nothing,Option[User]] = user.map(user => Some(user)).catchAll(_ => ZIO.succeed(None))

  /**
   * Recovers from all errors.
   **/
  val recoveredFold:ZIO[Any,Nothing,Option[User]] = user.fold(e => None, user => Some(user))

  val folded:ZIO[Any, Nothing, Option[User]] = user.foldCauseM(cause=> ZIO.effectTotal(None), user => ZIO.effectTotal(Some(user)))

  val foldedM:ZIO[Any, Nothing, Option[User]] = user.foldM(cause=> ZIO.effectTotal(None), user => ZIO.effectTotal(Some(user)))

  val fullExit:ZIO[Any,Nothing,Exit[Throwable,User]] = user.run




}
