package com.hootom.collection.immutable

import org.scalatest.{FlatSpec, Matchers}

class TriangleNumbersSpec extends FlatSpec with Matchers {

  "A triangle of numbers" should "get the first found minimal path" in {
    assert((List(7, 6, 3, 2), 18) == TriangleNumbers(7, 6, 3, 3, 8, 5, 11, 2, 10, 9).minimumPath())
    assert((List(7, 3, 8, 2), 20) == TriangleNumbers(7, 6, 3, 6, 8, 5, 11, 2, 10, 9).minimumPath())
    assert((List(1, 1, 1, 1), 4) == TriangleNumbers(1, 6, 1, 6, 1, 5, 11, 2, 1, 9).minimumPath())
    assert((List(1, 0, 1, 0), 2) == TriangleNumbers(1, 0, 1, 6, 1, 5, 11, 2, 0, 9).minimumPath())
    assert((List(2, 2, 2, 2), 8) == TriangleNumbers(2, 6, 2, 3, 8, 2, 11, 2, 10, 2).minimumPath())
    assert((List(1), 1) == TriangleNumbers(1).minimumPath())
    assert((List(-1, -2), -3) == TriangleNumbers(-1, 0, -2).minimumPath())
  }

  "An empty triangle of numbers" should "still get the first found minimal path" in {
    assert((List(), 0) == TriangleNumbers().minimumPath())
  }
}
