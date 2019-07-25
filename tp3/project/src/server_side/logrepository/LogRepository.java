/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server_side.logrepository;


import client_side.mythreadbroker.MyThreadBroker;
import client_side.mythreadhorses.MyThreadHorses;
import client_side.mythreadspec.MyThreadSpec;
import commonInfo.RegistryConfig;
import interfaces.registry.Register;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.concurrent.locks.ReentrantLock;
import static java.lang.System.*;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.rmi.NoSuchObjectException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import server_side.Log_Interface;
import server_side.betting_center.Betting_Center;
/**
 *
 * @author pedro
 * @author franciscoteixeira
 */
public class LogRepository implements Log_Interface{
    private final ReentrantLock rl;
    private MyThreadBroker.Broker_States broker_state;      //manager/broker state;
    private MyThreadSpec.Spec_States[] spec_states;         //spectator/better state (# - 0 .. 3)
    private MyThreadHorses.Horse_States[] horse_states;     //horse/jockey pair state in present race (# - 0 .. 3)
    private int[] spec_money;                               //spectator/better amount of money has presently (# - 0 .. 3)
    private int[] horse_length;                             //horse/jockey pair maximum moving length per iteration step in present race (# - 0 .. 3)               
    private int[] spec_horse_sel;                           //spectator/better bet selection in present race (# - 0 .. 3)
    private int[] spec_money_to_bet;                        //spectator/better bet amount in present race (# - 0 .. 3)
    private int[] horse_odds;                               //horse/jockey pair winning probability in present race (# - 0 .. 3)
    private int[] horse_it;                                 //horse/jockey pair iteration step number in present race (# - 0 .. 3)
    private int[] horse_pos;                                //horse/jockey pair track position in present race (# - 0 .. 3)
    private int[] horse_at_the_end_race;                    //horse/jockey pair standing at the end of present race (# - 0 .. 3)
    private int race_number;                                //race number
    private int race_track_distance;                        //race track distance in present race;
    
    private String rmiRegHostName;
    private int rmiRegPortNumber;
    
    PrintWriter pw;
    
    /**
     * LogRepository constructor;
     * @param race_track_distance race track distance; 
     */
    public LogRepository(int race_track_distance, String rmiRegHostName, int rmiRegPortNumber){
        this.rmiRegHostName = rmiRegHostName;
        this.rmiRegPortNumber = rmiRegPortNumber;
        rl = new ReentrantLock(true);
        broker_state = MyThreadBroker.Broker_States.START_THE_EVENT;
        spec_states = new MyThreadSpec.Spec_States[4];
        Arrays.fill(spec_states, MyThreadSpec.Spec_States.WAITING_FOR_A_RACE_TO_START);
        horse_states = new MyThreadHorses.Horse_States[4];
        Arrays.fill(horse_states, MyThreadHorses.Horse_States.AT_THE_STABLE);
        spec_money = new int[4];
        Arrays.fill(spec_money, 0);
        horse_length = new int[4];
        Arrays.fill(horse_length, 0);
        spec_horse_sel = new int[4];
        Arrays.fill(spec_horse_sel, 0);
        spec_money_to_bet = new int[4];
        Arrays.fill(spec_money_to_bet, 0);
        horse_odds = new int[4];
        Arrays.fill(horse_odds, 0);
        horse_it = new int[4];
        Arrays.fill(horse_it, 0);
        horse_pos = new int[4];
        Arrays.fill(horse_pos, 0);
        horse_at_the_end_race = new int[4];
        Arrays.fill(horse_at_the_end_race, 0);
        
        this.race_track_distance = race_track_distance;
        
        try{
            pw = new PrintWriter(new File("RaceTrackLog.txt"), "UTF-8");
        }catch(FileNotFoundException | UnsupportedEncodingException e){
            
        }
    }
    /**
     * Print first part of log information;
     */
    @Override
    public void printFirst(){
        rl.lock();
        try{
            out.println("            AFTERNOON AT THE RACE TRACK - Description of the internal state of the problem             ");
            out.println();
            out.println("MAN/BRK              SPECTATOR/BETTER                  HORSE/JOCKEY PAIR at Race RN                    ");
            out.println(" Stat St0      Am0 St1 Am1  St2 Am2  St3 Am3  RN  St0 Len0  St1 Len1  St2 Len2  St3 Len3               ");
            out.println("                                         Race RN Status                                                ");
            out.println(" RN Dist BS0 BA0 BS1 BA1 BS2 BA2 BS3 BA3 Od0 N0 Ps0 SD0 Od1 N1 Ps1 Sd1 Od2 N2 Ps2 Sd2 Od3 N3 Ps3 St3   ");
            out.println(" #  #### ### ### ### ### ### ### ### ### ### ## ### ### ### ## ### ### ### ## ### ### ### ## ### ###   ");
            out.println(" #  #### ### ### ### ### ### ### ### ### ### ## ### ### ### ## ### ### ### ## ### ### ### ## ### ###   ");
            out.println();
            out.println("Legend:");
            out.println("Stat - manager/broker state");
            out.println("St# - spectator/better state (# - 0 .. 3)");
            out.println("Am# - spectator/better amount of money she has presently (# - 0 .. 3)");
            out.println("RN - race number");
            out.println("St# - horse/jockey pair state in present race (# - 0 .. 3)");
            out.println("Len# - horse/jockey pair maximum moving length per iteration step in present race (# - 0 .. 3)");
            out.println("RN - race number");
            out.println("Dist - race track distance in present race");
            out.println("BS# - spectator/better bet selection in present race (# - 0 .. 3)");
            out.println("BA# - spectator/better bet amount in present race (# - 0 .. 3)");
            out.println("Od# - horse/jockey pair winning probability in present race (# - 0 .. 3)");
            out.println("N# - horse/jockey pair iteration step number in present race (# - 0 .. 3)");
            out.println("Ps# - horse/jockey pair track position in present race (# - 0 .. 3)");
            out.println("SD# - horse/jockey pair standing at the end of present race (# - 0 .. 3)");
            out.println("STEv - START_THE_EVENT state (broker)"); 
            out.println("OTEv - OPENING_THE_EVENT state (broker)"); 
            out.println("AnNR - ANNOUNCING_NEXT_RACE state (broker)");
            out.println("WaFB - WAITING_FOR_BETS state (broker)");
            out.println("WaFH - WAITING_FOR_HORSES state (broker)");
            out.println("SuTR - SUPERVISING_THE_RACE state (broker)");
            out.println("RRes - REPORTING_RESULTS state (broker)");
            out.println("SACc - SETTLING_ACCOUNTS state (broker)");
            out.println("PHAB - START_THE_EVENT state (broker)");
            out.println("STEv - PLAYING_HOST_AT_THE_BAR state (broker)");
            out.println("WRS - WAITING_FOR_A_RACE_TO_START state (spectator)");
            out.println("ATH - APPRAISING_THE_HORSES state (spectator)");
            out.println("PAB - PLACING_A_BET state (spectator)");
            out.println("WAR - WATCHING_A_RACE state (spectator)");
            out.println("CRE - CHECKING_RESULTS state (spectator)");
            out.println("CTG - COLLECTING_THE_GAINS state (spectator)");
            out.println("CEL - CELEBRATING state (spectator)");
            out.println("ATS - AT_THE_STABLE state (horse)");
            out.println("ATP - AT_THE_PADDOCK state (horse)");
            out.println("ASL - AT_THE_START_LINE state (horse)");
            out.println("RUN - RUNNING state (horse)");
            out.println("AFL - AT_THE_FINISH_LINE state (horse)");
            out.println("NDS - NOT_DEFINED_STATE (broker/spectator/horse)");

            pw.println("            AFTERNOON AT THE RACE TRACK - Description of the internal state of the problem             ");
            pw.println();
            pw.println("MAN/BRK              SPECTATOR/BETTER                  HORSE/JOCKEY PAIR at Race RN                    ");
            pw.println(" Stat St0      Am0 St1 Am1  St2 Am2  St3 Am3  RN  St0 Len0  St1 Len1  St2 Len2  St3 Len3               ");
            pw.println("                                         Race RN Status                                                ");
            pw.println(" RN Dist BS0 BA0 BS1 BA1 BS2 BA2 BS3 BA3 Od0 N0 Ps0 SD0 Od1 N1 Ps1 Sd1 Od2 N2 Ps2 Sd2 Od3 N3 Ps3 St3   ");
            pw.println(" #  #### ### ### ### ### ### ### ### ### ### ## ### ### ### ## ### ### ### ## ### ### ### ## ### ###   ");
            pw.println(" #  #### ### ### ### ### ### ### ### ### ### ## ### ### ### ## ### ### ### ## ### ### ### ## ### ###   ");
            pw.println();
            pw.println("Legend:");
            pw.println("Stat - manager/broker state");
            pw.println("St# - spectator/better state (# - 0 .. 3)");
            pw.println("Am# - spectator/better amount of money she has presently (# - 0 .. 3)");
            pw.println("RN - race number");
            pw.println("St# - horse/jockey pair state in present race (# - 0 .. 3)");
            pw.println("Len# - horse/jockey pair maximum moving length per iteration step in present race (# - 0 .. 3)");
            pw.println("RN - race number");
            pw.println("Dist - race track distance in present race");
            pw.println("BS# - spectator/better bet selection in present race (# - 0 .. 3)");
            pw.println("BA# - spectator/better bet amount in present race (# - 0 .. 3)");
            pw.println("Od# - horse/jockey pair winning probability in present race (# - 0 .. 3)");
            pw.println("N# - horse/jockey pair iteration step number in present race (# - 0 .. 3)");
            pw.println("Ps# - horse/jockey pair track position in present race (# - 0 .. 3)");
            pw.println("SD# - horse/jockey pair standing at the end of present race (# - 0 .. 3)");  
            pw.println("STEv - START_THE_EVENT state (broker)"); 
            pw.println("OTEv - OPENING_THE_EVENT state (broker)"); 
            pw.println("AnNR - ANNOUNCING_NEXT_RACE state (broker)");
            pw.println("WaFB - WAITING_FOR_BETS state (broker)");
            pw.println("WaFH - WAITING_FOR_HORSES state (broker)");
            pw.println("SuTR - SUPERVISING_THE_RACE state (broker)");
            pw.println("RRes - REPORTING_RESULTS state (broker)");
            pw.println("SACc - SETTLING_ACCOUNTS state (broker)");
            pw.println("PHAB - START_THE_EVENT state (broker)");
            pw.println("STEv - PLAYING_HOST_AT_THE_BAR state (broker)");
            pw.println("WRS - WAITING_FOR_A_RACE_TO_START state (spectator)");
            pw.println("ATH - APPRAISING_THE_HORSES state (spectator)");
            pw.println("PAB - PLACING_A_BET state (spectator)");
            pw.println("WAR - WATCHING_A_RACE state (spectator)");
            pw.println("CRE - CHECKING_RESULTS state (spectator)");
            pw.println("CTG - COLLECTING_THE_GAINS state (spectator)");
            pw.println("CEL - CELEBRATING state (spectator)");
            pw.println("ATS - AT_THE_STABLE state (horse)");
            pw.println("ATP - AT_THE_PADDOCK state (horse)");
            pw.println("ASL - AT_THE_START_LINE state (horse)");
            pw.println("RUN - RUNNING state (horse)");
            pw.println("AFL - AT_THE_FINISH_LINE state (horse)");
            pw.println("NDS - NOT_DEFINED_STATE (broker/spectator/horse)");
        }finally{
            rl.unlock();
        }
    }
    /**
     * Update log information;
     */
    @Override
    public void changeLog(){
        rl.lock();
        try{
            int rn = getRaceNumber();
            int rd = getTravelDistance();
            pw.println("\nMAN/BRK              SPECTATOR/BETTER                  HORSE/JOCKEY PAIR at Race R" + rn + "         ");
            pw.println(" Stat           St0 Am0  St1 Am1  St2 Am2  St3 Am3  RN  St0 Len0  St1 Len1  St2 Len2  St3 Len3               ");
            pw.printf(" %s           %s %04d %s %04d %s %04d %s %04d  %01d  %s %02d    %s %02d    %s %02d    %s %02d   \n", getBrokerState(broker_state), getSpectatorState(spec_states[0]), spec_money[0], getSpectatorState(spec_states[1]), spec_money[1], getSpectatorState(spec_states[2]), spec_money[2], getSpectatorState(spec_states[3]), spec_money[3], rn, getHorseState(horse_states[0]), horse_length[0], getHorseState(horse_states[1]), horse_length[1], getHorseState(horse_states[2]), horse_length[2], getHorseState(horse_states[3]), horse_length[3]);
            pw.println("                                         Race RN Status                                                ");
            pw.println(" RN Dist BS0 BA0  BS1 BA1  BS2 BA2  BS3 BA3  Od0  N0  Ps0 SD0 Od1  N1 Ps1 Sd1 Od2  N2 Ps2 Sd2 Od3  N3 Ps3 St3   ");
            pw.printf("  %01d %02d    %01d %04d   %01d %04d   %01d %04d   %01d %04d %04d %02d   %02d   %01d %04d %02d  %02d   %01d %04d %02d  %02d   %01d %04d %02d  %02d   %01d   \n", rn, rd, spec_horse_sel[0], spec_money_to_bet[0], spec_horse_sel[1], spec_money_to_bet[1], spec_horse_sel[2], spec_money_to_bet[2], spec_horse_sel[3], spec_money_to_bet[3], horse_odds[0], horse_it[0], horse_pos[0], horse_at_the_end_race[0], horse_odds[1], horse_it[1], horse_pos[1], horse_at_the_end_race[1], horse_odds[2], horse_it[2], horse_pos[2], horse_at_the_end_race[2], horse_odds[3], horse_it[3], horse_pos[3], horse_at_the_end_race[3]);        
        }finally{
            rl.unlock();
        }
    }
    @Override
    public int getRaceNumber(){
        return this.race_number;
    }
    @Override
    public int getTravelDistance(){
        return this.race_track_distance;
    }
    /**
     * Get Broker State;
     * @param state Current state of the broker;
     * @return Returns a String in a format ready for the log;
     */
    public String getBrokerState(MyThreadBroker.Broker_States state){
        String broker_stat;
        switch(state.toString()){
            case "START_THE_EVENT":
                broker_stat = "STEv";
                break;
            case "OPENING_THE_EVENT":
                broker_stat = "OTEv";
                break;
            case "ANNOUNCING_NEXT_RACE":
                broker_stat = "AnNR";
                break;
            case "WAITING_FOR_BETS":
                broker_stat = "WaFB";
                break;
            case "WAITING_FOR_HORSES":
                broker_stat = "WaFH";
                break;
            case "SUPERVISING_THE_RACE":
                broker_stat = "SuTR";
                break;
            case "REPORTING_RESULTS":
                broker_stat = "RRes";
                break;
            case "SETTLING_ACCOUNTS":
                broker_stat = "SACc";
                break;
            case "PLAYING_HOST_AT_THE_BAR":
                broker_stat = "PHAB";
                break;
            default:
                broker_stat = "NDS";
                break;
        }
        return broker_stat;
    }
    /**
     * Get Spectator State;
     * @param state Current state of the spectator;
     * @return Returns a String in a format ready for the log;
     */
    public String getSpectatorState(MyThreadSpec.Spec_States state){
        String spec_state;
        switch(state.toString()){
            case "WAITING_FOR_A_RACE_TO_START":
                spec_state = "WRS";
                break;
            case "APPRAISING_THE_HORSES":
                spec_state = "ATH";
                break;
            case "PLACING_A_BET":
                spec_state = "PAB";
                break;
            case "WATCHING_A_RACE":
                spec_state = "WAR";
                break;
            case "CHECKING_RESULTS":
                spec_state = "CRE";
                break;
            case "COLLECTING_THE_GAINS":
                spec_state = "CTG";
                break;
            case "CELEBRATING":
                spec_state = "CEL";
                break;
            default:
                spec_state = "NDS";
                break;
        }
        return spec_state;
    }
    /**
     * Get Horse State;
     * @param state current state of the horse;
     * @return Returns a String in a format ready for the log;
     */
    public String getHorseState(MyThreadHorses.Horse_States state){
        String horse_state;// = new String();
        switch(state.toString()){
            case "AT_THE_STABLE":
                horse_state = "ATS";
                break;
            case "AT_THE_PADDOCK":
                horse_state = "ATP";
                break;
            case "AT_THE_START_LINE":
                horse_state = "ASL";
                break;
            case "RUNNING":
                horse_state = "RUN";
                break;
            case "AT_THE_FINISH_LINE":
                horse_state = "AFL";
                break;
            default:
                horse_state = "NDS";
                break;
        }
        return horse_state;
    }
    /**
     * Set Broker state.
     * @param state broker state.
     */
    @Override
    public void setBrokerState(MyThreadBroker.Broker_States state){
        this.broker_state = state;
        //System.out.print("B : BROKER STATE -> " + this.broker_state.toString() + "\n"); //uncomment to test states.
    }
    /**
     * Set Spectator State
     * @param spec_id spectator id.
     * @param state spectator state.
     */
    @Override
    public void setSpectatorState(int spec_id, MyThreadSpec.Spec_States state){
        this.spec_states[spec_id] = state;
        //System.out.print("S : SPECTATOR " + spec_id + " STATE -> " + this.spec_states[spec_id].toString() + "\n"); //uncoment to test states;
    }
    /**
     * Set Spectator Money
     * @param spec_id spectator id.
     * @param money spectator money.
     */
    @Override
    public void setSpectatorMoney(int spec_id, int money){
        this.spec_money[spec_id] = money;
        //System.out.print("S : SPECTATOR " + spec_id + " RECEIVED " + this.spec_money[spec_id] + "$$$\n"); //uncoment to test states;
    }
    /**
     * Set Spectator Money to bet
     * @param spec_id spectator id.
     * @param horse_id horse id.
     * @param money_to_bet spectator money to bet.
     */
    @Override
    public void setSpectatorMoneyToBet(int spec_id, int horse_id, int money_to_bet){
        this.spec_money_to_bet[spec_id] = money_to_bet;
        //System.out.print("S : SPECTATOR " + spec_id + " BETTED " + money_to_bet + " $$$ ON HORSE " + horse_id + "\n"); //uncoment to test states;
    }
    /**
     * Set Spectator Horse Selection.
     * @param spec_id spectator id.
     * @param horse_id horse id.
     */
    @Override
    public void setSpectatorHorseSel(int spec_id, int horse_id){
        this.spec_horse_sel[spec_id] = horse_id;
        //System.out.print("S : SPECTATOR " + spec_id + " SELECTED HORSE " + horse_id + "\n"); //uncoment to test states;
    }
    /**
     * Set Horse State.
     * @param horse_id horse id.
     * @param state horse state.
     */
    @Override
    public void setHorseState(int horse_id, MyThreadHorses.Horse_States state){
        this.horse_states[horse_id] = state;
        //System.out.print("H : HORSE " + horse_id + " STATE -> " + this.horse_states[horse_id].toString() + "\n"); //uncoment to test states;
    }
    /**
     * Set Horse Length.
     * @param horse_id horse id.
     * @param length horse length.
     */
    @Override
    public void setHorseLength(int horse_id, int length){
        this.horse_length[horse_id] = length;
        //System.out.print("H : HORSE " + horse_id + " LENGTH -> " + this.horse_length[horse_id] + "\n"); //uncoment to test states;
    }
    /**
     * Set Horse Odds.
     * @param horse_id horse id.
     * @param odds horse odds.
     */
    @Override
    public void setHorseOdds(int horse_id, int odds){
        this.horse_odds[horse_id] = odds;
        //System.out.print("H : HORSE " + horse_id + " ODDS -> " + this.horse_odds[horse_id] + "\n"); //uncoment to test states;
    }
    /**
     * Set Horse Iteration.
     * @param horse_id horse id.
     * @param horse_it horse iteration.
     */
    @Override
    public void setHorseIT(int horse_id, int horse_it){
        this.horse_it[horse_id] = horse_it;
        //System.out.print("H : HORSE " + horse_id + " STEP_IT -> " + this.horse_it[horse_id] + "\n"); //uncoment to test states;
    }
    /**
     * Set Horse Placement Position;
     * @param horse_p array with horse position in the present race;
     */
    @Override
    public void setHorsePos(int[] horse_p){
        //arraycopy(horse_p, 0, horse_pos, 0, horse_p.length);
        horse_pos = horse_p;
        //for(int i = 0; i < horse_pos.length; i++){
        //    System.out.print("H : HORSE " + i + " POS -> " + this.horse_pos[i] + "\n"); //uncoment to test states;
        //}
        
    }
    /**
     * Horses at the end of race;
     * @param positions Standing of the horses at the end of the race;
     */
    @Override
    public void setHorsesAtTheEnd(int[] positions){
        arraycopy(positions, 0, horse_at_the_end_race, 0, positions.length);
        
        //for(int i = 0; i < horse_at_the_end_race.length; i++){
        //    System.out.print("H : HORSE " + i + " END -> " + this.horse_at_the_end_race[i] + "\n"); //uncoment to test states;
        //}
    }
    /**
     * Set Race Number
     * @param currentRace Number of the present race
     */
    @Override
    public void setRaceNumber(int currentRace){
        race_number = currentRace;
        //System.out.print("B : RACE NUMBER -> " + race_number + "\n"); //uncoment to test states;
    }
    /**
     * Finish writing in log;
     */
    @Override
    public void finishLog() {
        pw.close();
        terminate();
    }
    @Override
    @SuppressWarnings("CallToPrintStackTrace")
    public void terminate(){
        rl.lock();
        try{
            Register reg  = null;
            Registry registry = null;
            
            String rmiRegHostName = new String();
            int rmiRegPortNumber;
            
            rmiRegHostName = this.rmiRegHostName;
            rmiRegPortNumber = this.rmiRegPortNumber;
            
            try{
                registry = LocateRegistry.getRegistry(rmiRegHostName, rmiRegPortNumber);
            }catch(RemoteException e){
                System.out.println("Cannot find registry");
                e.printStackTrace();
                System.exit(1);
            }
            
           
            String nameEntryBase = RegistryConfig.rmiRegisterName;
            String nameEntryObj = RegistryConfig.logRepositoryNameEntry;
            
            try{
                reg = (Register) registry.lookup(nameEntryBase);
            }catch(RemoteException e){
                System.out.println("RegisterRemoteObject lookup exception: " + e.getMessage());
                e.printStackTrace();
                System.exit(1);
            } catch (NotBoundException e) {
                Logger.getLogger(Betting_Center.class.getName()).log(Level.SEVERE, null, e);
                System.out.println("RegisterRemoteObject lookup exception: " + e.getMessage());
                e.printStackTrace();
                System.exit(1);
            }
            
            try{
                //unregister monitor
                reg.unbind(nameEntryObj);
            }catch(RemoteException e){
                System.out.println("LogRepository registration exception: " + e.getMessage());
                e.printStackTrace();
                System.exit(1);
            }catch(NotBoundException e){
                System.out.println("LogRepository not bound exception: " + e.getMessage());
                e.printStackTrace();
                System.exit(1);
            }
            
            try{
                //Unexport : this will also remove Betting Center monitor from RMI runtime;
                UnicastRemoteObject.unexportObject(this, true);
            }catch(NoSuchObjectException e){
                e.printStackTrace();
                System.exit(1);
            }
            System.out.print("LogRepository module shutting down...\n");
        }finally{
            rl.unlock();
        }
    } 
    
}