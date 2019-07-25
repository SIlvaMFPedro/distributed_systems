echo "  > Sending the proper jar to the correct workstation"
cd ../jars
sshpass -p bitcommit scp -o StrictHostKeyChecking=no LogRepository.jar sd0302@l040101-ws01.ua.pt:~
sshpass -p bitcommit scp -o StrictHostKeyChecking=no -r lib sd0302@l040101-ws01.ua.pt:~
sshpass -p bitcommit scp -o StrictHostKeyChecking=no Stable.jar sd0302@l040101-ws02.ua.pt:~
sshpass -p bitcommit scp -o StrictHostKeyChecking=no -r lib sd0302@l040101-ws02.ua.pt:~
sshpass -p bitcommit scp -o StrictHostKeyChecking=no Stand.jar sd0302@l040101-ws03.ua.pt:~
sshpass -p bitcommit scp -o StrictHostKeyChecking=no -r lib sd0302@l040101-ws03.ua.pt:~
sshpass -p bitcommit scp -o StrictHostKeyChecking=no Paddock.jar sd0302@l040101-ws04.ua.pt:~
sshpass -p bitcommit scp -o StrictHostKeyChecking=no -r lib sd0302@l040101-ws04.ua.pt:~
sshpass -p bitcommit scp -o StrictHostKeyChecking=no Betting_Center.jar sd0302@l040101-ws05.ua.pt:~
sshpass -p bitcommit scp -o StrictHostKeyChecking=no -r lib sd0302@l040101-ws05.ua.pt:~
sshpass -p bitcommit scp -o StrictHostKeyChecking=no RaceTrack.jar sd0302@l040101-ws06.ua.pt:~
sshpass -p bitcommit scp -o StrictHostKeyChecking=no -r lib sd0302@l040101-ws06.ua.pt:~
sshpass -p bitcommit scp -o StrictHostKeyChecking=no Repository.jar sd0302@l040101-ws07.ua.pt:~
sshpass -p bitcommit scp -o StrictHostKeyChecking=no -r lib sd0302@l040101-ws07.ua.pt:~
sshpass -p bitcommit scp -o StrictHostKeyChecking=no MyThreadBroker.jar sd0302@l040101-ws08.ua.pt:~
sshpass -p bitcommit scp -o StrictHostKeyChecking=no -r lib sd0302@l040101-ws08.ua.pt:~
sshpass -p bitcommit scp -o StrictHostKeyChecking=no MyThreadHorses.jar sd0302@l040101-ws09.ua.pt:~
sshpass -p bitcommit scp -o StrictHostKeyChecking=no -r lib sd0302@l040101-ws09.ua.pt:~
sshpass -p bitcommit scp -o StrictHostKeyChecking=no MyThreadSpec.jar sd0302@l040101-ws10.ua.pt:~
sshpass -p bitcommit scp -o StrictHostKeyChecking=no -r lib sd0302@l040101-ws10.ua.pt:~
cd ../../

#echo "  > Cleaning logs from the server where LogRepository is going to run"
#sshpass -p bitcommit ssh sd0302@l040101-ws.ua.pt -o StrictHostKeyChecking 'rm *.txt'

echo "  > Executing each jar file on the proper workstation"
echo "  > Executing LogRepository.jar file"
sshpass -p bitcommit ssh sd0302@l040101-ws01.ua.pt -o StrictHostKeyChecking=no 'java -jar LogRepository.jar' & PID_LogRepository=$!
echo "  > Executing Stable.jar file"
sshpass -p bitcommit ssh sd0302@l040101-ws02.ua.pt -o StrictHostKeyChecking=no 'java -jar Stable.jar' & PID_Stable=$!
echo "	> Executing Stand.jar file"
sshpass -p bitcommit ssh sd0302@l040101-ws03.ua.pt -o StrictHostKeyChecking=no 'java -jar Stand.jar' & PID_Stand=$!
echo "  > Executing Paddock.jar file"
sshpass -p bitcommit ssh sd0302@l040101-ws04.ua.pt -o StrictHostKeyChecking=no 'java -jar Paddock.jar' & PID_Paddock=$!
echo "	> Executing Betting_Center.jar file"
sshpass -p bitcommit ssh sd0302@l040101-ws05.ua.pt -o StrictHostKeyChecking=no 'java -jar Betting_Center.jar' & PID_Betting_Center=$!
echo "	> Executing RaceTrack.jar file"
sshpass -p bitcommit ssh sd0302@l040101-ws06.ua.pt -o StrictHostKeyChecking=no 'java -jar RaceTrack.jar' & PID_RaceTrack=$!
echo "	> Executing Repository.jar file" 
sshpass -p bitcommit ssh sd0302@l040101-ws07.ua.pt -o StrictHostKeyChecking=no 'java -jar Repository.jar' & PID_Repository=$!
echo "	> Executing MyThreadBroker.jar file"
sshpass -p bitcommit ssh sd0302@l040101-ws08.ua.pt -o StrictHostKeyChecking=no 'java -jar MyThreadBroker.jar' & PID_MyThreadBroker=$!
echo "	> Executing MyThreadHorses.jar file"
sshpass -p bitcommit ssh sd0302@l040101-ws09.ua.pt -o StrictHostKeyChecking=no 'java -jar MyThreadHorses.jar' & PID_MyThreadHorses=$!
echo "	> Executing MyThreadSpec.jar file"
sshpass -p bitcommit ssh sd0302@l040101-ws10.ua.pt -o StrictHostKeyChecking=no 'java -jar MyThreadSpec.jar' & PID_MyThreadSpec=$!

#echo "  > Waiting for simulation to end (generate a logging file).."
#wait $PID_Logging

echo "	> Waiting for broker and spectators to finish"
sshpass -p bitcommit ssh sd0302@l040101-ws08.ua.pt -o StrictHostKeyChecking=no wait $PID_MyThreadBroker
sshpass -p bitcommit ssh sd0302@l040101-ws10.ua.pt -o StrictHostKeyChecking=no wait $PID_MyThreadSpec

echo "	> Killing proccesses"
sshpass -p bitcommit ssh sd0302@l040101-ws09.ua.pt -o StrictHostKeyChecking=no kill $PID_MyThreadHorses	#kill horses threads that are in the stable.
sshpass -p bitcommit ssh sd0302@l040101-ws02.ua.pt -o StrictHostKeyChecking=no kill $PID_Stable		#kill stable monitor
sshpass -p bitcommit ssh sd0302@l040101-ws04.ua.pt -o StrictHostKeyChecking=no kill $PID_Paddock	#kill paddock monitor
sshpass -p bitcommit ssh sd0302@l040101-ws05.ua.pt -o StrictHostKeyChecking=no kill $PID_Betting_Center #kill betting center monitor
sshpass -p bitcommit ssh sd0302@l040101-ws06.ua.pt -o StrictHostKeyChecking=no kill $PID_RaceTrack	#kill racetrack monitor
sshpass -p bitcommit ssh sd0302@l040101-ws07.ua.pt -o StrictHostKeyChecking=no kill $PID_Repository	#kill repository monitor
sshpass -p bitcommit ssh sd0302@l040101-ws01.ua.pt -o StrictHostKeyChecking=no kill $PID_LogRepository  #kill logrepository monitor
sshpass -p bitcommit ssh sd0302@l040101-ws03.ua.pt -o StrictHostKeyChecking=no kill $PID_Stand		#kill stand monitor

echo "	> Done!"

~                                                                                                                                                                                                           
~                                                                                                                                                                                                           
~                                                                                                                                                                                                           
~                                                                                                                                                                                                           
~                             
