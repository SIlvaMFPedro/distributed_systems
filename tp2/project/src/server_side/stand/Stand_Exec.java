/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server_side.stand;

import client_side.ClientCom;
import commonInfo.GlobalInfo;
import commonInfo.CommPorts;
import commonInfo.Message;
import genclass.GenericIO;
import static java.lang.Thread.sleep;

import server_side.ClientProxy;
import server_side.Log_Proxy;
import server_side.Server_Channel;
import server_side.logrepository.LogRepository;

/**
 *
 * @author pedro
 * @author franciscocmlt
 */
public class Stand_Exec {
     /**
     * Stand Monitor executable main function
     * @param args dispensable
     * @throws Exception exception
     */
    public static void main(String[] args) throws Exception{
        Server_Channel server_ch, server_com;
        ClientProxy cl_proxy;
        
        server_ch = new Server_Channel(CommPorts.standServerPort);
        server_ch.start();
        
        Log_Proxy log = new Log_Proxy();
        Stand stand = new Stand(log, GlobalInfo.numSpec);
        Stand_Itf stand_itf = new Stand_Itf(stand);
        GenericIO.writelnString("Stand service has started!");
        GenericIO.writelnString("Server is listening...");
        
        //run
        boolean go = true;
        while(go){
            if(stand_itf.endOfMonitors()){
                GenericIO.writelnString("Ending monitor...");
                go = false;
            }
            else{
                GenericIO.writelnString("Creating com channel");
                //System.out.println("Stall 1");
                server_com = server_ch.commChannel();
                //System.out.println("Stall 2");
                cl_proxy = new ClientProxy(server_ch, server_com, stand_itf);
                //System.out.println("Stall 3");
                cl_proxy.start();
                //System.out.println("Stall 4");
            }
            GenericIO.writelnString("Go: " + go);
        }
        
        GenericIO.writelnString("Stand service has ended!!!");
        
    }
}
