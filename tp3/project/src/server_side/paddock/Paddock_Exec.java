/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server_side.paddock;

import commonInfo.GlobalInfo;
import commonInfo.CommPorts;
import commonInfo.RegistryConfig;
import genclass.GenericIO;
import interfaces.Paddock_Itf;
import interfaces.registry.Register;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Scanner;

import server_side.ClientProxy;
import server_side.Log_Interface;
import server_side.Server_Channel;
import server_side.logrepository.LogRepository;

/**
 *
 * @author pedro
 * @author franciscoteixeira
 */
public class Paddock_Exec {
     private static boolean end = false;
     /**
     * Paddock monitor executable main function.
     * @param args dispensable
     * @throws Exception exception
     */
 
    public static void main(String[] args) throws Exception{
        /* get location of the generic registry service */
        String rmiRegHostName;
        int rmiRegPortNumber;
        
        Scanner sc = new Scanner(System.in);
        int listeningPort = 22323;
        String nameEntry = RegistryConfig.paddockNameEntry;
        
        System.out.println("Node process host name(RMI Service Host Name): ");
        rmiRegHostName = sc.nextLine();
        System.out.println("Node process port number(RMI Service Port Number): ");
        rmiRegPortNumber = sc.nextInt();
        
        /* Instanciate and install security manager */
        if(System.getSecurityManager() == null){
            System.setSecurityManager(new SecurityManager());
        }
        
        /* Get Central Registry */
        Registry registry = null;
        try{
            registry = LocateRegistry.getRegistry(rmiRegHostName, rmiRegPortNumber);
        }catch(RemoteException e){
            System.err.println("Wrong registry location!!!");
            System.exit(1);
        }
        System.out.println("RMI Registry created!");
        
        /* Get LogRepository register */
        Log_Interface log_itf = null;
        try{
            log_itf = (Log_Interface) registry.lookup("logRepository");
        } catch (Exception e){
            System.out.println("LogRepository lookup exception: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
        
        /* Create Paddock Stub */
        Paddock paddock = new Paddock(log_itf, GlobalInfo.numHorses, GlobalInfo.numSpec, rmiRegHostName, rmiRegPortNumber);
        Paddock_Itf paddock_itf = null;
        
        try{
            paddock_itf = (Paddock_Itf) UnicastRemoteObject.exportObject(paddock, listeningPort);
        } catch (RemoteException e) {
            System.out.println("Paddock stub create exception: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
        System.out.print("Paddock Stub created!\n");
        
        /* Register RMI register */
        Register register  = null;
        try {
            register = (Register) registry.lookup("RegisterHandler");
        } catch (RemoteException | NotBoundException ex) {
            System.out.println("Wrong register location!");
            System.exit (1);
        }
      
      try {
            register.bind(nameEntry, paddock_itf);
        } catch (RemoteException e) {
            System.out.println("Paddock register exception " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        } catch (Exception e) {
            System.out.println("Paddock is already registered: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
        System.out.println("Paddock module has been registered!");
    }
}