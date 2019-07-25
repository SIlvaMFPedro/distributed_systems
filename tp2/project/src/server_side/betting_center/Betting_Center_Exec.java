/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server_side.betting_center;

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
public class Betting_Center_Exec {
    /**
     * Betting_Center executable main function.
     * @param args dispensable.
     * @throws Exception exception
     */
    public static void main(String[] args) throws Exception{
        Server_Channel server_ch, server_com;
        ClientProxy cl_proxy;
        
        server_ch = new Server_Channel(CommPorts.bettingCenterServerPort);
        server_ch.start();
        
        Log_Proxy log = new Log_Proxy();
        Betting_Center betting_center = new Betting_Center(log, GlobalInfo.numSpec);
        Betting_Center_Itf bet_center_itf = new Betting_Center_Itf(betting_center);
        GenericIO.writelnString("Betting center service has started!");
        GenericIO.writelnString("Service is listening...");
        
        //run
        boolean go = true;
        while(go){
            if(bet_center_itf.endOfMonitors()){
                GenericIO.writelnString("Ending monitor...\n");
                go = false;
                //create_channel = false;
            }
            else{
                GenericIO.writelnString("Creating com channel...");
                server_com = server_ch.commChannel();
                cl_proxy = new ClientProxy(server_ch, server_com, bet_center_itf);
                cl_proxy.start();
            }
            GenericIO.writelnString("Go: " + go);
        }
        
        GenericIO.writelnString("Betting center service has ended");
    }
    
}
