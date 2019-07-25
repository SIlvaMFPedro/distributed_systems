/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server_side.repository;

import client_side.mythreadspec.MyThreadSpec;
import commonInfo.GlobalInfo;
import commonInfo.RegistryConfig;
import commonInfo.VectorClock;
import interfaces.Repository_Itf;
import interfaces.broker.Repository_Broker;
import interfaces.registry.Register;
import interfaces.spectator.Repository_Spec;
import java.rmi.NoSuchObjectException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import java.util.logging.Logger;
import server_side.Log_Interface;
import server_side.betting_center.Betting_Center;
import server_side.logrepository.LogRepository;

/**
 *
 * @author pedro
 * @author franciscoteixeira
 */
public class Repository implements Repository_Itf{
    private final ReentrantLock rl;
    private final Condition control_center_spec;
    private final int maxRaces;
    private int currentRace;
    
    private final HashMap<Integer, LinkedList> results = new HashMap<Integer, LinkedList>();
    
    private String rmiRegHostName;
    private int rmiRegPortNumber;
    
    public Log_Interface log;
    public VectorClock vector_clk;
    
    
    /**
     * Repository constructor;
     * @param log  log interface;
     * @param maxRaces maximum number of races;
     */
    public Repository(Log_Interface log, int maxRaces, String rmiRegHostName, int rmiRegPortNumber){
        rl = new ReentrantLock(true);
        control_center_spec = rl.newCondition();
        this.maxRaces = maxRaces;
        currentRace = 0;
        this.log = log;
        this.rmiRegHostName = rmiRegHostName;
        this.rmiRegPortNumber = rmiRegPortNumber;
        this.vector_clk = new VectorClock(GlobalInfo.numSpec+1);
    }
    
    //BROKER;
    /**
     * Report race results into repository.
     * @param race_id race number.
     * @param winner_id array with the id's of the winner horses.
     */
    @Override
    public void reportResults(int race_id, int[] winner_id, VectorClock vc){
        LinkedList w;
        rl.lock();
        //control_center_broker.signal();
        try{
            vector_clk.update(vc);
            //currentRace = race_id;
            w = new LinkedList<Integer>();
            for(int i = 0; i < winner_id.length; i++){
                //System.out.print("B : Horse " + winner_id[i] + " won race " + race_id + "\n");
                System.out.print("B : Horse " + winner_id[i] + " won race " + currentRace + "\n");
                w.add(winner_id[i]);
            }
            //results.put(race_id, w);      
            results.put(currentRace, w); 
            currentRace++;
        }finally{
            rl.unlock();
        }
    }
    /**
    * Alert winners to go check race results.
    */
    @Override
    public void alertWinners(VectorClock vc){
        rl.lock();
        try{
            vector_clk.update(vc);
            control_center_spec.signalAll();
        }finally{
            rl.unlock();
        }
    }
    /**
     * Spectator checks if there are any more races.
     * @return true if there are more races.
     */
    @Override
    public boolean checkFinalRace(VectorClock vc) {
        rl.lock();
        boolean b=false;
        try{
            vector_clk.update(vc);
            //currentRace++;
            System.out.println("currentRace: "+currentRace +"      maxRaces="+maxRaces);
            if(currentRace == maxRaces)
                b = true; //temporary return;
            
        }finally{
            rl.unlock();
        }
        return b;
    }
    
    /**
     * Broker shutdown Repository monitor
     */
    @Override
    @SuppressWarnings("CallToPrintStackTrace")
    public void terminate() throws RemoteException{
        rl.lock();
        try{
            //System.out.println("Shutting Down Repository...");
            //Repository_Exec.shutdown();
            Register reg  = null;
            Registry registry = null;
            
            String rmiRegHostName = new String();
            int rmiRegPortNumber;
            
            rmiRegHostName = this.rmiRegHostName;
            rmiRegPortNumber = this.rmiRegPortNumber;
            
            try{
                registry = LocateRegistry.getRegistry(rmiRegHostName, rmiRegPortNumber);
            }catch(RemoteException e){
                System.out.println("Cannot find registry");
                e.printStackTrace();
                System.exit(1);
            }
            
           
            String nameEntryBase = RegistryConfig.rmiRegisterName;
            String nameEntryObj = RegistryConfig.repositoryNameEntry;
            
            try{
                reg = (Register) registry.lookup(nameEntryBase);
            }catch(RemoteException e){
                System.out.println("RegisterRemoteObject lookup exception: " + e.getMessage());
                e.printStackTrace();
                System.exit(1);
            } catch (NotBoundException e) {
                Logger.getLogger(Betting_Center.class.getName()).log(Level.SEVERE, null, e);
                System.out.println("RegisterRemoteObject lookup exception: " + e.getMessage());
                e.printStackTrace();
                System.exit(1);
            }
            
            try{
                //unregister monitor
                reg.unbind(nameEntryObj);
            }catch(RemoteException e){
                System.out.println("Repository registration exception: " + e.getMessage());
                e.printStackTrace();
                System.exit(1);
            }catch(NotBoundException e){
                System.out.println("Repository not bound exception: " + e.getMessage());
                e.printStackTrace();
                System.exit(1);
            }
            
            try{
                //Unexport : this will also remove Betting Center monitor from RMI runtime;
                UnicastRemoteObject.unexportObject(this, true);
            }catch(NoSuchObjectException e){
                e.printStackTrace();
                System.exit(1);
            }
            System.out.print("Repository module shutting down...\n");
        }finally{
            rl.unlock();
        }
    }
    
    //SPECTATOR;
    /**
     * Spectator checks results to see if he won.
     * @param horse_id betted horse id.
     * @return true if horse won.
     */
    @Override
    public boolean checkWin(int spec_id, int horse_id, VectorClock vc){
        LinkedList w;
        boolean b = false;
        rl.lock();
        try{
            vector_clk.update(vc);
            System.out.print("S : Spectator " + spec_id + " checking if there are any winners\n");
            w=results.get(currentRace-1);
            if(w.contains(horse_id))
                b = true;
        }finally{
            rl.unlock();
        }
        return b;
    }
    /**
     * Winner spectator goes to betting center to collect the gains.
     */
    @Override
    public void goCollectGains(int spec_id, VectorClock vc) throws RemoteException{
        rl.lock();
        try{
            //change spectator state
//            MyThreadSpec spectator = (MyThreadSpec) Thread.currentThread();
//            spectator.spec_states = MyThreadSpec.Spec_States.COLLECTING_THE_GAINS;
            vector_clk.update(vc);
            //change log
            log.setSpectatorState(spec_id, MyThreadSpec.Spec_States.COLLECTING_THE_GAINS);
            log.changeLog();
        }finally{
            rl.unlock();
        }
    }
    /**
     * Spectator checks if there are any more races.
     * @return true if there are more races.
     */
    @Override
    public boolean checkFinalRace(int spec_id, VectorClock vc) {
        rl.lock();
        boolean b=false;
        try{
            vector_clk.update(vc);
            //currentRace++;
            if(currentRace == maxRaces) 
                b = true; //temporary return;
            
        }finally{
            rl.unlock();
        }
        return b;
    }
}