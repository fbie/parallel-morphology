#!/usr/bin/env bash
BASEDIR=$(cd $(dirname $0)/..; pwd)

mv $BASEDIR/site.properties /tmp/site.properties
(cd $BASEDIR/jpf-core; ant clean; ant)
(cd $BASEDIR/jpf-aprop; ant clean; ant)
(cd $BASEDIR/jpf-symbc; ant clean; ant)
mv /tmp/site.properties $BASEDIR/site.properties