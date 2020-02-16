package bench

import zio.arrow._

import FileUtils._

object BenchUtils {

  val rand = new scala.util.Random

  /**
   * Generates a random Int from a specific range
   */
  def fromRange(start: Int, end: Int) = rand.nextInt((end - start) + 1)

  /**
   * Simple non-stack safe factorial function
   */
  def factorial(n: Int): Int =
    if (n == 0) return 1
    else return n * factorial(n - 1)

  /**
   * Prepare test file data
   */
  val totalWorkers = 5
  lazy val files   = List.tabulate(totalWorkers)(num => ("file" + num.toString, num.toString))

  /**
   * Create test files
   */
  def setup() = {
    newDir("bench")
    files.foreach { f =>
      newFile(f._1)
      wrFile(f._1, f._2)
    }
  }

  /**
   * Remove test files
   */
  def clean() = files.foreach(f => delFile(f._1))

  /**
   * Impure unsafe worker process
   * This performs IO to read the file, gets a value and calculates a `factorial` for that value
   */
  def worker(file: String): Int = {
    val seed = rdFile(file).fold(0)(data => data.toInt)
    factorial(seed)
  }

  val workers = files.map(f => worker(f._1))

  /**
   * Calculates the total value from all workers
   */
  def sum(list: List[Int]): Int = list.foldLeft(0) { case (acc, item) => acc + item }

  /**
   * ZIO Arrow worker
   */
  val arrWorker  = ZArrow.lift(worker)
  // val arrWorkers = files.map(f => arrWorker(f._1))

}
