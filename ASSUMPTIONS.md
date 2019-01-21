# Assumptions

## Insertion

The a new number is always inserted from top to bottom, left to right.
For example:

```
     7          7           7
    ↙ ↘        ↙ ↘         ↙ ↘
   6   6      6   6       6   6
  ↙          ↙ ↘ ↙       ↙ ↘ ↙ ↘
 3          3   8       3   8   5
```

## Input

There is no difference between a file with inline numbers or with new
lines. The following input files give the same result:

```
# File 1
1
1 2
3 4 5

# File 2
1 2 3 4 5
```

## Completion

It's possible to have an incomplete triangle:

```
     7
    ↙ ↘
   6   6
  ↙
 3
```

The minimum path is calculated either way. It should be `7 + 6 = 13`.