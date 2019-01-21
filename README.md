# Minimum Triangle Path

This is an application to calculate the minimal path in a triangle of
numbers.

A triangle of numbers can be represented like so:

```
      7
     ↙ ↘
    6   6
   ↙ ↘ ↙ ↘
  3   8   5
 ↙ ↘ ↙ ↘ ↙ ↘
11  2   10  9
```

A path is a sequence of connected nodes. For example,
`7 -> 6 -> 3 -> 11` is path from the left the triangle.

The minimal path is the path whose sum of values is lower than any other
path. In the following example `7 -> 6 -> 3 -> 2` is the minimal path.

This application only gives one of the possible minimal paths.

## Run

To run this application:

```
$ sbt run <file>
```

Where the file can be:

```
7
6 3
3 8 5
11 2 10 9
```

Which will give the following result:

```
7 + 6 + 3 + 2 = 18
```

## TODO

Some code could be further optimized:

- Some parts of minimum path code are being executed twice
- The minimum path search could be short circuited in if a path is
  already over a known minimum
- The file is being entirely to memory into a list but each block could
  be added to the graph instead