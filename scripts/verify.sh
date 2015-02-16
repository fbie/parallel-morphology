#!/usr/bin/env bash

TIME=$(date +"%y-%m-%d-%H-%M-%S")
HOST=$(echo $USER)@$(hostname)
BASEDIR=$(cd $(dirname $0)/..; pwd)

function_verify () {
    sh $BASEDIR/scripts/jpf.sh $1
    mv jpf_output.log $BASEDIR/test/jpf-verify-$HOST-$TIME-$1.log
}

# build verification suite
(echo "*** building verification suite"; cd $BASEDIR; make clean; make -j8 verify)

# Make sure that bugs are found
function_verify seq-bogus
function_verify bogus-sequential

# Make sure that sequential algorithm is a valid reference
function_verify seq-sequential

# Verify optimistic algorithms
function_verify opt-block-bucket
function_verify opt-block-counting

function_verify opt-split
function_verify opt-split-counting

function_verify opt-line-queues-msq
function_verify opt-line-queues-array

function_verify opt-pixel-queues-msq
function_verify opt-pixel-queues-array

# Verify STM algorithms
# TODO: multiverse does not seem to be compatible with JPF
#function_verify stm-split
#function_verify stm-split-counting

#function_verify stm-line-queues-msq
#function_verify stm-line-queues-array

#function_verify stm-pixel-queues-msq
#function_verify stm-pixel-queues-array

#function_verify stm-block-bucket
#function_verify stm-block-counting
