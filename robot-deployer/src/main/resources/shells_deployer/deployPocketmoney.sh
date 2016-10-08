#!/bin/sh


projectDir=$1
deployDir=$2

echo "project dir:"
echo $projectDir

echo "project dir:"
echo $deployDir

cd $deployDir
rm -rf back
mv current back
mkdir current

echo "deploy new version"
cd current

jar -xvf $projectDir/portal-pocket/target/portal.war

cd WEB-INF/lib
find . -name "*.jar" | grep  -v "pocket" | xargs rm -f