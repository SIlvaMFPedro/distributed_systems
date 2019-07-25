/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server_side.paddock;

import commonInfo.GlobalInfo;
import commonInfo.Message;
import commonInfo.MessageException;
import server_side.Server_Channel;
import server_side.Server_Interface;

/**
 *
 * @author pedro
 * @author franciscocmlt
 */
public class Paddock_Itf implements Server_Interface{
    private  Paddock paddock;
    private boolean end_of_service;
    private boolean end_of_everything;
    
    /**
     * Paddock Server Interface constructor
     * @param paddock Paddock Monitor.
     */
    public Paddock_Itf(Paddock paddock){
        this.paddock = paddock;
        this.end_of_service = false;
        this.end_of_everything = false;
    }
    
    /**
     * Paddock Interface processAndReply
     * @param msg_in message in
     * @param sch server channel
     * @return message out
     */
    @Override
    public Message processAndReply(Message msg_in, Server_Channel sch){
        Message msg_out = null;
        
        switch(msg_in.getType()){
            case Message.REQANNOUNCERACE:   
                paddock.announceNextRace();
                msg_out= new Message(Message.RESPANNOUNCERACE, true);
                break;
            case Message.REQCHECKONSPEC:
                msg_out= new Message(Message.RESPCHECKONSPEC, paddock.checkSpecCount());
                break;
            case Message.REQACCEPTBETS:
                paddock.acceptTheBets();
                msg_out= new Message(Message.RESPACCEPTBETS, true);
                break;
            case Message.REQPROCSTARTLINE:
                paddock.proceedToStartLine(msg_in.getId());
                msg_out= new Message(Message.RESPPROCSTARTLINE, true);
                break;
            case Message.REQPROCPADDOCK:
                paddock.proceedToPaddock(msg_in.getId(), msg_in.getAddedInfo());
                msg_out= new Message(Message.RESPPROCPADDOCK, true);
                break;
            case Message.REQCHECKFORSPEC:
                msg_out= new Message(Message.RESPCHECKFORSPEC, paddock.checkForSpectators());
                break;
            case Message.REQWAITFORNEXTRACE:
                paddock.waitForNextRace(msg_in.getId());
                msg_out= new Message(Message.RESPWAITFORNEXTRACE, true);
                break;
            case Message.REQAPPRAISEHORSES:
                msg_out= new Message(Message.RESPAPPRAISEHORSES, true, msg_in.getId(), paddock.appraiseHorses(msg_in.getId()));
                break;
            case Message.REQGOPLACEABET:
                paddock.goPlaceABet(msg_in.getId());
                msg_out= new Message(Message.RESPGOPLACEABET, true);
                break;
            case Message.END:
                end_of_service=true;
                break;
            case Message.REQENDMONITORS:
                msg_out = new Message(Message.RESPENDMONITORS, true);
                end_of_everything = msg_out.getEndMonitors();
                System.out.print("End Monitor: " + end_of_everything + "\n");
                break;
        }
        
        return (msg_out);
    }
    
    /**
     * Paddock Interface serviceEnded function.
     * Check end of service.
     * @return true if service ended
     */
    @Override
    public boolean serviceEnded(){
        return end_of_service;
    }
    
    /**
     * Paddock Interface endOfMonitors function.
     * Check end of monitors
     * @return true if monitor end.
     */
    @Override
    public boolean endOfMonitors(){
        System.out.println(end_of_everything);
        return end_of_everything;
    }
    
}
