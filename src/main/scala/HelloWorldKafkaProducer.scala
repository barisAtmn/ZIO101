import io.circe.{Decoder, Encoder, Json}
import org.apache.kafka.clients.producer.{ProducerRecord, RecordMetadata}
import zio._
import zio.ZLayer
import zio.blocking.Blocking
import zio.kafka.producer._
import zio.kafka.serde._
import io.circe.generic.auto._, io.circe.syntax._

object HelloWorldKafkaProducer extends App{

  case class TwitterStatistic(accountId:String, campaignId: String, campaignName:String, lineItemId: String, placement: String, day:String, impressionCount: Int, clicksCount: Int, billedChargeLocalMicro: Int)

  type StringProducer = Producer[Any, String, String]

  val producerSettings = ZIO.effect(ProducerSettings(List("localhost:9092")))

  val stringProducer: ZLayer[Any, Throwable, StringProducer] =
    (ZLayer.fromEffect(producerSettings) ++ ZLayer.succeed(Serde.string: Serializer[Any,String])) >>>
      Producer.live[Any, String, String]

  override def run(args: List[String]): URIO[zio.ZEnv, ExitCode] = program.provideCustomLayer(stringProducer).exitCode

  def produceOne(
                  topic: String,
                  key: String,
                  message: String
                ): ZIO[Blocking with StringProducer, Throwable, RecordMetadata] =
    Producer.produce[Any, String, String](new ProducerRecord(topic, key, message))

  def produceMany(
                   topic: String,
                   kvs: Iterable[(String, String)]
                 ): ZIO[Blocking with StringProducer, Throwable, Chunk[RecordMetadata]] =
    Producer
      .produceChunk[Any, String, String](Chunk.fromIterable(kvs.map {
        case (k, v) => new ProducerRecord(topic, null, null, k, v)
      }))


  val program:ZIO[StringProducer with Blocking, Throwable, Unit] = for {
    topic      <- ZIO.effect("test")
    data       <- produceOne("test","asd",TwitterStatistic("ali","veli","r","as","s","k",0,0,0).asJson.toString())
  } yield ()


}
