package com.hootom

import com.hootom.collection.immutable.TriangleNumbers
import com.hootom.parser.NumbersParser

import scala.collection.mutable.ListBuffer
import scala.io.Source

object Main extends App {
  if (args.length != 1) {
    println("Usage: app <file>")
    System.exit(1)
  }

  val list = ListBuffer[Int]()
  for (line <- Source.fromFile(args(0)).getLines) {
    NumbersParser.parse(line) match {
      case fastparse.Parsed.Success(v, _) => list ++= v
      case f@fastparse.Parsed.Failure(_, _, _) =>
        println("Error found in file.")
        println(f.trace().longMsg)
        System.exit(1)
    }
  }

  val numbers = TriangleNumbers(list: _*)
  val minPath = numbers.minimalPath()
  println(minPath._1.mkString(" + ") + " = " + minPath._2)
}
