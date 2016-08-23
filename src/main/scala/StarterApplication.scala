import java.util

import org.apache.ignite.lang.IgniteCallable
import org.apache.ignite.Ignition
import scala.collection.JavaConversions._

object StarterApplication extends App {
  try {
    // Ignition.setClientMode(true)
    val ignite = Ignition.start("src/main/resources/example-ignite.xml")
    val calls = new util.ArrayList[IgniteCallable[Int]]()

    for (word <- "Count characters using callable".split(" ")) {
      calls.add(new IgniteCallable[Int] {
        override def call(): Int = word.length
      })
    }

    // Iterate through all the words in the sentence and create Callable jobs.
    // Execute collection of Callables on the grid.
    val res = ignite.compute().call(calls)
    // Add up all the results.

    val sum = res.toList.sum
    println("Total number of characters is '" + sum + "'.")
  } catch {
    case exception: Exception => exception.printStackTrace()
  }
}
