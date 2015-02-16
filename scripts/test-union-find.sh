#!/usr/bin/env bash

TIME=$(date +"%y-%m-%d-%H-%M-%S")
HOST=$(echo $USER)@$(hostname)
BASEDIR=$(cd $(dirname $0)/..; pwd)
DATADIR=$BASEDIR/data

function_test () {
    echo "Executing union-find for graph "$1" and "$2" repetitions."
    sh $BASEDIR/scripts/run-union-find-experiment.sh $DATADIR/$1 16 $2 > $BASEDIR/test/union-find-$TIME-$HOST-$1-$2.csv
}

(cd $BASEDIR; make clean; make experiments)

function_test n500000_p0.000001.gr 10
function_test n500000_p0.000005.gr 10

function_test n1000000_p0.000005.gr 10
function_test n1000000_p0.00001.gr 10

