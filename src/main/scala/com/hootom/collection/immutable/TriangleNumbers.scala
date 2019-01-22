package com.hootom.collection.immutable

import scalax.collection.Graph
import scalax.collection.GraphPredef._
import scalax.collection.edge.Implicits._
import scalax.collection.edge.WDiEdge

import scala.collection.Set

/**
  * A triangle of numbers.
  *
  * A triangle of numbers is a direct acyclic graph (DAG) with a root node, each
  * row bellow having one node more and every node connecting to the two adjacent
  * nodes. Bellow is an graphical representation of a triangle of numbers:
  *
  * {{{
  *       7
  *      ↙ ↘
  *     6   3
  *    ↙ ↘ ↙ ↘
  *   3   8   5
  *  ↙ ↘ ↙ ↘ ↙ ↘
  * 11  2  10   9
  * }}}
  */
object TriangleNumbers {
  def apply(numbers: Int*): TriangleNumbers = new TriangleNumbers(numbers: _*)
}

/**
  * A node of the triangle of numbers.
  *
  * @param x the x coordinate
  * @param y the y coordinate
  */
case class Node private(x: Int, y: Int) {
  require(x >= 0 && y >= 0, "The x and y coordinates must be positive")
  require(x <= y, "The x = " + x + " cannot be bigger than y = " + y)

  /** True if the node is the last in its row */
  def lastInRow: Boolean = x == y

  /** The next node */
  def next: Node = if (lastInRow) Node(0, y + 1) else Node(x + 1, y)

  /** The adjacent left node */
  def leftNode: Node = Node(x, y + 1)

  /** The adjacent right node */
  def rightNode: Node = leftNode.next

  /** The left edge */
  def leftEdge(weight: Int): WDiEdge[Node] = this ~> leftNode % weight

  /** The right edge */
  def rightEdge(weight: Int): WDiEdge[Node] = this ~> rightNode % weight

}

/**
  * A triangle of numbers.
  *
  * @param numbers the numbers
  */
class TriangleNumbers private(numbers: Int*) {

  private val RootNode = Node(0, 0)
  private val graph: Graph[Node, WDiEdge] = initGraph()

  /** Creates the edges of the triangle numbers graph */
  private def makeEdges(): List[WDiEdge[Node]] = {
    def adjacent(n: Node, values: Map[Node, Int]): List[WDiEdge[Node]] = (values get n)
      .map(v => n.leftEdge(v) :: n.rightEdge(v) :: Nil)
      .getOrElse(List.empty)

    val nodes = values.keys
    nodes.map(n => adjacent(n, values)).toList.flatten
  }

  /** Initializes the tree numbers graph */
  private def initGraph(): Graph[Node, WDiEdge] = Graph(makeEdges(): _*)

  /** Creates a  numbers values */
  private def values: Map[Node, Int] = numbers match {
    case Nil => Map()
    case _ => (List(RootNode -> numbers.head) /: numbers.tail) { (acc, i) =>
      acc.head._1.next -> i :: acc
    }.toMap
  }

  /** Returns the leaves of the graph */
  private def leaves: Set[graph.NodeT] = graph.nodes.filter(n => !n.hasSuccessors)

  private def rootNode: Option[graph.NodeT] = graph find RootNode

  /** Returns the first minimal path found in the triangle of numbers */
  def minimalPath(): (List[Int], Int) = {
    rootNode match {
      case None => (List(), 0)
      case Some(n) =>
        val minPath = ((n shortestPathTo leaves.head).get /: leaves.tail) ((a, b) => {
          val p = (n shortestPathTo b).get
          if (p.weight < a.weight) p else a
        })
        val minWeight = minPath.weight
        val list = minPath.edges.map(_.weight.toInt).toList
        (list, minWeight.toInt)
    }
  }
}