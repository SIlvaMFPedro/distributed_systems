
#!/usr/bin/env bash
java -Djava.rmi.server.codebase=$3\
     -Djava.rmi.server.useCodebaseOnly=true\
     -Djava.security.policy=java.policy\
     server_side.stable.Stable_Exec $1 $2