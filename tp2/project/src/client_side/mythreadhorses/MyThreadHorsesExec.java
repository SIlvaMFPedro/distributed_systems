/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client_side.mythreadhorses;

import commonInfo.CommPorts;
import commonInfo.GlobalInfo;
import genclass.GenericIO;
import java.util.ArrayList;
import proxy_monitors.Proxy_Paddock;
import proxy_monitors.Proxy_RaceTrack;
import proxy_monitors.Proxy_Stable;

/**
 *
 * @author pedro
 */
public class MyThreadHorsesExec {
    /**
     * MyThreadHorses executable main function.
     * @param args dispensable.
     */
    @SuppressWarnings("CallToPrintStackTrace")
    public static void main(String[] args){
        // TODO code application logic here
        
        //for(int i = 0; i < GlobalInfo.numRaces; i++){
            int i = 0;
            //create proxy monitors for horses
            Proxy_Paddock proxy_paddock = new Proxy_Paddock(CommPorts.horsesServerName, CommPorts.horsesServerPort);
            Proxy_RaceTrack proxy_racetrack = new Proxy_RaceTrack(CommPorts.horsesServerName, CommPorts.horsesServerPort);
            Proxy_Stable proxy_stable = new Proxy_Stable(CommPorts.horsesServerName, CommPorts.horsesServerPort);
            
            //Horse threads initialization
            MyThreadHorses[] horse_threads = new MyThreadHorses[GlobalInfo.numHorses];
            for(int h = 0; h < GlobalInfo.numHorses; h++){
                horse_threads[h] = new MyThreadHorses(h, proxy_stable, proxy_paddock, proxy_racetrack, h+50);
                horse_threads[h].start();
            }

            //close horse threads
            try{
                for(MyThreadHorses horse: horse_threads){
                    horse.join();
                }
            }catch(InterruptedException e){
                e.printStackTrace();
            }
        //}
        GenericIO.writelnString("Client (Horses) operations are done!");
    }
    
}