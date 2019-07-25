/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server_side.racetrack;

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
public class RaceTrack_Itf implements Server_Interface {
    private RaceTrack racetrack;
    private boolean end_of_service;
    private boolean end_of_everything;
    
    /**
     * RaceTrack Server Interface Constructor
     * @param racetrack RaceTrack monitor
     */
    public RaceTrack_Itf(RaceTrack racetrack){
        this.racetrack = racetrack;
        this.end_of_service = false;
        this.end_of_everything = false;
    }
    
    /**
     * RaceTrack Interface processAndReply function
     * @param msg_in message_in
     * @param sch server_channel
     * @return message_out
     */
    @Override
    public Message processAndReply(Message msg_in, Server_Channel sch){
        Message msg_out = null;
        
        switch(msg_in.getType()){
            case Message.REQWAITFORHORSES:   
                racetrack.waitForHorses();
                msg_out = new Message(Message.RESPWAITFORHORSES, true);
                break;
            case Message.REQSTARTRACE:   
                racetrack.startRace();
                msg_out = new Message(Message.RESPSTARTRACE, true);
                break;
            case Message.REQSUPERVISERACE:   
                racetrack.superviseTheRace();
                msg_out = new Message(Message.RESPSUPERVISERACE, true);
                break;
            case Message.REQREPORTRESULTS:   
                msg_out = new Message(Message.RESPREPORTRESULTS, true, msg_in.getId(), racetrack.reportResults());
                break;
            case Message.REQCHECKONHORSES:   
                msg_out = new Message(Message.RESPCHECKONHORSES, racetrack.checkOnHorses());
                break; 
            case Message.REQATSTARTLINE:   
                racetrack.atTheStartLine(msg_in.getId());
                msg_out = new Message(Message.RESPATSTARTLINE, true);
                break; 
            case Message.REQMAKEAMOVE:   
                racetrack.makeAMove(msg_in.getId(), msg_in.getAddedInfo());
                msg_out = new Message(Message.RESPMAKEAMOVE, true);
                break;
            case Message.REQCHECKFLINE:
                boolean aux = racetrack.hasFinishLineBeenCrossed(msg_in.getId());
                msg_out = new Message(Message.RESPCHECKFLINE, aux, racetrack.haveAllHorsesFinished());
                break;
            case Message.REQPROCSTABLE:   
                racetrack.proceedToStable(msg_in.getId());
                msg_out = new Message(Message.RESPPROCSTABLE, true);
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
     * RaceTrack Interface serviceEnded function
     * Check end of service
     * @return true if service ended.
     */
    @Override
    public boolean serviceEnded(){
        return end_of_service;
    }
    
    /**
     * RaceTrack Interface endOfMonitors function.
     * Check end of monitors.
     * @return true if monitor ended.
     */
    @Override
    public boolean endOfMonitors(){
        System.out.println(end_of_everything);
        return end_of_everything;
    }
    
}
