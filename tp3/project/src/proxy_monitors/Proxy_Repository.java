/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package proxy_monitors;

import client_side.ClientCom;
import client_side.mythreadspec.MyThreadSpec;
import commonInfo.CommPorts;
import commonInfo.Message;
import genclass.GenericIO;
import interfaces.broker.Repository_Broker;
import interfaces.spectator.Repository_Spec;
import static java.lang.Thread.sleep;
import java.util.concurrent.locks.ReentrantLock;

/**
 *
 * @author pedro
 */
public class Proxy_Repository implements Repository_Broker, Repository_Spec{
    private String hostserverName;
    private int hostserverPort;
    
    
    /**
     * Proxy_Repository constructor.
     * @param hostserverName proxy_repository host server name.
     * @param hostserverPort proxy_repository host server port.
     */
    public Proxy_Repository(String hostserverName, int hostserverPort){
        this.hostserverName = hostserverName;
        this.hostserverPort = hostserverPort;
    }
    
    /**
     * Proxy_Repository reportResults function.
     * Sends REQREPORTRESULTS message to repository server.
     * Receive RESPREPORTRESULTS message from repository server.
     * @param race_id race id.
     * @param winner_id winner horse id.
     */
    @Override
    public void reportResults(int race_id, int[] winner_id){
        ClientCom cl_com = new ClientCom(CommPorts.repServerName, CommPorts.repServerPort);
        Message msg_in, msg_out;

        while(!cl_com.open()){
            try{
                sleep((long) (10));
            }catch(InterruptedException e){
                e.printStackTrace();
            }
        }
        msg_out = new Message(Message.REQREPORTRESULTS, race_id, winner_id);
        cl_com.write(msg_out);
        msg_in = (Message) cl_com.read();

        if ((msg_in.getType () != Message.RESPREPORTRESULTS)){
            GenericIO.writelnString ("Thread Broker Report Results: Invalid Type!");
            GenericIO.writelnString (msg_in.toString ());
            System.exit (1);
        }
        cl_com.close();
    }
    
    /**
     * Proxy_Repository alertWinners function.
     * Sends REQALERTWINNERS message to repository server.
     * Receive RESPALERTWINNERS message from repository server.
     */
    @Override
    public void alertWinners(){
        ClientCom cl_com = new ClientCom(CommPorts.repServerName, CommPorts.repServerPort);
        Message msg_in, msg_out;

        while(!cl_com.open()){
            try{
                sleep((long) (10));
            }catch(InterruptedException e){
                e.printStackTrace();
            }
        }
        msg_out = new Message(Message.REQALERTWINNERS);
        cl_com.write(msg_out);
        msg_in = (Message) cl_com.read();

        if ((msg_in.getType () != Message.RESPALERTWINNERS)){
            GenericIO.writelnString ("Thread Broker Alert Winners: Invalid Type!");
            GenericIO.writelnString (msg_in.toString ());
            System.exit (1);
        }
        cl_com.close();
    }
    
    /**
     * Proxy_Repository checkFinalRace function -- broker.
     * Sends REQCHECKFINALRACE message to repository server.
     * Receive RESPCHECKFINALRACE message from repository server.
     * @return true if it's the final race.
     */
    @Override
    public boolean checkFinalRace(){
        ClientCom cl_com = new ClientCom(CommPorts.repServerName, CommPorts.repServerPort);
        Message msg_in, msg_out;

        while(!cl_com.open()){
            try{
                sleep((long) (10));
            }catch(InterruptedException e){
                e.printStackTrace();
            }
        }
        msg_out = new Message(Message.REQCHECKFINALRACE);
        cl_com.write(msg_out);
        msg_in = (Message) cl_com.read();

        if ((msg_in.getType () != Message.RESPCHECKFINALRACE)){
            GenericIO.writelnString ("Thread Broker Check Final Race: Invalid Type!");
            GenericIO.writelnString (msg_in.toString ());
            System.exit (1);
        }
        cl_com.close();
        return msg_in.getCheckFinalRace();
    }
    
    /**
     * Proxy_Repository checkWin function.
     * Sends REQCHECKWIN message to repository server.
     * Receive RESPCHECKWIN message from repository server.
     * @param spec_id spectator id.
     * @param horse_id horse id.
     * @return true if spectator won.
     */
    @Override
    public boolean checkWin(int spec_id, int horse_id){
        ClientCom cl_com = new ClientCom(CommPorts.repServerName, CommPorts.repServerPort);
        Message msg_in, msg_out;
        
        while(!cl_com.open()){
            try{
                sleep((long) (10));
            }catch(InterruptedException e){
                e.printStackTrace();
            }
        }
        msg_out = new Message(Message.REQCHECKWIN, spec_id, horse_id);
        cl_com.write(msg_out);
        msg_in = (Message) cl_com.read();
        
        if ((msg_in.getType () != Message.RESPCHECKWIN)){
            GenericIO.writelnString ("Thread Spectator " + spec_id +  "Check WIN: Invalid Type!");
            GenericIO.writelnString (msg_in.toString ());
            System.exit (1);
        }
        cl_com.close();
        return msg_in.getCheckWin();
    }
    
    /**
     * Proxy_Repository goCollectGains function.
     * Sends REQGOCOLLECTGAINS message to repository server.
     * Receive RESPGOCOLLECTGAINS message from repository server.
     * Change MyThreadSpec current state.
     * @param spec_id spectator id
     */
    @Override
    public void goCollectGains(int spec_id){
        ClientCom cl_com = new ClientCom(CommPorts.repServerName, CommPorts.repServerPort);
        Message msg_in, msg_out;

        while(!cl_com.open()){
            try{
                sleep((long) (10));
            }catch(InterruptedException e){
                e.printStackTrace();
            }
        }
        msg_out = new Message(Message.REQGOCOLLECTGAINS, spec_id);
        cl_com.write(msg_out);
        msg_in = (Message) cl_com.read();

        if ((msg_in.getType () != Message.RESPGOCOLLECTGAINS)){
            GenericIO.writelnString ("Thread Spectator " + spec_id + " Go Collect Gains: Invalid Type!");
            GenericIO.writelnString (msg_in.toString ());
            System.exit (1);
        }
        cl_com.close();
        //change spectator state
        MyThreadSpec spectator = (MyThreadSpec) Thread.currentThread();
        spectator.spec_states = MyThreadSpec.Spec_States.COLLECTING_THE_GAINS;
    } 
    
    /**
     * Proxy_Repository checkFinalRace function -- spectator.
     * Sends REQCHECKFINALRACESPEC message to repository server.
     * Receive RESPCHECKFINALRACESPEC message from repository server.
     * @param spec_id spectator id.
     * @return true if it's final race.
     */
    @Override
    public boolean checkFinalRace(int spec_id){
        ClientCom cl_com = new ClientCom(CommPorts.repServerName, CommPorts.repServerPort);
        Message msg_in, msg_out;

        while(!cl_com.open()){
            try{
                sleep((long) (10));
            }catch(InterruptedException e){
                e.printStackTrace();
            }
        }
        msg_out = new Message(Message.REQCHECKFINALRACESPEC, spec_id);
        cl_com.write(msg_out);
        msg_in = (Message) cl_com.read();

        if ((msg_in.getType () != Message.RESPCHECKFINALRACESPEC)){
            GenericIO.writelnString ("Thread Spectator " + spec_id + " Check Final Race: Invalid Type!");
            GenericIO.writelnString (msg_in.toString ());
            System.exit (1);
        }
        cl_com.close();

        return msg_in.getCheckFinalRace();
    }
}
