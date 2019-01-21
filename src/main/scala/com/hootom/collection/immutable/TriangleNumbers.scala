package com.hootom.collection.immutable

import scalax.collection.Graph
import scalax.collection.GraphPredef._
import scalax.collection.edge.Implicits._
import scalax.collection.edge.WDiEdge

import scala.collection.Set


object TriangleNumbers {
  def apply(numbers: Int*): TriangleNumbers = new TriangleNumbers(numbers: _*)

  def main(args: Array[String]): Unit = {
    val numbers = TriangleNumbers(7, 6, 6, 3, 8, 5, 11, 2, 10, 9)
    println(numbers.minimumPath())
  }
}

case class Node(x: Int, y: Int) {
  require(x >= 0 && y >= 0, "The x and y coordinates must be positive")
  require(x <= y, "The x = " + x + " cannot be bigger than y = " + y)

  def lastInRow: Boolean = x == y

  def next: Node = if (lastInRow) Node(0, y + 1) else Node(x + 1, y)

  def leftNode: Node = Node(x, y + 1)

  def rightNode: Node = leftNode.next

  def leftEdge(weight: Int): WDiEdge[Node] = this ~> leftNode % weight

  def rightEdge(weight: Int): WDiEdge[Node] = this ~> rightNode % weight

}

class TriangleNumbers(numbers: Int*) {

  private val RootNode = Node(0, 0)

  val values: Map[Node, Int] = initValues
  val graph: Graph[Node, WDiEdge] = initGraph()

  private def makeEdges(): List[WDiEdge[Node]] = {
    def adjacent(n: Node): List[WDiEdge[Node]] = (values get n)
      .map(v => n.leftEdge(v) :: n.rightEdge(v) :: Nil)
      .getOrElse(List.empty)

    values.keys.map(adjacent).toList.flatten
  }

  private def initGraph(): Graph[Node, WDiEdge] = Graph(makeEdges(): _*)

  private def initValues: Map[Node, Int] = numbers match {
    case Nil => Map()
    case _ =>
      (List(RootNode -> numbers.head) /: numbers.tail) { (acc, i) =>
        acc.head._1.next -> i :: acc
      }.toMap
  }

  def leaves: Set[graph.NodeT] = graph.nodes.filter(n => !n.hasSuccessors)

  def minimumPath(): (List[Int], Int) = {
    // TODO: This could be optimized
    // It's first getting the leave that gets the minimum path and only than getting the minimum path
    // It's getting converting a path to a list of ints and calculating the weight again
    val n = graph get RootNode
    val minLeave = leaves minBy (l => (n shortestPathTo l).get.weight)
    val list = (n shortestPathTo minLeave).get.edges.map(_.weight.toInt).toList
    (list, list.sum)
  }
}