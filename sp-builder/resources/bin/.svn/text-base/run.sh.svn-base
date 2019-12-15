#!/bin/bash

echo "-------------------------------------"
echo " >> ADENON Ict * Service Platform << " 
echo "-------------------------------------"

SP_HOME=`cd "$(dirname $0)/..";pwd`
echo "Using SP_HOME : $SP_HOME"

#
OSGI_HOME="$SP_HOME/osgi"
OSGI_JAR="$OSGI_HOME/bundles/org.eclipse.osgi.jar"
OSGI_OPTIONS="-configuration $OSGI_HOME"

# Remove OSGi cache
rm -r $OSGI_HOME/org.eclipse.osgi

LOG_HOME="$SP_HOME/logs"

echo "Using OSGi Home : $OSGI_HOME"

JVM_OPTIONS="-Dsp.home=$SP_HOME \
-Dapp.hostname=$APP_HOSTNAME \
-Dapp.id=$SP_HOME \
-Dosgi.startLevel=100 \
-server \
-Xms256m \
-Xmx256m \
-XX:PermSize=64m \
-XX:MaxPermSize=64m \
-Xloggc:$LOG_HOME/gc.log"

echo "Starting SP with JVM options : " $JVM_OPTIONS

java $JVM_OPTIONS -jar $OSGI_JAR $OSGI_OPTIONS > $LOG_HOME/out.txt 2>&1 &
echo
