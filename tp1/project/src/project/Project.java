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
public class Project {

    /**
     * @param args the command line arguments
     */
    private static final int numRaces = 5;    
    private static final int numHorses = 4;
    private static final int numSpec = 4;
    private static final int track_length=100;
    
    @SuppressWarnings("CallToPrintStackTrace")
    public static void main(String[] args) {
        // TODO code application logic here
        for(int i = 0; i < numRaces; i++){
            System.out.println("Attempt: " + i);
            
            //create monitors;
            LogRepository log = new LogRepository(track_length);
            Stable stable = new Stable(log, numHorses);
            Stand stand = new Stand(log,numSpec);
            Paddock paddock = new Paddock(log, numHorses, numSpec);
            BettingCenter betting_center = new BettingCenter(log, numSpec);
            RaceTrack race_track = new RaceTrack(log, track_length, numHorses, numRaces);
            Repository repository = new Repository(log,numRaces);
            
            //Broker Thread Initialization;
            MyThreadBroker broker;
            
            broker = new MyThreadBroker(i, (Stable_Broker) stable, (Paddock_Broker) paddock, (BettingCenter_Broker) betting_center, (RaceTrack_Broker) race_track, (Repository_Broker) repository, (Stand_Broker) stand);
            broker.start();
            
            //Horse Threads Initialization;
            MyThreadHorses[] horse_threads = new MyThreadHorses[numHorses];
            for(int h = 0; h < numHorses; h++){
                horse_threads[h] = new MyThreadHorses(h, (Stable_Horses) stable, (Paddock_Horses) paddock, (RaceTrack_Horses) race_track, h + 50);
                horse_threads[h].start();
            }
            
            //Spectator Threads Initialization;
            MyThreadSpec[] spec_threads = new MyThreadSpec[numSpec];
            for(int s = 0; s < numSpec; s++){
                spec_threads[s] = new MyThreadSpec(s, (Stand_Spec) stand, (Paddock_Spec) paddock, (Repository_Spec) repository, (BettingCenter_Spec) betting_center, s + 150);
                spec_threads[s].start();
            }
                       
            //close threads;
            while(true){
                try{
                    broker.join();
                    for(MyThreadSpec spec : spec_threads){
                        spec.join();
                    }
                    for(MyThreadHorses horse : horse_threads){
                        horse.join();
                    }
                    break;
                }catch(InterruptedException e){
                    e.printStackTrace();
                }
            } 
            
               
        }
        
    }
    
}
