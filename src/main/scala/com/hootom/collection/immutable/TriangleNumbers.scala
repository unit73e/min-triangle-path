package com.hootom.collection.immutable

import scalax.collection.Graph
import scalax.collection.GraphEdge._
import scalax.collection.GraphPredef._

import scala.annotation.tailrec

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

  private def next(g: Graph[Node, DiEdge], n: Node): Graph[Node, DiEdge] = n left g match {
    case None => Graph(n.leftEdge)
    case _ => if (n.lastInRow) Graph(n.rightEdge) else Graph(n.rightEdge, n.next.leftEdge)
  }

  private def add(g: Graph[Node, DiEdge], n: Node): Graph[Node, DiEdge] = g ++ next(g, n)

  @tailrec
  private def add(g: Graph[Node, DiEdge], n: Node, s: Int): Graph[Node, DiEdge] = s match {
    case 1 => g
    case _ =>
      val gs = add(g, n)
      n right gs match {
        case None => add(gs, n, s - 1)
        case _ => add(gs, n.next, s - 1)
      }
  }

  def initGraph(n: Int): Graph[Node, DiEdge] = n match {
    case 0 => Graph()
    case 1 => Graph(Node(0, 0))
    case _ =>
      val rootNode = Node(0, 0)
      add(Graph(rootNode), rootNode, n)
  }

  def initValues: Map[Node, Int] = numbers match {
    case Nil => Map()
    case _ =>
      (List(Node(0, 0) -> numbers.head) /: numbers.tail) { (acc, i) =>
        acc.head._1.next -> i :: acc
      }.toMap
  }

}