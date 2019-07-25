/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package client_side.mythreadhorses;

import proxy_monitors.Proxy_Paddock;
import proxy_monitors.Proxy_RaceTrack;
import proxy_monitors.Proxy_Stable;

/**
 *
 * @author pedro
 * @author franciscomclt
 */
public class MyThreadHorses extends Thread {
    private final int id;
    
    //Proxy Monitors for Horses
    private Proxy_Stable horse_stable;
    private Proxy_Paddock horse_paddock;
    private Proxy_RaceTrack horse_track;
    private final int agility;
    
    
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
     * @param agility horse agility;
     */
    public MyThreadHorses(int id, Proxy_Stable horse_stable, Proxy_Paddock horse_paddock, Proxy_RaceTrack horse_track, int agility){
        this.id = id;
        this.horse_stable = horse_stable;
        this.horse_paddock = horse_paddock;
        this.horse_track = horse_track;
        horse_states = Horse_States.AT_THE_STABLE;
        this.agility = agility;
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
    public void run(){
        boolean go = true;
        while(go){
            try{
                Thread.sleep(500);
            }catch(InterruptedException e){}    
            System.out.println(horse_states.toString());
            switch(horse_states) {
                case AT_THE_STABLE:
                    horse_stable.returnToStable(id);
                    horse_stable.proceedToPaddock(id);
                    break;
                case AT_THE_PADDOCK:
                    if (horse_paddock.checkForSpectators()) {
                        horse_paddock.proceedToPaddock(id, agility);
                        horse_paddock.proceedToStartLine(id);
                    }
                    break;
                case AT_THE_START_LINE:
                    horse_track.atTheStartLine(id);
                    //horse_track.makeAMove(id,distCalc());
                    break;
                case RUNNING:
                    if(!horse_track.hasFinishLineBeenCrossed(id)){// VER O CASO DE UM CAVALO AINDA TER DE TERMINAR SOZINHO E OS OUTROS JA TEREM ACABADO
                        horse_track.makeAMove(id,distCalc());           // VER NECESSIDADE DE OS CAVALOS NÃO ANDAREM A CORRER VARIAS ITERAÇÕES À FRENTE DOS OUTROS
                    }
                    break;
                case AT_THE_FINNISH_LINE:
                    horse_track.proceedToStable(id);
                    //horse_stable.returnToStable(id);    //só faz return de um cavalo para o stable....
                    //go = false;
                    break;
            }
        }
    }    
}