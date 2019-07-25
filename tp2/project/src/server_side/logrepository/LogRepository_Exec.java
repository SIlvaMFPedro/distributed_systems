/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server_side.logrepository;

import commonInfo.GlobalInfo;
import commonInfo.CommPorts;
import genclass.GenericIO;

import server_side.ClientProxy;
import server_side.Server_Channel;



/**
 *
 * @author pedro
 * @author franciscoteixeira
 */
public class LogRepository_Exec {
    /**
     * LogRepository monitor executable main function.
     * @param args dispensable.
     * @throws Exception exception
     */
    public static void main(String[] args) throws Exception{
        Server_Channel server_ch, server_com;
        ClientProxy cl_proxy;
        
        server_ch = new Server_Channel(CommPorts.logRepServerPort);
        server_ch.start();
        
        LogRepository log = new LogRepository(GlobalInfo.track_length);
        LogRepository_Itf log_itf = new LogRepository_Itf(log);
        GenericIO.writelnString("LogRepository service has started!");
        GenericIO.writelnString("Service is listening...");
        
        //run
        boolean go = true;
        while(go){
            if(log_itf.endOfMonitors()){
                GenericIO.writelnString("Ending monitor...");
                go = false;
            }
            else{
                GenericIO.writelnString("Creating com channel");
                server_com = server_ch.commChannel();
                cl_proxy = new ClientProxy(server_ch, server_com, log_itf);
                cl_proxy.start();
            }
            GenericIO.writelnString("Go: " + go);
        }
        
        GenericIO.writelnString("LogRepository service has ended");
    }
}
