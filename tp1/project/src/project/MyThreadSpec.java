/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package project;

import java.util.Arrays;

/**
 *
 * @author pedro
 * @author franciscomclt
 */
public class MyThreadSpec extends Thread{
    private final int id; //Universal ID for spectator;
    //Monitors for spectator;
    private final Stand_Spec spec_stand;
    private final Paddock_Spec spec_paddock;
    private final Repository_Spec spec_repository;
    private final BettingCenter_Spec spec_bet_center;
    private int horse_id; //betting horse ID;
    private int bet_amount; //betting amount for current bet;
    private final int upper_limit; //maximum number of horses;
    private final int lower_limit; //minimum number of horses;
    private final int horses_amount = 4; // number of horses per race
    private int[] horses_agility; // agility of the horses of current to be race
    private int money; // Currentamount of money    
    
    /**
     * Spectator States;
     */
    public enum Spec_States{
         WAITING_FOR_A_RACE_TO_START, APPRAISING_THE_HORSES, PLACING_A_BET, WATCHING_A_RACE, CHECKING_RESULTS, COLLECTING_THE_GAINS, CELEBRATING;
    }
    
    public volatile Spec_States spec_states;
    
    /**
     * MyThreadSpec constructor;
     * @param id spectator thread id;
     * @param spec_stand spectator stand monitor;
     * @param spec_paddock spectator paddock monitor;
     * @param spec_repository spectator repository monitor;
     * @param spec_bet_center spectator betting center monitor;
     * @param money spectator money;
     */
    public MyThreadSpec(int id, Stand_Spec spec_stand, Paddock_Spec spec_paddock, Repository_Spec spec_repository, BettingCenter_Spec spec_bet_center, int money){
        this.id = id;
        this.spec_stand = spec_stand;
        this.spec_paddock = spec_paddock;
        this.spec_repository = spec_repository;
        this.spec_bet_center = spec_bet_center;
        this.horse_id = 0;
        this.lower_limit = 0;
        this.upper_limit = 3;
        this.money = money;
        this.bet_amount = 0;
        spec_states = Spec_States.WAITING_FOR_A_RACE_TO_START;
        
	this.horses_agility = new int[horses_amount];
        Arrays.fill(horses_agility,0);
    }
    
    /**
     * @return Returns the Spectator's ID. 
     */
    public int getID(){
        return this.id;
    }
    
    /**
     * @return Returns the Spectator's current amount of money. 
     */
    public int getMoney(){
        return this.money;
    }
    /**
     * @return  Return the quickest horse;
     */
    private int max(){
        int r=0,m=0;
        for(int i=0; i<horses_amount; i++){
            if(horses_agility[i]>m){
                m=horses_agility[i];
                r=i;
            }
        }      
        return r;
    }
    /**
     * @return Return the underdog;
     */
    private int min(){
        int r=0,m=10000;
        for(int i=0; i<horses_amount; i++){
            if(horses_agility[i]<m){
                m=horses_agility[i];
                r=i;
            }
        }      
        return r;
    }
    /**
     * @return Spectator always bets on horse 3;
     */
    private int alwaysBetOnThree(){
        //int r=0;           
        //return r;
        return 3;
    }
    /**
     * @return Spectator picks a horse based on his liking;
     */
    private int pickAHorse() {
        int h;
        switch(id) {
            case(1):
                h=max(); // likes to bet on the quickest
                break;
            case(2):
                h=min(); // likes to pick the underdog
                break;
            case(3):
                h=alwaysBetOnThree(); // picks based on lucky number
                break;
            default:
                h=(int) (Math.random() * (upper_limit - lower_limit)) + lower_limit; // picks randomly
        }
        return h;
    }
    /**
     * @return Amount to bet;
     */
    private int bet_val(){
        int v;       
        
        switch(id) {
            case(1):
                v=Math.min(money,(int) (Math.random() * 20)); //Guy that bets ont he quickest horse is very caustious
                break;
            case(2):
                v=(int) (Math.random() * money); // Guy that bets on the underdog, bets an amount decided purely on the spur of the moment
                break;
            case(3):
                v=Math.min(money,33);  // This guy really likes number three
                break;
            default:
                v=Math.min(money, (int)(Math.random()*50)+50); // Get that pick the horse randomly likes to bet more money
        }        
        return v;
    }
    
    /**
     * Auxiliary function where the spectator loses the value of the amount of money, that he used to bet on.
     * @param val Value to pay.
     */
    public void pay(int val){
        money-= val;
    }
    /**
     * Auxiliary function where the spectator gains the value of the amount of money, that he won betting.
     * @param val Value to receive.
     */
    public void receive(int val){
        money += val;
    }
    
    /**
     * Run MyThreadSpec;
     */
    @Override
    public void run(){
        boolean go = true;
        while(go){
            try{
                Thread.sleep(500);
            }catch(Exception e){}
            switch(spec_states) {
                case WAITING_FOR_A_RACE_TO_START:
                    //spec_stand.waitForNextRace();
                    spec_stand.goCheckHorses();
                    spec_paddock.waitForNextRace();
                    break;
                case APPRAISING_THE_HORSES:
//                    spec_paddock.waitForNextRace();
                    horses_agility=spec_paddock.appraiseHorses();
                    spec_paddock.goPlaceABet(id);
                    break;
                case PLACING_A_BET:
                    horse_id = pickAHorse(); // Choose wich horse to bet on
                    bet_amount = bet_val();
                    spec_bet_center.placeABet(id, horse_id, bet_amount);
                    spec_bet_center.goWatchRace();
                    break;
                case WATCHING_A_RACE:
                    spec_stand.watchingTheRace();
                    spec_stand.goCheckWin();
                    break;
                case CHECKING_RESULTS:
                    if(spec_repository.checkWin(horse_id)) {
                        spec_bet_center.collectGains(id);
                    }
                    else if(spec_repository.checkFinalRace()){
                        spec_stand.celebrate();
                    }
                    else{
                        spec_stand.waitForNextRace();
                    }                   
                    break;
                case COLLECTING_THE_GAINS:                   
                    if(spec_repository.checkFinalRace()){
                        spec_bet_center.goRelax();
                    }
                    else{
                        spec_bet_center.waitForNextRace();
                    }
                    break;
                case CELEBRATING:
                    System.out.print("S : WE ARE CELEBRATING!!!\n");
                    go = false;
                    break;
            }
        }
    }    
}
