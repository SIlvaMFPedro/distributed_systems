/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proxy_monitors;

import client_side.ClientCom;
import client_side.mythreadbroker.MyThreadBroker;
import client_side.mythreadspec.MyThreadSpec;
import commonInfo.CommPorts;
import commonInfo.Message;
import genclass.GenericIO;
import interfaces.broker.BettingCenter_Broker;
import interfaces.spectator.BettingCenter_Spec;
import static java.lang.Thread.sleep;
import java.util.concurrent.locks.ReentrantLock;

/**
 *
 * @author pedro
 */
public class Proxy_Betting_Center implements BettingCenter_Broker, BettingCenter_Spec{
    private String hostserverName;
    private int hostserverPort;
    
    /**
     * Proxy Betting Center constructor
     * @param hostserverName proxy host server name
     * @param hostserverPort proxy host server port
     */
    public Proxy_Betting_Center(String hostserverName, int hostserverPort){
        this.hostserverName = hostserverName;
        this.hostserverPort = hostserverPort;
    }
    
    /**
     * Proxy_Betting_Center waitForBets function.
     * Send REQWAITFORBETS message to betting center server.
     * Receive RESPWAITFORBETS message from betting center server.
     */
    @Override
    public void waitForBets(){
        ClientCom cl_com = new ClientCom(CommPorts.bettingCenterServerName, CommPorts.bettingCenterServerPort);
        Message msg_in, msg_out;

        while(!cl_com.open()){
            try{
                sleep((long) (10));
            }catch(InterruptedException e){
                e.printStackTrace();
            }
        }
        msg_out = new Message(Message.REQWAITFORBETS);
        cl_com.write(msg_out);
        msg_in = (Message) cl_com.read();

       if ((msg_in.getType () != Message.RESPWAITFORBETS)){
           GenericIO.writelnString ("Thread Broker Wait For Bets: Invalid Type!");
           GenericIO.writelnString (msg_in.toString ());
           System.exit (1);
       }
       cl_com.close();
    }
    /**
     * Proxy_Betting_Center goWaitForHorses function.
     * Send REQGOWAITFORHORSES message to betting center server.
     * Receive RESPGWAITFORHORSES message from betting center server.
     * Change MyThreadBroker state.
     */
    @Override
    public void goWaitForHorses(){
        ClientCom cl_com = new ClientCom(CommPorts.bettingCenterServerName, CommPorts.bettingCenterServerPort);
            Message msg_in, msg_out;

            while(!cl_com.open()){
                try{
                    sleep((long) (10));
                }catch(InterruptedException e){
                    e.printStackTrace();
                }
            }
            msg_out = new Message(Message.REQGOWAITFORHORSES);
            cl_com.write(msg_out);
            msg_in = (Message) cl_com.read();

           if ((msg_in.getType () != Message.RESPGOWAITFORHORSES)){
               GenericIO.writelnString ("Thread Broker Go wait for horses: Invalid Type!");
               GenericIO.writelnString (msg_in.toString ());
               System.exit (1);
           }
           cl_com.close();
           //change broker state
           MyThreadBroker broker = (MyThreadBroker) Thread.currentThread();
           broker.broker_states = MyThreadBroker.Broker_States.WAITING_FOR_HORSES;
    }
    
    /**
     * Proxy_Betting_Center settlingAccounts function.
     * Send REQSETTLEACCOUNTS message to betting center server.
     * Receive RESPSETTLEACCOUNTS message from betting center server.
     */
    @Override
    public void settlingAccounts(){
        ClientCom cl_com = new ClientCom(CommPorts.bettingCenterServerName, CommPorts.bettingCenterServerPort);
        Message msg_in, msg_out;

        while(!cl_com.open()){
            try{
                sleep((long) (10));
            }catch(InterruptedException e){
                e.printStackTrace();
            }
        }
        msg_out = new Message(Message.REQSETTLEACCOUNTS);
        cl_com.write(msg_out);
        msg_in = (Message) cl_com.read();

       if ((msg_in.getType () != Message.RESPSETTLEACCOUNTS)){
           GenericIO.writelnString ("Thread Broker Settling Accounts: Invalid Type!");
           GenericIO.writelnString (msg_in.toString ());
           System.exit (1);
       }
       cl_com.close();
       //change broker state
       //MyThreadBroker broker = (MyThreadBroker) Thread.currentThread();
       //broker.broker_states = MyThreadBroker.Broker_States.SETTLING_ACCOUNTS;
    }
    
    /**
     * Proxy_Betting_Center horsesToPaddock function.
     * Send REQSENDHORSESPAD message to betting center server.
     * Receive RESPHORSESPAD message from betting center server.
     * Change MyThreadBroker state.
     */
    @Override
    public void horsesToPaddock(){
        ClientCom cl_com = new ClientCom(CommPorts.bettingCenterServerName, CommPorts.bettingCenterServerPort);
        Message msg_in, msg_out;

        while(!cl_com.open()){
            try{
                sleep((long) (10));
            }catch(InterruptedException e){
                e.printStackTrace();
            }
        }
        msg_out = new Message(Message.REQSENDHORSESPAD);
        cl_com.write(msg_out);
        msg_in = (Message) cl_com.read();

       if ((msg_in.getType () != Message.RESPSENDHORSESPAD)){
           GenericIO.writelnString ("Thread Broker Horses To Paddock: Invalid Type!");
           GenericIO.writelnString (msg_in.toString ());
           System.exit (1);
       }           
       cl_com.close();
       //change broker state
       MyThreadBroker broker = (MyThreadBroker) Thread.currentThread();
       broker.broker_states = MyThreadBroker.Broker_States.OPENING_THE_EVENT;
    }
    
    /**
     * Proxy_Betting_Center entertain function.
     * Send REQGOENTERTAIN message to betting center server.
     * Receive RESPGOENTERTAIN message from betting center server.
     * Change MyThreadBroker state.
     */
    @Override
    public void entertain(){
        ClientCom cl_com = new ClientCom(CommPorts.bettingCenterServerName, CommPorts.bettingCenterServerPort);
        Message msg_in, msg_out;

        while(!cl_com.open()){
            try{
                sleep((long) (10));
            }catch(InterruptedException e){
                e.printStackTrace();
            }
        }
        msg_out = new Message(Message.REQGOENTERTAIN);
        cl_com.write(msg_out);
        msg_in = (Message) cl_com.read();

       if ((msg_in.getType () != Message.RESPGOENTERTAIN)){
           GenericIO.writelnString ("Thread Broker Entertain: Invalid Type!");
           GenericIO.writelnString (msg_in.toString ());
           System.exit (1);
       }
       cl_com.close();
       //change broker state
       MyThreadBroker broker = (MyThreadBroker) Thread.currentThread();
       broker.broker_states = MyThreadBroker.Broker_States.PLAYING_HOST_AT_THE_BAR;
    }
    
    /**
     * Proxy_Betting_Center areThereAnyWinners function.
     * Send REQARETHEREANYWINNERS message to betting center server.
     * Receive RESPARETHEREANYWINNERS Message to betting center server.
     * @param a race results
     * @return true if there is a race winner.
     */
    @Override
    public boolean areThereAnyWinners(int[] a){
       
        ClientCom cl_com = new ClientCom(CommPorts.bettingCenterServerName, CommPorts.bettingCenterServerPort);
        Message msg_in, msg_out; 
        while(!cl_com.open()){
            try{
                sleep((long) (10));
            }catch(InterruptedException e){
                e.printStackTrace();
            }
        }
        msg_out = new Message(Message.REQARETHEREANYWINNERS, a);
        cl_com.write(msg_out);
        msg_in = (Message) cl_com.read();

        if ((msg_in.getType () != Message.RESPARETHEREANYWINNERS)){
           GenericIO.writelnString ("Thread Broker Are There Any Winners: Invalid Type!");
           GenericIO.writelnString (msg_in.toString ());
           System.exit (1);
        }
        cl_com.close();
        return msg_in.getAreThereAnyWinners();
    }
    
    /**
     * Proxy_Betting_Center getRelaxCount function.
     * Sends REQCHECKRELAX message to betting center server.
     * Receive RESPCHECKRELAX message from betting center server.
     * @return relaxed spectator count.
     */
    @Override
    public int getRelaxCount(){
        ClientCom cl_com = new ClientCom(CommPorts.bettingCenterServerName, CommPorts.bettingCenterServerPort);
        Message msg_in, msg_out; 
        while(!cl_com.open()){
            try{
                sleep((long) (10));
            }catch(InterruptedException e){
                e.printStackTrace();
            }
        }
        msg_out = new Message(Message.REQCHECKRELAX);
        cl_com.write(msg_out);
        msg_in = (Message) cl_com.read();

        if ((msg_in.getType () != Message.RESPCHECKRELAX)){
           GenericIO.writelnString ("Thread Broker Get Relax Count: Invalid Type!");
           GenericIO.writelnString (msg_in.toString ());
           System.exit (1);
        }
        cl_com.close();
        return msg_in.getId();
    }
    
    /**
     * Proxy_Betting_Center placeABet function.
     * Sends REQPLACEABET message to betting center server.
     * Receive RESPPLACEABET message from betting center server.
     * @param spec_id spectator id.
     * @param horse_id horse id.
     * @param bet_value bet value.
     * @param currentMoney spectator current money.
     */
    @Override
    public void placeABet(int spec_id, int horse_id, int bet_value, int currentMoney){
        ClientCom cl_com = new ClientCom(CommPorts.bettingCenterServerName, CommPorts.bettingCenterServerPort);
        Message msg_in, msg_out;

        while(!cl_com.open()){
            try{
                sleep((long) (10));
            }catch(InterruptedException e){
                e.printStackTrace();
            }
        }
        msg_out = new Message(Message.REQPLACEABET, spec_id, horse_id, bet_value, currentMoney);
        cl_com.write(msg_out);
        msg_in = (Message) cl_com.read();

       if ((msg_in.getType () != Message.RESPPLACEABET)){
           GenericIO.writelnString ("Thread Spectator " + spec_id + "Place A Bet: Invalid Type!");
           GenericIO.writelnString (msg_in.toString ());
           System.exit (1);
       }
       cl_com.close();
       // pay up $$$
       MyThreadSpec spectator = (MyThreadSpec) Thread.currentThread();
       spectator.pay(bet_value);
    }
    
    /**
     * Proxy_Betting_Center goWatchRace function.
     * Sends REQGOWATCHRACE message to betting center server.
     * Receive RESPGOWATCHRACE message from betting center server.
     * @param spec_id spectator id.
     */
    @Override
    public void goWatchRace(int spec_id){
        ClientCom cl_com = new ClientCom(CommPorts.bettingCenterServerName, CommPorts.bettingCenterServerPort);
        Message msg_in, msg_out;

        while(!cl_com.open()){
            try{
                sleep((long) (10));
            }catch(InterruptedException e){
                e.printStackTrace();
            }
        }
        msg_out = new Message(Message.REQGOWATCHRACE, spec_id);
        cl_com.write(msg_out);
        msg_in = (Message) cl_com.read();

       if ((msg_in.getType () != Message.RESPGOWATCHRACE)){
           GenericIO.writelnString ("Thread Spectator " + spec_id + " Go Watch Race: Invalid Type!");
           GenericIO.writelnString (msg_in.toString ());
           System.exit (1);
       }
       cl_com.close();
       //change spectator state
       MyThreadSpec spectator = (MyThreadSpec) Thread.currentThread();
       spectator.spec_states = MyThreadSpec.Spec_States.WATCHING_A_RACE;
    }
    
    /**
     * Proxy_Betting_Center collectGains function.
     * Sends REQCOLLECTGAINS message to betting center server.
     * Receives RESPCOLLECTGAINS message from betting center server.
     * Change MyThreadSpec current state.
     * @param spec_id spectator id.
     * @param currentMoney spectator current money in wallet.
     * @return 0.
     */
    @Override
    public int collectGains(int spec_id, int currentMoney){
        ClientCom cl_com = new ClientCom(CommPorts.bettingCenterServerName, CommPorts.bettingCenterServerPort);
        Message msg_in, msg_out;

        while(!cl_com.open()){
            try{
                sleep((long) (10));
            }catch(InterruptedException e){
                e.printStackTrace();
            }
        }
        msg_out = new Message(Message.REQCOLLECTGAINS, spec_id, currentMoney);
        cl_com.write(msg_out);
        msg_in = (Message) cl_com.read();

       if ((msg_in.getType () != Message.RESPCOLLECTGAINS)){
           GenericIO.writelnString ("Thread Spectator " + spec_id + " Collect Gains: Invalid Type!");
           GenericIO.writelnString (msg_in.toString ());
           System.exit (1);
       }
       cl_com.close();
       //collect $$$
       MyThreadSpec spectator = (MyThreadSpec) Thread.currentThread();
       spectator.receive(msg_in.getAddedInfo());
       //change spectator state
       spectator.spec_states = MyThreadSpec.Spec_States.COLLECTING_THE_GAINS;
           
       return 0;
    }
    
    /**
     * Proxy_Betting_Center goRelax function.
     * Sends REQGORELAX message to betting center server.
     * Receive RESPGORELAX message from betting center server.
     * Change MyThreadSpec current state.
     * @param spec_id spectator id.
     */
    @Override
    public void goRelax(int spec_id){
        ClientCom cl_com = new ClientCom(CommPorts.bettingCenterServerName, CommPorts.bettingCenterServerPort);
        Message msg_in, msg_out;

        while(!cl_com.open()){
            try{
                sleep((long) (10));
            }catch(InterruptedException e){
                e.printStackTrace();
            }
        }
        msg_out = new Message(Message.REQGORELAX, spec_id);
        cl_com.write(msg_out);
        msg_in = (Message) cl_com.read();

       if ((msg_in.getType () != Message.RESPGORELAX)){
           GenericIO.writelnString ("Thread Spectator " + spec_id + " Go Relax: Invalid Type!");
           GenericIO.writelnString (msg_in.toString ());
           System.exit (1);
       }
       cl_com.close();
       //change spectator state
       MyThreadSpec spectator = (MyThreadSpec) Thread.currentThread();
       spectator.spec_states = MyThreadSpec.Spec_States.CELEBRATING;
    }
    
    /**
     * Proxy_Betting_Center waitForNextRace function.
     * Sends REQWAITFORNEXTRACE message to betting center server.
     * Receive RESPWAITFORNEXTRACE message from betting center server.
     * Change MyThreadSpec current state.
     * @param spec_id spectator id.
     */
    @Override
    public void waitForNextRace(int spec_id){
        ClientCom cl_com = new ClientCom(CommPorts.bettingCenterServerName, CommPorts.bettingCenterServerPort);
        Message msg_in, msg_out;

        while(!cl_com.open()){
            try{
                sleep((long) (10));
            }catch(InterruptedException e){
                e.printStackTrace();
            }
        }
        msg_out = new Message(Message.REQWAITFORNEXTRACE, spec_id);
        cl_com.write(msg_out);
        msg_in = (Message) cl_com.read();

       if ((msg_in.getType () != Message.RESPWAITFORNEXTRACE)){
           GenericIO.writelnString ("Thread Spectator " + spec_id + " Wait For Next Race: Invalid Type!");
           GenericIO.writelnString (msg_in.toString ());
           System.exit (1);
       }
       cl_com.close();
       //change spectator state
       MyThreadSpec spectator = (MyThreadSpec) Thread.currentThread();
       spectator.spec_states = MyThreadSpec.Spec_States.WAITING_FOR_A_RACE_TO_START; 
    }
}
