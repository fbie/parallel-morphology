#!/usr/bin/env bash

BASEDIR=$(cd $(dirname $0)/..; pwd)
mkdir -p $BASEDIR/lib
cd $BASEDIR/lib

wget -nc http://repository.codehaus.org/org/multiverse/multiverse-core/0.7.0/multiverse-core-0.7.0.jar
wget -nc http://repository.codehaus.org/org/multiverse/multiverse-core/0.7.0/multiverse-core-0.7.0-javadoc.jar
