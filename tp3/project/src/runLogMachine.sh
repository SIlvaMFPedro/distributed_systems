#!/usr/bin/env bash
registryPortNumber=1099
username=sd0302
password=bitcommit
url=http://192.168.8.171/sd0302/classes/
GeneralRepositoryHostName=l040101-ws02.ua.pt
registryHostName=l040101-ws01.ua.pt


cd ~/Public/deploy/dir_LogRepository/
bash execLogRepository_com.sh $registryHostName $registryPortNumber $url