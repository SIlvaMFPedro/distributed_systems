/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client_side.mythreadbroker;

import commonInfo.GlobalInfo;
import commonInfo.VectorClock;
import interfaces.broker.BettingCenter_Broker;
import interfaces.broker.Paddock_Broker;
import interfaces.broker.RaceTrack_Broker;
import interfaces.broker.Repository_Broker;
import interfaces.broker.Stable_Broker;
import interfaces.broker.Stand_Broker;
import java.rmi.RemoteException;

/**
 *
 * @author pedro
 * @author franciscoteixeira
 */
public class MyThreadBroker extends Thread{
    private final int id; //broker id also race id;
    
    //Interfaces for Broker
    private BettingCenter_Broker broker_bet_center;
    private Paddock_Broker broker_paddock;
    private RaceTrack_Broker broker_track;
    private Repository_Broker broker_repository;
    private Stable_Broker broker_stable;
    private Stand_Broker broker_stand;
    
    private VectorClock vc;
    
    private int[] results;
    private int total_spec;
    
   
    
    /**
     * Broker States.
     */
    public enum Broker_States{
        START_THE_EVENT, OPENING_THE_EVENT, ANNOUNCING_NEXT_RACE, WAITING_FOR_BETS, WAITING_FOR_HORSES, SUPERVISING_THE_RACE, REPORTING_RESULTS, SETTLING_ACCOUNTS, PLAYING_HOST_AT_THE_BAR;
    }
    
    public volatile Broker_States broker_states;
    
    /**
     * MyThreadBroker constructor;
     * @param id broker id;
     */
    public MyThreadBroker(int id){
        this.id = id;
        broker_states = Broker_States.START_THE_EVENT;
        results = new int[50];
        vc = new VectorClock(GlobalInfo.numSpec+1, id+1);
    }
    
    /**
     * MyThreadBroker constructor;
     * @param id broker id;
     * @param broker_stable broker stable monitor;
     * @param broker_paddock broker paddock monitor;
     * @param broker_bet_center broker betting center monitor;
     * @param broker_track broker racetrack monitor;
     * @param broker_repository broker repository monitor;
     * @param broker_stand broker stand monitor;
     * @param total_spec total number of spectators;
     */
    public MyThreadBroker(int id, Stable_Broker broker_stable, Paddock_Broker broker_paddock, BettingCenter_Broker broker_bet_center, RaceTrack_Broker broker_track, Repository_Broker broker_repository, Stand_Broker broker_stand, int total_spec){
        this.id = id;
        this.broker_stable = broker_stable;
        this.broker_paddock = broker_paddock;
        this.broker_bet_center = broker_bet_center;
        this.broker_track = broker_track;
        this.broker_repository = broker_repository;
        this.broker_stand = broker_stand;
        this.total_spec = total_spec;
        broker_states = Broker_States.START_THE_EVENT;
        results = new int[50];
        vc = new VectorClock(total_spec+1, id+1);
    }
    
    /**
     * Run MyThreadBroker;
     */
    @Override
    public void run(){
        boolean go = true;
        while(go){
            try{
                Thread.sleep(500);
            }catch(Exception e){}
            try{
                System.out.println(broker_states.toString());
                switch(broker_states) {
                    case START_THE_EVENT:
                         vc.increment();
                         broker_stable.openEvent(vc);
                         broker_states = Broker_States.OPENING_THE_EVENT;
                         break;
                    case OPENING_THE_EVENT:
                        vc.increment();
                        if(broker_stable.checkHorses(vc)){
                            broker_stable.summonHorsesToPaddock(vc);
                            broker_states = Broker_States.ANNOUNCING_NEXT_RACE;
                        }
                            
                        break;
                    case ANNOUNCING_NEXT_RACE:
                        vc.increment();
                        if(broker_paddock.checkSpecCount(vc)){
                            broker_paddock.acceptTheBets(vc);
                            broker_states = Broker_States.WAITING_FOR_BETS;
                        }
                        break;
                    case WAITING_FOR_BETS:
                        vc.increment();
                        broker_bet_center.waitForBets(vc);
                        broker_bet_center.goWaitForHorses(vc);
                        broker_states = Broker_States.WAITING_FOR_HORSES;
                        break;
                    case WAITING_FOR_HORSES:
                        broker_track.waitForHorses(vc);
                        broker_track.startRace(vc);
                        broker_states = Broker_States.SUPERVISING_THE_RACE;
                        break;            
                    case SUPERVISING_THE_RACE:
                        broker_track.superviseTheRace(vc);
                        results = broker_track.reportResults(vc);
                        broker_states = Broker_States.REPORTING_RESULTS;
                        break;
                    case REPORTING_RESULTS:                    
                        if(broker_stand.checkSpectators(vc)) {
                            broker_repository.reportResults(id, results, vc); 
                            broker_stand.goReportResults(vc);
                            if(broker_bet_center.areThereAnyWinners(results, vc)){
                                broker_stand.honourBets(vc);
                                broker_states = Broker_States.SETTLING_ACCOUNTS;
                            }
                            else if(broker_repository.checkFinalRace(vc)){
                                broker_stand.entertain(vc);
                                broker_states = Broker_States.PLAYING_HOST_AT_THE_BAR;
                            }
                            else {
                                broker_bet_center.horsesToPaddock(vc);
                                broker_states = Broker_States.OPENING_THE_EVENT;
                            }
                        }
                        break;
                    case SETTLING_ACCOUNTS:
                        broker_bet_center.settlingAccounts(vc);
                        if(broker_repository.checkFinalRace(vc)){  
                            broker_bet_center.entertain(vc);
                            broker_states = Broker_States.PLAYING_HOST_AT_THE_BAR;
                        }
                        else{                  
                            broker_bet_center.horsesToPaddock(vc);
                            broker_states = Broker_States.OPENING_THE_EVENT;
                        }
                        break;
                    case PLAYING_HOST_AT_THE_BAR:    
                        if(!broker_stable.checkHorsesAway(vc)){
                            broker_stable.SendHorsesAway(vc);
                        }
                        else if(broker_stand.checkCelSpectators(vc) + broker_bet_center.getRelaxCount(vc) == total_spec){
                            go = false;
                            broker_stand.requestFinishLog(vc);
                            broker_stand.theEnd(vc);
                        }
                        break;
                }
            }catch(RemoteException e){
                e.printStackTrace();
            }
        }
    }
}