package zio.arrow.example

import zio._

object App0 extends App {

  val f = (_: Int) + 1
  val g = (_: Int) * 2
  val h = (_: Int) - 3

  val arrF = ZArrow.fromFunction(f)
  val arrG = ZArrow.fromFunction(g)
  val arrH = ZArrow.fromFunction(h)

  val arrows = List(arrF, arrG, arrH)
  val composed = arrows.foldLeft(ZArrow.identity[Int])(_ >>> _)

  val prog0 = composed.run(10)

  def run(args: List[String]) =
    prog0.flatMap(a => console.putStrLn(a.toString)).as(0)
}