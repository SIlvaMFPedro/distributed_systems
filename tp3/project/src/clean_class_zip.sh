#!/usr/bin/env bash
username=sd0302
password=bitcommit
url=http://192.168.8.171/sd0407/classes/
RegistryHostName=l040101-ws01.ua.pt
LogRepositoryHostName=l040101-ws02.ua.pt
StableHostName=l040101-ws03.ua.pt
StandHostName=l040101-ws04.ua.pt
PaddockHostName=l040101-ws05.ua.pt
BettingCenterHostName=l040101-ws06.ua.pt
RaceTrackHostName=l040101-ws07.ua.pt
MyThreadBrokerHostName=l040101-ws08.ua.pt
MyThreadHorsesHostName=l040101-ws09.ua.pt
MyThreadSpecHostName=l040101-ws10.ua.pt
registryPortNum=1099

find . -maxdepth 10 -type f -name "*.class" -delete

rm -rf *.tar.gz
echo "Cleaning..."
sshpass -p $password ssh -o StrictHostKeyChecking=no -f $username@$RegistryHostName "cd ~/Public/ ; rm -rf *.tar.gz; rm -rf classes/ ; rm -rf deploy/ ; rm -rf *.sh"
sshpass -p $password ssh -o StrictHostKeyChecking=no -f $username@$StableHostName "cd ~/Public/ ; rm -rf *"
sshpass -p $password ssh -o StrictHostKeyChecking=no -f $username@$StandHostName "cd ~/Public/ ; rm -rf *"
sshpass -p $password ssh -o StrictHostKeyChecking=no -f $username@$PaddockHostName "cd ~/Public/ ; rm -rf *"
sshpass -p $password ssh -o StrictHostKeyChecking=no -f $username@$BettingCenterHostName "cd ~/Public/ ; rm -rf *"
sshpass -p $password ssh -o StrictHostKeyChecking=no -f $username@$RaceTrackHostName "cd ~/Public/ ; rm -rf *"
sshpass -p $password ssh -o StrictHostKeyChecking=no -f $username@$MyThreadSpecHostName "cd ~/Public/ ; rm -rf *"
sshpass -p $password ssh -o StrictHostKeyChecking=no -f $username@$MyThreadHorsesHostName "cd ~/Public/ ; rm -rf *"
sshpass -p $password ssh -o StrictHostKeyChecking=no -f $username@$MyThreadBrokerHostName "cd ~/Public/ ; rm -rf *"
sshpass -p $password ssh -o StrictHostKeyChecking=no -f $username@$LogRepositoryHostName "cd ~/Public/ ; rm -rf *"
echo "Cleaning done! all .class & folders deleted!"