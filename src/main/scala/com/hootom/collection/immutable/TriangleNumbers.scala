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

    /** Returns the next node to a given node, counting from left to right, top to bottom. */
    def next(n: Node): Node = if (n.lastInRow) Node(0, n.y + 1) else Node(n.x + 1, n.y)

    /** Returns the left node of a given node */
    def leftNode(n: Node): Node = Node(n.x, n.y + 1)

    /** Returns the right node of a given node */
    def rightNode(n: Node): Node = next(leftNode(n))

    /** Returns the left edge of a given node */
    def leftEdge(n: Node, weight: Int): WDiEdge[Node] = n ~> leftNode(n) % weight

    /** Returns the right edge of a given node */
    def rightEdge(n: Node, weight: Int): WDiEdge[Node] = n ~> rightNode(n) % weight

    /** Returns a map of nodes and values */
    def values: Map[Node, Int] = numbers match {
      case Nil => Map()
      case _ => (List(RootNode -> numbers.head) /: numbers.tail) { (acc, i) =>
        next(acc.head._1) -> i :: acc
      }.toMap
    }

    /** Returns the adjacent nodes of a given node */
    def adjacent(n: Node, values: Map[Node, Int]): List[WDiEdge[Node]] = (values get n)
      .map(v => leftEdge(n, v) :: rightEdge(n, v) :: Nil)
      .getOrElse(List.empty)

    val nodes = values.keys
    nodes.map(n => adjacent(n, values)).toList.flatten
  }

  /** Initializes the tree numbers graph */
  private def initGraph(): Graph[Node, WDiEdge] = Graph(makeEdges(): _*)

  /** Returns the leaves of the graph */
  private def leaves: Set[graph.NodeT] = graph.nodes.filter(n => !n.hasSuccessors)

  /** Returns the root node of this graph */
  private def rootNode: Option[graph.NodeT] = graph find RootNode

  /** Returns the first minimal path found in the triangle of numbers */
  def minimalPath(): (List[Int], Int) = {

    /** Shortest path from a node to another */
    def shortestPathTo(from: graph.NodeT, to: graph.NodeT): (graph.Path, Double) = {
      val sp = (from shortestPathTo to).get // assuming there's always a path
      (sp, sp.weight.toInt)
    }

    rootNode match {
      case None => (List(), 0)
      case Some(n) =>
        val minPath = (shortestPathTo(n, leaves.head) /: leaves.tail) ((a, b) => {
          val p = shortestPathTo(n, b)
          if (p._2 < a._2) p else a
        })
        (minPath._1.edges.map(_.weight.toInt).toList, minPath._2.toInt)
    }
  }
}