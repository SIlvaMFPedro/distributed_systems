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
public class RaceTrack implements RaceTrack_Horses, RaceTrack_Broker{
    private final ReentrantLock rl;
    private final Condition race_track_horses;
    private final Condition race_track_broker;
    private final int horses_total;
    private int num_horses;
    private int num_races;
    private boolean keep_going = true;
    private boolean start_the_race = false;
    private boolean end_of_race = false;
    private boolean horse_wait = true;
    private final int track_length;
    private int[] race_dist; 
    private int[] race_it; 
    private boolean[] race_fin;
    private int horses_finished;
    private int win_it = 0;
    private int win_dist = 0;
    private int win_count = 0;
    private int[] race_pos;
    private int horse_turn;
    private int finish_count;
    private final int number_horses;
    private int currentRace;
    
    public LogRepository log;
    
    /**
     * RaceTrack constructor;
     * @param log log repository;
     * @param track_length length of the track;
     * @param horses_total total number of horses;
     * @param num_races total number of races;
     */
    public RaceTrack(LogRepository log, int track_length, int horses_total, int num_races){
        rl = new ReentrantLock(true);
        race_track_horses = rl.newCondition();
        race_track_broker = rl.newCondition();
	this.horses_total = horses_total;
        race_dist = new int[horses_total];
        Arrays.fill(race_dist,0);
        race_it = new int[horses_total];
        Arrays.fill(race_it,0);
        race_fin = new boolean[horses_total];
        Arrays.fill(race_fin,false);
        num_horses = 0;
        horses_finished = 0;
        this.num_races = num_races;
        horse_turn = 0;
        finish_count = 0;
        number_horses = horses_total;
        this.track_length=track_length;
        this.race_pos = new int[horses_total];
        Arrays.fill(race_dist,1);
        this.log = log;
        currentRace = 0;
    }
    
    //BROKER
    /**
     * Broker waits for horses at the start line of the race track.
     */
    @Override
    public void waitForHorses(){
        rl.lock();
        //num_horses++;
        race_track_horses.signal();
        try{
            System.out.print("B : Waiting for horses...\n");
            try{
                while(!checkOnHorses()){
                    System.out.println("B : Awaiting for all horses to reach the start line(Horse Count = " + num_horses + ")");
                    race_track_broker.await();
                }
                //num_horses = 0; //reset horse counter;
                start_the_race = true;
                end_of_race = false;
            } catch (InterruptedException ex) {
                Logger.getLogger(RaceTrack.class.getName()).log(Level.SEVERE, null, ex);
            }
        }finally{
            rl.unlock();
        }
    }
    /**
     * Broker starts the race when all the horses are ready and all bets have been made.
     */
    @Override
    public void startRace(){
        rl.lock();
        try{
            if(start_the_race){
                System.out.print("B : Starting the race...\n");
                currentRace++;
                horse_wait = false;
                Arrays.fill(race_pos,1);                
                race_track_horses.signalAll(); //signal all horses;
                //changing broker state;
                MyThreadBroker broker = (MyThreadBroker) Thread.currentThread();
                broker.broker_states = MyThreadBroker.Broker_States.SUPERVISING_THE_RACE;
                //change log
                log.setBrokerState(MyThreadBroker.Broker_States.SUPERVISING_THE_RACE);
                log.setRaceNumber(currentRace);
                log.changeLog();
            }
            else{
                System.err.print("B : Error! Cannot start race...\n");
            }
        }finally{
            rl.unlock();
        }
    }
    /**
     * Broker supervises the race until he receives a signal from the last horse to cross the finish line.
     */
    @Override
    public void superviseTheRace(){
        rl.lock();
        try{
            System.out.print("B : Supervising the race...\n");
            try{
                while(end_of_race == false){
                    race_track_broker.await();
                }
                System.out.print("B : End of race supervise...\n");
            } catch (InterruptedException ex) {
                Logger.getLogger(Stand.class.getName()).log(Level.SEVERE, null, ex);
            }
        }finally{
            rl.unlock();
        }
    }
    /**
     * Broker gets the race results and goes to the repository to report said results.
     * @return array with winner horses id;
     */
    @Override
    public int[] reportResults(){
        rl.lock();
        int[] array = null;
        int size = 0;
        try{
            array = new int[win_count];
            for(int i = 0; i < horses_total; i++){
                if((race_it[i]==  win_it) && (race_dist[i] == win_dist)){
                    array[size]=i;                                                  // SUBSTITUIR I SE NECESSARIO PARA O ID DO CAVALO
                    size++;
                }
            }            
            //reset race variables;
            horses_finished = 0;
            horse_turn = 0;
            Arrays.fill(race_dist,0);
            Arrays.fill(race_it,0);
            Arrays.fill(race_fin,false);
            Arrays.fill(race_pos,0);
            win_it = 0;
            win_dist = 0;
            win_count = 0;
            finish_count = 0;            
            //change broker state
            MyThreadBroker broker = (MyThreadBroker) Thread.currentThread();
            broker.broker_states = MyThreadBroker.Broker_States.REPORTING_RESULTS;
            //change log
            log.setHorsePos(race_pos);
            log.setBrokerState(MyThreadBroker.Broker_States.REPORTING_RESULTS);
            for (int i=0;i<horses_total;i++) {
                log.setHorseIT(i, 0);
                log.setHorseLength(i, 0);
                log.setHorsePos(race_pos);
            }
            log.changeLog();
        }
        finally{
            rl.unlock();
        }
        return array;
    }
    /**
     * Broker checks the number of horses.
     * @return true if number equals max number of horses.
     */
    @Override
    public boolean checkOnHorses(){
         boolean b=false;
        rl.lock();
        try{
            if (num_horses==horses_total)
                b=true;
        }finally{
            rl.unlock();
        }
        return b;
    }
    
    //HORSES
    /**
     * Horses reach start line and await signal from broker.
     * @param id horse id.
     */
    @Override
    public void atTheStartLine(int id){
        rl.lock();
        try{
            System.out.print("H : Horse " + id + " has reached the start line...\n");
            num_horses++;
            race_track_broker.signal();
            while(horse_wait){
                race_track_horses.await(); //waiting for broker to start the next race;
            }
            //change horse state
            MyThreadHorses th = (MyThreadHorses) Thread.currentThread();
            th.horse_states = MyThreadHorses.Horse_States.RUNNING;
            //change log
            log.setHorseState(id, MyThreadHorses.Horse_States.RUNNING);
            log.changeLog();
        } catch (InterruptedException ex) {
            Logger.getLogger(RaceTrack.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            rl.unlock();
        }
    }
    
    /**
     * Horse makes a move and signals the other horses.
     * @param id horse id
     * @param d travel distance.
     */
    @Override
    public void makeAMove(int id, int d){
        rl.lock();
        System.out.println("H : Horse " + id + " is on the move...");
        try{
            race_dist[id] += d;            
            race_it[id]++;
            System.out.print("H : Horse " + id + " Iteration: " + race_it[id] + " distance : " + race_dist[id] + "\n");
            horse_turn++;
            
            // update the current positions at the race            
            int aux;
            for (int i=0;i<horses_total;i++){                 
                if(i!=id) {
                    if(ahead(race_it[id],race_it[i],race_dist[id],race_dist[i])){
                        
                        if(race_pos[id]>race_pos[i]) {
                            aux=race_pos[id];
                            race_pos[id]=race_pos[i];
                            for (int j=0;j<horses_total;j++) {
                                if((j!=id)&&(race_pos[j]>=race_pos[id] && race_pos[j]<=aux))
                                    race_pos[j]++;
                            }
                        }
                        else if (race_pos[id]==race_pos[i]){                            
                            for (int k=0;k<horses_total;k++){
                                if((k!=id)&&(race_pos[id]==race_pos[k]))
                                    race_pos[k]++;
                            }
                        }
                        
                    }
                    else if(sideBySide(race_it[id],race_it[i],race_dist[id],race_dist[i])) {
                        if(race_pos[id]>race_pos[i]) {
                            aux=race_pos[id];
                            race_pos[id]=race_pos[i];
                            for (int l=0;l<horses_total;l++){
                                if((l!=id)&&(race_pos[l]>race_pos[id] && race_pos[l]<=aux))
                                    race_pos[l]++;
                            }
                        }
                    }
                }
            }
            //change log
            log.setHorseIT(id, race_it[id]);
            log.setHorseLength(id, race_dist[id]);
            log.setHorsePos(race_pos);
            log.changeLog();
            race_track_horses.signalAll();
        }finally{
            rl.unlock();
        }
    }
    /**
     * @param it1 first horse step iteration;
     * @param it2 second horse step iteration;
     * @param dist1 first horse travel distance;
     * @param dist2 second horse travel distance;
     * @return Checks if first horse is ahead of the second horse;
     */
    private boolean ahead(int it1, int it2, int dist1, int dist2){        
        if(dist1>=track_length && dist2>=track_length){
            if(it1<it2 || (it1==it2 && dist1>dist2))
                return true;
        }
        else if(dist1>dist2)        
            return true;
                
        return false;
    } 
    /**
     * @param it1 first horse step iteration;
     * @param it2 second horse step iteration;
     * @param dist1 first horse travel distance;
     * @param dist2 second horse travel distance;
     * @return Checks if the horses are traveling side by side;
     */
    private boolean sideBySide(int it1, int it2, int dist1, int dist2){        
        if(dist1>=track_length && dist2>=track_length){
            if(it1==it2 && dist1==dist2)
                return true;
        }
        else if(dist1==dist2)
            return true;
                
        return false;
    }
    /**
     * Horse checks if it is its turn to check if he crossed the finish line and if not he makes a move.
     * @param id horse id
     * @return true if horse crosses the finish line.
     */
    @Override
    public boolean hasFinishLineBeenCrossed(int id){
        boolean b=false;        
        System.out.println("H : Horse " + id + " is checking finish line...");
        int horse_id = id%number_horses;
        rl.lock();
        try{
            while(horse_turn%number_horses != horse_id){
                race_track_horses.await();
            }
            if(race_dist[id]>=track_length){
                b=true;
                if(race_fin[horse_id] == false){
                    race_fin[horse_id] = true;
                    horses_finished++;
                    
                    if(win_it==0||win_it>race_it[horse_id]) {   /////////////////////////////////////
                        win_it=race_it[horse_id];               //
                        win_dist=race_dist[horse_id];           //
                        win_count=1;                            //
                    }                                           //
                    else if(win_it==race_it[horse_id]){         //
                        if(win_dist<race_dist[horse_id]){       //
                            win_dist=race_dist[horse_id];       //  Horse checks if it has the best run yet, or is tied for first, and changes winner values accordingly
                            win_count=1;                        //
                        }                                       //
                        else if(win_dist==race_dist[horse_id])  //
                            win_count++;                        //
                    }                                           /////////////////////////////////////
                    
                }
                if(horses_finished==horses_total){
                    System.out.println("H : Horse " + id + " is going to finish line...");
                    //change horses
                    MyThreadHorses th = (MyThreadHorses) Thread.currentThread();
                    th.horse_states = MyThreadHorses.Horse_States.AT_THE_FINNISH_LINE;
                    //change log
                    log.setHorseState(id, MyThreadHorses.Horse_States.AT_THE_FINNISH_LINE);
                    
                    finish_count++;
                    if(finish_count==horses_total) {
                        System.out.println("H : Race END! Signaling broker...");
                        //change log
                        log.setHorsesAtTheEnd(race_pos);
                 
                        end_of_race = true;
                        horse_wait = true;
                        race_track_broker.signal();
                    }                    
                    else{
                        horse_turn++;
                        race_track_horses.signalAll();                   
                    }
                    log.changeLog();
                                    
                }
                else{
                    horse_turn++;
                    race_track_horses.signalAll();                    
                }
            }
            else{
                b=false;
            }
        } catch (InterruptedException ex) {
            Logger.getLogger(RaceTrack.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            rl.unlock();
        }
        return b;
    }
    /**
     * After the end of the race, the horses go back to the stable.
     * @param id horse id.
     */
    @Override
    public void proceedToStable(int id){
        rl.lock();
        num_horses--;
        //race_track_broker.signal();
        try{
            System.out.println("H : Sending horse " + id + " to stable...");
            //change horse state
            MyThreadHorses th = (MyThreadHorses) Thread.currentThread();
            th.horse_states = MyThreadHorses.Horse_States.AT_THE_STABLE;
            //change log
            log.setHorseState(id, MyThreadHorses.Horse_States.AT_THE_STABLE);
            log.changeLog();
        }finally{
            rl.unlock();
        }
                
    }
    
}