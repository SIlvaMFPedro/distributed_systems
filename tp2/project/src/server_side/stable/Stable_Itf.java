/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server_side.stable;

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
public class Stable_Itf implements Server_Interface{
    private Stable stable;
    private boolean end_of_service;
    private boolean end_of_everything;
    
    /**
     * Stable Server Interface constructor
     * @param stable Stable monitor
     */
    public Stable_Itf(Stable stable){
        this.stable = stable;
        this.end_of_service = false;
        this.end_of_everything = false;
    }
    
    /**
     * Stable Interface processAndReply function
     * @param msg_in message in
     * @param sch server channel
     * @return message out
     */
    @Override
    public Message processAndReply(Message msg_in, Server_Channel sch){
        Message msg_out = null;
        
        switch(msg_in.getType()){
            case Message.REQOPENEVENT:
                stable.openEvent();
                msg_out= new Message(Message.RESPOPENEVENT, true);
                break;
            case Message.REQSUMMONHORSES:
                stable.summonHorsesToPaddock();
                msg_out= new Message(Message.RESPSUMMONHORSES, true);
                break;
            case Message.REQCHECKONHORSES:
                msg_out= new Message(Message.RESPCHECKONHORSES, stable.checkHorses());
                break;
            case Message.REQPROCPADDOCK:
                stable.proceedToPaddock(msg_in.getId());
                msg_out= new Message(Message.RESPPROCPADDOCK, true);
                break;
            case Message.REQRETURNTOSTABLE:
                stable.returnToStable(msg_in.getId());
                msg_out= new Message(Message.RESPRETURNTOSTABLE, true);
                break;
            case Message.END:
                end_of_service=true;
                break;
            case Message.REQENDMONITORS:
                msg_out= new Message(Message.RESPENDMONITORS, true);
                end_of_everything = msg_out.getEndMonitors();
                System.out.print("End Monitor: " + end_of_everything + "\n");
                break;
                
        }        
        return msg_out;
    }
    
    /**
     * Stand Interface serviceEnded function.
     * Check end of service.
     * @return true if service ended.
     */
    @Override
    public boolean serviceEnded(){
        return end_of_service;
    }
    
    /**
     * Stand Interface endOfMonitors function.
     * Check end of monitors
     * @return true if monitor ended.
     */
    @Override
    public boolean endOfMonitors(){
        System.out.println(end_of_everything);
        return end_of_everything;
    }
    
    
}
