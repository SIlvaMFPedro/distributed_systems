/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package project;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author pedro
 * @author franciscomclt
 */
public class Stand implements Stand_Broker, Stand_Spec{
    private final ReentrantLock rl;
    private final Condition stand_spec;
    //private final boolean start_of_race = false;
    private boolean watching = true;
    private int spec_count;
    private final int spec_total;
    private int celebration_count;
    
    public LogRepository log;
  
    public Stand(LogRepository log, int spec_total){
        rl = new ReentrantLock(true);
        stand_spec = rl.newCondition();
        spec_count=spec_total;
        this.spec_total=spec_total;
        this.log = log;
        this.celebration_count=0;
    }
    
    //BROKER
    /**
     * Broker goes to the repository to report race results.
     */
    @Override
    public void goReportResults(){
        rl.lock();
        try{
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
    public void entertain(){
        rl.lock();
        try{
            celebration_count++;
            System.out.print("B : Entertaining the guests...\n");
            //change broker state
            MyThreadBroker broker = (MyThreadBroker) Thread.currentThread();
            broker.broker_states = MyThreadBroker.Broker_States.PLAYING_HOST_AT_THE_BAR;
            //change log
            log.setBrokerState(MyThreadBroker.Broker_States.PLAYING_HOST_AT_THE_BAR);
            log.changeLog();
            if(celebration_count==spec_total+1)
                log.finishLog();
        }finally{
            rl.unlock();
        }
    }
    /**
     * If the there is a race winner, broker goes to the betting center to settle accounts.
     */
    @Override
    public void honourBets(){
        rl.lock();
        try{
            System.out.print("B : Going to betting center to honour bets...\n");
            //change broker state
            MyThreadBroker broker = (MyThreadBroker) Thread.currentThread();
            broker.broker_states = MyThreadBroker.Broker_States.SETTLING_ACCOUNTS;
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
    public void horsesToPaddock(){
        rl.lock();
        try{
            System.out.print("B : Going back to send horses back to paddock...\n");
            //change broker state
            MyThreadBroker broker = (MyThreadBroker) Thread.currentThread();
            broker.broker_states = MyThreadBroker.Broker_States.ANNOUNCING_NEXT_RACE;
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
    public boolean checkSpectators(){
        boolean b=false;
        rl.lock();
        try{
            if (spec_count==spec_total)
                b=true;
        }finally{
            rl.unlock();
        }
        return b;
    }
    
    //SPECTATOR
    /**
     * Spectator waits for next race to start;
     */
    @Override
    public void waitForNextRace(){
        rl.lock();
        try {
            System.out.print("S : Waiting for race to start!\n");
            //change spectator state;
            MyThreadSpec spectator = (MyThreadSpec) Thread.currentThread();
            spectator.spec_states = MyThreadSpec.Spec_States.WAITING_FOR_A_RACE_TO_START;
            //change log
            int spec_id = spectator.getID();
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
    public void goCheckHorses(){
        rl.lock();
        try{
            System.out.print("S : Going to paddock to check on horses...\n");          
            spec_count--;
            //change state of spectator
            MyThreadSpec spectator = (MyThreadSpec) Thread.currentThread();
            spectator.spec_states = MyThreadSpec.Spec_States.APPRAISING_THE_HORSES;
            //change log
            int spec_id = spectator.getID();
            int spec_mon= spectator.getMoney();
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
    public void watchingTheRace(){
        rl.lock();
        watching = true;
        try{
            try{
                spec_count++;
                System.out.print("S : Watching the race....\n");
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
    public void goCheckWin(){
        rl.lock();
        try{
            System.out.print("S : Going to repository to check win...\n");
            //change state of spectator
            MyThreadSpec spectator = (MyThreadSpec) Thread.currentThread();
            spectator.spec_states = MyThreadSpec.Spec_States.CHECKING_RESULTS;
            //change log
            int spec_id = spectator.getID();
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
    public void celebrate(){
        rl.lock();
        try{
            celebration_count++;
            System.out.print("S : Celebrating...\n");
            //change state of spectator
            MyThreadSpec spectator = (MyThreadSpec) Thread.currentThread();
            spectator.spec_states = MyThreadSpec.Spec_States.CELEBRATING;
            //change log
            int spec_id = spectator.getID();
            log.setSpectatorState(spec_id, MyThreadSpec.Spec_States.CELEBRATING);
            log.changeLog();
            if(celebration_count==spec_total+1)
                log.finishLog();
        }finally{
            rl.unlock();
        }
    }
}
