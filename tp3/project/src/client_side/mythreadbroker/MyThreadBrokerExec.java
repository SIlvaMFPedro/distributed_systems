/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client_side.mythreadbroker;

import commonInfo.CommPorts;
import commonInfo.GlobalInfo;
import commonInfo.RegistryConfig;
import genclass.GenericIO;
import interfaces.broker.BettingCenter_Broker;
import interfaces.broker.Paddock_Broker;
import interfaces.broker.RaceTrack_Broker;
import interfaces.broker.Repository_Broker;
import interfaces.broker.Stable_Broker;
import interfaces.broker.Stand_Broker;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.Scanner;



/**
 *
 * @author pedro
 * @author franciscoteixeira
 */
public class MyThreadBrokerExec {
    /**
     * MyThreadBroker executable main function.
     * @param args dispensable.
     */
    @SuppressWarnings("CallToPrintStackTrace")
    public static void main(String[] args){
        // TODO code application logic here
        String rmiRegHostName; //rmi register service system name;
        int rmiRegPortNumber;  //rmi register service port number;
        Scanner sc = new Scanner(System.in);
        
        System.out.println("Node process register service name: ");
        rmiRegHostName = sc.nextLine();
        System.out.println("Listening server register port number: ");
        rmiRegPortNumber = sc.nextInt();
        
        /* Initialize interfaces */
        BettingCenter_Broker broker_bet_center_itf = null;
        Paddock_Broker broker_paddock_itf = null;
        RaceTrack_Broker broker_track_itf = null;
        Repository_Broker broker_repository_itf = null;
        Stable_Broker broker_stable_itf = null;
        Stand_Broker broker_stand_itf = null;
        
        /* Localize remote object name on the RMI records */
        try{
            Registry registry = LocateRegistry.getRegistry(rmiRegHostName, rmiRegPortNumber);
            broker_bet_center_itf = (BettingCenter_Broker) registry.lookup(RegistryConfig.bettingCenterNameEntry);
            broker_paddock_itf = (Paddock_Broker) registry.lookup(RegistryConfig.paddockNameEntry);
            broker_track_itf = (RaceTrack_Broker) registry.lookup(RegistryConfig.racetrackNameEntry);
            broker_repository_itf = (Repository_Broker) registry.lookup(RegistryConfig.repositoryNameEntry);
            broker_stable_itf = (Stable_Broker) registry.lookup(RegistryConfig.stableNameEntry);
            broker_stand_itf = (Stand_Broker) registry.lookup(RegistryConfig.standNameEntry);
        }catch(RemoteException | NotBoundException e){
            System.out.print("Server not localized exception: " + e.getMessage() + "!");
            e.printStackTrace();
            System.exit(1);
        }
        
        /* create MyThreadBroker(client) thread */
        MyThreadBroker broker = new MyThreadBroker(1, broker_stable_itf, broker_paddock_itf, broker_bet_center_itf, broker_track_itf, broker_repository_itf, broker_stand_itf, GlobalInfo.numSpec);
        
        /* start MyThreadBroker(client) thread */
        broker.start();
        System.out.println("Client (MyThreadBroker) operations have started!");
        
        /* wait for MyThreadBroker(client) thread to finish */
        System.out.println();
        try{
            broker.join();
        }catch(InterruptedException e){
            e.printStackTrace();
        }
        System.out.println("Client (MyThreadBroker) operations have finished!");
        System.out.println();
        
        /* close monitors */
        try{
            broker_stable_itf.terminate();
            broker_paddock_itf.terminate();
            broker_bet_center_itf.terminate();
            broker_track_itf.terminate();
            broker_repository_itf.terminate();
            broker_stand_itf.terminate();
        }catch(RemoteException e){
            System.out.print("Remote Method Terminate Exception: " + e.getMessage() + "!\n");
            e.printStackTrace();
            System.exit(1);
        }
        System.out.println("Server shutting down...");
        System.out.println();
    }
}