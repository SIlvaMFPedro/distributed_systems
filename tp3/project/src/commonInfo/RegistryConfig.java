/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package commonInfo;

import interfaces.registry.Register;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author pedro
 * @author franciscoteixeira
 */
public class RegistryConfig {  
    /**
     * RMI register service name;
     */
    public static String rmiRegisterName = "RegisterHandler";
    
    /**
     * RMI register host name;
     */
    public static String rmiRegisterHostName = "localhost";
    
    /**
     * LogRepository Monitor Name Entry.
     */
    public static String logRepositoryNameEntry = "logRepository";
    
    /**
     * Stable Monitor Name Entry.
     */
    public static String stableNameEntry = "stable";
    
    /**
     * Stand Monitor Name Entry.
     */
    public static String standNameEntry = "stand";
    
    /**
     * Paddock Monitor Name Entry.
     */
    public static String paddockNameEntry = "paddock";
    
    /**
     * Betting Center Monitor Name Entry.
     */
    public static String bettingCenterNameEntry = "betting_center";
    
    /**
     * RaceTrack Monitor Name Entry.
     */
    public static String racetrackNameEntry = "racetrack";
    
    /**
     * Repository Monitor Name Entry.
     */
    public static String repositoryNameEntry = "repository";
    
    /**
     * MyThreadBroker Name Entry.
     */
    public static String mythreadbrokerNameEntry = "mythreadbroker";
    
    /**
     * MyThreadHorses Name Entry.
     */
    public static String mythreadhorsesNameEntry = "mythreadhorses";
    
    /**
     * MyThreadSpec Name Entry.
     */
    public static String mythreadspecNameEntry = "mythreadspec";
    
    /**
     * Bash file property.
     */
    private Properties props;
    
    /**
     * RegistryConfig class constructor that receives the configuration file.
     * @param filename path to the configuration file.
     */
    public RegistryConfig(String filename){
        props = new Properties();
        InputStream input = null;
        try {
            input = new FileInputStream(filename);
            props.load(input);
            input.close();
        } catch (IOException ex) {
            Logger.getLogger(RegistryConfig.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException ex) {
                    Logger.getLogger(RegistryConfig.class.getName()).log(Level.SEVERE, null, ex);
                } 
            }
        }
    }
    
    /**
     * Loads parameter from bash file
     * @param param parameter name.
     * @return parameter value.
     */
    public String loadParam(String param){
        return props.getProperty(param);
    }
    
    /**
     * Loads REGISTER_HOST parameter from configuration file.
     * @return REGISTER_HOST value.
     */
    public String registryGetHost(){
        return loadParam("REGISTER_HOST");
    }
    
    /**
     * Loads REGISTER_PORT parameter from configuration file.
     * @return REGISTER_PORT value.
     */
    public int registryGetPort(){
        return Integer.parseInt(loadParam("REGISTER_PORT"));
    }
    
    /**
     * Loads REGISTER_OBJ_PORT parameter from configuration file.
     * @return REGISTER_OBJ_PORT value.
     */
    public int registryGetObjectPort(){
        return Integer.parseInt(loadParam("REGISTER_OBJ_PORT"));
    }
    
    /**
     * Loads LOG_REPOSITORY_PORT parameter from configuration file.
     * @return LOG_REPOSITORY_PORT value.
     */
    public int logRepositoryGetPort(){
        return Integer.parseInt(loadParam("LOG_REPOSITORY_PORT"));
    }
    
    /**
     * Loads STABLE_PORT parameter from configuration file.
     * @return STABLE_PORT value.
     */
    public int stableGetPort(){
        return Integer.parseInt(loadParam("STABLE_PORT"));
    }
    
    /**
     * Loads STAND_PORT parameter from configuration file.
     * @return STAND_PORT value.
     */
    public int standGetPort(){
        return Integer.parseInt(loadParam("STAND_PORT"));
    }
    
    /**
     * Loads PADDOCK_PORT parameter from configuration file.
     * @return PADDOCK_PORT value.
     */
    public int paddockGetPort(){
        return Integer.parseInt(loadParam("PADDOCK_PORT"));
    }
    
    /**
     * Loads BETTING_CENTER_PORT parameter from configuration file.
     * @return BETTING_CENTER_PORT value.
     */
    public int bettingCenterGetPort(){
        return Integer.parseInt(loadParam("BETTING_CENTER_PORT"));
    }
    
    /**
     * Loads RACETRACK_PORT parameter from configuration file.
     * @return RACETRACK_PORT value.
     */
    public int raceTrackGetPort(){
        return Integer.parseInt(loadParam("RACETRACK_PORT"));
    }
    
    /**
     * Loads REPOSITORY_PORT parameter from configuration file.
     * @return REPOSITORY_PORT value.
     */
    public int repositoryGetPort(){
        return Integer.parseInt(loadParam("REPOSITORY_PORT"));
    }
    
    /**
     * Loads MYTHREADBROKER_PORT parameter from configuration file.
     * @return MYTHREADBROKER_PORT value.
     */
    public int myThreadBrokerGetPort(){
        return Integer.parseInt(loadParam("MYTHREADBROKER_PORT"));
    }
    
    /**
     * Loads MYTHREADHORSES_PORT parameter from configuration file.
     * @return MYTHREADHORSES_PORT value.
     */
    public int myThreadHorsesGetPort(){
        return Integer.parseInt(loadParam("MYTHREADHORSES_PORT"));
    }
    
    /**
     * Loads MYTHREADSPECS_PORT parameter from configuration file.
     * @return MYTHREADSPECS_PORT value.
     */
    public int myThreadSpecsGetPort(){
        return Integer.parseInt(loadParam("MYTHREADSPECS_PORT"));
    }
    
    
}
