/*
package com.knoldus

import java.util

import org.apache.ignite.Ignition
import org.apache.ignite.lang.IgniteCallable

import scala.collection.JavaConversions._

/**
  * Created by shivansh on 22/8/16.
  */
object StarterApplication extends App {
  try {
    val ignite = Ignition.start("src/main/resources/example-ignite.xml")
    val calls = new util.ArrayList[IgniteCallable[Int]]()
    "Count characters using callable".split(" ").map(a => calls.add(new IgniteCallable[Int] {
      override def call(): Int = a.length
    }))
    val res = ignite.compute().call(calls)
    val sum = res.toList.sum
    println("Total number of characters is '" + sum + "'.");
  } catch {
    case e: Exception =>
      println(e.getLocalizedMessage)
  }

}
*/
