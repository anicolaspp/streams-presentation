/**
  * Created by anicolaspp on 2/22/17.
  */

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.Source
import reactivemongo.akkastream.State
import reactivemongo.bson.BSONDocument

import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}


object MongoStreams extends MongoSource {

  implicit val system = ActorSystem("demo")
  implicit val mat = ActorMaterializer()
  implicit val ec = scala.concurrent.ExecutionContext.global


  def main(args: Array[String]): Unit = {

    val done =
      getDocumentSourceFromCollection()
        .flatMap(source => processSource(source))

    Await.ready(done, Duration.Inf)

    system.shutdown()
  }

  def processSource(source: Source[BSONDocument, Future[State]]) = {
    source
      .map(doc => doc.get("defaultLabel"))
      .runForeach(println)
  }






}
