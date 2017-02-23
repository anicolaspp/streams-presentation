import akka.actor.ActorSystem
import akka.kafka.scaladsl.Consumer
import akka.kafka.{ConsumerSettings, Subscriptions}
import akka.stream.ActorMaterializer
import org.apache.kafka.clients.consumer.ConsumerConfig
import org.apache.kafka.common.serialization.{ByteArrayDeserializer, StringDeserializer}

import scala.concurrent.Await
import scala.concurrent.duration.Duration

/**
  * Created by anicolaspp on 2/22/17.
  */
object KafkaSource {
  implicit val system = ActorSystem("demo")
  implicit val mat = ActorMaterializer()

  def main(args: Array[String]): Unit = {
    val settings = ConsumerSettings(system, new ByteArrayDeserializer, new StringDeserializer)
      .withBootstrapServers("localhost:9092")
      .withGroupId("id_1")
      .withProperty(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest")

    val kafkaSource = Consumer.plainSource(settings, Subscriptions.topics("randoms"))

    val done = 
      kafkaSource
      .map { record =>
        record.value()
      }
        .runForeach(println)

    Await.ready(done, Duration.Inf)
  }
}
