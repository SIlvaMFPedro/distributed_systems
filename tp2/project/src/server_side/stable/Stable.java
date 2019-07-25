/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server_side.stable;

import client_side.mythreadbroker.MyThreadBroker;
import client_side.mythreadhorses.MyThreadHorses;
import interfaces.broker.Stable_Broker;
import interfaces.horses.Stable_Horses;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import java.util.logging.Logger;
import server_side.Log_Interface;
import server_side.logrepository.LogRepository;

/**
 *
 * @author pedro
 * @author franciscomclt
 */
public class Stable implements Stable_Horses, Stable_Broker{
    private final ReentrantLock rl;
    private final Condition stable_horses;
    //private int horse_count = 0;
    private int numHorses_stable = 0;
    private boolean keep_going = true;
    private boolean keep_waiting = true;
    private final int horses_total;
    
    public Log_Interface log;
    
    /**
     * Stable Monitor constructor.
     * @param log log interface.
     * @param horses_total total number of horses.
     */
    public Stable(Log_Interface log, int horses_total){
        rl = new ReentrantLock(true);
        stable_horses = rl.newCondition();
        this.log = log;
        this.horses_total = horses_total;
    }
    
    //BROKER
    /**
     * Broker opens the event.
     */
    @Override
    public void openEvent(){
        rl.lock();
        try{
            log.printFirst();
            System.out.println("B : Opening the event...");
            //change broker state
//            MyThreadBroker broker = (MyThreadBroker) Thread.currentThread();
//            broker.broker_states = MyThreadBroker.Broker_States.OPENING_THE_EVENT;
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
    public void summonHorsesToPaddock(){
        rl.lock();
        try{
            System.out.println("B : Trying to summon horse...");
            //change broker state
//            MyThreadBroker broker = (MyThreadBroker) Thread.currentThread();
//            broker.broker_states = MyThreadBroker.Broker_States.ANNOUNCING_NEXT_RACE;
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
     * Broker checks if all the horses are in the stable before he summons.
     * @return true if number equals max number of horses in stable.
     */
    @Override
    public boolean checkHorses(){
        boolean b=false;
        rl.lock();
        try{
            if (numHorses_stable==horses_total)
                b=true;
        }finally{
            rl.unlock();
        }
        return b;
    }
    
    //HORSES
    /**
     * Horses once they reach the stable can proceed to paddock.
     * @param id horse id.
     */
    @Override
    public void proceedToPaddock(int id){
        rl.lock();
        //horse_count++;
        //stable_broker.signal();
        try{
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
     */
    @Override
    public void returnToStable(int id){
        rl.lock();
        numHorses_stable++; //increment number of horses in stable;
        try{
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
    }
    
    
}