# script to run oozie workflow for mylog
#

OOZIE="oozie job -oozie http://sachidn001.hq.navteq.com:11000/oozie/"
CONFIG="-config coordProp.xml"

OPT="-run"

CMD="${OOZIE} ${ARG} ${CONFIG} ${OPT}"

echo ${CMD}

${CMD}
