#!/usr/bin/env bash

if [ -z "$1" ]; then
    echo "Usage: generate-graphs.sh path/to/GTgraph/random/GTgraph-random"
    exit 1
fi

GENERATOR="$1"
BASEDIR=$(dirname $0)
CONF=$BASEDIR/graph.conf
DATA=data

generate()
{
    # GTgraph does not support combining
    # configuration files and parameters.
    # Therefore, we build a configuration
    # out of parameters and a standard config.
    TMPCONF=/tmp/gtgraph_n$2_p$3
    cp $CONF $TMPCONF
    echo "n $2" >> $TMPCONF
    echo "p $3" >> $TMPCONF

    $1 -c $TMPCONF -o $DATA/n$2_p$3.gr

    # Remove the build configuration afterwards.
    rm $TMPCONF
}

generate $GENERATOR 500000 0.00001
generate $GENERATOR 500000 0.0001
generate $GENERATOR 500000 0.001
