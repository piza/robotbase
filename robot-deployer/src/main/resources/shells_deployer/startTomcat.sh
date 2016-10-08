#!/bin/sh

tomcartDir=$1

echo "tomcat dir:"
echo $tomcartDir

ps -ef | grep tomcat | grep -v grep | awk {'print $2'} | xargs kill -9

echo "start up tomcat"
$tomcartDir/bin/startup.sh
