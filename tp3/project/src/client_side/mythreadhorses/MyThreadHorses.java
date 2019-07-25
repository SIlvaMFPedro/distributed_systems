/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client_side.mythreadhorses;

import commonInfo.GlobalInfo;
import commonInfo.VectorClock;
import interfaces.horses.Paddock_Horses;
import interfaces.horses.RaceTrack_Horses;
import interfaces.horses.Stable_Horses;
import interfaces.horses.Repository_Horses;
import java.rmi.RemoteException;

/**
 *
 * @author pedro
 * @author franciscoteixeira
 */
public class MyThreadHorses extends Thread {
    private final int id;
     
    //Interfaces for Horses
    Paddock_Horses horse_paddock;
    RaceTrack_Horses horse_track;
    Stable_Horses horse_stable;
    Repository_Horses horse_repository;
    
    private final int agility;
    private VectorClock vc;
    
    
    /**
     * Horse States;
     */
    public enum Horse_States {
        AT_THE_STABLE, AT_THE_PADDOCK, AT_THE_START_LINE, RUNNING, AT_THE_FINNISH_LINE;
    }
    
    public volatile Horse_States horse_states;
    
    /**
     * MyThreadHorses new constructor
     * @param id horse thread id;
     * @param agility horse agility;
     */
    public MyThreadHorses(int id, int agility){
        this.id = id;
        this.agility = agility;
        horse_states = Horse_States.AT_THE_STABLE;
    }
    
    /**
     * MyThreadHorses constructor;
     * @param id horse thread id;
     * @param horse_stable horse stable monitor;
     * @param horse_paddock horse paddock monitor;
     * @param horse_track horse racetrack monitor;
     * @param horse_repository horse repository monitor;
     * @param agility horse agility;
     */
    public MyThreadHorses(int id, Stable_Horses horse_stable, Paddock_Horses horse_paddock, RaceTrack_Horses horse_track, Repository_Horses horse_repository,int agility){
       this.id = id;
       this.horse_stable = horse_stable;
       this.horse_paddock = horse_paddock;
       this.horse_track = horse_track;
       this. horse_repository = horse_repository;
       horse_states = Horse_States.AT_THE_STABLE;
       this.agility = agility;
       vc = new VectorClock(GlobalInfo.numHorses+1, id+1);
    }
    
    /**
     * Get Horse ID
     * @return horse thread id;
     */
    public int getID(){
        return this.id;
    }
    /**
     * Calculate Horse Distance;
     * @return horse distance;
     */
    private int distCalc(){
        return (int)(Math.random()*(agility - 1) + 1);
    }
    /**
     * Run MyThreadHorses;
     */    
    @Override
    @SuppressWarnings("CallToPrintStackTrace")
    public void run(){
        boolean go = true;
        boolean aux[];//= new boolean[2];
        while(go){
            try{
                Thread.sleep(500);
            }catch(InterruptedException e){}
            try{
                System.out.println(horse_states.toString());
                switch(horse_states) {
                    case AT_THE_STABLE:
                        vc.increment();
                        if(horse_stable.returnToStable(id, vc)) {
                            horse_stable.leaveStableFinal(id, vc);
                            go=false;
                        }                        
                        else{
                            horse_stable.proceedToPaddock(id, vc);
                            horse_states = Horse_States.AT_THE_PADDOCK;
                        }
                        break;
                    case AT_THE_PADDOCK:
                        vc.increment();
                        if (horse_paddock.checkForSpectators(vc)) {
                            horse_paddock.proceedToPaddock(id, agility, vc);
                            horse_paddock.proceedToStartLine(id, vc);
                            horse_states = Horse_States.AT_THE_START_LINE;
                        }
                        break;
                    case AT_THE_START_LINE:
                        vc.increment();
                        horse_track.atTheStartLine(id, vc);
                        horse_states = Horse_States.RUNNING;
                        break;
                    case RUNNING:
                        vc.increment();
                        aux = horse_track.hasFinishLineBeenCrossed(id, vc);
                        if(!aux[0]){// VER O CASO DE UM CAVALO AINDA TER DE TERMINAR SOZINHO E OS OUTROS JA TEREM ACABADO
                            horse_track.makeAMove(id,distCalc(), vc);           // VER NECESSIDADE DE OS CAVALOS NÃO ANDAREM A CORRER VARIAS ITERAÇÕES À FRENTE DOS OUTROS
                        }
                        if(aux[1]){
                            horse_states = Horse_States.AT_THE_FINNISH_LINE;
                        }
                        break;
                    case AT_THE_FINNISH_LINE:
                        vc.increment();
                        horse_track.proceedToStable(id, vc);
                        horse_states = Horse_States.AT_THE_STABLE;
                        break;
                }
            }catch(RemoteException e){
                e.printStackTrace();
            }
        }
    }    
}