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

  def left: Node = Node(x, y + 1)

  def right: Node = left.next

  def leftEdge: DiEdge[Node] = this ~> left

  def rightEdge: DiEdge[Node] = this ~> right

  def left(g: Graph[Node, DiEdge]): Option[g.NodeT] = g find left

  def right(g: Graph[Node, DiEdge]): Option[g.NodeT] = g find right

  def leftEdge(g: Graph[Node, DiEdge]): Option[g.EdgeT] = g find leftEdge

  def rightEdge(g: Graph[Node, DiEdge]): Option[g.EdgeT] = g find rightEdge

}

class TriangleNumbers(numbers: Int*) {

  val values: Map[Node, Int] = initValues
  val graph: Graph[Node, DiEdge] = initGraph(numbers.size)

  def edges(nodes: Set[Node]): List[DiEdge[Node]] = {
    def leftEdge(n: Node): Option[DiEdge[Node]] = if (nodes contains n.left) Some(n.leftEdge) else None

    def rightEdge(n: Node): Option[DiEdge[Node]] = if (nodes contains n.right) Some(n.rightEdge) else None

    def adjacent(n: Node): List[DiEdge[Node]] = leftEdge(n).toList ++ rightEdge(n).toList

    nodes.map(adjacent).toList.flatten
  }

  def initGraph(n: Int): Graph[Node, DiEdge] = n match {
    case 0 => Graph()
    case 1 => Graph(Node(0, 0))
    case _ => Graph(edges(values.keySet): _*)
  }

  def initValues: Map[Node, Int] = numbers match {
    case Nil => Map()
    case _ =>
      (List(Node(0, 0) -> numbers.head) /: numbers.tail) { (acc, i) =>
        acc.head._1.next -> i :: acc
      }.toMap
  }

}