# Minimum Triangle Path

This is an application to calculate the minimum paths in a triangle of
numbers.

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