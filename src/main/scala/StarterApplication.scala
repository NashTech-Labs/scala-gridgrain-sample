import java.util

import org.apache.ignite.lang.IgniteCallable
import org.apache.ignite.Ignition
import scala.collection.JavaConversions._

/**
  * Created by shivansh on 22/8/16.
  */
object StarterApplication extends App {

  try {
    val ignite = Ignition.start("src/main/resources/example-ignite.xml")
    val calls = new util.ArrayList[IgniteCallable[Int]]()
    val word = "Count characters using callable".split(" ").map(a => calls(a.length))
    // Iterate through all the words in the sentence and create Callable jobs.
    // Execute collection of Callables on the grid.
    val res = ignite.compute().call(calls)
    // Add up all the results.
    val sum = res.toList.sum
    println("Total number of characters is '" + sum + "'.");
  } catch {
    case e: Exception =>
      println(e.getLocalizedMessage)
  }

}
