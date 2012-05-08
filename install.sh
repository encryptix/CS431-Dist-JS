#!/bin/bash
server="$@"

serverBin=$server"bin/"
serverApp=$server"webapps/distSystem/"
currentDir=`pwd`
echo $currentDir
echo $server
echo $serverBin
echo $serverApp
cd "$serverBin"
./shutdown.sh
cd "$currentDir"
ant compileSources
cp -r build/* "$serverApp"
cp -r server/bin/WEB-INF "$serverApp"
cd "$serverBin"
./startup.sh
