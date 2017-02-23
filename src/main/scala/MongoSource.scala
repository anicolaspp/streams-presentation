/**
  * Created by anicolaspp on 2/22/17.
  */

import akka.stream.ActorMaterializer
import reactivemongo.akkastream.cursorProducer
import reactivemongo.api.collections.bson.BSONCollection
import reactivemongo.api.{MongoConnection, MongoDriver}
import reactivemongo.bson.BSONDocument

import scala.concurrent.{ExecutionContext, Future}

trait MongoSource {

  def getDocumentSourceFromCollection()(implicit ec: ExecutionContext, mat: ActorMaterializer) =
    getCollection().map(_.find(BSONDocument.empty).cursor().documentSource())

  def getCollection()(implicit ec: ExecutionContext): Future[BSONCollection] = for {
    connection <- getConnection()
    db <- connection.database("concepts")
  } yield db.collection("concepts")

  private def getConnection()(implicit ec: ExecutionContext): Future[MongoConnection] =
    Future.fromTry(MongoConnection.parseURI("mongo.service:27017").map(MongoDriver().connection))
}
