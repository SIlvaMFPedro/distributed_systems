/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package project;

import java.util.Arrays;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 *
 * @author pedro
 * @author franciscomclt
 */
public class Paddock implements Paddock_Broker, Paddock_Horses, Paddock_Spec {
    private final ReentrantLock rl;
    //private final Condition paddock_broker;
    private final Condition paddock_horses;
    private final Condition paddock_spec;
    
    private int horse_count = 0;
    private int appraise_count = 0;
    private int spec_count;
    private final int horses_total;
    private final int spec_total;
    
    private boolean horse_appraised = false;
    private boolean horse_spec = false;
    //private boolean last_spec = false;
   
    private int[] horses_agi;
    private int[] horses_odds;
    
    public LogRepository log;
    
    /**
     * Paddock constructor;
     * @param log log repository;
     * @param horses_total total number of horses;
     * @param spec_total total number of spectators;
     */
    public Paddock(LogRepository log, int horses_total, int spec_total){
        rl = new ReentrantLock(true);
        //paddock_broker = rl.newCondition();
        paddock_horses = rl.newCondition();
        paddock_spec = rl.newCondition();
        
        this.horses_total=horses_total;
        this.horses_agi = new int[horses_total];
        Arrays.fill(horses_agi, 0);
        this.horses_odds = new int[horses_total];
        Arrays.fill(horses_odds, 0);
        this.log = log;
        this.spec_count=0;
        this.spec_total = spec_total;
    }
    
    //BROKER
    /**
     * Broker announces the next race.
     */
    @Override
    public void announceNextRace(){
        rl.lock();
        try{
            System.out.print("B : Announcing next race...\n");
            //horse_count++;
            paddock_horses.signal();
            paddock_spec.signal();
        }finally{
            rl.unlock();
        }
    }
    /**
     * Broker checks spec count.
     */
    @Override
    public boolean checkSpecCount(){
        boolean b = false;
        rl.lock();
        try {
            if(spec_count==spec_total)
                b=true;
        }finally{
            rl.unlock();
        }
        return b;
    }
    /**
     * Broker goes to the betting center to wait for bets.
     */
    @Override
    public void acceptTheBets(){
        rl.lock();
        try{
            System.out.print("B : Broker is going to accept the bets...\n");
            //change broker state
            MyThreadBroker broker = (MyThreadBroker) Thread.currentThread();
            broker.broker_states = MyThreadBroker.Broker_States.WAITING_FOR_BETS;
            //change log
            log.setBrokerState(MyThreadBroker.Broker_States.WAITING_FOR_BETS);
            log.changeLog();
        }finally{
            rl.unlock();
        }
    }
    
    //HORSE
    /**
     * The horses go the race track to proceed to start line and await for a signal from broker.
     * @param id horse id.
     */
    @Override
    public void proceedToStartLine(int id){
        rl.lock();
        try{
            System.out.print("H : Horse " + id + " is proceeding to start line(Horse count: " + horse_count + ")\n");
            horse_count--;
            if (horse_count>0){
                //change horse state
                MyThreadHorses horse = (MyThreadHorses) Thread.currentThread();
                horse.horse_states = MyThreadHorses.Horse_States.AT_THE_START_LINE;
                //change log
                log.setHorseState(id, MyThreadHorses.Horse_States.AT_THE_START_LINE);
                log.changeLog();
                //signal next horse;
                paddock_horses.signal(); 
            }
            else{
                System.out.print("H : All Horses have left the paddock!\n");
                //change horse state
                MyThreadHorses horse = (MyThreadHorses) Thread.currentThread();
                horse.horse_states = MyThreadHorses.Horse_States.AT_THE_START_LINE;
                //change log
                log.setHorseState(id, MyThreadHorses.Horse_States.AT_THE_START_LINE);
                log.changeLog();
                //signal next horse;
                paddock_spec.signal();
            }
        }finally{
            rl.unlock();
        }
    }
    /**
     * The horses reach paddock and await to appraised by the spectators.
     * @param id horse id.
     * @param agi horse's agility.
     */
    @Override
    public void proceedToPaddock(int id, int agi){
        double totalagi;
        double tmp;
        rl.lock();
        try {
            System.out.println("H : Trying reach Paddock (Horse): " + horse_count);
            try {
                    horse_count++;
                    horses_agi[id]=agi; // horses displays its agility at the paddock
                    if(horse_count == horses_total){
                        horse_spec = true;
                        totalagi=0;
                        for(int i=0;i<horses_agi.length;i++){
                            totalagi+=(double)horses_agi[i];
                        }
                        for(int j=0;j<horses_agi.length;j++){
                            tmp=(double)horses_agi[j]/totalagi;
                            horses_odds[j]=(int)(100.0*tmp);        // calculation of the odds to win, based on the horses' agility;
                        }                                           
                        paddock_spec.signalAll(); //signal all spectators;  
                        while(horse_appraised == false){
                            appraise_count = 0; //reset horse appraise counter;
                            paddock_horses.await();
                        }
                        horse_appraised = false;
                        horse_spec = false;
                        //horse_count = 0;
                    }
                    else{
                        while(horse_appraised == false){
                            paddock_horses.await();
                        }
                    }
                    //change log
                    log.setHorseOdds(id, horses_odds[id]);
                    log.changeLog();
                    System.out.println("H : Horse " + id + " leaving to the Paddock(HorseCount = " + horse_count + ")");
                } catch (InterruptedException ex) {
                    Logger.getLogger(Stable.class.getName()).log(Level.SEVERE, null, ex);
                }
        }
        finally{
            rl.unlock();
        }
    }
    
    /**
     * @return Checks if number of spectators equals total;
     */
    @Override
    public boolean checkForSpectators() {
        boolean b = false;
        rl.lock();
        try {
            if(spec_count==spec_total)
                b=true;
        }finally{
            rl.unlock();
        }
        return b;
    }
    
    //SPECTATOR
    /**
     * The spectators wait for next race awaiting for all horses to reach paddock.
     * After that spectators go appraise the horses.
     */
    @Override
    public void waitForNextRace(){
        rl.lock();
        try{
            spec_count++;
            System.out.print("S : Waiting for race to start!\n");
            try{
                System.out.println("S : Awaiting for all horses to reach the paddock(Horse_Count = " + horse_count + ")");
                while(horse_spec == false){
                    paddock_spec.await();
                }
                //change state of spectator
                MyThreadSpec spectator = (MyThreadSpec) Thread.currentThread();
                spectator.spec_states = MyThreadSpec.Spec_States.APPRAISING_THE_HORSES;
                //change log
                int spec_id = spectator.getID();
                log.setSpectatorState(spec_id, MyThreadSpec.Spec_States.APPRAISING_THE_HORSES);
                log.changeLog();
            } catch (InterruptedException ex) {
                Logger.getLogger(Stand.class.getName()).log(Level.SEVERE, null, ex);
            }
        }finally{
            rl.unlock();
        }
    }
    /**
     * Spectators appraise the horses based on their agility.
     * @return array with the agility of all horses.
     */
    @Override
    public int[] appraiseHorses(){
        int[] a = null;
        rl.lock();
        try{
            System.out.print("S : Appraising the Horses(Horse_Count = " + appraise_count + ")\n");
            a=horses_agi;
            appraise_count++;
            if(appraise_count == horses_total){
                horse_appraised = true;
                paddock_horses.signal();
                //paddock_broker.signal();
                System.out.print("S : Horses are being appraised (Horse_count = " + appraise_count + ") -> All Horses have been apraised!\n");
            }
            else{
                System.out.println("S : Horses are being appraised (Horse_count = " + appraise_count + ")");
            }
        }finally{
            rl.unlock();
        }
        return a;        
    }
    /**
     * Spectator goes to the betting center to place his bet.
     * @param id spectator id.
     */
    @Override
    public void goPlaceABet(int id){
        rl.lock();
        try{           
            spec_count--;
           System.out.print("S : Spectator " + id + " is going to place a bet...\n");
           //change spectator state;
           MyThreadSpec spectator = (MyThreadSpec) Thread.currentThread();
           spectator.spec_states = MyThreadSpec.Spec_States.PLACING_A_BET;
           //change log
           log.setSpectatorState(id, MyThreadSpec.Spec_States.PLACING_A_BET);
           log.changeLog();
        }finally{
            rl.unlock();
        }
    }
    
    
}
