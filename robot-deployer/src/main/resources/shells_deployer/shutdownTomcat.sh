#!/bin/sh

tomcartDir=$1

echo "tomcat dir:"
echo $tomcartDir

echo "tomcat shuting down .... sleep 5 seconds"
$tomcartDir/bin/shutdown.sh


