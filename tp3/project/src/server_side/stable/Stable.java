/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server_side.stable;

import client_side.mythreadbroker.MyThreadBroker;
import client_side.mythreadhorses.MyThreadHorses;
import commonInfo.RegistryConfig;
import commonInfo.VectorClock;
import interfaces.Stable_Itf;
import interfaces.broker.Stable_Broker;
import interfaces.horses.Stable_Horses;
import interfaces.registry.Register;
import java.rmi.NoSuchObjectException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
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
public class Stable implements Stable_Itf{
    private final ReentrantLock rl;
    private final Condition stable_horses;
    //private int horse_count = 0;
    private int numHorses_stable = 0;
    private int numHorses_finished = 0;
    private boolean races_finished = false;
    private boolean keep_going = true;
    private boolean keep_waiting = true;
    private final int horses_total;
    
    private String rmiRegHostName;
    private int rmiRegPortNumber;
    
    public Log_Interface log;
    public VectorClock vector_clk;
    
    /**
     * Stable Monitor constructor.
     * @param log log interface.
     * @param horses_total total number of horses.
     */
    public Stable(Log_Interface log, int horses_total, String rmiRegHostName, int rmiRegPortNumber){
        rl = new ReentrantLock(true);
        stable_horses = rl.newCondition();
        this.log = log;
        this.horses_total = horses_total;
        this.rmiRegHostName = rmiRegHostName;
        this.rmiRegPortNumber = rmiRegPortNumber;
        this.vector_clk = new VectorClock(horses_total+1);
    }
    
    //BROKER
    /**
     * Broker opens the event.
     */
    @Override
    public void openEvent(VectorClock vc) throws RemoteException{
        rl.lock();
        try{
            log.printFirst();
            System.out.println("B : Opening the event...");
            //change broker state
//            MyThreadBroker broker = (MyThreadBroker) Thread.currentThread();
//            broker.broker_states = MyThreadBroker.Broker_States.OPENING_THE_EVENT;
            vector_clk.update(vc);
            //change log
            log.changeLog();
            log.setBrokerState(MyThreadBroker.Broker_States.OPENING_THE_EVENT);            
            log.changeLog();
        }finally{
            rl.unlock();
        }
    }
     
    /**
     * Broker summons a horse from stable and sends it to paddock.
     */
    @Override
    public void summonHorsesToPaddock(VectorClock vc) throws RemoteException{
        rl.lock();
        try{
            System.out.println("B : Trying to summon horse...");
            //change broker state
//            MyThreadBroker broker = (MyThreadBroker) Thread.currentThread();
//            broker.broker_states = MyThreadBroker.Broker_States.ANNOUNCING_NEXT_RACE;
            vector_clk.update(vc);
            //change log
            log.setBrokerState(MyThreadBroker.Broker_States.ANNOUNCING_NEXT_RACE);
            log.changeLog();
            keep_waiting = false;
            stable_horses.signal();          
        }finally{
            rl.unlock();
        }
        
    }
    
    /**
     * Broker summons a horse from stable and sends it to paddock.
     */
    @Override
    public void SendHorsesAway(VectorClock vc) throws RemoteException{
        rl.lock();
        try{
            System.out.println("B : Trying to send horses away...");
            vector_clk.update(vc);
            races_finished = true;
            keep_waiting = false;
            stable_horses.signal();          
        }finally{
            rl.unlock();
        }
        
    }
    
    /**
     * Broker checks if all the horses are in the stable before he summons.
     * @return true if number equals max number of horses in stable.
     */
    @Override
    public boolean checkHorses(VectorClock vc) throws RemoteException{
        boolean b=false;
        rl.lock();
        try{
            vector_clk.update(vc);
            if (numHorses_stable==horses_total) {             
                b=true;
            }
            
        }finally{
            rl.unlock();
        }
        return b;
    }
    
    /**
     * Broker checks if all the horses are finished.
     * @return true if number equals max number of horses.
     */
    @Override
    public boolean checkHorsesAway(VectorClock vc) throws RemoteException{
        boolean b=false;
        rl.lock();
        try{
            vector_clk.update(vc);
            if (numHorses_finished==horses_total) {             
                b=true;
            }
            
        }finally{
            rl.unlock();
        }
        return b;
    }
    
    
    /**
     * Broker shutdown Stable monitor
     */
    @Override
    @SuppressWarnings("CallToPrintStackTrace")
    public void terminate() throws RemoteException{
        rl.lock();
        try{
            //System.out.println("Shutting Down Stable Monitor...");
            //Stable_Exec.shutdown();
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
            String nameEntryObj = RegistryConfig.stableNameEntry;
            
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
                System.out.println("Stable registration exception: " + e.getMessage());
                e.printStackTrace();
                System.exit(1);
            }catch(NotBoundException e){
                System.out.println("Stable not bound exception: " + e.getMessage());
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
            System.out.print("Stable module shutting down...\n");
        }finally{
            rl.unlock();
        }
    }
    
    //HORSES
    /**
     * Horses once they reach the stable can proceed to paddock.
     * @param id horse id.
     */
    @Override
    public void proceedToPaddock(int id, VectorClock vc) throws RemoteException{
        rl.lock();
        //horse_count++;
        //stable_broker.signal();
        try{
            vector_clk.update(vc);
            System.out.println("H : Sending horse " + id + " to paddock...");
            numHorses_stable--;
            if(numHorses_stable == 0){
                keep_waiting = true;
                System.out.println("H : All horses have reached the Paddock(HorseCount = " + numHorses_stable + ")");
            }
            else{
                System.out.println("H : Horse " + id + " is reaching paddock(HorseCount = " + numHorses_stable + ")");
            }
            //change horse_states
//            MyThreadHorses horse = (MyThreadHorses) Thread.currentThread();
//            horse.horse_states = MyThreadHorses.Horse_States.AT_THE_PADDOCK;
            //change log
            log.setHorseState(id, MyThreadHorses.Horse_States.AT_THE_PADDOCK);
            log.changeLog();
            //signal next horse
            stable_horses.signal();
            
        }finally{
            rl.unlock();
        }
    }
    /**
     * Horses reach the stable.
     * @param id horse id.
     * @return returns true when the Broker has activated the races finished flag
     */
    @Override
    public boolean returnToStable(int id, VectorClock vc){
        rl.lock();
        numHorses_stable++; //increment number of horses in stable;
        try{
            vector_clk.update(vc);
            System.out.println("H : Returning horse " + id + " to stable...");
            while(keep_waiting){
                stable_horses.await();
            }
        } catch (InterruptedException ex) {
            Logger.getLogger(Stable.class.getName()).log(Level.SEVERE, null, ex);
        }
        finally{
            rl.unlock();
        }
        return races_finished;
    }
    
    /**
     * Horses reach the stable.
     * @param id horse id.
     * @param vc
     */
    @Override
    public void leaveStableFinal(int id, VectorClock vc){
        rl.lock();
        try {
            vector_clk.update(vc);
            numHorses_finished++; //increment number of horses in stable;
            System.out.println("H : Horse leaving: " + id);
        } finally {
            rl.unlock();
        }
    }
    
    
}