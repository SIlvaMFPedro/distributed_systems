/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server_side.racetrack;

import commonInfo.GlobalInfo;
import commonInfo.CommPorts;
import genclass.GenericIO;

import server_side.ClientProxy;
import server_side.Log_Proxy;
import server_side.Server_Channel;
import server_side.logrepository.LogRepository;

/**
 *
 * @author pedro
 * @author franciscocmlt
 */
public class RaceTrack_Exec {
     /**
     * RaceTrack Monitor executable main function.
     * @param args dispensable
     * @throws Exception exception
     */
    public static void main(String[] args) throws Exception{
        Server_Channel server_ch, server_com;
        ClientProxy cl_proxy;
        
        server_ch = new Server_Channel(CommPorts.racetrackServerPort);
        server_ch.start();
        
        Log_Proxy log = new Log_Proxy();
        RaceTrack racetrack = new RaceTrack(log, GlobalInfo.track_length, GlobalInfo.numHorses, GlobalInfo.numRaces);
        RaceTrack_Itf racetrack_itf = new RaceTrack_Itf(racetrack);
        GenericIO.writelnString("RaceTrack service has started!");
        GenericIO.writelnString("Server is listening...");
        
        //run
        boolean go = true;
        while(go){
            if(racetrack_itf.endOfMonitors()){
                GenericIO.writelnString("Ending monitor...");
                go = false;
            }
            else{
                GenericIO.writelnString("Creating com channel");
                server_com = server_ch.commChannel();
                cl_proxy = new ClientProxy(server_ch, server_com, racetrack_itf);
                cl_proxy.start();
            }
            GenericIO.writelnString("Go: " + go);
        }
        
        GenericIO.writelnString("RaceTrack service has ended");
    }
}
