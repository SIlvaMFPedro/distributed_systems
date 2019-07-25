/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server_side.logrepository;

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
public class LogRepository_Itf implements Server_Interface{
    private LogRepository logRepository;
    private boolean end_of_service;
    private boolean end_of_everything;
    
    /**
     * LogRepository Server Interface Constructor
     * @param logRepository LogRepository monitor.
     */
    public LogRepository_Itf(LogRepository logRepository){
        this.logRepository = logRepository;
        this.end_of_service = false;
        this.end_of_everything = false;
    }
    
    /**
     * LogRepository Interface processAndReply function
     * @param msg_in message in
     * @param sch server channel
     * @return message out
     */
    @Override
    public Message processAndReply(Message msg_in, Server_Channel sch){
        Message msg_out = null;
        
        switch(msg_in.getType()){
            case Message.REQPRINTFIRST:   
                logRepository.printFirst();
                msg_out= new Message(Message.RESPPRINTFIRST, true);
                break;
            case Message.REQCHANGELOG:   
                logRepository.changeLog();
                msg_out= new Message(Message.RESPCHANGELOG, true);
                break;
            case Message.REQGETRACENUM:
                msg_out= new Message(Message.RESPGETRACENUM, 0, logRepository.getRaceNumber());
                break;
            case Message.REQGETTRAVELDIST:
                msg_out= new Message(Message.RESPGETTRAVELDIST, 0, logRepository.getTravelDistance());
                break;
            case Message.REQSETBROKERSTATE:
                logRepository.setBrokerState(msg_in.getBroker_state());
                msg_out= new Message(Message.RESPSETBROKERSTATE);
                break;
            case Message.REQSETSPECSTATE:
                logRepository.setSpectatorState(msg_in.getId(), msg_in.getSpec_state());
                msg_out= new Message(Message.RESPSETSPECSTATE);
                break;
            case Message.REQSETSPECMONEY:
                logRepository.setSpectatorMoney(msg_in.getId(), msg_in.getAddedInfo());
                msg_out= new Message(Message.RESPSETSPECMONEY);
                break;
            case Message.REQSETSPECMONEYTOBET:
                logRepository.setSpectatorMoneyToBet(msg_in.getId(), msg_in.getHorseID(), msg_in.getAddedInfo());
                msg_out= new Message(Message.RESPSETSPECMONEYTOBET);
                break;
            case Message.REQSETSPECHORSETOSEL:
                logRepository.setSpectatorHorseSel(msg_in.getId(), msg_in.getAddedInfo());
                msg_out= new Message(Message.RESPSETSPECHORSETOSEL);
                break;
            case Message.REQSETHORSESTATE:
                logRepository.setHorseState(msg_in.getId(), msg_in.getHorse_state());
                msg_out= new Message(Message.RESPSETHORSESTATE);
                break;
            case Message.REQSETHORSELEN:
                logRepository.setHorseLength(msg_in.getId(), msg_in.getAddedInfo());
                msg_out= new Message(Message.RESPSETHORSELEN);
                break;
            case Message.REQSETHORSEODDS:
                logRepository.setHorseOdds(msg_in.getId(), msg_in.getAddedInfo());
                msg_out= new Message(Message.RESPSETHORSEODDS);
                break;
            case Message.REQSETHORSEIT:
                logRepository.setHorseIT(msg_in.getId(), msg_in.getAddedInfo());
                msg_out= new Message(Message.RESPSETHORSEIT);
                break;
            case Message.REQSETHORSEPOS:
                logRepository.setHorsePos(msg_in.getInfo());
                msg_out= new Message(Message.RESPSETHORSEPOS);
                break;
            case Message.REQSETHORSESATEND:
                logRepository.setHorsesAtTheEnd(msg_in.getInfo());
                msg_out= new Message(Message.RESPSETHORSESATEND);
                break;
            case Message.REQSETRACENUM:
                logRepository.setRaceNumber(msg_in.getAddedInfo());
                msg_out= new Message(Message.RESPSETRACENUM);
                break;
            case Message.REQFINISH:
                logRepository.finishLog();
                msg_out= new Message(Message.RESPFINISH);
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
     * LogRepository Interface serviceEnd function.
     * check end of service.
     * @return true if end of service.
     */
    @Override
    public boolean serviceEnded(){
        return end_of_service;
    }
    
    /**
     * LogRepository Interface endOfMonitors function.
     * check end of monitors.
     * @return true if end of monitors.
     */
    @Override
    public boolean endOfMonitors(){
        System.out.println(end_of_everything);
        return end_of_everything;
    }
    
}
