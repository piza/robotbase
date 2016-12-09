#!/bin/bash

cd $1
echo 'kill process'
ps -ef | grep "robot-deployer" | grep -v grep | awk {'print $2'} | xargs kill -9

echo 'start project'
cd robot-deployer/target
nohup java -jar ./robot-deployer-1.0.jar >> /root/robot/logs/robot.log &