/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server_side.stand;

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
public class Stand_Itf implements Server_Interface{
    private Stand stand;
    private boolean end_of_service;
    private boolean end_of_everything;
    
    /**
     * Stand Server Interface Constructor
     * @param stand Stand monitor
     */
    public Stand_Itf(Stand stand){
        this.stand = stand;
        this.end_of_service = false;
        this.end_of_everything = false;
    }
    
    /**
     * Stand Interface processAndReply function
     * @param msg_in message in
     * @param sch server channel
     * @return message out
     */
    @Override
    public Message processAndReply(Message msg_in, Server_Channel sch){
        Message msg_out = null;
        
        switch(msg_in.getType()){
            case Message.REQGOREPORTRESULTS:
                stand.goReportResults();
                msg_out= new Message(Message.RESPGOREPORTRESULTS, true);
                break;
                
            case Message.REQGOENTERTAIN:
                stand.entertain();
                msg_out= new Message(Message.RESPGOENTERTAIN, true);
                break;
            case Message.REQHONOURBETS:
                stand.honourBets();
                msg_out= new Message(Message.RESPHONOURBETS, true);
                break;
            case Message.REQSENDHORSESPAD:
                stand.horsesToPaddock();
                msg_out= new Message(Message.RESPSENDHORSESPAD, true);
                break;
            case Message.REQCHECKONSPEC:
                msg_out= new Message(Message.RESPCHECKONSPEC, stand.checkSpectators());
                break;
            case Message.REQWAITFORNEXTRACE:
                stand.waitForNextRace(msg_in.getId());
                msg_out= new Message(Message.RESPWAITFORNEXTRACE, true);
                break;
            case Message.REQGOCHECKHORSES:
                stand.goCheckHorses(msg_in.getId(), msg_in.getAddedInfo());
                msg_out= new Message(Message.RESPGOCHECKHORSES, true);
                break;
            case Message.REQWATCHRACE:
                stand.watchingTheRace(msg_in.getId());
                msg_out= new Message(Message.RESPWATCHRACE, true);
                break;
            case Message.REQGOCHECKWIN:
                stand.goCheckWin(msg_in.getId());
                msg_out= new Message(Message.RESPGOCHECKWIN, true);
                break;
            case Message.REQGORELAX:
                stand.celebrate(msg_in.getId());
                msg_out= new Message(Message.RESPGORELAX, true);
                break;
            case Message.REQCHECKCELSPECTATORS:
                int celCount = stand.checkCelSpectators();
                msg_out = new Message(Message.RESPCHECKCELSPECTATORS, celCount);
                break;
            case Message.REQFINISH:
                stand.requestFinishLog();
                msg_out = new Message(Message.RESPFINISH);
                break;
            case Message.END:
                end_of_service=true;
                break;
            case Message.REQENDMONITORS:
                stand.theEnd();
                msg_out = new Message(Message.RESPENDMONITORS, true);
                end_of_everything = msg_out.getEndMonitors();
                System.out.print("End Monitor: " + end_of_everything + "\n");
                break;
        }
        
        return (msg_out);
    }
    
    /**
     * Stand Interface serviceEnd function.
     * Check end of service
     * @return true if service ended.
     */
    @Override
    public boolean serviceEnded(){
        return end_of_service;
    }
    
    /**
     * Stand Interface endOfMonitors function.
     * Check end of monitors.
     * @return true if monitor ended.
     */
    @Override
    public boolean endOfMonitors(){
        System.out.println(end_of_everything);
        return end_of_everything;
    }
}
