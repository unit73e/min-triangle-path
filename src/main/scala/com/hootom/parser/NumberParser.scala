package com.hootom.parser

import fastparse._, SingleLineWhitespace._

object NumberParser {

  def number[_: P]: P[Int] = P(CharIn("0-9").repX(1).!.map(_.toInt))

  def numbers[_: P]: P[Seq[Int]] = P(number.!.map(_.toInt).rep ~ End)

  def parse(s: String): Parsed[Seq[Int]] = fastparse.parse(s, numbers(_))

}
