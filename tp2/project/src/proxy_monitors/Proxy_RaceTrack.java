/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proxy_monitors;

import client_side.ClientCom;
import client_side.mythreadbroker.MyThreadBroker;
import client_side.mythreadhorses.MyThreadHorses;
import commonInfo.CommPorts;
import commonInfo.Message;
import genclass.GenericIO;
import interfaces.broker.RaceTrack_Broker;
import interfaces.horses.RaceTrack_Horses;
import static java.lang.Thread.sleep;
import java.util.concurrent.locks.ReentrantLock;

/**
 *
 * @author pedro
 */
public class Proxy_RaceTrack implements RaceTrack_Broker, RaceTrack_Horses{
    
    private String hostserverName;
    private int hostserverPort;
    
    
    /**
     * Proxy_RsceTrack constructor.
     * @param hostserverName proxy_racetrack host server name.
     * @param hostserverPort proxy_racetrack host server port.
     */
    public Proxy_RaceTrack(String hostserverName, int hostserverPort){
        this.hostserverName = hostserverName;
        this.hostserverPort = hostserverPort;
    }
    
    /**
     * Proxy_RaceTrack superviseTheRace function.
     * Sends REQSUPERVISERACE message to racetrack server.
     * Receive RESPSUPERVISERACE message from racetrack server.
     */
    @Override
    public void superviseTheRace(){
        ClientCom cl_com = new ClientCom(CommPorts.racetrackServerName, CommPorts.racetrackServerPort);
        Message msg_in, msg_out;

        while(!cl_com.open()){
            try{
                sleep((long) (10));
            }catch(InterruptedException e){
                e.printStackTrace();
            }
        }
        msg_out = new Message(Message.REQSUPERVISERACE);
        cl_com.write(msg_out);
        msg_in = (Message) cl_com.read();

       if ((msg_in.getType () != Message.RESPSUPERVISERACE)){
           GenericIO.writelnString ("Thread Broker Supervise The Race: Invalid Type!");
           GenericIO.writelnString (msg_in.toString ());
           System.exit (1);
       }
       cl_com.close();
    }
    
    /**
     * Proxy_RaceTrack waitForHorses function.
     * Sends REQWAITFORHORSES message to racetrack server.
     * Receive RESPWAITFORHORSES message from racetrack server.
     */
    @Override
    public void waitForHorses(){
        ClientCom cl_com = new ClientCom(CommPorts.racetrackServerName, CommPorts.racetrackServerPort);
        Message msg_in, msg_out;

        while(!cl_com.open()){
            try{
                sleep((long) (10));
            }catch(InterruptedException e){
                e.printStackTrace();
            }
        }
        msg_out = new Message(Message.REQWAITFORHORSES);
        cl_com.write(msg_out);
        msg_in = (Message) cl_com.read();

       if ((msg_in.getType () != Message.RESPWAITFORHORSES)){
           GenericIO.writelnString ("Thread Broker Wait For Horses: Invalid Type!");
           GenericIO.writelnString (msg_in.toString ());
           System.exit (1);
       }
       cl_com.close();
    }
    
    /**
     * Proxy_RaceTrack startRace function.
     * Sends REQSTARTRACE message to racetrack server.
     * Receive RESPSTARTRACE message from racetrack server.
     * Change MyThreadBroker current state.
     */
    @Override
    public void startRace(){
        ClientCom cl_com = new ClientCom(CommPorts.racetrackServerName, CommPorts.racetrackServerPort);
        Message msg_in, msg_out;

        while(!cl_com.open()){
            try{
                sleep((long) (10));
            }catch(InterruptedException e){
                e.printStackTrace();
            }
        }
        msg_out = new Message(Message.REQSTARTRACE);
        cl_com.write(msg_out);
        msg_in = (Message) cl_com.read();

        if ((msg_in.getType () != Message.RESPSTARTRACE)){
            GenericIO.writelnString ("Thread Broker Start Race: Invalid Type!");
            GenericIO.writelnString (msg_in.toString ());
            System.exit (1);
        }
        cl_com.close();
        //changing broker state;
        MyThreadBroker broker = (MyThreadBroker) Thread.currentThread();
        broker.broker_states = MyThreadBroker.Broker_States.SUPERVISING_THE_RACE;
    }
    
    /**
     * Proxy_RaceTrack reportResults function.
     * Sends REQREPORTRESULTS message to racetrack server.
     * Receive RESPREPORTRESULTS message from racetrack server.
     * Change MyThreadBroker current state.
     * @return race results.
     */
    @Override
    public int[] reportResults(){
        ClientCom cl_com = new ClientCom(CommPorts.racetrackServerName, CommPorts.racetrackServerPort);
        Message msg_in, msg_out;
        int [] a=null;

        while(!cl_com.open()){
            try{
                sleep((long) (10));
            }catch(InterruptedException e){
                e.printStackTrace();
            }
        }
        msg_out = new Message(Message.REQREPORTRESULTS);
        cl_com.write(msg_out);
        msg_in = (Message) cl_com.read();

        if ((msg_in.getType () != Message.RESPREPORTRESULTS)){
            GenericIO.writelnString ("Thread Broker Report Results: Invalid Type!");
            GenericIO.writelnString (msg_in.toString ());
            System.exit (1);
        }
        cl_com.close();
        a=msg_in.getInfo();
        //change broker state
        MyThreadBroker broker = (MyThreadBroker) Thread.currentThread();
        broker.broker_states = MyThreadBroker.Broker_States.REPORTING_RESULTS;
       return a;
    }
    
    /**
     * Proxy_RaceTrack checkOnHorses function.
     * Sends REQCHECKONHORSES message to racetrack server.
     * Receive RESPCHECKONHORSES Message from racetrack server.
     * @return true if all the horses have reached the race track.
     */
    @Override
    public boolean checkOnHorses(){
        ClientCom cl_com = new ClientCom(CommPorts.racetrackServerName, CommPorts.racetrackServerPort);
        Message msg_in, msg_out;

        while(!cl_com.open()){
            try{
                sleep((long) (10));
            }catch(InterruptedException e){
                e.printStackTrace();
            }
        }
        msg_out = new Message(Message.REQCHECKONHORSES);
        cl_com.write(msg_out);
        msg_in = (Message) cl_com.read();

        if ((msg_in.getType () != Message.RESPCHECKONHORSES)){
            GenericIO.writelnString ("Thread Broker Check on Horses: Invalid Type!");
            GenericIO.writelnString (msg_in.toString ());
            System.exit (1);
        }
        cl_com.close();
        return msg_in.getCheckHorses();
    }
    
    /**
     * Proxy_RaceTrack atTheStartLine function.
     * Sends REQATSTARTLINE message to racetrack server.
     * Receive RESPATSTARTLINE message from racetrack server.
     * Change MyThreadHorses current state.
     * @param id horse id.
     */
    @Override
    public void atTheStartLine(int id){
        ClientCom cl_com = new ClientCom(CommPorts.racetrackServerName, CommPorts.racetrackServerPort);
        Message msg_in, msg_out;

        while(!cl_com.open()){
            try{
                sleep((long) (10));
            }catch(InterruptedException e){
                e.printStackTrace();
            }
        }
        msg_out = new Message(Message.REQATSTARTLINE, id);
        cl_com.write(msg_out);
        msg_in = (Message) cl_com.read();

       if ((msg_in.getType () != Message.RESPATSTARTLINE)){
            GenericIO.writelnString ("Thread Horses " + id + " At The Start Line: Invalid Type!");
            GenericIO.writelnString (msg_in.toString ());
            System.exit (1);
        }
        cl_com.close();
        //change horse state
        MyThreadHorses th = (MyThreadHorses) Thread.currentThread();
        th.horse_states = MyThreadHorses.Horse_States.RUNNING;
    }
    
    /**
     * Proxy_RaceTrack makeAMove function.
     * Sends REQMAKEAMOVE message to racetrack server.
     * Receive RESPMAKEAMOVE message from racetrack server.
     * @param id horse id.
     * @param d distance.
     */
    @Override
    public void makeAMove(int id, int d){
        ClientCom cl_com = new ClientCom(CommPorts.racetrackServerName, CommPorts.racetrackServerPort);
        Message msg_in, msg_out;

        while(!cl_com.open()){
            try{
                sleep((long) (10));
            }catch(InterruptedException e){
                e.printStackTrace();
            }
        }
        msg_out = new Message(Message.REQMAKEAMOVE, id, d);
        cl_com.write(msg_out);
        msg_in = (Message) cl_com.read();

        if ((msg_in.getType () != Message.RESPMAKEAMOVE)){
           GenericIO.writelnString ("Thread Horses " + id + " make a move: Invalid Type!");
           GenericIO.writelnString (msg_in.toString ());
           System.exit (1);
        }
        cl_com.close();
    }
    
    /**
     * Proxy_RaceTrack hasFinishLineBeenCrossed function.
     * Sends REQCHECKFLINE message to racetrack server.
     * Receive RESPCHECKFLINE message from racetrack server.
     * Change MyThreadHorses current state.
     * @param id horse id
     * @return true if finish line has been crossed.
     */
    @Override
    public boolean hasFinishLineBeenCrossed(int id){
        ClientCom cl_com = new ClientCom(CommPorts.racetrackServerName, CommPorts.racetrackServerPort);
        Message msg_in, msg_out; 

        while(!cl_com.open()){
            try{
                sleep((long) (10));
            }catch(InterruptedException e){
                e.printStackTrace();
            }
        }
        msg_out = new Message(Message.REQCHECKFLINE, id);
        cl_com.write(msg_out);
        msg_in = (Message) cl_com.read();

       if ((msg_in.getType () != Message.RESPCHECKFLINE)){
           GenericIO.writelnString ("Thread Horses " + id + " finish line been crossed: Invalid Type!");
           GenericIO.writelnString (msg_in.toString ());
           System.exit (1);
       }
       cl_com.close();
       //change horses
       if(msg_in.getAuxBool()) {
           MyThreadHorses th = (MyThreadHorses) Thread.currentThread();
           th.horse_states = MyThreadHorses.Horse_States.AT_THE_FINNISH_LINE;
       }
       return msg_in.getCheckFinishLine();
    }
    
    /**
     * Proxy_RaceTrack proceedToStable function.
     * Sends REQPROCSTABLE message to racetrack server.
     * Receive RESPPROCSTABLE message from racetrack server.
     * Change MyThreadHorses current state.
     * @param id horse id.
     */
    @Override
    public void proceedToStable(int id){
        ClientCom cl_com = new ClientCom(CommPorts.racetrackServerName, CommPorts.racetrackServerPort);
        Message msg_in, msg_out;

        while(!cl_com.open()){
            try{
                sleep((long) (10));
            }catch(InterruptedException e){
                e.printStackTrace();
            }
        }
        msg_out = new Message(Message.REQPROCSTABLE, id);
        cl_com.write(msg_out);
        msg_in = (Message) cl_com.read();

       if ((msg_in.getType () != Message.RESPPROCSTABLE)){
           GenericIO.writelnString ("Thread Horses " + id + " proceed to stable: Invalid Type!");
           GenericIO.writelnString (msg_in.toString ());
           System.exit (1);
       }
       cl_com.close();
        //change horse state
         MyThreadHorses th = (MyThreadHorses) Thread.currentThread();
         th.horse_states = MyThreadHorses.Horse_States.AT_THE_STABLE;
    }
    
}
