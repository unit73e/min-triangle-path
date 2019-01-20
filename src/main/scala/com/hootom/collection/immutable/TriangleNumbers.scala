package com.hootom.collection.immutable

import scalax.collection.Graph
import scalax.collection.GraphEdge._
import scalax.collection.GraphPredef._

object TriangleNumbers {
  def apply(numbers: Int*): TriangleNumbers = new TriangleNumbers(numbers: _*)
}

case class Node(x: Int, y: Int) {
  require(x >= 0 && y >= 0, "The x and y coordinates must be positive")
  require(x <= y, "The x = " + x + " cannot be bigger than y = " + y)

  def lastInRow: Boolean = x == y

  def next: Node = if (lastInRow) Node(0, y + 1) else Node(x + 1, y)

  def leftNode: Node = Node(x, y + 1)

  def rightNode: Node = leftNode.next

  def leftEdge: DiEdge[Node] = this ~> leftNode

  def rightEdge: DiEdge[Node] = this ~> rightNode

  def leftNode(g: Graph[Node, DiEdge]): Option[g.NodeT] = g find leftNode

  def rightNode(g: Graph[Node, DiEdge]): Option[g.NodeT] = g find rightNode

  def leftEdge(g: Graph[Node, DiEdge]): Option[g.EdgeT] = g find leftEdge

  def rightEdge(g: Graph[Node, DiEdge]): Option[g.EdgeT] = g find rightEdge

  def left(g: Graph[Node, DiEdge]): Option[g.NodeT] = leftEdge(g).map(_._2)

  def right(g: Graph[Node, DiEdge]): Option[g.NodeT] = rightEdge(g).map(_._2)
}

class TriangleNumbers(numbers: Int*) {

  private val RootNode = Node(0, 0)

  val values: Map[Node, Int] = initValues
  val graph: Graph[Node, DiEdge] = initGraph(numbers.size)

  def edges(nodes: Set[Node]): List[DiEdge[Node]] = {
    def leftEdge(n: Node): Option[DiEdge[Node]] = if (nodes contains n.leftNode) Some(n.leftEdge) else None

    def rightEdge(n: Node): Option[DiEdge[Node]] = if (nodes contains n.rightNode) Some(n.rightEdge) else None

    def adjacent(n: Node): List[DiEdge[Node]] = leftEdge(n).toList ++ rightEdge(n).toList

    nodes.map(adjacent).toList.flatten
  }

  def initGraph(n: Int): Graph[Node, DiEdge] = n match {
    case 0 => Graph()
    case 1 => Graph(RootNode)
    case _ => Graph(edges(values.keySet): _*)
  }

  def initValues: Map[Node, Int] = numbers match {
    case Nil => Map()
    case _ =>
      (List(RootNode -> numbers.head) /: numbers.tail) { (acc, i) =>
        acc.head._1.next -> i :: acc
      }.toMap
  }

  private def value(n: Node): Int = values getOrElse(n,
    throw new IllegalArgumentException("Node " + n + " exists in the graph but does not have a value.")
  )

  private def paths(n: Node): List[List[Int]] = {
    (n.left(graph), n.right(graph)) match {
      case (None, None) => List(List(value(n)))
      case (Some(l), None) => paths(l).map(e => value(n) :: e)
      case (None, Some(r)) => paths(r).map(e => value(n) :: e)
      case (Some(l), Some(r)) => (paths(l) ++ paths(r)).map(e => value(n) :: e)
    }
  }

  def paths(): List[List[Int]] = paths(RootNode)

  private def pathSums(n: Node): List[Int] =
    (n.left(graph), n.right(graph)) match {
      case (None, None) => List(value(n))
      case (Some(l), None) => pathSums(l).map(e => value(n) + e)
      case (None, Some(r)) => pathSums(r).map(e => value(n) + e)
      case (Some(l), Some(r)) => (pathSums(l) ++ pathSums(r)).map(e => value(n) + e)
    }

  def pathSums(): List[Int] = pathSums(RootNode)

  private def pathSumsMin(): Int = pathSums().min

  def minimumPath(): (List[Int], Int) = {
    val min = pathSumsMin()
    println("Minimum " + min)
    (paths().filter(_.sum == min).head, min)
  }

}