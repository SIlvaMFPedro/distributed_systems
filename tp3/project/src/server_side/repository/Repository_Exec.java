/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server_side.repository;

import commonInfo.GlobalInfo;
import commonInfo.CommPorts;
import commonInfo.RegistryConfig;
import genclass.GenericIO;
import interfaces.Repository_Itf;
import interfaces.registry.Register;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Scanner;

import server_side.Log_Interface;



/**
 *
 * @author pedro
 * @author franciscoteixeira
 */
public class Repository_Exec {
    private static boolean end = false;
     /**
     * Repository monitor executable main function
     * @param args dispensable
     * @throws Exception exception
     */
   
    public static void main(String[] args) throws Exception{
        /* get location of the generic registry service */
        String rmiRegHostName;
        int rmiRegPortNumber;

        Scanner sc = new Scanner(System.in);
        int listeningPort = 22326;
        String nameEntry = RegistryConfig.repositoryNameEntry;

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

        /* Create Repository Stub */
        Repository repository = new Repository(log_itf, GlobalInfo.numRaces, rmiRegHostName, rmiRegPortNumber);
        Repository_Itf repository_itf = null;
        
        try{
            repository_itf = (Repository_Itf) UnicastRemoteObject.exportObject(repository, listeningPort);
        } catch (RemoteException e) {
            System.out.println("Repository stub create exception: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
        System.out.print("Repository Stub created!\n");

        /* Register RMI register */
        Register register  = null;
        try {
            register = (Register) registry.lookup("RegisterHandler");
        } catch (RemoteException | NotBoundException ex) {
            System.out.println("Wrong register location!");
            System.exit (1);
        }

        try {
            register.bind(nameEntry, repository_itf);
        } catch (RemoteException e) {
            System.out.println("Repository register exception " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        } catch (Exception e) {
            System.out.println("Repository is already registered: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
        System.out.println("Repository module has been registered!");
       
    }
}
