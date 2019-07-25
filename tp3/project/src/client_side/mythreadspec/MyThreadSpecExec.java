/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client_side.mythreadspec;

import commonInfo.CommPorts;
import commonInfo.GlobalInfo;
import commonInfo.RegistryConfig;
import genclass.GenericIO;
import interfaces.spectator.BettingCenter_Spec;
import interfaces.spectator.Paddock_Spec;
import interfaces.spectator.Repository_Spec;
import interfaces.spectator.Stand_Spec;
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
public class MyThreadSpecExec {
    /**
     * MyThreadSpectator executable main function.
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
        BettingCenter_Spec spec_bet_center_itf = null;
        Paddock_Spec spec_paddock_itf = null;
        Repository_Spec spec_repository_itf = null;
        Stand_Spec spec_stand_itf = null;
        
        
        /* Localize remote object name on the RMI records */
        try{
            Registry registry = LocateRegistry.getRegistry(rmiRegHostName, rmiRegPortNumber);
            spec_bet_center_itf = (BettingCenter_Spec) registry.lookup(RegistryConfig.bettingCenterNameEntry);
            spec_paddock_itf = (Paddock_Spec) registry.lookup(RegistryConfig.paddockNameEntry);
            spec_repository_itf = (Repository_Spec) registry.lookup(RegistryConfig.repositoryNameEntry);
            spec_stand_itf = (Stand_Spec) registry.lookup(RegistryConfig.standNameEntry);
            
        }catch(RemoteException | NotBoundException e){
            System.out.print("Server not localized exception: " + e.getMessage() + "!");
            e.printStackTrace();
            System.exit(1);
        }
        
        /* create MyThreadSpec(client) threads */
        MyThreadSpec[] spec_threads = new MyThreadSpec[GlobalInfo.numSpec];
        for(int s = 0; s < GlobalInfo.numSpec; s++){
            spec_threads[s] = new MyThreadSpec(s, spec_stand_itf, spec_paddock_itf, spec_repository_itf, spec_bet_center_itf, s + 150);
        }
        /* start MyThreadSpec(client) threads */
        for(MyThreadSpec spec : spec_threads){
            spec.start();
        }
        
        /* close MyThreadSpec(client) threads */
        System.out.println("Client (MyThreadSpec) operations have started!");
        for(MyThreadSpec spec : spec_threads){
            try{
                spec.join();
            }catch(InterruptedException e){}
        }
        System.out.println("Client (MyThreadSpec) operations have finished!");
        
    }
}