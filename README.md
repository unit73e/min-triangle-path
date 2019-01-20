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

The current solution performs poorly:

- Goes through all paths to get the minimum
- Creates all paths to construct the minimum path