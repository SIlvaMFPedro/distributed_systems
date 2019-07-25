/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server_side.stand;

import interfaces.broker.Stand_Broker;
import interfaces.spectator.Stand_Spec;

import client_side.ClientCom;
import client_side.mythreadbroker.MyThreadBroker;
import client_side.mythreadspec.MyThreadSpec;
import commonInfo.CommPorts;
import commonInfo.Message;
import commonInfo.RegistryConfig;
import commonInfo.VectorClock;
import static java.lang.Thread.sleep;
import genclass.GenericIO;
import interfaces.Stand_Itf;
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
public class Stand implements Stand_Itf{
    private final ReentrantLock rl;
    private final Condition stand_spec;
    //private final boolean start_of_race = false;
    private boolean watching = true;
    private int spec_count;
    private final int spec_total;
    private int celebration_count;
    
    private String rmiRegHostName;
    private int rmiRegPortNumber;
    
    public Log_Interface log;
    public VectorClock vector_clk;
    
    /**
     * Stand Monitor constructor.
     * @param log log interface
     * @param spec_total total number of spectators.
     */
    public Stand(Log_Interface log, int spec_total, String rmiRegHostName, int rmiRegPortNumber){
        rl = new ReentrantLock(true);
        stand_spec = rl.newCondition();
        spec_count=spec_total;
        this.spec_total=spec_total;
        this.log = log;
        this.celebration_count=0;
        this.rmiRegHostName = rmiRegHostName;
        this.rmiRegPortNumber = rmiRegPortNumber;
        this.vector_clk = new VectorClock(spec_total+1);
    }
    
    //BROKER
    /**
     * Broker goes to the repository to report race results.
     */
    @Override
    public void goReportResults(VectorClock vc){
        rl.lock();
        try{
            vector_clk.update(vc);
            System.out.print("B : Reporting results to repository...\n");
            watching = false;
            stand_spec.signalAll(); //signal all spectators to go check if they won;
        }finally{
            rl.unlock();
        }       
    }
    /**
     * After the races are all done. He plays host at the bar and entertains guests.
     */
    @Override
    public void entertain(VectorClock vc) throws RemoteException{
        rl.lock();
        try{
            vector_clk.update(vc);
            System.out.print("B : Entertaining the guests...\n");
            //change broker state
//            MyThreadBroker broker = (MyThreadBroker) Thread.currentThread();
//            broker.broker_states = MyThreadBroker.Broker_States.PLAYING_HOST_AT_THE_BAR;
            //change log
            log.setBrokerState(MyThreadBroker.Broker_States.PLAYING_HOST_AT_THE_BAR);
            log.changeLog();
            
        }finally{
            rl.unlock();
        }
    }
    /**
     * If the there is a race winner, broker goes to the betting center to settle accounts.
     */
    @Override
    public void honourBets(VectorClock vc) throws RemoteException{
        rl.lock();
        try{
            vector_clk.update(vc);
            System.out.print("B : Going to betting center to honour bets...\n");
            //change broker state
//            MyThreadBroker broker = (MyThreadBroker) Thread.currentThread();
//            broker.broker_states = MyThreadBroker.Broker_States.SETTLING_ACCOUNTS;
            //change log
            log.setBrokerState(MyThreadBroker.Broker_States.SETTLING_ACCOUNTS);
            log.changeLog();
        }finally{
            rl.unlock();
        } 
    }
    /**
     * Broker goes back to stable to summon horses to paddock.
     */
    @Override
    public void horsesToPaddock(VectorClock vc) throws RemoteException{
        rl.lock();
        try{
            vector_clk.update(vc);
            System.out.print("B : Going back to send horses back to paddock...\n");
            //change broker state
//            MyThreadBroker broker = (MyThreadBroker) Thread.currentThread();
//            broker.broker_states = MyThreadBroker.Broker_States.ANNOUNCING_NEXT_RACE;
            //change log
            log.setBrokerState(MyThreadBroker.Broker_States.ANNOUNCING_NEXT_RACE);
            log.changeLog();
        }finally{
            rl.unlock();
        }
    }
    /**
     * Broker checks the number of spectators
     * @return true if spec_count equals max number of spectators.
     */
    @Override
    public boolean checkSpectators(VectorClock vc) throws RemoteException{
        boolean b=false;
        rl.lock();
        try{
            if (spec_count==spec_total)
                vector_clk.update(vc);
                b=true;
        }finally{
            rl.unlock();
        }
        return b;
    }
    
    /**
     * Broker checks number of celebrating spectators in stand.
     * @return number of celebrating spectators.
     */
    @Override
    public int checkCelSpectators(VectorClock vc) throws RemoteException{
        rl.lock();
        try{
            vector_clk.update(vc);
            System.out.println("Checking Celebrating Spec Count: " + celebration_count);
        }finally{
            rl.unlock();
        }
        return celebration_count;
    }
    
    /**
     * Broker request finish log.
     */
    @Override
    public void requestFinishLog(VectorClock vc) throws RemoteException{
        rl.lock();
        try{
            vector_clk.update(vc);
            System.out.println("Broker finishing log...");
            log.finishLog();
        }finally{
            rl.unlock();
        }
    }
    
    /**
     * Broker shutdown Stand monitor
     */
    @Override
    @SuppressWarnings("CallToPrintStackTrace")
    public void terminate() throws RemoteException{
        rl.lock();
        try{
            //System.out.println("Shutting Down Stand Monitor...");
            //Stand_Exec.shutdown();
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
            String nameEntryObj = RegistryConfig.standNameEntry;
            
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
                System.out.println("Stand registration exception: " + e.getMessage());
                e.printStackTrace();
                System.exit(1);
            }catch(NotBoundException e){
                System.out.println("Stand not bound exception: " + e.getMessage());
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
            System.out.print("Stand module shutting down...\n");
        }finally{
            rl.unlock();
        }
    }
    
    //SPECTATOR
    /**
     * Spectator waits for next race to start;
     */
    @Override
    public void waitForNextRace(int spec_id, VectorClock vc) throws RemoteException{
        rl.lock();
        try {
            vector_clk.update(vc);
            System.out.print("S : Waiting for race to start!\n");
            //change spectator state;
//            MyThreadSpec spectator = (MyThreadSpec) Thread.currentThread();
//            spectator.spec_states = MyThreadSpec.Spec_States.WAITING_FOR_A_RACE_TO_START;
            //change log
            log.setSpectatorState(spec_id, MyThreadSpec.Spec_States.WAITING_FOR_A_RACE_TO_START);
            log.changeLog();
        } finally {
            rl.unlock();
        }
    }
    /**
     * Spectator goes to paddock to check horses.
     */
    @Override
    public void goCheckHorses(int spec_id, int currentMoney, VectorClock vc) throws RemoteException{
        rl.lock();
        try{
            System.out.print("S" + spec_id + " : Going to paddock to check on horses...\n");          
            spec_count--;
            //change state of spectator
//            MyThreadSpec spectator = (MyThreadSpec) Thread.currentThread();
//            spectator.spec_states = MyThreadSpec.Spec_States.APPRAISING_THE_HORSES;   
            vector_clk.update(vc);
            //change log
            int spec_mon= currentMoney;
            log.setSpectatorMoney(spec_id, spec_mon);
            log.changeLog();

            
            if(spec_count>0) 
                stand_spec.signal();   
                    
        }finally{
            rl.unlock();
        }
    }
    /**
     * Spectator watches the race until broker reports results.
     */
    @Override
    public void watchingTheRace(int spec_id, VectorClock vc){
        rl.lock();
        watching = true;
        try{
            try{
                spec_count++;
                System.out.print("S" + spec_id + " : Watching the race....\n");
                while(watching){
                    stand_spec.await();
                }
            } catch (InterruptedException ex) {
                Logger.getLogger(Stand.class.getName()).log(Level.SEVERE, null, ex);
            }
        }finally{
            rl.unlock();
        }
    }
    /**
     * Spectator goes to the repository to check if he won.
     */
    @Override
    public void goCheckWin(int spec_id, VectorClock vc) throws RemoteException{
        rl.lock();
        try{
            System.out.print("S" + spec_id + " : Going to repository to check win...\n");
            //change state of spectator
//            MyThreadSpec spectator = (MyThreadSpec) Thread.currentThread();
//            spectator.spec_states = MyThreadSpec.Spec_States.CHECKING_RESULTS;
            //change log
            log.setSpectatorState(spec_id, MyThreadSpec.Spec_States.CHECKING_RESULTS);
            log.changeLog();
        }finally{
            rl.unlock();
        }
    }
    /**
     * Spectator goes out to celebrate.
     */
    @Override
    public void celebrate(int spec_id, VectorClock vc) throws RemoteException{
        rl.lock();
        try{
            celebration_count++;
            System.out.print("S" + spec_id + " : Celebrating...\n");
            //change state of spectator
//            MyThreadSpec spectator = (MyThreadSpec) Thread.currentThread();
//            spectator.spec_states = MyThreadSpec.Spec_States.CELEBRATING;
            //change log
            log.setSpectatorState(spec_id, MyThreadSpec.Spec_States.CELEBRATING);
            log.changeLog();
            
        }finally{
            rl.unlock();
        }
    }
    
    /**
     * Final Function
     */
    @Override
    public void theEnd(VectorClock vc) throws RemoteException{
        boolean b = false;
        rl.lock();
        try{
            //log.finishLog();
            System.out.println("END OF RACES!!!!");
        }finally{
            rl.unlock();
        }
        //return b;
    }
}