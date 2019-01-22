package com.hootom.parser

import fastparse._, SingleLineWhitespace._

/**
  * A numbers parser.
  */
object NumbersParser {

  /** A parser of one number */
  private def number[_: P]: P[Int] = "-".? ~ P(CharIn("0-9").repX(1).!.map(_.toInt))

  /** A parser of numbers */
  private def numbers[_: P]: P[Seq[Int]] = P(number.!.map(_.toInt).rep ~ End)

  /** Parses a string to a sequence of integers */
  def parse(s: String): Parsed[Seq[Int]] = fastparse.parse(s, numbers(_))

}
