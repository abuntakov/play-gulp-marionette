#!/bin/bash

DOCKER_DIR=`dirname $0`

PROJECT_NAME="play-webpack"

DOCKER_CMD="docker-compose -f $DOCKER_DIR/docker-compose.yml -p $PROJECT_NAME"

case "$1" in
	up)
		`$DOCKER_CMD up -d`
		;;

	s)
		`$DOCKER_CMD start`
		;;

	d)
		`$DOCKER_CMD stop`
		;;

	rm)
		`$DOCKER_CMD rm -v --force`
		;;

	*)
		echo "Usage $0 {up|s|d|rm}"
		exit 1

esac
