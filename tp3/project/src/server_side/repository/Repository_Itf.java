/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server_side.repository;

import commonInfo.GlobalInfo;
import commonInfo.Message;
import commonInfo.MessageException;
import server_side.Server_Channel;
import server_side.Server_Interface;



/**
 *
 * @author pedro
 */
public class Repository_Itf implements Server_Interface{
    private Repository repository;
    private boolean end_of_service;
    private boolean end_of_everything;
    
    /**
     * Repository Server Interface Constructor
     * @param repository Repository monitor
     */
    public Repository_Itf(Repository repository){
        this.repository = repository;
        this.end_of_service = false;
        this.end_of_everything = false;
    }
    
    /**
     * Repository Interface processAndReply function
     * @param msg_in message in
     * @param sch server channel
     * @return message out
     */
    @Override
    public Message processAndReply(Message msg_in, Server_Channel sch){
        Message msg_out = null;
        switch(msg_in.getType()){
            case Message.REQREPORTRESULTS:
                repository.reportResults(msg_in.getId(), msg_in.getInfo());
                msg_out= new Message(Message.RESPREPORTRESULTS, true);
                break;
            case Message.REQALERTWINNERS:
                repository.alertWinners();
                msg_out= new Message(Message.RESPALERTWINNERS, true);
                break;
            case Message.REQCHECKWIN:                
                msg_out= new Message(Message.RESPCHECKWIN, repository.checkWin(msg_in.getId(), msg_in.getAddedInfo()));
                break;
            case Message.REQCOLLECTGAINS:
                repository.goCollectGains(msg_in.getId());
                msg_out= new Message(Message.RESPCOLLECTGAINS, true);
                break;
            case Message.REQCHECKFINALRACE:
                msg_out= new Message(Message.RESPCHECKFINALRACE, repository.checkFinalRace());
                break;
            case Message.REQCHECKFINALRACESPEC:
                msg_out= new Message(Message.RESPCHECKFINALRACESPEC, repository.checkFinalRace(msg_in.getId()));
                break;
            case Message.END:
                this.end_of_service=true;
                break;
            case Message.REQENDMONITORS:
                msg_out= new Message(Message.RESPENDMONITORS, true);
                this.end_of_everything = msg_out.getEndMonitors();
                System.out.print("End Monitor: " + this.end_of_everything + "\n");
                break;
        }
        
        return (msg_out);
    }
    
    /**
     * Repository Interface serviceEnd function.
     * Check end of service.
     * @return true if service ended.
     */
    @Override
    public boolean serviceEnded(){
        return end_of_service;
    }
    
    /**
     * Repository Interface endOfMonitors function.
     * Check end of monitors
     * @return true if monitor ended.
     */
    @Override
    public boolean endOfMonitors(){
        System.out.println(end_of_everything);
        return end_of_everything;
    }
    
}
