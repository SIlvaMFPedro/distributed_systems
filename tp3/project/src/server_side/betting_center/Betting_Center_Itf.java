/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server_side.betting_center;

import commonInfo.GlobalInfo;
import commonInfo.Message;
import commonInfo.MessageException;
import server_side.Server_Channel;
import server_side.Server_Interface;

/**
 *
 * @author pedro
 * @author franciscoteixeira
 */
public class Betting_Center_Itf implements Server_Interface{
    private Betting_Center betting_center;
    private boolean end_of_service;
    private boolean end_of_everything;
    
    /**
     * Betting_Center interface constructor.
     * @param betting_center Betting_Center monitor.
     */
    public Betting_Center_Itf(Betting_Center betting_center){
        this.betting_center = betting_center;
        this.end_of_service = false;
        this.end_of_everything = false;
    }
    
    /**
     * Betting_Center Interface processAndReply function
     * @param msg_in message in
     * @param sch server channel
     * @return message out.
     */
    @Override
    public Message processAndReply(Message msg_in, Server_Channel sch){
        Message msg_out = null;
        
        switch(msg_in.getType()){
            case Message.REQWAITFORBETS:   
                betting_center.waitForBets();
                msg_out= new Message(Message.RESPWAITFORBETS, true);
                break;
            case Message.REQGOWAITFORHORSES:   
                betting_center.goWaitForHorses();
                msg_out= new Message(Message.RESPGOWAITFORHORSES, true);
                break;
            case Message.REQSETTLEACCOUNTS:   
                betting_center.settlingAccounts();
                msg_out= new Message(Message.RESPSETTLEACCOUNTS, true);
                break; 
            case Message.REQSENDHORSESPAD:   
                betting_center.horsesToPaddock();
                msg_out= new Message(Message.RESPSENDHORSESPAD, true);
                break; 
            case Message.REQGOENTERTAIN:
                betting_center.entertain();
                msg_out= new Message(Message.RESPGOENTERTAIN, true);
                break; 
            case Message.REQARETHEREANYWINNERS:                
                msg_out= new Message(Message.RESPARETHEREANYWINNERS, betting_center.areThereAnyWinners(msg_in.getInfo()));
                break; 
            case Message.REQPLACEABET:
                betting_center.placeABet(msg_in.getId(), msg_in.getHorseID(), msg_in.getAddedInfo(), msg_in.getAddedInfo2());
                msg_out= new Message(Message.RESPPLACEABET, true);
                break; 
            case Message.REQGOWATCHRACE:
                betting_center.goWatchRace(msg_in.getId());
                msg_out= new Message(Message.RESPGOWATCHRACE, true);
                break;
            case Message.REQCOLLECTGAINS:                
                msg_out= new Message(Message.RESPCOLLECTGAINS, 0, betting_center.collectGains(msg_in.getId(), msg_in.getAddedInfo()));
                break;
            case Message.REQGORELAX:
                betting_center.goRelax(msg_in.getId());
                msg_out= new Message(Message.RESPGORELAX, true);
                break;
            case Message.REQCHECKRELAX:
                int relax_count = betting_center.getRelaxCount();
                msg_out = new Message(Message.RESPCHECKRELAX, relax_count);
                break;
            case Message.REQWAITFORNEXTRACE:
                betting_center.waitForNextRace(msg_in.getId());
                msg_out= new Message(Message.RESPWAITFORNEXTRACE, true);
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
        
        return (msg_out);
    }
    
    /**
     * Betting_Center interface serviceEnded function.
     * Check end of service.
     * @return true if service ended.
     */
    @Override
    public boolean serviceEnded(){
        return end_of_service;
    }
    
    /**
     * Betting_Center interface endOfMonitors function.
     * Check end of monitors
     * @return true if monitors end.
     */
    @Override
    public boolean endOfMonitors(){
        System.out.println(end_of_everything);
        return end_of_everything;
    }
}
