#!/usr/bin/env bash

TIME=$(date +"%y-%m-%d-%H-%M-%S")
HOST=$(echo $USER)@$(hostname)
BASEDIR=$(cd $(dirname $0)/..; pwd)

function_test () {
    echo "Executing area open for "$1", lambda="$2" and "$3" repetitions."
    sh $BASEDIR/scripts/run-area-opening-experiment.sh $BASEDIR/data/$1 $2 16 $3 > $BASEDIR/test/area-open-$TIME-$HOST-$1-$2-$3.csv
}

(cd $BASEDIR; make clean; make experiments)

SYNTH1=synth001.jpg
SYNTH2=synth002.jpg
SYNTH3=synth003.jpg
SYNTH4=synth004.jpg
SYNTH5=synth005.jpg

NAT1=natural001.jpg
NAT2=natural002.jpg
NAT3=natural003.jpg

LAMBDA=1500

function_test $SYNTH1 $LAMBDA 10
function_test $SYNTH2 $LAMBDA 10
function_test $SYNTH3 $LAMBDA 10
function_test $SYNTH4 $LAMBDA 10
function_test $SYNTH5 $LAMBDA 10

function_test $NAT1 $LAMBDA 10
function_test $NAT2 $LAMBDA 10
function_test $NAT3 $LAMBDA 10
