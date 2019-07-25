#!/usr/bin/env bash
registryPortNum=1099
localhost=localhost


bash compile_source_code.sh

echo "running... "
bash set_rmiregistry_alt.sh 1099 &
sleep 2
bash scripts/runRegistry.sh &
sleep 2
bash scripts/runLogRepository.sh & PID_Logging=$!
sleep 2
bash scripts/runBettingCenter.sh  &
sleep 2
bash scripts/runPaddock.sh  &
sleep 2
bash scripts/runRaceTrack.sh  &
sleep 2
bash scripts/runRepository.sh  &
sleep 2
bash scripts/runStable.sh  &
sleep 2
bash scripts/runStand.sh &
sleep 2
bash scripts/runMyThreadBroker &
sleep 2
bash scripts/runMyThreadHorses &
sleep 2
bash scripts/runMyThreadSpec.sh  &

wait $PID_Logging

find . -maxdepth 11 -type f -name "*.class" -delete

rm -rf *.zip

kill `lsof -t -i:1099`
kill `lsof -t -i:1100`