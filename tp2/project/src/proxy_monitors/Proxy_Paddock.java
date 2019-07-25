/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proxy_monitors;

import client_side.ClientCom;
import client_side.mythreadbroker.MyThreadBroker;
import client_side.mythreadhorses.MyThreadHorses;
import client_side.mythreadspec.MyThreadSpec;
import commonInfo.CommPorts;
import commonInfo.Message;
import genclass.GenericIO;
import interfaces.broker.Paddock_Broker;
import interfaces.horses.Paddock_Horses;
import interfaces.spectator.Paddock_Spec;
import static java.lang.Thread.sleep;
import java.util.concurrent.locks.ReentrantLock;
import server_side.Log_Interface;

/**
 *
 * @author pedro
 */
public class Proxy_Paddock implements Paddock_Broker, Paddock_Spec, Paddock_Horses{
    private String hostserverName;
    private int hostserverPort;
    
    
    /**
     * Proxy_Paddock constructor
     * @param hostserverName proxy_paddock server host name.
     * @param hostserverPort proxy_paddock server host port.
     */
    public Proxy_Paddock(String hostserverName, int hostserverPort){
        this.hostserverName = hostserverName;
        this.hostserverPort = hostserverPort;
    }
    
    /**
     * Proxy_Paddock announceNextRace function.
     * Sends REQANNOUNCERACE message to paddock server.
     * Receive RESPANNOUNCERACE message from paddock server.
     */
    @Override
    public void announceNextRace(){
        ClientCom cl_com = new ClientCom(CommPorts.paddockServerName, CommPorts.paddockServerPort);
        Message msg_in, msg_out;

        while(!cl_com.open()){
            try{
                sleep((long) (10));
            }catch(InterruptedException e){
                e.printStackTrace();
            }
        }
        msg_out = new Message(Message.REQANNOUNCERACE);
        cl_com.write(msg_out);
        msg_in = (Message) cl_com.read();

       if ((msg_in.getType () != Message.RESPANNOUNCERACE)){
           GenericIO.writelnString ("Thread Broker Announce next Race: Invalid Type!");
           GenericIO.writelnString (msg_in.toString ());
           System.exit (1);
       }
       cl_com.close();
        
    }
    
    /**
     * Proxy_Paddock acceptBets function.
     * Sends REQACCEPTBETS message to paddock server.
     * Receive RESPACCEPTBETS message from paddock server.
     * Change MyThreadBroker current state.
     */
    @Override
    public void acceptTheBets(){
        ClientCom cl_com = new ClientCom(CommPorts.paddockServerName, CommPorts.paddockServerPort);
        Message msg_in, msg_out;

        while(!cl_com.open()){
            try{
                sleep((long) (10));
            }catch(InterruptedException e){
                e.printStackTrace();
            }
        }
        msg_out = new Message(Message.REQACCEPTBETS);
        cl_com.write(msg_out);
        msg_in = (Message) cl_com.read();

        if ((msg_in.getType () != Message.RESPACCEPTBETS)){
           GenericIO.writelnString ("Thread Broker Accept Bets: Invalid Type!");
           GenericIO.writelnString (msg_in.toString ());
           System.exit (1);
        }
        cl_com.close();
        //change broker state
        MyThreadBroker broker = (MyThreadBroker) Thread.currentThread();
        broker.broker_states = MyThreadBroker.Broker_States.WAITING_FOR_BETS;
    }
    
    /**
     * Proxy_Paddock checkSpecCount function.
     * Sends REQCHECKONSPEC message to paddock server.
     * Receive RESPCHECKONSPEC message from paddock server.
     * @return true if all spectators are in the paddock.
     */
    @Override
    public boolean checkSpecCount(){
        ClientCom cl_com = new ClientCom(CommPorts.paddockServerName, CommPorts.paddockServerPort);            
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
           GenericIO.writelnString ("Thread Broker Check Spec Count: Invalid Type!");
           GenericIO.writelnString (msg_in.toString ());
           System.exit (1);
       }
       cl_com.close();
       return msg_in.getCheckSpec();
    }
    
    /**
     * Proxy_Paddock proceedToPaddock function.
     * Sends REQPROCPADDOCK message to paddock server.
     * Receive RESPPROCPADDOCK message from paddock server.
     * @param id horse id.
     * @param agi horse agility.
     */
    @Override
    public void proceedToPaddock(int id, int agi){
        ClientCom cl_com = new ClientCom(CommPorts.paddockServerName, CommPorts.paddockServerPort);
        Message msg_in, msg_out;

        while(!cl_com.open()){
            try{
                sleep((long) (10));
            }catch(InterruptedException e){
                e.printStackTrace();
            }
        }
        msg_out = new Message(Message.REQPROCPADDOCK, id, agi);
        cl_com.write(msg_out);
        msg_in = (Message) cl_com.read();

       if ((msg_in.getType () != Message.RESPPROCPADDOCK)){
           GenericIO.writelnString ("Thread Horse " + id + "Proceed to Paddock: Invalid Type!");
           GenericIO.writelnString (msg_in.toString ());
           System.exit (1);
       }
       cl_com.close();
    }
    
    /**
     * Proxy_Paddock proceedToStartLine function.
     * Sends REQPROCSTARTLINE message to paddock server.
     * Receive RESPPROCSTARTLINE message from paddock server.
     * Change MyThreadHorses current state.
     * @param id horse id.
     */
    @Override
    public void proceedToStartLine(int id){
        ClientCom cl_com = new ClientCom(CommPorts.paddockServerName, CommPorts.paddockServerPort);
        Message msg_in, msg_out;

        while(!cl_com.open()){
            try{
                sleep((long) (10));
            }catch(InterruptedException e){
                e.printStackTrace();
            }
        }
        msg_out = new Message(Message.REQPROCSTARTLINE, id);
        cl_com.write(msg_out);
        msg_in = (Message) cl_com.read();

        if ((msg_in.getType () != Message.RESPPROCSTARTLINE)){
           GenericIO.writelnString ("Thread Horse " + id + "Proceed to Start Line : Invalid Type!");
           GenericIO.writelnString (msg_in.toString ());
           System.exit (1);
        }
        cl_com.close();
       //change horse state
        MyThreadHorses horse = (MyThreadHorses) Thread.currentThread();
        horse.horse_states = MyThreadHorses.Horse_States.AT_THE_START_LINE;
    }
    
    /**
     * Proxy_Paddock checkForSpectators function -- horses.
     * Sends REQCHECKFORSPEC message to paddock server.
     * Receive RESPCHECKFORSPEC message from paddock server.
     * @return true if all the spectators have reached the paddock.
     */
    @Override
    public boolean checkForSpectators(){
        ClientCom cl_com = new ClientCom(CommPorts.paddockServerName, CommPorts.paddockServerPort);
        Message msg_in, msg_out;

        while(!cl_com.open()){
            try{
                sleep((long) (10));
            }catch(InterruptedException e){
                e.printStackTrace();
            }
        }
        msg_out = new Message(Message.REQCHECKFORSPEC);
        cl_com.write(msg_out);
        msg_in = (Message) cl_com.read();

        if ((msg_in.getType () != Message.RESPCHECKFORSPEC)){
            GenericIO.writelnString ("Thread Horse Check For Spectators : Invalid Type!");
            GenericIO.writelnString (msg_in.toString ());
            System.exit (1);
        }
        cl_com.close();
        return msg_in.getCheckSpec();
    }
    
    /**
     * Proxy_Paddock waitForNextRace function.
     * Sends REQWAITFORNEXTRACE message to paddock server.
     * Receive RESPWAITFORNEXTRACE Message from paddock server.
     * Change MyThreadSpec current state.
     * @param spec_id spectator id.
     */
    @Override
    public void waitForNextRace(int spec_id){
        ClientCom cl_com = new ClientCom(CommPorts.paddockServerName, CommPorts.paddockServerPort);
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
            GenericIO.writelnString ("Thread Spectator " + spec_id + " Wait for Next Race : Invalid Type!");
            GenericIO.writelnString (msg_in.toString ());
            System.exit (1);
        }
        cl_com.close();
        //change state of spectator
        MyThreadSpec spectator = (MyThreadSpec) Thread.currentThread();
        spectator.spec_states = MyThreadSpec.Spec_States.APPRAISING_THE_HORSES;
    }
    
    /**
     * Proxy_Paddock appraiseHorses function.
     * Sends REQAPPRAISEHORSES message to paddock server.
     * Receive RESPAPPRAISEHORSES message from paddock server.
     * @param spec_id spectator id.
     * @return appraise horses info.
     */
    @Override
    public int[] appraiseHorses(int spec_id){
       ClientCom cl_com = new ClientCom(CommPorts.paddockServerName, CommPorts.paddockServerPort);
        Message msg_in, msg_out;

        while(!cl_com.open()){
            try{
                sleep((long) (10));
            }catch(InterruptedException e){
                e.printStackTrace();
            }
        }
        msg_out = new Message(Message.REQAPPRAISEHORSES, spec_id);
        cl_com.write(msg_out);
        msg_in = (Message) cl_com.read();

        if ((msg_in.getType () != Message.RESPAPPRAISEHORSES)){
            GenericIO.writelnString ("Thread Spectator " + spec_id + " Appraise Horses : Invalid Type!");
            GenericIO.writelnString (msg_in.toString ());
            System.exit (1);
        }
        cl_com.close();
        return msg_in.getInfo();
    }
    
    /**
     * Proxy_Paddock goPlaceABet
     * Sends REQGOPLACEABET message to paddock server.
     * Receive RESPGOPLACEABET message from paddock server.
     * Change MyThreadSpec current state.
     * @param spec_id spectator id.
     */
    @Override
    public void goPlaceABet(int spec_id){
        ClientCom cl_com = new ClientCom(CommPorts.paddockServerName, CommPorts.paddockServerPort);
        Message msg_in, msg_out;

        while(!cl_com.open()){
            try{
                sleep((long) (10));
            }catch(InterruptedException e){
                e.printStackTrace();
            }
        }
        msg_out = new Message(Message.REQGOPLACEABET, spec_id);
        cl_com.write(msg_out);
        msg_in = (Message) cl_com.read();

       if ((msg_in.getType () != Message.RESPGOPLACEABET)){
           GenericIO.writelnString ("Thread Spectator " + spec_id + " Appraise Horses : Invalid Type!");
           GenericIO.writelnString (msg_in.toString ());
           System.exit (1);
       }
       cl_com.close();
       //change spectator state;
       MyThreadSpec spectator = (MyThreadSpec) Thread.currentThread();
       spectator.spec_states = MyThreadSpec.Spec_States.PLACING_A_BET;
    }
}
