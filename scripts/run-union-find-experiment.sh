#!/usr/bin/env bash

BASEDIR=$(cd $(dirname $0)/..; pwd)

MEM="-Xmx1024M -Xms1024M"
DEBUG="-verbose:gc -XX:+PrintCompilation"

java $MEM -cp $CLASSPATH:$BASEDIR/bin:$BASEDIR/lib/multiverse-core-0.7.0.jar dk.itu.parallel.experiments.unionfind.UnionFindRunner $1 $2 $3 $4