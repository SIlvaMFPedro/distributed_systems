#!/usr/bin/env bash
java -cp .:\* -Djava.rmi.server.codebase="file://$(pwd)/"\
     -Djava.rmi.server.useCodebaseOnly=false\
     -Djava.security.policy=java.policy\
     server_side.paddock.Paddock_Exec $1
