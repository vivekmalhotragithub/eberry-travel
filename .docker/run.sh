#!/bin/sh
set -x
TAG=$(mvn -f ../pom.xml -Dexec.executable='echo' -Dexec.args='${project.version}' --non-recursive exec:exec -q)

docker run -it --name eberry \
           --rm \
           -p16001:16001 \
           eberry-travel/eberry-travel:$TAG