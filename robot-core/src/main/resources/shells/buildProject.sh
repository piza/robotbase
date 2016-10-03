#!/bin/bash

cd $1
mvn clean compile package install -Dmaven.test.skip=true