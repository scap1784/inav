#!/bin/sh

MINOR_VERSION=`cat minorVersion.txt`
MAJOR_VERSION=`cat majorVersion.txt`
BUILD=`cat build.txt`

case $1 in

-r)

MINOR_VERSION=`expr ${MINOR_VERSION} + 1`
BUILD=0
echo $BUILD > build.txt
echo $MINOR_VERSION > minorVersion.txt
exit
;;

-b)

BUILD=`expr ${BUILD} + 1`
echo $BUILD > build.txt
exit
;;

*)

;;

esac

echo \\\"${MAJOR_VERSION}.${MINOR_VERSION}.${BUILD}\\\"
