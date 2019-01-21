# Design

This document details the design decisions for this application.

## Graph

The representation of a triangle of numbers is as follows:

```
      7
     ↙ ↘
    6   3
   ↙ ↘ ↙ ↘
  3   8   5
```

A triangle of numbers is a [direct acyclic graph][dag] with the
following characteristics:

- The first row always starts with one node (the root node).
- Each row has one node more than the row above.
- The top nodes are connected to the two adjacent nodes bellow.

It's very similar to a binary tree except the middle nodes are shared by
the two adjacent nodes above. The first challenge is to construct such a
graph and for that it's reasonable to use an existing library.

### Graph Library

There are many graph libraries out there but most are implemented as
follows:

- Node are uniquely identified with a key
- Edges are are pointers from one node to another
- Nodes can be stored in a set and edges in a map

Meaning it's possible to have the following run time characteristics:

- Creation: `O(N)`
- Addition:	`O(1)`
- Look-up: `O(1)`

One such library is [Scala Graph][sc]. To construct triangle graph
efficiently one only has to generate the edges. The nodes are created
implicitly.

### Create Edges

To create the edges first we need to think of unique identifiers for
each node. Note that the triangle numbers graph can be represented in a
matrix:

```
   | 0 | 1 | 2
---------------
 0 | 7 |   |
---------------
 1 | 6 | 3 |
---------------
 2 | 3 | 8 | 5
```

Meaning the nodes can be represented as follows (without the values):

```
      (0,0)
      ↙   ↘
   (0,1) (1,1)
   ↙  ↘   ↙  ↘
(0,2) (1,2) (2,2)
```

The advantage of this representation is that it's easy to know the edges
of any given node:

 - A node `(x,y)` will be connected to the nodes `(x, y + 1)` and
   `(x + 1, y + 1)`, if those nodes exist.

It's also easy to figure the next node of any given node:

 - Given a `(x, y)` node, if `x = y`, the next node is `(0, y + 1)`,
   otherwise it's `(x + 1, y)`

This allows us to create all nodes ones in one go and all the edges
based on the set of nodes.

Than the edges only have to be passed to Scala Graph:

```
val graph = Graph(edges)
```

### Edges Weight

Having each node with the value included would be enough to represent
the triangle of numbers graph, however by doing so one cannot take
advantage of the Scala Graph weighted edges functions, such as the
shortest path from one node to another.

Instead of having each node associated with a value, the graph can also
be represented as follows:

```
          (0,0)
         7↙  7↘
       (0,1) (1,1)
      6↙  6↘ 3↙ 3↘
    (0,2) (1,2) (2,2)
   3↙  3↘ 8↙ 8↘ 5↙ 5↘
 (0,3) (1,3) (2,3) (3,3)
```

The paths represent the triangle of numbers values instead of the nodes.
It ends up being the same, except the graph is larger.

### Minimal Path

Getting the minimal path is trivial after if we have the shortest path
from one node to another:

- Get all the leaves. These are the last nodes, the nodes that do not
  have any connection.
- For each leave, calculate the shortest path from to root to the leaf.
- From all calculated shortest paths, get the shortest path.

[dag]:(https://en.wikipedia.org/wiki/Directed_acyclic_graph)
[sc]:(http://www.scala-graph.org)