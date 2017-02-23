/**
  * Created by anicolaspp on 2/17/17.
  */
import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.{Flow, Source}

import scala.concurrent.Await
import scala.concurrent.duration._


case class Number(value: String)

object Streams {
  def main(args: Array[String]): Unit = {

    implicit val system = ActorSystem("demo")
    implicit val mat = ActorMaterializer()

    val numbers = Source.fromIterator(() => unboundedNumbersFrom(0).toIterator)

    val runnable =
      numbers
        .via(succ)
        .via(toNumber)
        .take(10)
        .runForeach(println)

    Await.ready(runnable, Duration.Inf)

    system.shutdown()
  }

  def unboundedNumbersFrom(n: Int): Stream[Int] = n #:: unboundedNumbersFrom(n + 1)

  def isEven(n: Int) = n % 2 == 0

  def succ = Flow[Int].map(_ + 1)

  def toNumber = Flow[Int].map(n => Number(n.toString))
}







