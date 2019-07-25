/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client_side.mythreadbroker;

import commonInfo.CommPorts;
import commonInfo.GlobalInfo;
import genclass.GenericIO;
import interfaces.broker.BettingCenter_Broker;
import interfaces.broker.Paddock_Broker;
import interfaces.broker.RaceTrack_Broker;
import interfaces.broker.Repository_Broker;
import interfaces.broker.Stable_Broker;
import proxy_monitors.Proxy_Betting_Center;
import proxy_monitors.Proxy_Paddock;
import proxy_monitors.Proxy_RaceTrack;
import proxy_monitors.Proxy_Repository;
import proxy_monitors.Proxy_Stable;
import proxy_monitors.Proxy_Stand;



/**
 *
 * @author pedro
 */
public class MyThreadBrokerExec {
    /**
     * MyThreadBroker executable main function.
     * @param args dispensable.
     */
    @SuppressWarnings("CallToPrintStackTrace")
    public static void main(String[] args){
        // TODO code application logic here
        //for(int i = 0; i < GlobalInfo.numRaces; i++){
            //create proxy monitors for broker
            int i=0;
            Proxy_Betting_Center proxy_betting_center = new Proxy_Betting_Center(CommPorts.brokerServerName, CommPorts.brokerServerPort);
            Proxy_Paddock proxy_paddock = new Proxy_Paddock(CommPorts.brokerServerName, CommPorts.brokerServerPort);
            Proxy_RaceTrack proxy_race_track = new Proxy_RaceTrack(CommPorts.brokerServerName, CommPorts.brokerServerPort);
            Proxy_Repository proxy_repository = new Proxy_Repository(CommPorts.brokerServerName, CommPorts.brokerServerPort);
            Proxy_Stable proxy_stable = new Proxy_Stable(CommPorts.brokerServerName, CommPorts.brokerServerPort);
            Proxy_Stand proxy_stand = new Proxy_Stand(CommPorts.brokerServerName, CommPorts.brokerServerPort);
            
            //Broker Thread Initialization;
            MyThreadBroker broker;
            
            broker = new MyThreadBroker(i, proxy_stable, proxy_paddock, proxy_betting_center, proxy_race_track, proxy_repository, proxy_stand, GlobalInfo.numSpec);
            broker.start();
            
            //Broker Thread close;
            try{
                broker.join();                
            }catch(InterruptedException e){
                e.printStackTrace();
            }
        //}
        GenericIO.writelnString("Client (Broker) operations are done!");
    }
}