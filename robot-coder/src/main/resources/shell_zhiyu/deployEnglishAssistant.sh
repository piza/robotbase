#!/bin/sh

projectDir=$1
deployDir=$2

echo "change dir to project dir:"
echo $projectDir

cd $projectDir
git pull
npm run build > /tmp/buildEnglish.output

echo "deploy dir:"
echo $deployDir

cd $deployDir
rm -rf english_assistant_back
mv english_assistant english_assistant_back
mv $projectDir/dist ./
mv dist english_assistant

echo "deploy success"
