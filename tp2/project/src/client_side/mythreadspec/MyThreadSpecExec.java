/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client_side.mythreadspec;

import commonInfo.CommPorts;
import commonInfo.GlobalInfo;
import genclass.GenericIO;
import proxy_monitors.Proxy_Betting_Center;
import proxy_monitors.Proxy_Paddock;
import proxy_monitors.Proxy_Repository;
import proxy_monitors.Proxy_Stand;

/**
 *
 * @author pedro
 */
public class MyThreadSpecExec {
    /**
     * MyThreadSpectator executable main function.
     * @param args dispensable.
     */
    @SuppressWarnings("CallToPrintStackTrace")
    public static void main(String[] args){
        // TODO code application logic here
        //for(int i = 0; i < GlobalInfo.numRaces; i++){
            int i=0;
            //creste proxy monitors for spectator
            Proxy_Betting_Center proxy_betting_center = new Proxy_Betting_Center(CommPorts.specsServerName, CommPorts.specsServerPort);
            Proxy_Paddock proxy_paddock = new Proxy_Paddock(CommPorts.specsServerName, CommPorts.specsServerPort);
            Proxy_Repository proxy_repository = new Proxy_Repository(CommPorts.specsServerName, CommPorts.specsServerPort);
            Proxy_Stand proxy_stand = new Proxy_Stand(CommPorts.specsServerName, CommPorts.specsServerPort);
            
            //Horse threads initialization
            MyThreadSpec[] spec_threads = new MyThreadSpec[GlobalInfo.numSpec];
            for(int s = 0; s < GlobalInfo.numSpec; s++){
                spec_threads[s] = new MyThreadSpec(s, proxy_stand, proxy_paddock, proxy_repository, proxy_betting_center, s + 150);
                spec_threads[s].start();
            }

            //close horse threads
            try{
                for(MyThreadSpec spec : spec_threads){
                      spec.join();
                }
            }catch(InterruptedException e){
                e.printStackTrace();
            }
        //}
        GenericIO.writelnString("Client (Spectator) operations are done!");
    }
}