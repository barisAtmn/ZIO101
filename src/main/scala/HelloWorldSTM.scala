import zio._
import zio.stm._

/**
 * Software Transactional Memory (STM)
 * TRef: A mutable reference to an immutable value
 * TPromise: A mutable reference that can be set exactly once
 * TArray: An array of mutable references
 * TMap: A mutable map
 * TQueue: A mutable queue
 * TSet: A mutable set
 * TSemaphore: A semaphore
 * TReentrantLock: A reentrant lock
 **/
object HelloWorldSTM extends App {
  override def run(args: List[String]): URIO[zio.ZEnv, ExitCode] = program.tap(value => console.putStr(value.toString)).exitCode

  def transferMoney(from: TRef[Long], to: TRef[Long], amount: Long): STM[String, Long] =
    for {
      senderBal <- from.get
      _         <- if (senderBal < amount) STM.fail("Not enough money")
      else STM.unit
      _         <- from.update(existing => existing - amount)
      _         <- to.update(existing => existing + amount)
      recvBal   <- to.get
    } yield recvBal

  val program: IO[String, Long] = for {
    sndAcc  <- STM.atomically(TRef.make(500L))
    rcvAcc  <- STM.atomically(TRef.make(0L))
    recvAmt <- STM.atomically(transferMoney(sndAcc, rcvAcc, 500L))
  } yield recvAmt
}
