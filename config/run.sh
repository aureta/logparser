#!/bin/bash

# Get the class path via the lib directory
JARS=''
for jar in `find ./jars/* -type f -name \*\.jar`;
        do JARS=$JARS:$jar;
done;

if [ $# -eq 0 ]
  then
    echo "ERROR: Job Name required";
    exit 128;
fi;

java -server -XX:PermSize=512M -XX:MaxNewSize=512M -XX:NewSize=512M -Xmx7g -Xms1g -XX:-UseGCOverheadLimit -XX:+CMSClassUnloadingEnabled -XX:+UseConcMarkSweepGC -XX:+UseParNewGC -Dcom.sun.management.jmxremote -classpath $JARS org.springframework.batch.core.launch.support.CommandLineJobRunner ./job-launcher-context.xml $1 run.id=`date +%s%N`
