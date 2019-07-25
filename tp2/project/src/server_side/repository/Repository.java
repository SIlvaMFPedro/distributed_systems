/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server_side.repository;

import client_side.mythreadspec.MyThreadSpec;
import interfaces.broker.Repository_Broker;
import interfaces.spectator.Repository_Spec;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;
import server_side.Log_Interface;
import server_side.logrepository.LogRepository;

/**
 *
 * @author pedro
 * @author franciscomclt
 */
public class Repository implements Repository_Broker, Repository_Spec{
    private final ReentrantLock rl;
    private final Condition control_center_spec;
    private final int maxRaces;
    private int currentRace;
    
    private final HashMap<Integer, LinkedList> results = new HashMap<Integer, LinkedList>();
    
    public Log_Interface log;
    
    /**
     * Repository constructor;
     * @param log  log interface;
     * @param maxRaces maximum number of races;
     */
    public Repository(Log_Interface log, int maxRaces){
        rl = new ReentrantLock(true);
        control_center_spec = rl.newCondition();
        this.maxRaces = maxRaces;
        currentRace = 0;
        this.log = log;
    }
    
    //BROKER;
    /**
     * Report race results into repository.
     * @param race_id race number.
     * @param winner_id array with the id's of the winner horses.
     */
    @Override
    public void reportResults(int race_id, int[] winner_id){
        LinkedList w;
        rl.lock();
        //control_center_broker.signal();
        try{
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
    public void alertWinners(){
        rl.lock();
        try{
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
    public boolean checkFinalRace() {
        rl.lock();
        boolean b=false;
        try{
            //currentRace++;
            System.out.println("currentRace: "+currentRace +"      maxRaces="+maxRaces);
            if(currentRace == maxRaces)
                b = true; //temporary return;
            
        }finally{
            rl.unlock();
        }
        return b;
    }
    
    //SPECTATOR;
    /**
     * Spectator checks results to see if he won.
     * @param horse_id betted horse id.
     * @return true if horse won.
     */
    @Override
    public boolean checkWin(int spec_id, int horse_id){
        LinkedList w;
        boolean b = false;
        rl.lock();
        try{
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
    public void goCollectGains(int spec_id){
        rl.lock();
        try{
            //change spectator state
//            MyThreadSpec spectator = (MyThreadSpec) Thread.currentThread();
//            spectator.spec_states = MyThreadSpec.Spec_States.COLLECTING_THE_GAINS;
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
    public boolean checkFinalRace(int spec_id) {
        rl.lock();
        boolean b=false;
        try{
            //currentRace++;
            if(currentRace == maxRaces) 
                b = true; //temporary return;
            
        }finally{
            rl.unlock();
        }
        return b;
    }
}