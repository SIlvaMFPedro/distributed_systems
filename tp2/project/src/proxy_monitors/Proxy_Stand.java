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
import interfaces.broker.Stand_Broker;
import interfaces.spectator.Stand_Spec;
import static java.lang.Thread.sleep;
import java.util.concurrent.locks.ReentrantLock;

/**
 *
 * @author pedro
 */
public class Proxy_Stand implements Stand_Broker, Stand_Spec{
    
    private String hostserverName;
    private int hostserverPort;
    
    
    /**
     * Proxy_Stand constructor.
     * @param hostserverName proxy_stand host server name.
     * @param hostserverPort proxy_stand host server port.
     */
    public Proxy_Stand(String hostserverName, int hostserverPort){
        this.hostserverName = hostserverName;
        this.hostserverPort = hostserverPort;
    }
    
    /**
     * Proxy_Stand goReportReulsts function.
     * Sends REQGOREPORTRESULTS message to stand server.
     * Receive RESPGOREPORTRESULTS message from stand server.
     */
    @Override
    public void goReportResults(){
        ClientCom cl_com = new ClientCom(CommPorts.standServerName, CommPorts.standServerPort);
        Message msg_in, msg_out;

        while(!cl_com.open()){
            try{
                sleep((long) (10));
            }catch(InterruptedException e){
                e.printStackTrace();
            }
        }
        msg_out = new Message(Message.REQGOREPORTRESULTS);
        cl_com.write(msg_out);
        msg_in = (Message) cl_com.read();

        if ((msg_in.getType () != Message.RESPGOREPORTRESULTS)){
            GenericIO.writelnString ("Thread  Broker Report Results: Invalid Type!");
            GenericIO.writelnString (msg_in.toString ());
            System.exit (1);
        }
        cl_com.close();
    }
    
    /**
     * Proxy_Stand entertain function.
     * Sends REQGOENTERTAIN message to stand server.
     * Receive RESPGOENTERTAIN message from stand server.
     * Change MyThreadBroker current state.
     */
    @Override
    public void entertain(){
        ClientCom cl_com = new ClientCom(CommPorts.standServerName, CommPorts.standServerPort);
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
            GenericIO.writelnString ("Thread  Broker Entertain: Invalid Type!");
            GenericIO.writelnString (msg_in.toString ());
            System.exit (1);
        }
        cl_com.close();
        //change broker state
        MyThreadBroker broker = (MyThreadBroker) Thread.currentThread();
        broker.broker_states = MyThreadBroker.Broker_States.PLAYING_HOST_AT_THE_BAR;
        
    }
    
    /**
     * Proxy_Stand honourBets function.
     * Sends REQHONOURBETS message to stand server.
     * Receive RESPHONOURBETNS message from stand server.
     * Change MyThreadBroker current state.
     */
    @Override
    public void honourBets(){
        ClientCom cl_com = new ClientCom(CommPorts.standServerName, CommPorts.standServerPort);
        Message msg_in, msg_out;

        while(!cl_com.open()){
            try{
                sleep((long) (10));
            }catch(InterruptedException e){
                e.printStackTrace();
            }
        }
        msg_out = new Message(Message.REQHONOURBETS);
        cl_com.write(msg_out);
        msg_in = (Message) cl_com.read();

        if ((msg_in.getType () != Message.RESPHONOURBETS)){
            GenericIO.writelnString ("Thread  Broker Honour Bets: Invalid Type!");
            GenericIO.writelnString (msg_in.toString ());
            System.exit (1);
        }
        cl_com.close();
        //change broker state
        MyThreadBroker broker = (MyThreadBroker) Thread.currentThread();
        broker.broker_states = MyThreadBroker.Broker_States.SETTLING_ACCOUNTS;
    }
    
    /**
     * Proxy_Stand horsesToPaddock function.
     * Sends REQSENDHORSESPAD message to stand server.
     * Receive RESPSENDHORSESPAD message from stand server.
     * Change MyThreadBroker current state.
     */
    @Override
    public void horsesToPaddock(){
        ClientCom cl_com = new ClientCom(CommPorts.standServerName, CommPorts.standServerPort);
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
            GenericIO.writelnString ("Thread  Broker Horses to paddock: Invalid Type!");
            GenericIO.writelnString (msg_in.toString ());
            System.exit (1);
        }
        cl_com.close();
        //change broker state
        MyThreadBroker broker = (MyThreadBroker) Thread.currentThread();
        broker.broker_states = MyThreadBroker.Broker_States.ANNOUNCING_NEXT_RACE;
    }
    
    /**
     * Proxy_Stand checkSpectators function.
     * Sends REQCHECKONSPEC message to stand server.
     * Receive RESPCHECKONSPEC message from stand server.
     * @return true if all spectators have reached the stand.
     */
    @Override
    public boolean checkSpectators(){
        ClientCom cl_com = new ClientCom(CommPorts.standServerName, CommPorts.standServerPort);
        Message msg_in, msg_out;

        while(!cl_com.open()){
            try{
                sleep((long) (10));
            }catch(InterruptedException e){
                e.printStackTrace();
            }
        }
        msg_out = new Message(Message.REQCHECKONSPEC);
        cl_com.write(msg_out);
        msg_in = (Message) cl_com.read();

        if ((msg_in.getType () != Message.RESPCHECKONSPEC)){
            GenericIO.writelnString ("Thread  Broker Check Spectators: Invalid Type!");
            GenericIO.writelnString (msg_in.toString ());
            System.exit (1);
        }
        cl_com.close();
        return msg_in.getCheckSpec();
    }
    
    /**
     * Proxy_Stand waitForNextRace
     * Sends REQWAITFORNEXTRACE message to stand server.
     * Receive RESPWAITFORNEXTRACE message from stand server.
     * Change MyThreadSpec current state.
     * @param spec_id spectator id.
     */
    @Override
    public void waitForNextRace(int spec_id){
        ClientCom cl_com = new ClientCom(CommPorts.standServerName, CommPorts.standServerPort);
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
           GenericIO.writelnString ("Thread Spectator " + spec_id + " Wait for next Race: Invalid Type!");
           GenericIO.writelnString (msg_in.toString ());
           System.exit (1);
        }
        cl_com.close();
        //change spectator state;
        MyThreadSpec spectator = (MyThreadSpec) Thread.currentThread();
        spectator.spec_states = MyThreadSpec.Spec_States.WAITING_FOR_A_RACE_TO_START;
    }
    
    /**
     * Proxy_Stand goCheckHorses function.
     * Sends REQGOCHECKHORSES message to stand server.
     * Receive RESPGOCHECKHORSES message from stand server.
     * Change MyThreadSpec current state.
     * @param spec_id spectator id.
     * @param currentMoney spectator current money in wallet.
     */
    @Override
    public void goCheckHorses(int spec_id, int currentMoney){
        ClientCom cl_com = new ClientCom(CommPorts.standServerName, CommPorts.standServerPort);
        Message msg_in, msg_out;

        while(!cl_com.open()){
            try{
                sleep((long) (10));
            }catch(InterruptedException e){
                e.printStackTrace();
            }
        }
        msg_out = new Message(Message.REQGOCHECKHORSES, spec_id, currentMoney);
        cl_com.write(msg_out);
        msg_in = (Message) cl_com.read();

        if ((msg_in.getType () != Message.RESPGOCHECKHORSES)){
            GenericIO.writelnString ("Thread Spectator " + spec_id + "Go Check Horses: Invalid Type!");
            GenericIO.writelnString (msg_in.toString ());
            System.exit (1);
        }
        cl_com.close();
        //change state of spectator
        MyThreadSpec spectator = (MyThreadSpec) Thread.currentThread();
        spectator.spec_states = MyThreadSpec.Spec_States.APPRAISING_THE_HORSES;
    }
    
    /**
     * Proxy_Stand watchingRace function.
     * Sends REQWATCHRACE message to stand server.
     * Receive RESPWATCHRACE message from stand server.
     * @param spec_id spectator id.
     */
    @Override
    public void watchingTheRace(int spec_id){
        ClientCom cl_com = new ClientCom(CommPorts.standServerName, CommPorts.standServerPort);
        Message msg_in, msg_out;

        while(!cl_com.open()){
            try{
                sleep((long) (10));
            }catch(InterruptedException e){
                e.printStackTrace();
            }
        }
        msg_out = new Message(Message.REQWATCHRACE, spec_id);
        cl_com.write(msg_out);
        msg_in = (Message) cl_com.read();

       if ((msg_in.getType () != Message.RESPWATCHRACE)){
           GenericIO.writelnString ("Thread Spectator " + spec_id + " Watching the race: Invalid Type!");
           GenericIO.writelnString (msg_in.toString ());
           System.exit (1);
       }
       cl_com.close();
        
    }
    
    /**
     * Proxy_Stand goCheckWin function.
     * Sends REQGOCHECKWIN message to stand server.
     * Receive RESPGOCHECKWIN message from stand server.
     * Change MyThreadSpec current state.
     * @param spec_id spectator id.
     */
    @Override
    public void goCheckWin(int spec_id){
        ClientCom cl_com = new ClientCom(CommPorts.standServerName, CommPorts.standServerPort);
        Message msg_in, msg_out;

        while(!cl_com.open()){
            try{
                sleep((long) (10));
            }catch(InterruptedException e){
                e.printStackTrace();
            }
        }
        msg_out = new Message(Message.REQGOCHECKWIN, spec_id);
        cl_com.write(msg_out);
        msg_in = (Message) cl_com.read();

        if ((msg_in.getType () != Message.RESPGOCHECKWIN)){
            GenericIO.writelnString ("Thread Spectator" + spec_id + " Go Check Win: Invalid Type!");
            GenericIO.writelnString (msg_in.toString ());
            System.exit (1);
        }
        cl_com.close();
        //change state of spectator
        MyThreadSpec spectator = (MyThreadSpec) Thread.currentThread();
        spectator.spec_states = MyThreadSpec.Spec_States.CHECKING_RESULTS;
    }
    
    /**
     * Proxy_Stand celebrate function.
     * Sends REQGORELAX message to stand server.
     * Receive RESPGORELAX message from stand server.
     * Change MyThreadSpec current state.
     * @param spec_id spectator id.
     */
    @Override
    public void celebrate(int spec_id){
        ClientCom cl_com = new ClientCom(CommPorts.standServerName, CommPorts.standServerPort);
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
            GenericIO.writelnString ("Thread Spectator " + spec_id + "Celebrate: Invalid Type!");
            GenericIO.writelnString (msg_in.toString ());
            System.exit (1);
        }
        cl_com.close();
        //change state of spectator
        MyThreadSpec spectator = (MyThreadSpec) Thread.currentThread();
        spectator.spec_states = MyThreadSpec.Spec_States.CELEBRATING;
    }
    
    /**
     * Proxy_Stand checkCelSpectators function.
     * Sends REQCHECKCELSPECTATORS message to stand server.
     * Receive RESPCHECKCELSPECTATORS message from stand server.
     * @return number of celebrating spectators in stand.
     */
    @Override
    public int checkCelSpectators(){
        ClientCom cl_com = new ClientCom(CommPorts.standServerName, CommPorts.standServerPort);
        Message msg_in, msg_out;
        
        while(!cl_com.open()){
            try{
                sleep((long) (10));
            }catch(InterruptedException e){
                e.printStackTrace();
            }
        }
        msg_out = new Message(Message.REQCHECKCELSPECTATORS);
        cl_com.write(msg_out);
        msg_in = (Message) cl_com.read();

        if ((msg_in.getType () != Message.RESPCHECKCELSPECTATORS)){
            GenericIO.writelnString ("Thread Broker check cel spectators: Invalid Type!");
            GenericIO.writelnString (msg_in.toString ());
            System.exit (1);
        }
        cl_com.close();
        return msg_in.getId();
    }
    
    /**
     * Proxy_Stand requestFinishLog function
     * Sends REQFINISH message to stand server.
     * Receive RESPFINISH message from stand server.
     */
    @Override
    public void requestFinishLog(){
        ClientCom cl_com = new ClientCom(CommPorts.standServerName, CommPorts.standServerPort);
        Message msg_in, msg_out;
        
        while(!cl_com.open()){
            try{
                sleep((long) (10));
            }catch(InterruptedException e){
                e.printStackTrace();
            }
        }
        msg_out = new Message(Message.REQFINISH);
        cl_com.write(msg_out);
        msg_in = (Message) cl_com.read();

        if ((msg_in.getType () != Message.RESPFINISH)){
            GenericIO.writelnString ("Thread Broker check cel spectators: Invalid Type!");
            GenericIO.writelnString (msg_in.toString ());
            System.exit (1);
        }
        cl_com.close();
    }
    
    /**
     * Proxy_Stand theEnd function.
     * ---------------------------------------------------
     * Sends RESPENDMONITORS to stable server.
     * Receive RESPENDMONITORS from stable server.
     * ---------------------------------------------------
     * Sends RESPENDMONITORS to paddock server.
     * Receive RESPENDMONITORS from paddock server.
     * ---------------------------------------------------
     * Sends RESPENDMONITORS to betting center server.
     * Receive RESPENDMONITORS from betting center server.
     * ---------------------------------------------------
     * Sends RESPENDMONITORS to racetrack server.
     * Receive RESPENDMONITORS from racetrack server.
     * ---------------------------------------------------
     * Sends RESPENDMONITORS to repository server.
     * Receive RESPENDMONITORS from repository server.
     * ---------------------------------------------------
     * Sends RESPENDMONITORS to log repository server.
     * Receive RESPENDMONITORS from log repository server.
     * ---------------------------------------------------
     * Sends RESPENDMONITORS to stand server.
     * Receive RESPENDMONITORS from stand server.
     * ---------------------------------------------------
     */
    @Override
    public void theEnd(){ 
        //Close Stable Monitor
        ClientCom cl_com = new ClientCom(CommPorts.stableServerName, CommPorts.stableServerPort);
        Message msg_in, msg_out;
        msg_out = new Message(Message.REQENDMONITORS);
        while(!cl_com.open()){
            try{
                sleep((long) (10));
            }catch(InterruptedException e){
                e.printStackTrace();
            }
        }
        cl_com.write(msg_out);
        msg_in = (Message) cl_com.read();
        if ((msg_in.getType () != Message.RESPENDMONITORS)){
           GenericIO.writelnString ("End of Stable monitor: Invalid Type!");
           GenericIO.writelnString (msg_in.toString ());
           System.exit (1);
        }
        cl_com.close();
        System.out.println("Stable Monitor End: " + msg_in.getEndMonitors());
        System.out.println("Stable monitor shutdown...");
        
        // Close Paddock Monitor
        cl_com = new ClientCom(CommPorts.paddockServerName, CommPorts.paddockServerPort);
        while(!cl_com.open()){
            try{
                sleep((long) (10));
            }catch(InterruptedException e){
                e.printStackTrace();
            }
        }
        cl_com.write(msg_out);
        msg_in = (Message) cl_com.read();
        if ((msg_in.getType () != Message.RESPENDMONITORS)){
           GenericIO.writelnString ("End of Paddock Monitor: Invalid Type!");
           GenericIO.writelnString (msg_in.toString ());
           System.exit (1);
        }
        cl_com.close();
        System.out.println("Paddock Monitor End: " + msg_in.getEndMonitors());
        System.out.println("Paddock monitor shutdown...");
        
        // Close Betting Center Monitor
        cl_com = new ClientCom(CommPorts.bettingCenterServerName, CommPorts.bettingCenterServerPort);
        while(!cl_com.open()){
            try{
                sleep((long) (10));
            }catch(InterruptedException e){
                e.printStackTrace();
            }
        }
        cl_com.write(msg_out);
        msg_in = (Message) cl_com.read();
        if ((msg_in.getType () != Message.RESPENDMONITORS)){
           GenericIO.writelnString ("End of Betting Center: Invalid Type!");
           GenericIO.writelnString (msg_in.toString ());
           System.exit (1);
        }
        cl_com.close();
        System.out.println("Betting Center End: " + msg_in.getEndMonitors());
        System.out.println("Betting Center monitor shutdown...");
        
        // Close RaceTrack Monitor
        cl_com = new ClientCom(CommPorts.racetrackServerName, CommPorts.racetrackServerPort);
        while(!cl_com.open()){
            try{
                sleep((long) (10));
            }catch(InterruptedException e){
                e.printStackTrace();
            }
        }
        cl_com.write(msg_out);
        msg_in = (Message) cl_com.read();
        if ((msg_in.getType () != Message.RESPENDMONITORS)){
           GenericIO.writelnString ("End of RaceTrack: Invalid Type!");
           GenericIO.writelnString (msg_in.toString ());
           System.exit (1);
        }
        cl_com.close();
        System.out.println("RaceTrack Monitor End: " + msg_in.getEndMonitors());
        System.out.println("RaceTrack Monitor shutdown...");
        
        // Close Repository Monitor
        cl_com = new ClientCom(CommPorts.repServerName, CommPorts.repServerPort);
        while(!cl_com.open()){
            try{
                sleep((long) (10));
            }catch(InterruptedException e){
                e.printStackTrace();
            }
        }
        cl_com.write(msg_out);
        msg_in = (Message) cl_com.read();
        if ((msg_in.getType () != Message.RESPENDMONITORS)){
           GenericIO.writelnString ("End of Repository: Invalid Type!");
           GenericIO.writelnString (msg_in.toString ());
           System.exit (1);
        }
        cl_com.close();
        System.out.println("Repository Monitor End: " + msg_in.getEndMonitors());
        System.out.println("Repository Monitor shutdown...");
          
         // Close LogRepository Monitor
        cl_com = new ClientCom(CommPorts.logRepServerName, CommPorts.logRepServerPort);
        while(!cl_com.open()){
              try{
                sleep((long) (10));
            }catch(InterruptedException e){
                e.printStackTrace();
            }
        }
        cl_com.write(msg_out);
        msg_in = (Message) cl_com.read();
        if ((msg_in.getType () != Message.RESPENDMONITORS)){
           GenericIO.writelnString ("End of LogRepository: Invalid Type!");
           GenericIO.writelnString (msg_in.toString ());
           System.exit (1);
        }
        cl_com.close();
        System.out.println("LogRepository Monitor End: " + msg_in.getEndMonitors());
        System.out.println("LogRepository Monitor shutdown...");
         
        // Close Stand Monitor
        cl_com = new ClientCom(CommPorts.standServerName, CommPorts.standServerPort);
        while(!cl_com.open()){
             try{
                 sleep((long) (10));
             }catch(InterruptedException e){
                 e.printStackTrace();
             }
         }
         cl_com.write(msg_out);
         msg_in = (Message) cl_com.read();

         if ((msg_in.getType () != Message.RESPENDMONITORS)){
             GenericIO.writelnString ("End of Stand: Invalid Type!");
             GenericIO.writelnString (msg_in.toString ());
             System.exit (1);
         }
         cl_com.close();
         System.out.println("Stand Monitor End: " + msg_in.getEndMonitors());
         System.out.println("Stand Monitor shutdown...");
    }
}
