#!/bin/sh


projectDir=$1
deployDir=$2
moduleDir=$3
jarName=$4

echo "project dir:"
echo $projectDir

echo "deploy dir:"
echo $deployDir

echo "kill program"
ps -ef | grep $jarName | grep -v grep | awk {'print $2'} | xargs kill -9 >> $deployDir/kill.log

echo $?

cd $deployDir/$moduleDir
rm $jarName
cp $projectDir/$moduleDir/target/$jarName ./

echo "start new version"
nohup java -jar $jarName &