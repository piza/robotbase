#!/bin/sh

tomcartDir=$1

echo "tomcat dir:"
echo $tomcartDir

pid=`ps -ef | grep "org.apache.catalina.startup.Bootstrap" | grep -v grep | awk {'print $2'}`
if [ "${pid}" != "" ]
then
echo "force shutdown tomcat"
kill -9 $pid
fi
echo "start up tomcat"
$tomcartDir/bin/startup.sh
