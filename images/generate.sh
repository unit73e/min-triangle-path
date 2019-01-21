#!/bin/bash
for i in *.dot; do
  dot -Tpng -Gdpi=60 "$i" -o "${i%.dot}.png"
done
