#!/usr/bin/env bash

BASEDIR=$(cd $(dirname $0)/..; pwd)
cd $BASEDIR

clone_jpf () {
    hg clone http://babelfish.arc.nasa.gov/hg/jpf/$1 $BASEDIR/$1
}

clone_jpf jpf-core
clone_jpf jpf-aprop
clone_jpf jpf-symbc
(cd $BASEDIR/jpf-symbc; hg import --no-commit $BASEDIR/scripts/jpf-symbc-fix)
