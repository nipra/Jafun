#!/bin/bash

OOZIE="oozie job -oozie http://sachidn001.hq.navteq.com:11000/oozie/"
CONFIG="-config bundleProp.xml"
OPT="-run"

CMD="${OOZIE} ${CONFIG} ${OPT}"
echo ${CMD}

${CMD}
