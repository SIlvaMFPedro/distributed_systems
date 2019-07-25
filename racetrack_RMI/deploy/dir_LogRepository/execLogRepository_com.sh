
#!/usr/bin/env bash
java -Djava.rmi.server.codebase=$3\
     -Djava.rmi.server.useCodebaseOnly=true\
     -Djava.security.policy=java.policy\
     server_side.logrepository.LogRepository_Exec $1 $2