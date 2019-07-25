/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package commonInfo;

/**
 *
 * @author pedro
 */
public class CommPorts {
    /*
    Somos o Grupo 02
    Ports:  223GX, X € [0,9] - G numero do grupo
    PC's:   l040101-wsYY.ua.pt - YY € [01,12]
    login:  sd030g - g numero do grupo
    passwd: bitcommit
    
    server : l040101-ws01.ua.pt/sd0302
    ports : 22320 - 22329 - real ports
    ports : 22340 - 22349 - test ports */
    
    /**
     * Variable that holds the address for the logRepository server.
     */
    //public final static String logRepServerName = "l040101-ws01.ua.pt";
    public final static String logRepServerName = "localhost";

    /**
     * Variable that holds the port number for the logRepository server.
     */
    public final static int logRepServerPort = 22320; //correct server port;
    //public final static int logRepServerPort = 22340;
    
    /**
     * Variable that holds the address for the Stable server.
     */
    //public final static String stableServerName = "l040101-ws02.ua.pt";
    public final static String stableServerName = "localhost";
    /**
     * Variable that holds the port number for the Stable server.
     */
    public final static int stableServerPort = 22321; //correct server port;
    //public final static int stableServerPort = 22341;
    
    /**
     * Variable that holds the address for the Stand server.
     */
    //public final static String standServerName = "l040101-ws03.ua.pt";
    public final static String standServerName = "localhost";
    /**
     * Variable that holds the port number for the Stand server.
     */
    public final static int standServerPort = 22322; //correct server port;
    //public final static int standServerPort = 22342;
    
     /**
     * Variable that holds the address for the Paddock server.
     */
    //public final static String paddockServerName = "l040101-ws04.ua.pt";
    public final static String paddockServerName = "localhost";
    /**
     * Variable that holds the port number for the Paddock server.
     */
    public final static int paddockServerPort = 22323; //correct server port;
    //public final static int paddockServerPort = 22343;
    
    /**
     * Variable that holds the address for the betting center server.
     */
    //public final static String bettingCenterServerName = "l040101-ws05.ua.pt";
    public final static String bettingCenterServerName = "localhost";

    /**
     * Variable that holds the port number for the betting center server.
     */
    public final static int bettingCenterServerPort = 22324; //correct server port;
    //public final static int bettingCenterServerPort = 22344;
    //public final static int bettingCenterServerPort = 23344; //Francisco PC only;
    
     /**
     * Variable that holds the address for the RaceTrack server.
     */
    //public final static String racetrackServerName = "l040101-ws06.ua.pt";
    public final static String racetrackServerName = "localhost";
    /**
     * Variable that holds the port number for the RaceTrack server.
     */
    public final static int racetrackServerPort = 22325; //correct server port;
    //public final static int racetrackServerPort = 22345;
    //public final static int racetrackServerPort = 23345; //Pedro PC only;
    
    /**
     * Variable that holds the address for the Repository server.
     */
    //public final static String repServerName = "l040101-ws07.ua.pt";
    public final static String repServerName = "localhost";
    
    /**
     * Variable that holds the port number for the Repository server.
     */
    public final static int repServerPort = 22326; //correct server port;
    //public final static int repServerPort = 22346;
    
    /**
     * Variable that holds the address for the MyThreadBroker server.
     */
    //public final static String brokerServerName = "l040101-ws07.ua.pt";
    public final static String brokerServerName = "localhost";
    
    /**
     * Variable that holds the port number for the Paddock server.
     */
    public final static int brokerServerPort = 22327; //correct server port;
    //public final static int brokerServerPort = 22347;
    
    /**
     * Variable that holds the address for the MyThreadHorses server.
     */
    //public final static String horsesServerName = "l040101-ws08.ua.pt";
    public final static String horsesServerName = "localhost";
    
    /**
     * Variable that holds the port number for the RaceTrack server.
     */
    public final static int horsesServerPort = 22328; //correct server port;
    //public final static int horsesServerPort = 22348;
    
    /**
     * Variable that holds the address for the MyThreadSpec server.
     */
    //public final static String specsServerName = "l040101-ws09.ua.pt";
    public final static String specsServerName = "localhost";
    
    /**
     * Variable that holds the port number for the Stable server.
     */
    public final static int specsServerPort = 22329; //correct server port;
    //public final static int specsServerPort = 22349;
    
    
    /**
     * Variable that holds the timeout value for the server sockets.
     */
    public final static int socketTimeout = 500;
}
