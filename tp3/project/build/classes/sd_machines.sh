#!/usr/bin/env bash
username=sd0302
password=bitcommit
url=http://192.168.8.171/sd0302/classes/
RegistryHostName=l040101-ws01.ua.pt
LogRepositoryHostName=l040101-ws02.ua.pt
RepositoryHostName=l040101-ws02.ua.pt
StableHostName=l040101-ws03.ua.pt
StandHostName=l040101-ws04.ua.pt
PaddockHostName=l040101-ws05.ua.pt
BettingCenterHostName=l040101-ws06.ua.pt
RaceTrackHostName=l040101-ws07.ua.pt
MyThreadBrokerHostName=l040101-ws08.ua.pt
MyThreadHorsesHostName=l040101-ws09.ua.pt
MyThreadSpecHostName=l040101-ws10.ua.pt
RegistryPortNumber=1099

bash compile_src_code.sh

echo "compressing source code..."
tar -czf dir_Registry.tar.gz deploy/dir_Registry/
tar -czf dir_LogRepository.tar.gz deploy/dir_LogRepository/
tar -czf dir_BettingCenter.tar.gz deploy/dir_BettingCenter/
tar -czf dir_Paddock.tar.gz deploy/dir_Paddock/
tar -czf dir_RaceTrack.tar.gz deploy/dir_RaceTrack/
tar -czf dir_Repository.tar.gz deploy/dir_Repository/
tar -czf dir_Stable.tar.gz deploy/dir_Stable/
tar -czf dir_Stand.tar.gz deploy/dir_Stand/
tar -czf dir_MyThreadBroker.tar.gz deploy/dir_MyThreadBroker/
tar -czf dir_MyThreadHorses.tar.gz deploy/dir_MyThreadHorses/
tar -czf dir_MyThreadSpec.tar.gz deploy/dir_MyThreadSpec/


sleep 5

echo "Unziping code in machines"
sshpass -p $password scp dir_Registry.tar.gz $username@$registryHostName:~/Public/
sshpass -p $password ssh -o StrictHostKeyChecking=no -f $username@$RegistryHostName "cd ~/Public/ ; tar -xmzf dir_Registry.tar.gz" &

sshpass -p $password scp dir_LogRepository.tar.gz $username@$LogRepositoryHostName:~/Public/
sshpass -p $password ssh -o StrictHostKeyChecking=no -f $username@$LogRepositoryHostName "cd ~/Public/ ; tar -xmzf dir_LogRepository.tar.gz" &

sshpass -p $password scp dir_Repository.tar.gz $username@$RepositoryHostName:~/Public/
sshpass -p $password ssh -o StrictHostKeyChecking=no -f $username@$RepositoryHostName "cd ~/Public/ ; tar -xmzf dir_Repository.tar.gz" &

sshpass -p $password scp dir_Stable.tar.gz $username@$StableHostName:~/Public/
sshpass -p $password ssh -o StrictHostKeyChecking=no -f $username@$StableHostName "cd ~/Public/ ; tar -xmzf dir_Stable.tar.gz" &

sshpass -p $password scp dir_Stand.tar.gz $username@$StableHostName:~/Public/
sshpass -p $password ssh -o StrictHostKeyChecking=no -f $username@$StandHostName "cd ~/Public/ ; tar -xmzf dir_Stand.tar.gz" &

sshpass -p $password scp dir_Paddock.tar.gz $username@$PaddockHostName:~/Public/
sshpass -p $password ssh -o StrictHostKeyChecking=no -f $username@$PaddockHostName "cd ~/Public/ ; tar -xmzf dir_Paddock.tar.gz" &

sshpass -p $password scp dir_BettingCenter.tar.gz $username@$BettingCenterHostName:~/Public/
sshpass -p $password ssh -o StrictHostKeyChecking=no -f $username@$BettingCenterHostName "cd ~/Public/ ; tar -xmzf dir_BettingCenter.tar.gz" &

sshpass -p $password scp dir_RaceTrack.tar.gz $username@$RaceTrackHostName:~/Public/
sshpass -p $password ssh -o StrictHostKeyChecking=no -f $username@$RaceTrackHostName "cd ~/Public/ ; tar -xmzf dir_RaceTrack.tar.gz" &

sshpass -p $password scp dir_MyThreadBroker.tar.gz $username@$MyThreadBrokerHostName:~/Public/
sshpass -p $password ssh -o StrictHostKeyChecking=no -f $username@$MyThreadBrokerHostName "cd ~/Public/ ; tar -xmzf dir_MyThreadBroker.tar.gz" &

sshpass -p $password scp dir_MyThreadHorses.tar.gz $username@$MyThreadHorsesHostName:~/Public/
sshpass -p $password ssh -o StrictHostKeyChecking=no -f $username@$MyThreadHorsesHostName "cd ~/Public/ ; tar -xmzf dir_MyThreadHorses.tar.gz" &

sshpass -p $password scp dir_MyThreadSpec.tar.gz $username@$MyThreadSpecHostName:~/Public/
sshpass -p $password ssh -o StrictHostKeyChecking=no -f $username@$MyThreadSpecHostName "cd ~/Public/ ; tar -xmzf dir_MyThreadSpec.tar.gz" &


echo "Setting RMI repository.... "
echo " "
sshpass -p $password ssh -o StrictHostKeyChecking=no -f $username@$registryHostName "cd ~/Public/ ; mkdir classes" &
sshpass -p $password scp set_rmi_registry.sh $username@$registryHostName:~/Public/
sshpass -p $password scp -r deploy/interfaces/ $username@$registryHostName:~/Public/classes/interfaces/
sshpass -p $password scp -r deploy/support/ $username@$registryHostName:~/Public/classes/support/
sshpass -p $password ssh -o StrictHostKeyChecking=no -f $username@$registryHostName "bash ~/Public/set_rmi_registry.sh $RegistryPortNumber" &
sleep 5
echo " "


echo "Setting Service Register.... "
echo " "
sshpass -p $password ssh -o StrictHostKeyChecking=no -f $username@$RegistryHostName "cd ~/Public/deploy/dir_Registry/ ; ./execRegistry_com.sh $RegistryHostName $RegistryPortNumber $url" &
sleep 5
echo " "


echo "Setting Log Repository.... "
echo " "
sshpass -p $password ssh -o StrictHostKeyChecking=no $username@$LogRepositoryHostName 'bash -s' < runLogMachine.sh & PID_Logging=$!
sleep 5
echo " "

echo "Setting Repository.... "
echo " "
sshpass -p $password ssh -o StrictHostKeyChecking=no -f $username@$RepositoryHostName "cd ~/Public/deploy/dir_Repository/ ; ./execRepository_com.sh $RegistryHostName $RegistryPortNumber $url" &
sleep 5
echo " "


echo "Setting Stable.... "
echo " "
sshpass -p $password ssh -o StrictHostKeyChecking=no -f $username@$StableHostName "cd ~/Public/deploy/dir_Stable/ ; ./execStable_com.sh $RegistryHostName $RegistryPortNumber $url" &
sleep 5
echo " "


echo "Setting Stand.... "
echo " "
sshpass -p $password ssh -o StrictHostKeyChecking=no -f $username@$StandHostName "cd ~/Public/deploy/dir_Stand/ ; ./execStand_com.sh $RegistryHostName $RegistryPortNumber $url" &
sleep 5
echo " "

echo "Setting Paddock.... "
echo " "
sshpass -p $password ssh -o StrictHostKeyChecking=no -f $username@$PaddockHostName "cd ~/Public/deploy/dir_Paddock/ ; ./execPaddock_com.sh $RegistryHostName $RegistryPortNumber $url" &
sleep 5
echo " "

echo "Setting BettingCenter.... "
echo " "
sshpass -p $password ssh -o StrictHostKeyChecking=no -f $username@$BettingCenterHostName "cd ~/Public/deploy/dir_BettingCenter/ ; ./execBettingCenter_com.sh $RegistryHostName $RegistryPortNumber $url" &
sleep 5
echo " "

echo "Setting RaceTrack.... "
echo " "
sshpass -p $password ssh -o StrictHostKeyChecking=no -f $username@$RaceTrackHostName "cd ~/Public/deploy/dir_RaceTrack/ ; ./execRaceTrack_com.sh $RegistryHostName $RegistryPortNumber $url" &
sleep 5
echo " "


echo "Setting MyThreadBroker.... "
echo " "
sshpass -p $password ssh -o StrictHostKeyChecking=no -f $username@$MyThreadBrokerHostName "cd ~/Public/deploy/dir_MyThreadBroker/ ; ./execMyThreadBroker_com.sh $RegistryHostName $RegistryPortNumber" &
sleep 5
echo " "


echo "Setting MyThreadHorses.... "
echo " "
sshpass -p $password ssh -o StrictHostKeyChecking=no -f $username@$MyThreadHorsesHostName "cd ~/Public/deploy/dir_MyThreadHorses/ ; ./execMyThreadHorses_com.sh $RegistryHostName $RegistryPortNumber" &
sleep 5
echo " "


echo "Setting MyThreadSpec.... "
echo " "
sshpass -p $password ssh -o StrictHostKeyChecking=no -f $username@$MyThreadSpecHostName "cd ~/Public/deploy/dir_MyThreadSpec/ ; ./execMyThreadSpec_com.sh $RegistryHostName $RegistryPortNumber" &
sleep 5
echo " "



wait $PID_Logging

sshpass -p $password  scp $username@$LogRepositoryHostName:~/Public/deploy/dir_LogRepository/*.txt .

bash clean_class_zip.sh
echo " "


subl RaceTrackLog.txt