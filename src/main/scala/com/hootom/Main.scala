package com.hootom

import com.hootom.collection.immutable.TriangleNumbers
import com.hootom.parser.NumberParser

import scala.collection.mutable.ListBuffer
import scala.io.Source

object Main extends App {
  if (args.length != 1) {
    println("Usage: app <file>")
    System.exit(1)
  }

  val list = ListBuffer[Int]()
  for (line <- Source.fromFile(args(0)).getLines) {
    NumberParser.parse(line) match {
      case fastparse.Parsed.Success(v, _) => list ++= v
      case fastparse.Parsed.Failure(e, _, _) =>
        println(e)
        System.exit(1)
    }
  }

  val numbers = TriangleNumbers(list:_*)
  val minPath = numbers.minimumPath()
  println(minPath._1.mkString(" + ") + " = " + minPath._2)
}
