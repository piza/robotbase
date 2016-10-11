#!/bin/bash

cd $1
git add .
git commit -m "coder commit"
git push

echo "coder push success"