/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package commonInfo;

import client_side.mythreadbroker.MyThreadBroker;
import client_side.mythreadhorses.MyThreadHorses;
import client_side.mythreadspec.MyThreadSpec;
import genclass.GenericIO;
import java.io.*;

/**
 *   Este tipo de dados define as mensagens que são trocadas entre os clientes e o servidor numa solução do Problema
 *   do "An afternoon at the races" que implementa o modelo cliente-servidor de tipo 2 (replicação do servidor) com lançamento
 *   estático de threads.
 *   A comunicação propriamente dita baseia-se na troca de objectos de tipo Message num canal TCP.
 */

/**
 *
 * @author pedro
 */
public class Message implements Serializable{
    /**
     * Serializable key
     */
    private static final long serialVersionUID = 1001L;
    
    /**
     * Message Types
     */
    
    /**
     * BROKER MESSAGES
     */
    
    /**
     * Login file initialization(client)
     */
    public static final int SETNFIC = 1;
    
    /**
     * Login file initialization(server)
     */
    public static final int NFICDONE = 2;
    
    /**
     * Success operation
     */
    public static final int ACK = 3;
    
    /**
     * Open event request(client -- broker)
     */
    public static final int REQOPENEVENT = 4;
    
    /**
     * Open event response(server)
     */
    public static final int RESPOPENEVENT = 5;
    
    /**
     * Go check horses request(client -- broker)
     */
    public static final int REQCHECKONHORSES = 6;
    
    /**
     * Go check horses response(server)
     */
    public static final int RESPCHECKONHORSES = 7;
    
    /**
     * Summon horses request(client -- broker)
     */
    public static final int REQSUMMONHORSES = 8;
    
    /**
     * Summon horses response(server)
     */
    public static final int RESPSUMMONHORSES = 9;
    
    /**
     * Go check spectator request(client -- broker)
     */
    public static final int REQCHECKONSPEC = 10;
    
    /**
     * Go check spectator response(server)
     */
    public static final int RESPCHECKONSPEC = 11;
    
    /**
     * Announce next race request(client -- broker)
     */
    public static final int REQANNOUNCERACE = 12;
    
    /**
     * Announce next race response(server)
     */
    public static final int RESPANNOUNCERACE = 13;
    
    /**
     * Accept bets request(client -- broker)
     */
    public static final int REQACCEPTBETS = 14;
    
    /**
     * Accept bets response(server)
     */
    public static final int RESPACCEPTBETS = 15;
    
    /**
     * Wait for bets request(client --broker)
     */
    public static final int REQWAITFORBETS = 16;
    
    /**
     * Wait for bets response(server)
     */
    public static final int RESPWAITFORBETS = 17;
    
    /**
     * Go wait for horses request(client -- broker);
     */
    public static final int REQGOWAITFORHORSES = 123;
    
    /**
     * Go wait for horses response(server)
     */
    public static final int RESPGOWAITFORHORSES = 124;
    
    /**
     * Wait for horses request(client -- broker)
     */
    public static final int REQWAITFORHORSES = 18;
    
    /**
     * Wait for horses response(server)
     */
    public static final int RESPWAITFORHORSES = 19;
    
    /**
     * Start race request(client -- broker)
     */
    public static final int REQSTARTRACE = 20;
    
    /**
     * Start race response(server)
     */
    public static final int RESPSTARTRACE = 21;
    
    /**
     * Supervise race request(client -- broker)
     */
    public static final int REQSUPERVISERACE = 75;
    
    /**
     * Supervise race response(server)
     */
    public static final int RESPSUPERVISERACE = 76;
    
    /**
     * Go report results request(client -- broker)
     */
    public static final int REQGOREPORTRESULTS = 71;
    
    /**
     * Go report results response(server)
     */
    public static final int RESPGOREPORTRESULTS = 72;
    
    /**
     * Report results request(client -- broker)
     */
    public static final int REQREPORTRESULTS = 22;
    
    /**
     * Report results response(server)
     */
    public static final int RESPREPORTRESULTS = 23;
    
    /**
     * check if there are any winners request(client -- broker)
     */
    public static final int REQARETHEREANYWINNERS = 81;
           
    /**
     * check if there are any winners response(server)
     */
    public static final int RESPARETHEREANYWINNERS = 82;
    
    /**
     * Alert winner request(client -- broker)
     */
    public static final int REQALERTWINNERS = 24;
    
    /**
     * Alert winner response(server)
     */
    public static final int RESPALERTWINNERS = 25;
    
    /**
     * Honour bets request(client -- broker)
     */
    public static final int REQHONOURBETS = 26;
    
    /**
     * Honour bets response(server)
     */
    public static final int RESPHONOURBETS = 27;
    
    /**
     * Settling accounts request(client -- broker)
     */
    public static final int REQSETTLEACCOUNTS = 28;
    
    /**
     * Settling accounts response(server)
     */
    public static final int RESPSETTLEACCOUNTS = 29;
    
    /**
     * Send horses to paddock request(client -- broker)
     */
    public static final int REQSENDHORSESPAD = 30;
    
    /**
     * Send horses to paddock response(server)
     */
    public static final int RESPSENDHORSESPAD = 31;    
    
    /**
     * Check if this was the final race request(client -- broker)
     */
    public static final int REQCHECKFINALRACE = 73;
    
    /**
     * Check if this was the final race response(server)
     */
    public static final int RESPCHECKFINALRACE = 74;
    
    /**
     * Check if this was the final race request(client -- spectator)
     */
    public static final int REQCHECKFINALRACESPEC = 131;
    
    /**
     * Check if this was the final race response(server)
     */
    public static final int RESPCHECKFINALRACESPEC = 132;
    
    /**
     * Check celebrating spectators request(client -- broker)
     */
    public static final int REQCHECKCELSPECTATORS = 133;
    
    /**
     * Check celebrating spectators response(server)
     */
    public static final int RESPCHECKCELSPECTATORS = 134;
    
    /**
     * Check relax spectators request(client -- broker)
     */
    public static final int REQCHECKRELAX = 135;
    
    /**
     * Check relax spectators response(server)
     */
    public static final int RESPCHECKRELAX = 136;
    
    /**
     * Go collect the gains request(client -- spectator)
     */
    public static final int REQGOCOLLECTGAINS = 129;
    
    /**
     * Go collect the gains response(server);
     */
    public static final int RESPGOCOLLECTGAINS = 130;
    
    /**
     * Go entertain request(client -- broker)
     */
    public static final int REQGOENTERTAIN = 32;
    
    /**
     * Go entertain response(server)
     */
    public static final int RESPGOENTERTAIN = 33;
    
    
    /**
     * HORSE MESSAGES
     */
    
    /**
     * Proceed to paddock request(client -- horses)
     */
    public static final int REQPROCPADDOCK = 34;
    
    /**
     * Proceed to paddock response(server)
     */
    public static final int RESPPROCPADDOCK = 35;
    
    /**
     * Last horse signal spectator request(client -- horses)
     */
    public static final int REQSIGNALSPEC = 36;
    
    /**
     * Last horse signal spectator response(server)
     */
    public static final int RESPSIGNALSPEC = 37;
    
    /**
     * Last horse signal spectator response(server)
     */
    public static final int REQCHECKFORSPEC = 79;
    
    /**
     * Last horse signal spectator response(server)
     */
    public static final int RESPCHECKFORSPEC = 80;
    
    /**
     * Proceed to start line request(client -- horses)
     */
    public static final int REQPROCSTARTLINE = 38;
    
    /**
     * Proceed to start line response(server)
     */
    public static final int RESPPROCSTARTLINE = 39;
    
    /**
     * At the start line request(client -- horses)
     */
    public static final int REQATSTARTLINE = 77;
    
    /**
     * At the start line response(server)
     */
    public static final int RESPATSTARTLINE = 78;
    
    /**
     * Last horse signal broker request(client -- horses)
     */
    public static final int REQSIGNALBROKER = 40;
    
    /**
     * Last horse signal broker response(server)
     */
    public static final int RESPSIGNALBROKER = 41;
    
    /**
     * Make a move request(client -- horses)
     */
    public static final int REQMAKEAMOVE = 42;
    
    /**
     * Make a move response(server)
     */
    public static final int RESPMAKEAMOVE = 43;
    
    /**
     * Check finish line request(client -- horses)
     */
    public static final int REQCHECKFLINE = 44;
    
    /**
     * Check finish line response(server)
     */
    public static final int RESPCHECKFLINE = 45;
    
    /**
     * Check win request(client -- horses)
     */
    public static final int REQCHECKWIN = 46;
    
    /**
     * Check win response(server)
     */
    public static final int RESPCHECKWIN = 47;
    
    /**
     * Proceed to stable request(client -- horses)
     */
    public static final int REQPROCSTABLE = 48;
    
    /**
     * Proceed to stable response(server)
     */
    public static final int RESPPROCSTABLE = 49;
    
    /**
     * Return to stable request(client -- horses)
     */
    public static final int REQRETURNTOSTABLE = 69;
    
    /**
     * Return to stable response(server)
     */
    public static final int RESPRETURNTOSTABLE = 70;
    
    /**
     * SPECTATOR MESSAGES
     */
    
    /**
     * Wait for next race request(client -- spectator
     */
    public static final int REQWAITFORNEXTRACE = 50;
    
    /**
     * Wait for next race response(server)
     */
    public static final int RESPWAITFORNEXTRACE = 51;
    
    /**
     * Go check horses request(client -- spectator)
     */
    public static final int REQGOCHECKHORSES = 52;
    
    /**
     * Go check horses response(server)
     */
    public static final int RESPGOCHECKHORSES = 53;
    
    /**
     * Go appraise horses request(client -- spectator)
     */
    public static final int REQAPPRAISEHORSES = 54;
    
    /**
     * Go appraise horse response(server)
     */
    public static final int RESPAPPRAISEHORSES = 55;
    
    /**
     * Go place a bet request(client -- spectator)
     */
    public static final int REQGOPLACEABET = 127;
    
    /**
     * Go place a bet response(server)
     */
    public static final int RESPGOPLACEABET = 128;
    
    /**
     * Go place a bet request(client -- spectator)
     */
    public static final int REQPLACEABET = 56;
    
    /**
     * Go place a bet response(server)
     */
    public static final int RESPPLACEABET = 57;
    
     /**
     * Go place a bet request(client -- spectator)
     */
    public static final int REQGOWATCHRACE = 125;
    
    /**
     * Go place a bet response(server)
     */
    public static final int RESPGOWATCHRACE = 126;
    
    /**
     * Go watch race request(client -- spectator)
     */
    public static final int REQWATCHRACE = 58;
    
    /**
     * Go watch race response(server)
     */
    public static final int RESPWATCHRACE = 59;
    
    /**
     * Go check win request(client -- spectator)
     */
    public static final int REQGOCHECKWIN = 60;
    
    /**
     * Go check win response(server)
     */
    public static final int RESPGOCHECKWIN = 61;
    
    /**
     * Go collect gains request(client -- spectator)
     */
    public static final int REQCOLLECTGAINS = 62;
    
    /**
     * Go collect gains response(server)
     */
    public static final int RESPCOLLECTGAINS = 63;
    
    /**
     * Go wait for next race request(client -- spectator)
     */
    public static final int REQGOWAITFORNEXTRACE = 64;
    
    /**
     * Go wait for next race response(server)
     */
    public static final int RESPGOWAITFORNEXTRACE = 65;
    
    /**
     * Go relax request(client -- spectator)
     */
    public static final int REQGORELAX = 66;
    
    /**
     * Go relax response(server)
     */
    public static final int RESPGORELAX = 67;
    
    /**
     * End life cycle(server)
     */
    public static final int END = 68;
    
    // LOG
    /**
     * First print of the log request
     */
    public static final int REQPRINTFIRST = 83;
    
    /**
     * First print of the log response(server)
     */
    public static final int RESPPRINTFIRST = 84;
    
    /**
     * Update log request
     */
    public static final int REQCHANGELOG = 85;
    
    /**
     * Update log response(server)
     */
    public static final int RESPCHANGELOG = 86;
    
    
    /**
     * Get race number log request
     */
    public static final int REQGETRACENUM = 87;
    
    
    /**
     * Get race number log response(server)
     */
    public static final int RESPGETRACENUM = 88;
    
    /**
     * Get track distance log request
     */
    public static final int REQGETTRAVELDIST = 89;
    
    /**
     * Get track distance log response(server)
     */
    public static final int RESPGETTRAVELDIST= 90;
    
    /**
     * Get broker state log request
     */
    public static final int REQGETBROKERSTATE = 91;
    
    /**
     * Get broker state log response(server)
     */
    public static final int RESPGETBROKERSTATE = 92;
    
    /**
     * Get spectator state log request
     */
    public static final int REQGETSPECSTATE = 93;
    
    /**
     * Get spectator state log response(server)
     */
    public static final int RESPGETSPECSTATE = 94;
    
    /**
     * Get horse state log request
     */
    public static final int REQGETHORSESTATE = 95;
    
    /**
     * Get horse state log response(server)
     */
    public static final int RESPGETHORSESTATE = 96;
    
    /**
     * Set broker state log request
     */
    public static final int REQSETBROKERSTATE = 97;
    
    /**
     * Set broker state log response(server)
     */
    public static final int RESPSETBROKERSTATE = 98;
    
    /**
     * Set spectator state log request
     */
    public static final int REQSETSPECSTATE = 99;
    
    /**
     * Set spectator state log response(server)
     */
    public static final int RESPSETSPECSTATE = 100;
    
    /**
     * Set spectator money log request
     */
    public static final int REQSETSPECMONEY = 101;
    
    /**
     * Set spectator money log response(server)
     */
    public static final int RESPSETSPECMONEY = 102;
    
    /**
     * Set spectator money to bet log request
     */
    public static final int REQSETSPECMONEYTOBET = 103;
    
    /**
     * Set spectator money to bet log response(server)
     */
    public static final int RESPSETSPECMONEYTOBET = 104;
    
    /**
     * Set spectator horse selected for the bet log request
     */
    public static final int REQSETSPECHORSETOSEL = 105;
    
    /**
     * Set spectator horse selected for the bet log response(server)
     */
    public static final int RESPSETSPECHORSETOSEL = 106;
    
    /**
     * Set horse state log request
     */
    public static final int REQSETHORSESTATE = 107;
    
    /**
     * Set horse state log response(server)
     */
    public static final int RESPSETHORSESTATE = 108;
    
    /**
     * Set horse's length traveled log request
     */
    public static final int REQSETHORSELEN = 109;
    
    /**
     * Set horse's length traveled log response(server)
     */
    public static final int RESPSETHORSELEN = 110;
    
    /**
     * Set horse's odds to win log request
     */
    public static final int REQSETHORSEODDS = 111;
    
    /**
     * Set horse's odds to win log response(server)
     */
    public static final int RESPSETHORSEODDS = 112;
    
    /**
     * Set horse's current iteration in the race log request
     */
    public static final int REQSETHORSEIT = 113;
    
    /**
     * Set horse's current iteration in the race log response(server)
     */
    public static final int RESPSETHORSEIT = 114;
    
    /**
     * Set horse's current position in the race log request
     */
    public static final int REQSETHORSEPOS = 115;
    
    /**
     * Set horse's current position in the race log response(server)
     */
    public static final int RESPSETHORSEPOS = 116;
    
    /**
     * Set horse's at the end in the race log request
     */
    public static final int REQSETHORSESATEND = 117;
    
    /**
     * Set horse's at the end in the race log response(server)
     */
    public static final int RESPSETHORSESATEND = 118;
    
    /**
     * Set race number in the race log request
     */
    public static final int REQSETRACENUM = 119;
    
    /**
     * Set race number in the race log response(server)
     */
    public static final int RESPSETRACENUM = 120;
    
    /**
     * Finish log file request
     */
    public static final int REQFINISH = 121;
    
    /**
     * Finish log file response(server)
     */
    public static final int RESPFINISH = 122;
    
    /**
     * End monitors request
     */
    public static final int REQENDMONITORS = 666;
    
    /**
     * End monitors response(server)
     */
    public static final int RESPENDMONITORS = 667;
    
    //Message fields
    
    private int msgType = -1;
    
    private int horseID = -1;
    
    private int specID = -1;
    
    private int brokerID = -1;
    
    private int id;
    
    private boolean auxbool;
    
    private int addedInfo;
    
    private int addedInfo2;
    
    private int[] info;
    
    private MyThreadBroker.Broker_States broker_state;
    
    private MyThreadHorses.Horse_States horse_state;
    
    private MyThreadSpec.Spec_States spec_state;
            
    //BROKER FLAGS;
    private boolean start_event = false;
    
    private boolean check_horses = false;
    
    private boolean summon_horses = false;
    
    private boolean check_spec = false;
    
    private boolean announce_race = false;
    
    private boolean accept_bets = false;
    
    private boolean wait_for_bets = false;
    
    private boolean wait_for_horses = false;
    
    private boolean start_race = false;
    
    private boolean report_results = false;
    
    private boolean alert_winners = false;
    
    private boolean honour_bets = false;
    
    private boolean settle_accounts = false;
    
    private boolean send_horses_paddock = false;
    
    private boolean go_entertain = false;
    
    private boolean are_there_any_winners = false;
    
    private boolean check_cel_spectators = false;
    
    private boolean end_monitors = false;
    
    
    
    //HORSE FLAGS
    private boolean proceed_paddock = false;
    
    private boolean signal_spec = false;
    
    private boolean proceed_start_line = false;
    
    private boolean signal_broker = false;
    
    private boolean make_a_move = false;
    
    private boolean check_f_line = false;
    
    private boolean check_win = false;
    
    private boolean proceed_stable = false;
    
    private boolean return_stable = false;
    
    //SPECTATOR FLAGS
    private boolean wait_for_next_race = false;
    
    private boolean go_check_horses = false;
    
    private boolean appraise_horses = false;
    
    private boolean place_a_bet = false;
    
    private boolean watch_race = false;
    
    private boolean go_check_win = false;
    
    private boolean collect_gains = false;
    
    private boolean go_wait_for_next_race = false;
    
    private boolean go_relax = false;  
    
    private boolean check_final_race = false;
    
    //Log Flag
    private boolean log_bool = false; 
    
    /**
     * Message constructor
     * @param type type of message
     * @param id Id of spectator/horse/celebrating count/relax count
     */
    public Message(int type, int id){
        msgType = type;
        this.id=id;
    }
    
    /**
     * Message constructor
     * @param type type of message
     * @param value boolean value
     */
    public Message(int type, boolean value){
        msgType = type;
        switch(type){
            //BROKER
            case RESPOPENEVENT:
                start_event = value;
                break;
            case RESPCHECKONHORSES:
                check_horses = value;
                break;
            case RESPSUMMONHORSES:
                summon_horses = value;
                break;
            case RESPCHECKONSPEC:
                check_spec = value;
                break;
            case RESPANNOUNCERACE:
                announce_race = value;
                break;
            case RESPACCEPTBETS:
                accept_bets = value;
                break;
            case RESPWAITFORBETS:
                wait_for_bets = value;
                break;
            case RESPSTARTRACE:
                start_race = value;
                break;
            case RESPREPORTRESULTS:
                report_results = value;
                break;
            case RESPALERTWINNERS:
                alert_winners = value;
                break;
            case RESPHONOURBETS:
                honour_bets = value;
                break;
            case RESPSETTLEACCOUNTS:
                settle_accounts = value;
                break;
            case RESPSENDHORSESPAD:
                send_horses_paddock = value;
                break;
            case RESPGOENTERTAIN:
                go_entertain = value;
                break;
            case RESPGOWAITFORHORSES:
                break;
            case RESPWAITFORHORSES:
                break;
            case RESPGOPLACEABET:
                break;
            case RESPENDMONITORS:
                end_monitors = value;
                break;
            //HORSES
            case RESPPROCPADDOCK:
                proceed_paddock = value;
                break;
            case RESPSIGNALSPEC:
                signal_spec = value;
                break;
            case RESPCHECKFORSPEC:
                check_spec = value;
                break;
            case RESPPROCSTARTLINE:
                proceed_start_line = value;
                break;
            case RESPSIGNALBROKER:
                signal_broker = value;
                break;
            case RESPMAKEAMOVE:
                make_a_move = value;
                break;
            case RESPCHECKFLINE:
                check_f_line = value;
                break;
            case RESPCHECKWIN:
                check_win = value;
                break;
            case RESPPROCSTABLE:
                proceed_stable = value;
                break;
            case RESPRETURNTOSTABLE:
                return_stable = value;
                break;
            //SPECTATOR
            case RESPWAITFORNEXTRACE:
                wait_for_next_race = value;
                break;
            case RESPGOCHECKHORSES:
                go_check_horses = value;
                break;
            case RESPAPPRAISEHORSES:
                appraise_horses = value;
                break;
            case RESPPLACEABET:
                place_a_bet = value;
                break;
            case RESPWATCHRACE:
                watch_race = value;
                break;
            case RESPGOCHECKWIN:
                go_check_win = value;
                break;
            case RESPCOLLECTGAINS:
                collect_gains = value;
                break;
            case RESPGOWAITFORNEXTRACE:
                go_wait_for_next_race = value;
                break;
            case RESPGORELAX:
                go_relax = value;
            case RESPPRINTFIRST:
                log_bool=value;
                break;
            case RESPCHANGELOG:
                log_bool=value;
                break;
            case RESPGETRACENUM:
                log_bool=value;
                break;
            case RESPGOCOLLECTGAINS:
                break;
            case RESPGETTRAVELDIST:
                log_bool=value;
                break;
            case RESPGETBROKERSTATE:
                log_bool=value;
                break;
            case RESPGETSPECSTATE:
                log_bool=value;
                break;
            case RESPGETHORSESTATE:
                log_bool=value;
                break;
            case RESPSETBROKERSTATE:
                log_bool=value;
                break;
            case RESPSETSPECSTATE:
                log_bool=value;
                break;
            case RESPSETSPECMONEY:
                log_bool=value;
                break;
            case RESPSETSPECMONEYTOBET:
                log_bool=value;
                break;
            case RESPSETSPECHORSETOSEL:
                log_bool=value;
                break;
            case RESPSETHORSESTATE:
                log_bool=value;
                break;
            case RESPSETHORSELEN:
                log_bool=value;
                break;
            case RESPSETHORSEODDS:
                log_bool=value;
                break;
            case RESPSETHORSEIT:
                log_bool=value;
                break;
            case RESPSETHORSEPOS:
                log_bool=value;
                break;
            case RESPSETHORSESATEND:
                log_bool=value;
                break;
            case RESPSETRACENUM:
                log_bool=value;
                break;
            case RESPFINISH:
                log_bool=value;
                break;            
            case RESPGOWATCHRACE:
                break;
            case RESPATSTARTLINE:
                break;
            case RESPGOREPORTRESULTS:
                break;
            case RESPCHECKFINALRACE:
                check_final_race=value;
                break;
            case RESPCHECKFINALRACESPEC:
                check_final_race=value;
                break;
            case RESPSUPERVISERACE:
                break;
            case RESPARETHEREANYWINNERS:
                are_there_any_winners=value;
                break;
            case END:
                break;
            default:
                GenericIO.writelnString("ERROR! Invalid message type received!");
                break;
        }
    }
    
    /**
     * Message constructor
     * @param type message type
     * @param value boolean value
     * @param id message id
     * @param info message info
     */
    public Message(int type, boolean value, int id, int[] info){
        msgType = type;
        this.id = id;
        this.info = info;
        switch(type){
            case RESPREPORTRESULTS:
                report_results = value;
                break;
            case RESPAPPRAISEHORSES:
                appraise_horses = value;
                break;
            default:
                GenericIO.writelnString("ERROR! Invalid message type received!");
                break;
        }
     }
    
    /**
     * Message constructor
     * @param type type of message
     * @param info info required by some functions
     * @param id Id of spectator/horse
     */
    public Message(int type, int[] info, int id){
        msgType = type;
        this.info = info;
        this.id=id;
    }
    
    /**
     * Message constructor
     * @param type type of message
     * @param info info required by some functions
     */
    public Message(int type, int[] info){
        msgType = type;
        this.info = info;
    }
    
    /**
     * Message constructor
     * @param type type of message
     * @param addedInfo info required by some functions
     * @param id Id of spectator/horse
     */
    public Message(int type, int id, int addedInfo){
        msgType = type;
        this.addedInfo = addedInfo;
        this.id=id;
    }
    
    public Message(int type, boolean value, boolean aux){
        msgType = type   ;     
        if(type!=RESPCHECKFLINE)
            GenericIO.writelnString("ERROR! Invalid message type received!");
        check_f_line = value;
        auxbool = aux;
    }
       
    /**
     * Message constructor
     * @param type type of message
     * @param addedInfo info required by some functions
     * @param id Id of thread calling the function in client
     * @param horseID id of horse
     */
    public Message(int type, int id, int horseID, int addedInfo){
        msgType = type;
        this.addedInfo = addedInfo;
        this.id=id;
        this.horseID=horseID;
    }
    
    /**
     * Message Constructor
     * @param type message type.
     * @param id ID of thread calling the function in client
     * @param horseID horse ID
     * @param addedInfo info required by some functions
     * @param addedInfo2 additional info required by some functions
     */
    public Message(int type, int id, int horseID, int addedInfo, int addedInfo2){
        msgType = type;
        this.addedInfo = addedInfo;
        this.addedInfo2 = addedInfo2;
        this.id=id;
        this.horseID=horseID;
    }
    
    /**
     * Message constructor
     * @param type type of message
     * @param broker_state broker state
     */
    public Message(int type, MyThreadBroker.Broker_States broker_state){
        msgType = type;
        this.broker_state = broker_state;
    }
    
    /**
     * Message constructor
     * @param type type of message
     * @param id id of horse
     * @param horse_state horse state
     */
    public Message(int type, int id, MyThreadHorses.Horse_States horse_state){
        msgType = type;
        this.id = id;
        this.horse_state = horse_state;
    }
    
    /**
     * Message constructor
     * @param type type of message
     * @param id id of spectator
     * @param spec_state spectator state
     */
    public Message(int type, int id, MyThreadSpec.Spec_States spec_state){
        msgType = type;
        this.id = id;
        this.spec_state = spec_state;
    }    
    
    /**
     * Message constructor
     * @param type type of message
     */
    public Message(int type){
        msgType = type;
        /*
        if(msgType == RESPENDMONITORS){
            end_monitors = true;
        }
        else{
            end_monitors = false;
        }
        */
    }
    
    /**
     * Message constructor
     * @param type type of message
     * @param value boolean value
     * @param id Id of spectator/horse
     */
    public Message(int type, boolean value, int id){
        msgType = type;
        this.id=id;        
    }
    
    /**
     * Message constructor
     * @param type type of message
     * @param id race id
     * @param results race results
     */
    public Message(int type, int id, int[] results){
        msgType = type;
        this.id = id;
        this.info = results;
    }
    
    public boolean getStartEvent(){
        return (start_event);
    }
    
    public boolean getCheckHorses(){
        return (check_horses);
    }
    
    public boolean getSummonHorses(){
        return (summon_horses);
    }
    
    public boolean getCheckSpec(){
        return (check_spec);
    }
    
    public boolean getAnnounceRace(){
        return (announce_race);
    }
    
    public boolean getAcceptBets(){
        return (accept_bets);
    }
    
    public boolean getWaitForBets(){
        return (wait_for_bets);
    }
    
    public boolean getWaitForHorses(){
        return (wait_for_horses);
    }
    
    public boolean getStartRace(){
        return (start_race);
    }
    
    public boolean getReportResults(){
        return (report_results);
    }
    
    public boolean getAlertWinners(){
        return (alert_winners);
    }
    
    public boolean getHonourBets(){
        return (honour_bets);
    }
    
    public boolean getSettleAccounts(){
        return (settle_accounts);
    }
    
    public boolean getSendHorsesPaddock(){
        return (send_horses_paddock);
    }
    
    public boolean getGoEntertain(){
        return (go_entertain);
    }
    
    public boolean getProcPaddock(){
        return (proceed_paddock);
    }
    
    public boolean getSignalSpec(){
        return (signal_spec);
    }
    
    public boolean getProcStartLine(){
        return (proceed_start_line);
    }
    
    public boolean getSignalBroker(){
        return (signal_broker);
    }
    
    public boolean getMakeAMove(){
        return (make_a_move);
    }
    
    public boolean getCheckFinishLine(){
        return (check_f_line);
    }
    
    public boolean getCheckWin(){
        return (check_win);
    }
    
    public boolean getProcStable(){
        return (proceed_stable);
    }
    
    public boolean getReturnStable(){
        return (return_stable);
    }
    
    public boolean getWaitForNextRace(){
        return (wait_for_next_race);
    }
    
    public boolean getGoCheckHorses(){
        return (go_check_horses);
    }
    
    public boolean getAppraiseHorses(){
        return (appraise_horses);
    }
    
    public boolean getPlaceABet(){
        return (place_a_bet);
    }
    
    public boolean getWatchRace(){
        return (watch_race);
    }
    
    public boolean getGoCheckWin(){
        return (go_check_win);
    }
    
    public boolean getCollectGains(){
        return (collect_gains);
    }
    
    public boolean getGoWaitForNextRace(){
        return (go_wait_for_next_race);
    }
    
    public boolean getGoRelax(){
        return (go_relax);
    }
    
    public boolean getCheckFinalRace(){
        return (check_final_race);
    }

    public boolean getAreThereAnyWinners() {
        return are_there_any_winners;
    }
    
    public boolean getCheckCelSpectators() {
        return check_cel_spectators;
    }
    
    public boolean getEndMonitors(){
        return end_monitors;
    }
    public int getType(){
        return (msgType);
    }
    
    public int getHorseID(){
        return (horseID);
    }
    
    public int getSpecID(){
        return (specID);
    }
    
    public int getBrokerID(){
        return (brokerID);
    }
    
    public boolean hasID(){
        return (id != -1);
    }
    
    public int getId(){
        return (id);
    }

    public boolean getAuxBool() {
        return auxbool;
    }
    
    public int[] getInfo(){
        return (info);
    }

    public int getAddedInfo() {
        return addedInfo;
    }
    
    public int getAddedInfo2() {
        return addedInfo2;
    }
    
    public MyThreadBroker.Broker_States getBroker_state() {
        return broker_state;
    }

    public MyThreadHorses.Horse_States getHorse_state() {
        return horse_state;
    }

    public MyThreadSpec.Spec_States getSpec_state() {
        return spec_state;
    }

}
