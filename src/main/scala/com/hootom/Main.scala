package com.hootom

import com.hootom.collection.immutable.TriangleNumbers

object Main extends App {
  val numbers = TriangleNumbers(7, 6, 3, 3, 8, 5, 11, 2, 10, 9)
  val minPath = numbers.minimumPath()
  println(minPath._1.mkString(" + ") + " = " + minPath._2)
}
