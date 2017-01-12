#!/bin/sh

projectDir=$1
deployDir=$2

echo "project dir:"
echo $projectDir

echo "deploy dir:"
echo $deployDir

cd $deployDir
rm -rf back
mv current back
mkdir current

echo "deploy new version"
cd current

jar -xvf ~/festivalWechat.war