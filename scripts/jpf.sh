#!/usr/bin/env bash

BASEDIR=$(cd $(dirname $0)/..; pwd)

JPF_CORE=$BASEDIR/jpf-core
JPF_APROP=$BASEDIR/jpf-aprop
JPF_SYMBC=$BASEDIR/jpf-symbc

# needed by jpf-symbc
export LD_LIBRARY_PATH=$JPF_SYMBC/lib

echo "*** verifying"
time java -Xmx4096m -ea -jar $JPF_CORE/build/RunJPF.jar -log -show +target.args=$1 $BASEDIR/parallel-morphology/parallel-morphology.jpf
