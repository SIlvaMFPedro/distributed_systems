#!/usr/bin/env bash
java -cp .:\* -Djava.rmi.server.codebase="http://192.168.8.171/sd0302/classes/"\
     -Djava.rmi.server.useCodebaseOnly=false\
     -Djava.security.policy=java.policy\
     server_side.repository.Repository $1
