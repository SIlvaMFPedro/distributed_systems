#!/usr/bin/env bash
java -cp .:\* -Djava.rmi.server.codebase="file://$(pwd)/"\
     -Djava.rmi.server.useCodebaseOnly=false\
     -Djava.security.policy=java.policy\
     server_side.repository.Repository_Exec $1 
