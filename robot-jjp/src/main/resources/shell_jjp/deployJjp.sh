#!/bin/sh


projectDir=$1
deployDir=$2
jarName=$3

echo "project dir:"
echo $projectDir

echo "deploy dir:"
echo $deployDir

echo "kill program"
ps -ef | grep $jarName | grep -v grep | awk {'print $2'} | xargs kill -9

cd $deployDir
rm $jarName
cp $projectDir/target/$jarName ./

echo "start new version"
nohup java -jar $jarName &