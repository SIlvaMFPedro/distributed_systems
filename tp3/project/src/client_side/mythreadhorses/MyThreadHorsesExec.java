/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client_side.mythreadhorses;

import commonInfo.GlobalInfo;
import commonInfo.RegistryConfig;
import interfaces.horses.Paddock_Horses;
import interfaces.horses.RaceTrack_Horses;
import interfaces.horses.Stable_Horses;
import interfaces.horses.Repository_Horses;
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
public class MyThreadHorsesExec {
    /**
     * MyThreadHorses executable main function.
     * @param args dispensable.
     */
    @SuppressWarnings("CallToPrintStackTrace")
    public static void main(String[] args){
        // TODO code application logic here
        
        // TODO code application logic here
        String rmiRegHostName; //rmi register service system name;
        int rmiRegPortNumber;  //rmi register service port number;
        Scanner sc = new Scanner(System.in);
        
        System.out.println("Node process register service name: ");
        rmiRegHostName = sc.nextLine();
        System.out.println("Listening server register port number: ");
        rmiRegPortNumber = sc.nextInt();
        
        /* Initialize interfaces */
        Paddock_Horses horses_paddock_itf = null;
        RaceTrack_Horses horses_track_itf = null;
        Stable_Horses horses_stable_itf = null;
        Repository_Horses horses_repository_itf = null;
        
        /* Localize remote object name on the RMI records */
        try{
            Registry registry = LocateRegistry.getRegistry(rmiRegHostName, rmiRegPortNumber);
            horses_paddock_itf = (Paddock_Horses) registry.lookup(RegistryConfig.paddockNameEntry);
            horses_track_itf = (RaceTrack_Horses) registry.lookup(RegistryConfig.racetrackNameEntry);
            horses_stable_itf = (Stable_Horses) registry.lookup(RegistryConfig.stableNameEntry);
            horses_repository_itf = (Repository_Horses) registry.lookup(RegistryConfig.repositoryNameEntry);
        }catch(RemoteException | NotBoundException e){
            System.out.print("Server not localized exception: " + e.getMessage() + "!");
            e.printStackTrace();
            System.exit(1);
        }
        
        /* create MyThreadHorses(client) threads */
        MyThreadHorses[] horse_threads = new MyThreadHorses[GlobalInfo.numHorses];
        for(int h = 0; h < GlobalInfo.numHorses; h++){
            horse_threads[h] = new MyThreadHorses(h, horses_stable_itf, horses_paddock_itf, horses_track_itf, horses_repository_itf,h + 50);
        }
        /* start MyThreadHorses(client) threads */
        for(MyThreadHorses horse : horse_threads){
            horse.start();
        }
        
        /* close MyThreadHorses(client) threads */
        System.out.println("Client (MyThreadHorses) operations have started!");
        for(MyThreadHorses horse : horse_threads){
            try{
                horse.join();
            }catch(InterruptedException e){}
        }
        System.out.println("Client (MyThreadHorses) operations have finished!");
        
    }
    
}