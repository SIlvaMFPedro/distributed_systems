/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server_side.betting_center;

import aux.Bet;
import client_side.mythreadbroker.MyThreadBroker;
import client_side.mythreadspec.MyThreadSpec;
import commonInfo.RegistryConfig;
import commonInfo.VectorClock;
import interfaces.BettingCenter_Itf;
import interfaces.broker.BettingCenter_Broker;
import interfaces.registry.Register;
import interfaces.spectator.BettingCenter_Spec;
import java.rmi.AccessException;
import java.rmi.NoSuchObjectException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import java.util.logging.Logger;
import server_side.Log_Interface;
import server_side.logrepository.LogRepository;

/**
 *
 * @author pedro
 * @author franciscoteixeira
 */
public class Betting_Center implements BettingCenter_Itf {
    private final ReentrantLock rl;
    private final Condition bet_center_broker;
    private final Condition bet_center_spec;
    private boolean bet_wait = true;
    private boolean broker_bet = true;
    private int bet_count = 0;
    private boolean  bet_turn;
    private int spec_total;
    private final HashMap<Integer, Bet> horse_bets = new HashMap<Integer, Bet>();
    
    private boolean broker_ready;
    private boolean to_pay_flag;
    private int winner_count;
    private int betting_pool;
    private int prize_per_winner;
    private int relax_count;
    
    private String rmiRegHostName;
    private int rmiRegPortNumber;
    
    public Log_Interface log;
    public VectorClock vector_clk;
    
    /**
     * Betting Center constructor;
     * @param log log interface.
     * @param spec_total total number of spectators;
     */
    public Betting_Center(Log_Interface log, int spec_total, String rmiRegHostName, int rmiRegPortNumber){
        rl = new ReentrantLock(true);
        bet_center_broker = rl.newCondition();
        bet_center_spec = rl.newCondition();
        bet_turn = false;
        this.spec_total = spec_total;
        
        this.broker_ready=false;
        this.winner_count = 0;
        this.to_pay_flag = false;
        this.betting_pool = 0;
        this.prize_per_winner = 0;
        this.relax_count = 0;
        this.log = log;
        this.vector_clk = new VectorClock(spec_total+1);
        
        this.rmiRegHostName = rmiRegHostName;
        this.rmiRegPortNumber = rmiRegPortNumber;
    }
    
    //BROKER;
    /**
     * Broker waits for spectators to make their bets.
     */
    @Override
    public void waitForBets(VectorClock vc) throws RemoteException{
        rl.lock();
        broker_bet = true;
        bet_turn = true;
        try{
            System.out.print("B : Waiting for bets...\n");
            vector_clk.update(vc);
            try{
                while(broker_bet){
                    switch (bet_count) {
                        case 0:
                            System.out.print("B : BetCount -----> " + bet_count + "\n");
                            while(bet_wait){
                                bet_center_broker.await();
                            }
                            break;
                            //bet_count = 0; //reset bet counter;
                        case 4:
                            System.out.print("B : BetCount -----> " + bet_count + "\n");
                            System.out.println("B : All bets have been placed(Bet_Count = " + bet_count + ")");
                            //reset bet variables
                            bet_count = 0; //reset bet counter;
                            bet_wait = true;
                            bet_turn = false;
                            broker_bet = false;
                            break;
                        default:
                            System.out.print("B : BetCount -----> " + bet_count + "\n");
                            System.out.println("B : Waiting for all bets to be placed(Bet_Count = " + bet_count + ")");
                            bet_wait = true;
                            bet_turn = true;
                            bet_center_spec.signal();
                            while(bet_wait){
                                bet_center_broker.await();
                            }
                            //bet_count = 0; //reset bet counter;
                            break;
                    }
                }
            } catch (InterruptedException ex) {
                Logger.getLogger(Betting_Center.class.getName()).log(Level.SEVERE, null, ex);
            }
        }finally{
            rl.unlock();
        }
    }
    /**
     * Broker goes to the race track and waits for horses.
     */
    @Override
    public void goWaitForHorses(VectorClock vc) throws RemoteException{
        rl.lock();
        try{
            System.out.println("B : Going to racetrack to check on horses...");
            vector_clk.update(vc);
            //change broker state;
//            MyThreadBroker broker = (MyThreadBroker) Thread.currentThread();
//            broker.broker_states = MyThreadBroker.Broker_States.WAITING_FOR_HORSES;
            //change log;
            log.setBrokerState(MyThreadBroker.Broker_States.WAITING_FOR_HORSES);
            log.changeLog();
        }finally{
            rl.unlock();
        }
    }
    /**
     * Broker hands over the money to the winner spectators.
     */
    @Override
    public void settlingAccounts(VectorClock vc) throws RemoteException{
        rl.lock();
        try{          
            broker_ready=true;    
            while(winner_count!=0){                
                System.out.println("B : Settling accounts... " + winner_count + " Spectators to pay to"); 
                while(!to_pay_flag && winner_count!=0)
                    bet_center_broker.await();
                to_pay_flag=false; 
                broker_ready=true;
                if(winner_count!=0)                        
                    bet_center_spec.signal(); 
                               
            }
            broker_ready=false;            
            prize_per_winner=0;
            betting_pool=0;
            winner_count=0;
            vector_clk.update(vc);
            //change broker state
//            MyThreadBroker broker = (MyThreadBroker) Thread.currentThread();
//            broker.broker_states = MyThreadBroker.Broker_States.SETTLING_ACCOUNTS;
            //change log
            log.setBrokerState(MyThreadBroker.Broker_States.SETTLING_ACCOUNTS);
            log.changeLog();
        } catch (InterruptedException ex) {
            Logger.getLogger(Betting_Center.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            rl.unlock();
        }
        
    }
    /**
     * Broker goes back to the stable to send horses to paddock.
     */
    @Override
    public void horsesToPaddock(VectorClock vc) throws RemoteException{
        rl.lock();
        try{
            System.out.print("B : Going back to send horses back to paddock...\n");
            //change broker state
//            MyThreadBroker broker = (MyThreadBroker) Thread.currentThread();
//            broker.broker_states = MyThreadBroker.Broker_States.OPENING_THE_EVENT;
            vector_clk.update(vc);
            //change log
            log.setBrokerState(MyThreadBroker.Broker_States.OPENING_THE_EVENT);
            log.changeLog();
        }finally{
            rl.unlock();
        }
    }
    /**
     * At the end of all the races broker is playing host at the bar entertaining the guests.
     */
    @Override
    public void entertain(VectorClock vc) throws RemoteException{
        rl.lock();
        try{
            System.out.print("B : Going to entertain guests...\n");
            //change broker state
//            MyThreadBroker broker = (MyThreadBroker) Thread.currentThread();
//            broker.broker_states = MyThreadBroker.Broker_States.PLAYING_HOST_AT_THE_BAR;
            vector_clk.update(vc);
            //change logsch
            log.setBrokerState(MyThreadBroker.Broker_States.PLAYING_HOST_AT_THE_BAR);
            log.changeLog();
            
            bet_center_spec.signalAll(); //signal all spectators.
        }finally{
            rl.unlock();
        }
    }
    /**
     * At the end of all the races broker is playing host at the bar entertaining the guests.
     * @param a Array with th id of the winning horses.
     * @return Returns true if there's any winning bet. Returns False otherwise.
     */
    @Override
    public boolean areThereAnyWinners(int[] a, VectorClock vc) throws RemoteException {
        boolean b=false;
        rl.lock();
        try{
            vector_clk.update(vc);
            for(int i=0; i<horse_bets.size(); i++){
                for(int j=0; j<a.length;j++){
                    if(horse_bets.get(i).getHorse_id()==a[j])
                        winner_count++;
                }
            }            
            if(winner_count!=0){
                b=true;
                prize_per_winner = betting_pool/winner_count;
            }
        }finally{
            rl.unlock();
        }
        return b;
    }
    
    /**
     * Check number of relaxed spectators.
     * @return number of relaxed spectators.
     */
    @Override
    public int getRelaxCount(VectorClock vc) throws RemoteException{
        rl.lock();
        try{
            vector_clk.update(vc);
            System.out.println("Checking relax count: " + relax_count);
        }finally{
            rl.unlock();
        }
        return relax_count;
    }
    
    /**
     * Shutdown BettingCenter monitor
     */
    @Override
    @SuppressWarnings("CallToPrintStackTrace")
    public void terminate() throws RemoteException, AccessException{
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
            String nameEntryObj = RegistryConfig.bettingCenterNameEntry;
            
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
                System.out.println("Betting Center registration exception: " + e.getMessage());
                e.printStackTrace();
                System.exit(1);
            }catch(NotBoundException e){
                System.out.println("Betting Center not bound exception: " + e.getMessage());
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
            System.out.print("Betting Center module shutting down...\n");
        }finally{
            rl.unlock();
        }
    }
    
    //SPECTATOR
    /**
     * Spectator places the bet in betting center.
     * @param spec_id Spectator id.
     * @param horse_id Betting horse id.
     * @param bet_value Amount of money being bet.
     */
    @Override
    public void placeABet(int spec_id, int horse_id, int bet_value, int currentMoney, VectorClock vc) throws RemoteException{
        Bet aposta;
        rl.lock();
        System.out.print("S : User " + spec_id + " is placing his bet of " + bet_value + " on " + horse_id + "\n");
        //change log
        log.setSpectatorHorseSel(spec_id, horse_id);
        try{
            while(bet_turn == false){
                bet_center_spec.await();
            }
            aposta=new Bet(horse_id,bet_value);
            horse_bets.put(spec_id, aposta);
            betting_pool+=bet_value;
            bet_count++;
            bet_wait = false;
            bet_center_broker.signal(); //wake up broker
            bet_turn = false;
                     
            // pay up $$$
//            MyThreadSpec spectator = (MyThreadSpec) Thread.currentThread();
//            spectator.pay(bet_value);
            vector_clk.update(vc);
            //change log
            log.setSpectatorMoneyToBet(spec_id, horse_id, bet_value);  
            log.setSpectatorMoney(spec_id, currentMoney-bet_value);
            log.changeLog();
        } catch (InterruptedException ex) {
            Logger.getLogger(Betting_Center.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            rl.unlock();
        }
    }
    /**
     * Spectator goes to stand to watch the race.
     */
    @Override
    public void goWatchRace(int spec_id, VectorClock vc) throws RemoteException{
        rl.lock();
        try{
            System.out.print("S : Going to stand to watch the race...\n");
            //change spectator state
//            MyThreadSpec spectator = (MyThreadSpec) Thread.currentThread();
//            spectator.spec_states = MyThreadSpec.Spec_States.WATCHING_A_RACE;
            vector_clk.update(vc);
            //change log
            log.setSpectatorState(spec_id, MyThreadSpec.Spec_States.WATCHING_A_RACE);
            log.changeLog();            
        }finally{
            rl.unlock();
        }
        
    }
    /**
     * Spectator goes to collect his gains in the betting center.
     * @param id spectator id.
     */
    @Override
    public int collectGains(int id, int currentMoney, VectorClock vc) throws RemoteException{
        rl.lock();
        int receive_money=0;
        try{
            System.out.print("S : Spectator " + id + " is going to collect his gains...\n");
            to_pay_flag=true;
            while(!broker_ready)
                bet_center_spec.await();            
            System.out.print("S : Spectator " + id + " is getting paid...\n");
            winner_count--;
            broker_ready=false;
            to_pay_flag=true;
            bet_center_broker.signal();
            //collect $$$
//            MyThreadSpec spectator = (MyThreadSpec) Thread.currentThread();
            receive_money = Math.min(prize_per_winner,horse_bets.get(id).getValue()*spec_total);
//            spectator.receive(receive_money);
            //change spectator state
//            spectator.spec_states = MyThreadSpec.Spec_States.COLLECTING_THE_GAINS;
            vector_clk.update(vc);
            //change log
            log.setSpectatorState(id, MyThreadSpec.Spec_States.COLLECTING_THE_GAINS);
            log.setSpectatorMoney(id, currentMoney);
            log.changeLog();
        } catch (InterruptedException ex) {
            Logger.getLogger(Betting_Center.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            rl.unlock();
        }
        return receive_money;
    }
    /**
     * Spectator goes to stand to relax.
     */
    @Override
    public void goRelax(int spec_id, VectorClock vc) throws RemoteException{
        rl.lock();
        try{
            System.out.print("S : Going to relax...\n");
            //change spectator state
//            MyThreadSpec spectator = (MyThreadSpec) Thread.currentThread();
//            spectator.spec_states = MyThreadSpec.Spec_States.CELEBRATING;
            vector_clk.update(vc);
            //change log
            log.setSpectatorState(spec_id, MyThreadSpec.Spec_States.CELEBRATING);
            log.changeLog();
            relax_count++;
        }finally{
            rl.unlock();
        }
    }
    /**
     * Spectator waits for next race.
     */
    @Override
    public void waitForNextRace(int spec_id, VectorClock vc) throws RemoteException{
        rl.lock();
        try{
            System.out.print("S : Going to stand to wait for next race...\n");
            //change spectator state
//            MyThreadSpec spectator = (MyThreadSpec) Thread.currentThread();
//            spectator.spec_states = MyThreadSpec.Spec_States.WAITING_FOR_A_RACE_TO_START;  
            vector_clk.update(vc);
            //change log
            log.setSpectatorState(spec_id, MyThreadSpec.Spec_States.WAITING_FOR_A_RACE_TO_START);
            log.changeLog();
        }finally{
            rl.unlock();
        }
        
    }
}