/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server_side.paddock;

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
 * @author franciscoteixeira
 */
public class Paddock_Exec {
     /**
     * Paddock monitor executable main function.
     * @param args dispensable
     * @throws Exception exception
     */
    public static void main(String[] args) throws Exception{
        Server_Channel server_ch, server_com;
        ClientProxy cl_proxy;
        
        server_ch = new Server_Channel(CommPorts.paddockServerPort);
        server_ch.start();
        
        Log_Proxy log = new Log_Proxy();
        Paddock paddock = new Paddock(log, GlobalInfo.numHorses, GlobalInfo.numSpec);
        Paddock_Itf paddock_itf = new Paddock_Itf(paddock);
        GenericIO.writelnString("Paddock service has started!");
        GenericIO.writelnString("Server is listening...");
        
        //run
        boolean go = true;
        while(go){
            if(paddock_itf.endOfMonitors()){
                GenericIO.writelnString("Ending monitor...");
                go = false;
            }
            else{
                GenericIO.writelnString("Creating com channel");
                server_com = server_ch.commChannel();
                cl_proxy = new ClientProxy(server_ch, server_com, paddock_itf);
                cl_proxy.start();
            }
            GenericIO.writelnString("Go: " + go);
        }
        
        GenericIO.writelnString("Paddock service has ended");
    }
}
