#!/bin/sh
TAG=$(mvn -f ../pom.xml -Dexec.executable='echo' -Dexec.args='${project.version}' --non-recursive exec:exec -q)

cp ../target/eberry-travel.jar ./eberry-travel.jar
cp ../config/application.yaml ./application.yaml

docker build -f ./Dockerfile -t eberry-travel/eberry-travel:$TAG .