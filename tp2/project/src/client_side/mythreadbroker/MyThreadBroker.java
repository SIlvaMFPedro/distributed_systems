/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client_side.mythreadbroker;

import proxy_monitors.Proxy_Betting_Center;
import proxy_monitors.Proxy_Paddock;
import proxy_monitors.Proxy_RaceTrack;
import proxy_monitors.Proxy_Repository;
import proxy_monitors.Proxy_Stable;
import proxy_monitors.Proxy_Stand;

/**
 *
 * @author pedro
 * @author franciscomclt
 */
public class MyThreadBroker extends Thread{
    private final int id; //broker id also race id;
    
    //Proxy Monitors for broker;
    private Proxy_Betting_Center broker_bet_center;
    private Proxy_Paddock broker_paddock;
    private Proxy_RaceTrack broker_track;
    private Proxy_Repository broker_repository;
    private Proxy_Stable broker_stable;
    private Proxy_Stand broker_stand;
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
    public MyThreadBroker(int id, Proxy_Stable broker_stable, Proxy_Paddock broker_paddock, Proxy_Betting_Center broker_bet_center, Proxy_RaceTrack broker_track, Proxy_Repository broker_repository, Proxy_Stand broker_stand, int total_spec){
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
            System.out.println(broker_states.toString());
            switch(broker_states) {
                case START_THE_EVENT:
                     broker_stable.openEvent();
                     break;
                case OPENING_THE_EVENT:
                    if(broker_stable.checkHorses())
                        broker_stable.summonHorsesToPaddock();  
                    break;
                case ANNOUNCING_NEXT_RACE:
                    if(broker_paddock.checkSpecCount())
                        broker_paddock.acceptTheBets();
                    break;
                case WAITING_FOR_BETS:
                    broker_bet_center.waitForBets();
                    broker_bet_center.goWaitForHorses();
                    break;
                case WAITING_FOR_HORSES:
                    broker_track.waitForHorses();
                    broker_track.startRace();
                    break;            
                case SUPERVISING_THE_RACE:
                    broker_track.superviseTheRace();
                    results = broker_track.reportResults();
                    break;
                case REPORTING_RESULTS:                    
                    if(broker_stand.checkSpectators()) {
                        broker_repository.reportResults(id, results); 
                        broker_stand.goReportResults();
                        if(broker_bet_center.areThereAnyWinners(results)){
                            broker_stand.honourBets();
                        }
                        else if(broker_repository.checkFinalRace()){
                            broker_stand.entertain();
                        }
                        else {
                            broker_bet_center.horsesToPaddock();
                        }
                    }
                    break;
                case SETTLING_ACCOUNTS:
                    broker_bet_center.settlingAccounts();
                    if(broker_repository.checkFinalRace()){  
                        broker_bet_center.entertain();
                    }
                    else{                  
                        broker_bet_center.horsesToPaddock();
                    }
                    break;
                case PLAYING_HOST_AT_THE_BAR:                    
                    if(broker_stand.checkCelSpectators() + broker_bet_center.getRelaxCount() == total_spec){
                        go = false;
                        broker_stand.requestFinishLog();
                        broker_stand.theEnd();
                    }
                    break;
            }
        }
    }  
}