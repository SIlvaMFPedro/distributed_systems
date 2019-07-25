cd ..
echo "	> Sending files to workstations"
sshpass -p sd_sockets scp -o StrictHostKeyChecking=no -r heist_RMI sd0304@l040101-ws01.ua.pt:~
sshpass -p sd_sockets scp -o StrictHostKeyChecking=no -r heist_RMI sd0304@l040101-ws03.ua.pt:~
sshpass -p sd_sockets scp -o StrictHostKeyChecking=no -r heist_RMI sd0304@l040101-ws04.ua.pt:~
sshpass -p sd_sockets scp -o StrictHostKeyChecking=no -r heist_RMI sd0304@l040101-ws05.ua.pt:~
sshpass -p sd_sockets scp -o StrictHostKeyChecking=no -r heist_RMI sd0304@l040101-ws07.ua.pt:~

wait $PID_Logging
