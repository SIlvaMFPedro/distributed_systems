/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server_side.stable;

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
public class Stable_Exec {
    /**
     * Stable Monitor executable main function
     * @param args dispensable
     * @throws Exception exception
     */
    public static void main(String[] args) throws Exception{
        Server_Channel server_ch, server_com;
        ClientProxy cl_proxy;
        
        server_ch = new Server_Channel(CommPorts.stableServerPort);
        server_ch.start();
        
        Log_Proxy log = new Log_Proxy();
        Stable stable = new Stable(log, GlobalInfo.numHorses);
        Stable_Itf stable_itf = new Stable_Itf(stable);
        GenericIO.writelnString("Stable service has started!");
        GenericIO.writelnString("Server is listening...");
        
        //run
        boolean go = true;
        while(go){
            if(stable_itf.endOfMonitors()){
                GenericIO.writelnString("Ending monitor...");
                go = false;
            }
            else{
                 GenericIO.writelnString("Creating com channel");
                 server_com = server_ch.commChannel();
                 cl_proxy = new ClientProxy(server_ch, server_com, stable_itf);
                 cl_proxy.start();
            }
            GenericIO.writelnString("Go: " + go);
        }
        
        GenericIO.writelnString("Stable service has ended");
    }
    
}
