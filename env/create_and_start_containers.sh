#!/bin/bash

docker rm -f play-db
docker run --name play-db -e POSTGRES_PASSWORD=secret  -p 5432:5432 -d mdillon/postgis:9.4

echo "postgres init..."
sleep 12

docker run -it --link play-db:postgres --rm postgres sh -c 'export PGPASSWORD=secret; exec psql -h "$POSTGRES_PORT_5432_TCP_ADDR" -p "$POSTGRES_PORT_5432_TCP_PORT" -U postgres <<EOF
	\x
	CREATE ROLE play LOGIN NOSUPERUSER INHERIT CREATEDB CREATEROLE NOREPLICATION;
	CREATE DATABASE play WITH OWNER = play;
	GRANT ALL ON DATABASE play TO play;
EOF'
