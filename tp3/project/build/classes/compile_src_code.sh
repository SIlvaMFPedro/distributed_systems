#!/usr/bin/env bash

echo "compiling source code..."

cd ..
cd build/classes
javac -cp \* commonInfo/*.java interfaces/*.java interfaces/broker/*.java interfaces/horses/*.java interfaces/spectator/*.java registry/*.java server_side/logrepository/*.java server_side/betting_center/*.java server_side/paddock/*.java server_side/racetrack/*.java server_side/repository/*.java  server_side/stable/* server_side/stand/* client_side/mythreadbroker/*.java client_side/mythreadhorses/*.java client_side/mythreadspec/*.java
echo "Copying Interfaces .class files ... "
cp interfaces/Register.class ../../src/deploy/dir_Registry/interfaces/
cp interfaces/*.class ../../src/deploy/dir_LogRepository/interfaces/
cp interfaces/*.class ../../src/deploy/dir_BettingCenter/interfaces/
cp interfaces/*.class ../../src/deploy/dir_Paddock/interfaces/
cp interfaces/*.class ../../src/deploy/dir_RaceTrack/interfaces/
cp interfaces/*.class ../../src/deploy/dir_Repository/interfaces/
cp interfaces/*.class ../../src/deploy/dir_Stable/interfaces/
cp interfaces/*.class ../../src/deploy/dir_Stand/interfaces/
cp interfaces/*.class ../../src/deploy/dir_MyThreadBroker/interfaces/
cp interfaces/*.class ../../src/deploy/dir_MyThreadHorses/interfaces/
cp interfaces/*.class ../../src/deploy/dir_MyThreadSpec/interfaces/
cp interfaces/*.class ../../src/deploy/interfaces/


echo "Copying registry .class files ... "
cp registry/*.class deploy/dir_Registry/registry/

echo "Copying Server Side .class files ... "
cp server_side/logrepository/*.class deploy/dir_LogRepository/server_side/logrepository/
cp server_side/betting_center/*.class deploy/dir_BettingCenter/server_side/betting_center/
cp server_side/paddock/*.class deploy/dir_Paddock/server_side/paddock/
cp server_side/racetrack/*.class deploy/dir_RaceTrack/server_side/racetrack/
cp server_side/repository/*.class deploy/dir_Repository/server_side/repository/
cp server_side/stable/*.class deploy/dir_Stable/server_side/stable/
cp server_side/stand/*.class deploy/dir_Stand/server_side/stand/

echo "Copying Client Side .class files ... "
cp client_side/mythreadbroker/*.class deploy/dir_MyThreadBroker/client_side/mythreadbroker/
cp client_side/mythreadhorses/*.class deploy/dir_MyThreadHorses/client_side/mythreadhorses/
cp client_side/mythreadspec/*.class deploy/dir_MyThreadSpec/client_side/mythreadspec/

echo "Copying commonInfo .class files ... "
cp commonInfo/*.class deploy/dir_LogRepository/support/
cp commonInfo/*.class deploy/dir_BettingCenter/support/
cp commonInfo/*.class deploy/dir_Paddock/support/
cp commonInfo/*.class deploy/dir_RaceTrack/support/
cp commonInfo/*.class deploy/dir_Repository/support/
cp commonInfo/*.class deploy/dir_Stable/support/
cp commonInfo/*.class deploy/dir_Stand/support/
cp commonInfo/*.class deploy/dir_MyThreadBroker/support/
cp commonInfo/*.class deploy/dir_MyThreadHorses/support/
cp commonInfo/*.class deploy/dir_MyThreadSpec/support/
cp commonInfo/*.class deploy/support/
echo "done..."
