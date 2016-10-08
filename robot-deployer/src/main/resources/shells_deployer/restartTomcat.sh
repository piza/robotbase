#!/bin/sh

tomcartDir=$1

echo "tomcat dir:"
echo $tomcartDir

$tomcartDir/bin/shutdown.sh
echo "tomcat shuting down .... sleep 5 seconds"
for i in {5..1}
do
sleep 1s
echo "."
done

ps -ef | grep tomcat | grep -v grep | awk {'print $2'} | xargs kill -9

echo "start up tomcat"
cd $tomcartDir/bin/startup.sh
