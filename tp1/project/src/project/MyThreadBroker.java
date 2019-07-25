/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package project;

/**
 *
 * @author pedro
 * @author franciscomclt
 */
public class MyThreadBroker extends Thread{
    private final int id; //broker id also race id;
    //Monitors for broker;
    private final Stable_Broker broker_stable; 
    private final Paddock_Broker broker_paddock; 
    private final BettingCenter_Broker broker_bet_center; 
    private final RaceTrack_Broker broker_track; 
    private final Repository_Broker broker_repository; 
    private final Stand_Broker broker_stand; 
    private int[] results;
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
     * @param broker_stable broker stable monitor;
     * @param broker_paddock broker paddock monitor;
     * @param broker_bet_center broker betting center monitor;
     * @param broker_track broker racetrack monitor;
     * @param broker_repository broker repository monitor;
     * @param broker_stand broker stand monitor;
     */
    public MyThreadBroker(int id, Stable_Broker broker_stable, Paddock_Broker broker_paddock, BettingCenter_Broker broker_bet_center, RaceTrack_Broker broker_track, Repository_Broker broker_repository, Stand_Broker broker_stand){
        this.id = id;
        this.broker_stable = broker_stable;
        this.broker_paddock = broker_paddock;
        this.broker_bet_center = broker_bet_center;
        this.broker_track = broker_track;
        this.broker_repository = broker_repository;
        this.broker_stand = broker_stand;
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
                    go = false;
                    break;
            }
        }
    }
}
