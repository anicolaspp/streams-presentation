/**
  * Created by anicolaspp on 2/22/17.
  */

import akka.Done
import akka.actor.ActorSystem
import akka.kafka.ProducerSettings
import akka.kafka.scaladsl.Producer
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.{Flow, Sink, Source}
import org.apache.kafka.clients.producer.ProducerRecord
import org.apache.kafka.common.serialization.{ByteArraySerializer, StringSerializer}
import reactivemongo.akkastream.State
import reactivemongo.bson.BSONDocument

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}
import scala.util.Random

object KafkaSink extends MongoSource {
  implicit val system = ActorSystem("demo")
  implicit val mat = ActorMaterializer()
  implicit val ec = scala.concurrent.ExecutionContext.global

  def main(args: Array[String]): Unit = {
    val settings =
      ProducerSettings(system, new ByteArraySerializer, new StringSerializer)
      .withBootstrapServers("localhost:9092")

    val kafkaSink = Producer.plainSink(settings)

//    val done =
//      getDocumentSourceFromCollection()
//        .flatMap(source =>  processSourceWithSink(source, kafkaSink))

    val done =
      randomValuesSource
        .via(aFlow)
        .runWith(kafkaSink)

    Await.ready(done, Duration.Inf)

    system.shutdown()
  }

  def processSourceWithSink(source: Source[BSONDocument, Future[State]], kafkaSink: Sink[ProducerRecord[Array[Byte], String], Future[Done]]) = {
    source
      .map(_.get("defaultLabel"))
      .map(s => new ProducerRecord[Array[Byte], String]("mongo-topic", s.fold("NONE")(_.toString)))
      .runWith(kafkaSink)
  }

  def randomValuesSource = Source.fromIterator(() => rnd.iterator)

  def aFlow = Flow[String]
    .map(s => new ProducerRecord[Array[Byte], String]("randoms", (s, s.length).toString()))

  def rnd: Stream[String] = Random.nextString(10) #:: rnd
}
