/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server_side;

import client_side.ClientCom;
import client_side.mythreadbroker.MyThreadBroker;
import client_side.mythreadhorses.MyThreadHorses;
import client_side.mythreadspec.MyThreadSpec;
import commonInfo.CommPorts;
import commonInfo.Message;
import genclass.GenericIO;
import static java.lang.Thread.sleep;

/**
 * @author pedro
 * @author franciscoteixeira
 */
public class Log_Proxy implements Log_Interface{
    String logName = CommPorts.logRepServerName;
    int logPort = CommPorts.logRepServerPort;
    
    /**
     * Log_Proxy constructor.
     */
    public Log_Proxy() {
        
    }
    
    /**
     * Log_Proxy printFirst function.
     * Sends REQPRINTFIRST message to log repository server.
     * Receive RESPPRINTFIRST message from log repository server.
     */
    @Override
    public void printFirst() {
        ClientCom cl_com = new ClientCom(logName, logPort);
        Message msg_in, msg_out;        
        
        while(!cl_com.open()){
            try{
                sleep((long) (10));
            }catch(InterruptedException e){
                e.printStackTrace();
            }
        }
        msg_out = new Message(Message.REQPRINTFIRST);
        cl_com.write(msg_out);
        msg_in = (Message) cl_com.read();
        if ((msg_in.getType () != Message.RESPPRINTFIRST)){
           GenericIO.writelnString ("Log_Poxy : Invalid Type! " + msg_in.getType());
           System.exit (1);
        }
        cl_com.close();
    }
    
    /**
     * Log_Proxy changeLog function.
     * Sends REQCHANGELOG message to log repository server.
     * Receive RESPCHANGELOG message from log repository server.
     */
    @Override
    public void changeLog() {
        ClientCom cl_com = new ClientCom(logName, logPort);
        Message msg_in, msg_out;        
        
        while(!cl_com.open()){
            try{
                sleep((long) (10));
            }catch(InterruptedException e){
                e.printStackTrace();
            }
        }
        msg_out = new Message(Message.REQCHANGELOG);
        cl_com.write(msg_out);
        msg_in = (Message) cl_com.read();
        if ((msg_in.getType () != Message.RESPCHANGELOG)){
           GenericIO.writelnString ("Log_Poxy : Invalid Type! " + msg_in.getType());
           System.exit (1);
        }
        cl_com.close();
    }
    
    /**
     * Log_Proxy getRaceNumber function.
     * Sends REQGETRACENUM message to log repository server.
     * Receive RESPGETRACENUM message from log repository server.
     * @return race number.
     */
    @Override
    public int getRaceNumber() {
        ClientCom cl_com = new ClientCom(logName, logPort);
        Message msg_in, msg_out;        
        
        while(!cl_com.open()){
            try{
                sleep((long) (10));
            }catch(InterruptedException e){
                e.printStackTrace();
            }
        }
        msg_out = new Message(Message.REQGETRACENUM);
        cl_com.write(msg_out);
        msg_in = (Message) cl_com.read();
        if ((msg_in.getType () != Message.RESPGETRACENUM)){
           GenericIO.writelnString ("Log_Poxy : Invalid Type! " + msg_in.getType());
           System.exit (1);
        }
        cl_com.close();
        return msg_in.getAddedInfo();
    }
    
    /**
     * Log_Proxy getTravelDistance function.
     * Sends REQGETTRAVELDIST message to log repository server.
     * Receive RESPGETTRAVELDIST message from log repository server
     * @return travel distance.
     */
    @Override
    public int getTravelDistance() {
        ClientCom cl_com = new ClientCom(logName, logPort);
        Message msg_in, msg_out;        
        
        while(!cl_com.open()){
            try{
                sleep((long) (10));
            }catch(InterruptedException e){
                e.printStackTrace();
            }
        }
        msg_out = new Message(Message.REQGETTRAVELDIST);
        cl_com.write(msg_out);
        msg_in = (Message) cl_com.read();
        if ((msg_in.getType () != Message.RESPGETTRAVELDIST)){
           GenericIO.writelnString ("Log_Poxy : Invalid Type! " + msg_in.getType());
           System.exit (1);
        }
        cl_com.close();
        return msg_in.getAddedInfo();
    }
    
    /**
     * Log_Proxy setBrokerState function.
     * Sends REQSETBROKERSTATE message to log repository server.
     * Receive RESPSETBROKERSTATE message from log repository server.
     * @param state MyThreadBroker state. 
     */
    @Override
    public void setBrokerState(MyThreadBroker.Broker_States state) {
        ClientCom cl_com = new ClientCom(logName, logPort);
        Message msg_in, msg_out;        
        
        while(!cl_com.open()){            
            try{
                sleep((long) (10));
            }catch(InterruptedException e){
                e.printStackTrace();
            }
        }
        msg_out = new Message(Message.REQSETBROKERSTATE, state);
        cl_com.write(msg_out);
        msg_in = (Message) cl_com.read();
        if ((msg_in.getType () != Message.RESPSETBROKERSTATE)){
           GenericIO.writelnString ("Log_Poxy : Invalid Type! " + msg_in.getType());
           System.exit (1);
        }
        cl_com.close();
    }
    
     
    /**
     * Log_Proxy setSpectatorState function.
     * Sends REQSETSPECSTATE message to log repository server.
     * Receive RESPSETSPECSTATE message from log repository server.
     * @param state MyThreadSpec state. 
     */
    @Override
    public void setSpectatorState(int spec_id, MyThreadSpec.Spec_States state) {
        ClientCom cl_com = new ClientCom(logName, logPort);
        Message msg_in, msg_out;        
        
        while(!cl_com.open()){            
            try{
                sleep((long) (10));
            }catch(InterruptedException e){
                e.printStackTrace();
            }
        }
        msg_out = new Message(Message.REQSETSPECSTATE, spec_id, state);
        cl_com.write(msg_out);
        msg_in = (Message) cl_com.read();
        if ((msg_in.getType () != Message.RESPSETSPECSTATE)){
           GenericIO.writelnString ("Log_Poxy : Invalid Type! " + msg_in.getType());
           System.exit (1);
        }
        cl_com.close();
    }
    
    /**
     * Log_Proxy setSpectatorMoney function.
     * Sends REQSETSPECMONEY message to log repository server.
     * Receive RESPSETSPECMONEY message from log repository server.
     * @param spec_id spectator id.
     * @param money money value.
     */
    @Override
    public void setSpectatorMoney(int spec_id, int money) {
        ClientCom cl_com = new ClientCom(logName, logPort);
        Message msg_in, msg_out;        
        
        while(!cl_com.open()){            
            try{
                sleep((long) (10));
            }catch(InterruptedException e){
                e.printStackTrace();
            }
        }
        msg_out = new Message(Message.REQSETSPECMONEY, spec_id, money);
        cl_com.write(msg_out);
        msg_in = (Message) cl_com.read();
        if ((msg_in.getType () != Message.RESPSETSPECMONEY)){
           GenericIO.writelnString ("Log_Poxy : Invalid Type! " + msg_in.getType());
           System.exit (1);
        }
        cl_com.close();
    }
    
    /**
     * Log_Proxy setSpectatorMoneyToBet function
     * Sends REQSETSPECMONEYTOBET message to log repository server.
     * Receive RESPSETSPECMONEYTOBET message from log repository server.
     * @param spec_id spectator id
     * @param horse_id horse id
     * @param money_to_bet money to bet
     */
    @Override
    public void setSpectatorMoneyToBet(int spec_id, int horse_id, int money_to_bet) {
        ClientCom cl_com = new ClientCom(logName, logPort);
        Message msg_in, msg_out;        
        
        while(!cl_com.open()){            
            try{
                sleep((long) (10));
            }catch(InterruptedException e){
                e.printStackTrace();
            }
        }
        msg_out = new Message(Message.REQSETSPECMONEYTOBET, spec_id, horse_id, money_to_bet);
        cl_com.write(msg_out);
        msg_in = (Message) cl_com.read();
        if ((msg_in.getType () != Message.RESPSETSPECMONEYTOBET)){
           GenericIO.writelnString ("Log_Poxy : Invalid Type! " + msg_in.getType());
           System.exit (1);
        }
        cl_com.close();
    }
    
    /**
     * Log_Proxy setSpectatorHorseSel function.
     * Sends REQSETSPECHORSETOSEL message to log repository server.
     * Receive RESPSETSPECHORSETOSEL message from log repository server.
     * @param spec_id spectator id.
     * @param horse_id horse id.
     */
    @Override
    public void setSpectatorHorseSel(int spec_id, int horse_id) {
        ClientCom cl_com = new ClientCom(logName, logPort);
        Message msg_in, msg_out;        
        
        while(!cl_com.open()){            
            try{
                sleep((long) (10));
            }catch(InterruptedException e){
                e.printStackTrace();
            }
        }
        msg_out = new Message(Message.REQSETSPECHORSETOSEL, spec_id, horse_id);
        cl_com.write(msg_out);
        msg_in = (Message) cl_com.read();
        if ((msg_in.getType () != Message.RESPSETSPECHORSETOSEL)){
           GenericIO.writelnString ("Log_Poxy : Invalid Type! " + msg_in.getType());
           System.exit (1);
        }
        cl_com.close();
    }
    
    /**
     * Log_Proxy setHorseState function.
     * Sends REQSETHORSESTATE message to log repository server.
     * Receive RESPSETHORSESTATE message from log repository server.
     * @param horse_id horse id.
     * @param state MyThreadHorses state.
     */
    @Override
    public void setHorseState(int horse_id, MyThreadHorses.Horse_States state) {
        ClientCom cl_com = new ClientCom(logName, logPort);
        Message msg_in, msg_out;        
        
        while(!cl_com.open()){            
            try{
                sleep((long) (10));
            }catch(InterruptedException e){
                e.printStackTrace();
            }
        }
        msg_out = new Message(Message.REQSETHORSESTATE, horse_id, state);
        cl_com.write(msg_out);
        msg_in = (Message) cl_com.read();
        if ((msg_in.getType () != Message.RESPSETHORSESTATE)){
           GenericIO.writelnString ("Log_Poxy : Invalid Type! " + msg_in.getType());
           System.exit (1);
        }
        cl_com.close();
    }
    
    /**
     * Log_Proxy setHorseLength function-
     * Sends REQSETHORSELEN message to log repository server.
     * Receive RESPSETHORSELEN message from log repository server.
     * @param horse_id horse id.
     * @param length horse length.
     */
    @Override
    public void setHorseLength(int horse_id, int length) {
        ClientCom cl_com = new ClientCom(logName, logPort);
        Message msg_in, msg_out;        
        
        while(!cl_com.open()){            
            try{
                sleep((long) (10));
            }catch(InterruptedException e){
                e.printStackTrace();
            }
        }
        msg_out = new Message(Message.REQSETHORSELEN, horse_id, length);
        cl_com.write(msg_out);
        msg_in = (Message) cl_com.read();
        if ((msg_in.getType () != Message.RESPSETHORSELEN)){
           GenericIO.writelnString ("Log_Poxy : Invalid Type! " + msg_in.getType());
           System.exit (1);
        }
        cl_com.close();
    }
    
    /**
     * Log_Proxy setHorseOdds function.
     * Sends REQSETHORSEODDS message to log repository server.
     * Receive RESPSETHORSEODDS message from log repository server.
     * @param horse_id horse id.
     * @param odds horse odds.
     */
    @Override
    public void setHorseOdds(int horse_id, int odds) {
        ClientCom cl_com = new ClientCom(logName, logPort);
        Message msg_in, msg_out;        
        
        while(!cl_com.open()){            
            try{
                sleep((long) (10));
            }catch(InterruptedException e){
                e.printStackTrace();
            }
        }
        msg_out = new Message(Message.REQSETHORSEODDS, horse_id, odds);
        cl_com.write(msg_out);
        msg_in = (Message) cl_com.read();
        if ((msg_in.getType () != Message.RESPSETHORSEODDS)){
           GenericIO.writelnString ("Log_Poxy : Invalid Type! " + msg_in.getType());
           System.exit (1);
        }
        cl_com.close();
    }
    
    /**
     * Log_Proxy setHorseIT function.
     * Sends REQSETHORSEIT message to log repository server.
     * Receive RESPSETHORSEIT message from log repository server.
     * @param horse_id horse id
     * @param horse_it horse iteration
     */
    @Override
    public void setHorseIT(int horse_id, int horse_it) {
        ClientCom cl_com = new ClientCom(logName, logPort);
        Message msg_in, msg_out;        
        
        while(!cl_com.open()){            
            try{
                sleep((long) (10));
            }catch(InterruptedException e){
                e.printStackTrace();
            }
        }
        msg_out = new Message(Message.REQSETHORSEIT, horse_id, horse_it);
        cl_com.write(msg_out);
        msg_in = (Message) cl_com.read();
        if ((msg_in.getType () != Message.RESPSETHORSEIT)){
           GenericIO.writelnString ("Log_Poxy : Invalid Type! " + msg_in.getType());
           System.exit (1);
        }
        cl_com.close();
    }
    
    /**
     * Log_Proxy setHorsePos function.
     * Sends REQSETHORSEPOS message to log repository server.
     * Receive RESPSETHORSEPOS message from log repository server.
     * @param horse_p horse position.
     */
    @Override
    public void setHorsePos(int[] horse_p) {
        ClientCom cl_com = new ClientCom(logName, logPort);
        Message msg_in, msg_out;        
        
        while(!cl_com.open()){            
            try{
                sleep((long) (10));
            }catch(InterruptedException e){
                e.printStackTrace();
            }
        }
        msg_out = new Message(Message.REQSETHORSEPOS, horse_p);
        cl_com.write(msg_out);
        msg_in = (Message) cl_com.read();
        if ((msg_in.getType () != Message.RESPSETHORSEPOS)){
           GenericIO.writelnString ("Log_Poxy : Invalid Type! " + msg_in.getType());
           System.exit (1);
        }
        cl_com.close();
    }
    
    /**
     * Log_Proxy setHorsesAtTheEnd function.
     * Sends REQSETHORSESATEND message to log repository server.
     * Receive RESPSETHORSESATEND message from log repository server.
     * @param positions horse positions.
     */
    @Override
    public void setHorsesAtTheEnd(int[] positions) {
        ClientCom cl_com = new ClientCom(logName, logPort);
        Message msg_in, msg_out;        
        
        while(!cl_com.open()){            
            try{
                sleep((long) (10));
            }catch(InterruptedException e){
                e.printStackTrace();
            }
        }
        msg_out = new Message(Message.REQSETHORSESATEND, positions);
        cl_com.write(msg_out);
        msg_in = (Message) cl_com.read();
        if ((msg_in.getType () != Message.RESPSETHORSESATEND)){
           GenericIO.writelnString ("Log_Poxy : Invalid Type! " + msg_in.getType());
           System.exit (1);
        }
        cl_com.close();
    }
    
    /**
     * Log_Proxy setRaceNumber function.
     * Sends REQSETRACENUMBER message to log repository server.
     * Receive RESPSETRACENUMBER message from log repository server.
     * @param currentRace current race id.
     */
    @Override
    public void setRaceNumber(int currentRace) {
        ClientCom cl_com = new ClientCom(logName, logPort);
        Message msg_in, msg_out;        
        
        while(!cl_com.open()){            
            try{
                sleep((long) (10));
            }catch(InterruptedException e){
                e.printStackTrace();
            }
        }
        msg_out = new Message(Message.REQSETRACENUM, 0, currentRace);
        cl_com.write(msg_out);
        msg_in = (Message) cl_com.read();
        if ((msg_in.getType () != Message.RESPSETRACENUM)){
           GenericIO.writelnString ("Log_Poxy : Invalid Type! " + msg_in.getType());
           System.exit (1);
        }
        cl_com.close();
    }
    
    /**
     * Log_Proxy finishLog function.
     * Sends REQFINISH message to log repository server.
     * Receive RESPFINISH message from log repository server.
     */
    @Override
    public void finishLog() {
        ClientCom cl_com = new ClientCom(logName, logPort);
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
           GenericIO.writelnString ("Log_Poxy : Invalid Type! " + msg_in.getType());
           System.exit (1);
        }
        cl_com.close();
    }
    
}