#!/bin/sh
cd $(dirname $0)

if [ ! -d "$ANDROID_HOME" ]; then
  echo "ANDROID_HOME is not available"
  exit
fi

cd ../complete
mvn clean package
ret=$?
if [ $ret -ne 0 ]; then
  exit $ret
fi
rm -rf build

exit
