#!/usr/bin/env bash
java -Djava.rmi.server.codebase="http://192.168.8.171/sd0302/classes/"\
     -Djava.rmi.server.useCodebaseOnly=true\
     -Djava.security.policy=java.policy\
     server_side.logrepository.LogRepository_Exec $1

