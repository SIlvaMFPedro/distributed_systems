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
import commonInfo.RegistryConfig;
import genclass.GenericIO;
import interfaces.Stand_Itf;
import interfaces.registry.Register;
import static java.lang.Thread.sleep;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Scanner;
import java.util.concurrent.locks.ReentrantLock;

import server_side.Log_Interface;

/**
 *
 * @author pedro
 * @author franciscoteixeira
 */
public class Stand_Exec {
     private static boolean end = false;
     private static ReentrantLock rl;

     /**
     * Stand Monitor executable main function
     * @param args dispensable
     * @throws Exception exception
     */
     public static void main(String[] args) throws Exception{
        rl = new ReentrantLock(true);
         /* get location of the generic registry service */
        String rmiRegHostName;
        int rmiRegPortNumber;

        Scanner sc = new Scanner(System.in);
        int listeningPort = 22322;
        String nameEntry = RegistryConfig.standNameEntry;

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

        /* Create Stand Stub */
        Stand stand = new Stand(log_itf, GlobalInfo.numSpec, rmiRegHostName, rmiRegPortNumber);
        Stand_Itf stand_itf = null;
        
        try{
            stand_itf = (Stand_Itf) UnicastRemoteObject.exportObject(stand, listeningPort);
        } catch (RemoteException e) {
            System.out.println("Stand stub create exception: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
        System.out.print("Stand Stub created!\n");

        /* Register RMI register */
        Register register  = null;
        try {
            register = (Register) registry.lookup("RegisterHandler");
        } catch (RemoteException | NotBoundException ex) {
            System.out.println("Wrong register location!");
            System.exit (1);
        }

        try {
            register.bind(nameEntry, stand_itf);
        } catch (RemoteException e) {
            System.out.println("Stand register exception " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        } catch (Exception e) {
            System.out.println("Stand is already registered: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
        System.out.println("Stand module has been registered!");
    }
}
