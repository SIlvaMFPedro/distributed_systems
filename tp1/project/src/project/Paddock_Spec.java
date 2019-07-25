/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package project;

/**
 *
 * @author pedro
 * @author franciscomclt
 */
public interface Paddock_Spec {
    public void waitForNextRace();
    public int[] appraiseHorses();
    public void goPlaceABet(int spec_id);
}

