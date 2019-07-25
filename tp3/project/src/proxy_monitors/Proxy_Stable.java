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
import interfaces.broker.Stable_Broker;
import interfaces.horses.Stable_Horses;
import static java.lang.Thread.sleep;
import java.util.concurrent.locks.ReentrantLock;

/**
 *
 * @author pedro
 */
public class Proxy_Stable implements Stable_Broker, Stable_Horses {
    private String hostserverName;
    private int hostserverPort;
    
    /**
     * Proxy_Stable constructor
     * @param hostserverName proxy_stable host server name.
     * @param hostserverPort proxy_stable host server port.
     */
    public Proxy_Stable(String hostserverName, int hostserverPort){
        this.hostserverName = hostserverName;
        this.hostserverPort = hostserverPort;
    }
    
    /**
     * Proxy_Stable openEvent function.
     * Sends REQOPENEVENT message to stable server.
     * Receive RESPOPENEVENT message from stable server,
     * Change MyThreadBroker current state.
     */
    @Override
    public void openEvent(){
        ClientCom cl_com = new ClientCom(CommPorts.stableServerName, CommPorts.stableServerPort);
        Message msg_in, msg_out;
        while(!cl_com.open()){
            try{
                sleep((long) (10));
            }catch(InterruptedException e){
                e.printStackTrace();
            }
        }
        msg_out = new Message(Message.REQOPENEVENT);
        cl_com.write(msg_out);
        msg_in = (Message) cl_com.read();

        if ((msg_in.getType () != Message.RESPOPENEVENT)){
            GenericIO.writelnString ("Thread  Broker Open Event: Invalid Type!");
            GenericIO.writelnString (msg_in.toString ());
            System.exit (1);
        }
        cl_com.close();
        //change broker state
        MyThreadBroker broker = (MyThreadBroker) Thread.currentThread();
        broker.broker_states = MyThreadBroker.Broker_States.OPENING_THE_EVENT;
    }
    
    /**
     * Proxy_Stable summonHorsesToPaddock function.
     * Sends REQSUMMONHORSES message to stable server.
     * Receive RESPSUMMONHORSES message from stable server.
     * Change MyThreadBroker current state.
     */
    @Override
    public void summonHorsesToPaddock(){
        ClientCom cl_com = new ClientCom(CommPorts.stableServerName, CommPorts.stableServerPort);
        Message msg_in, msg_out;

        while(!cl_com.open()){
            try{
                sleep((long) (10));
            }catch(InterruptedException e){
                e.printStackTrace();
            }
        }
        msg_out = new Message(Message.REQSUMMONHORSES);
        cl_com.write(msg_out);
        msg_in = (Message) cl_com.read();

        if ((msg_in.getType () != Message.RESPSUMMONHORSES)){
            GenericIO.writelnString ("Thread  Broker Summon Horses: Invalid Type!");
            GenericIO.writelnString (msg_in.toString ());
            System.exit (1);
        }
        cl_com.close();
        //change broker state
        MyThreadBroker broker = (MyThreadBroker) Thread.currentThread();
        broker.broker_states = MyThreadBroker.Broker_States.ANNOUNCING_NEXT_RACE;
    }
    
    /**
     * Proxy_Stable checkHorses function.
     * Sends REQCHECKONHORSES message to stable server.
     * Receive RESPCHECKONHORSES message from stable server.
     * @return true if all horses are in the stable.
     */
    @Override
    public boolean checkHorses(){
        ClientCom cl_com = new ClientCom(CommPorts.stableServerName, CommPorts.stableServerPort);
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
           GenericIO.writelnString ("Thread  Broker Check Horses: Invalid Type!");
           GenericIO.writelnString (msg_in.toString ());
           System.exit (1);
       }
       cl_com.close();
       return msg_in.getCheckHorses();
    }
    
    /**
     * Proxy_Stable proceedToPaddock function.
     * Sends REQPROCPADDOCK message to stable server.
     * Receive RESPPROCPADDOCK Message from stable server.
     * Change MyThreadHorses current state
     * @param id horse id.
     */
    @Override
    public void proceedToPaddock(int id){
        ClientCom cl_com = new ClientCom(CommPorts.stableServerName, CommPorts.stableServerPort);
        Message msg_in, msg_out;

        while(!cl_com.open()){
            try{
                sleep((long) (10));
            }catch(InterruptedException e){
                e.printStackTrace();
            }
        }
        msg_out = new Message(Message.REQPROCPADDOCK, id);
        cl_com.write(msg_out);
        msg_in = (Message) cl_com.read();
        System.out.print("Checkpoint 1");
        if ((msg_in.getType () != Message.RESPPROCPADDOCK)){
            GenericIO.writelnString ("Thread  Horses " + id + " Proceed Paddock: Invalid Type!");
            GenericIO.writelnString (msg_in.toString ());
            System.exit (1);
        }
        cl_com.close();
        //change horse_states
        MyThreadHorses horse = (MyThreadHorses) Thread.currentThread();
        horse.horse_states = MyThreadHorses.Horse_States.AT_THE_PADDOCK;
    }
    
    /**
     * Proxy_Stable returnToStable function.
     * Sends REQRETURNTOSTABLE message to stable server,
     * Receive RESPRETURNTOSTABLE message from stable server.
     * @param id horse id.
     */
    @Override
    public void returnToStable(int id){
        ClientCom cl_com = new ClientCom(CommPorts.stableServerName, CommPorts.stableServerPort);
        Message msg_in, msg_out;

        while(!cl_com.open()){
            try{
                sleep((long) (10));
            }catch(InterruptedException e){
                e.printStackTrace();
            }
        }
        msg_out = new Message(Message.REQRETURNTOSTABLE, id);
        cl_com.write(msg_out);
        msg_in = (Message) cl_com.read();

        if ((msg_in.getType () != Message.RESPRETURNTOSTABLE)){
            GenericIO.writelnString ("Thread  Horses " + id + " Return to Stable: Invalid Type!");
            GenericIO.writelnString (msg_in.toString ());
            System.exit (1);
        }
        cl_com.close();
        
    }
}
