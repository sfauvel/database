#!/usr/bin/env bash

#su - db2inst1
#db2start
#db2 create db hibern8

SCHEMA_NAME=MYSCHEMA

echo Start database
runuser -l db2inst1 -c "db2start"
echo Create database
runuser -l db2inst1 -c "db2 create db ${SCHEMA_NAME}"
echo Database initialized
