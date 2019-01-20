package com.hootom.collection.immutable

import org.scalatest._
import scalax.collection.Graph
import scalax.collection.GraphPredef._

class TriangleNumbersSpec extends FlatSpec with Matchers {

  "A triangle numbers graph" should "be a pyramid like graph were each top node connects the bottom adjacent nodes" in {
    assert(TriangleNumbers().graph == Graph(
    ))
    assert(TriangleNumbers(1).graph == Graph(
      Node(0, 0))
    )
    assert(TriangleNumbers(1, 2).graph == Graph(
      Node(0, 0) ~> Node(0, 1)
    ))
    assert(TriangleNumbers(1, 2, 3).graph == Graph(
      Node(0, 0) ~> Node(0, 1),
      Node(0, 0) ~> Node(1, 1)
    ))
    assert(TriangleNumbers(1, 2, 3, 4).graph == Graph(
      Node(0, 0) ~> Node(0, 1),
      Node(0, 0) ~> Node(1, 1),
      Node(0, 1) ~> Node(0, 2)
    ))
    assert(TriangleNumbers(1, 2, 3, 4, 5).graph == Graph(
      Node(0, 0) ~> Node(0, 1),
      Node(0, 0) ~> Node(1, 1),
      Node(0, 1) ~> Node(0, 2),
      Node(0, 1) ~> Node(1, 2),
      Node(1, 1) ~> Node(1, 2)
    ))
    assert(TriangleNumbers(1, 2, 3, 4, 5, 6).graph == Graph(
      Node(0, 0) ~> Node(0, 1),
      Node(0, 0) ~> Node(1, 1),
      Node(0, 1) ~> Node(0, 2),
      Node(0, 1) ~> Node(1, 2),
      Node(1, 1) ~> Node(1, 2),
      Node(1, 1) ~> Node(2, 2)
    ))
  }

}
