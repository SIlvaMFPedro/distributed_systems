/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package interfaces.spectator;

/**
 *
 * @author pedro
 * @author franciscomclt
 */
public interface Paddock_Spec {
    public void waitForNextRace(int spec_id);
    public int[] appraiseHorses(int spec_id);
    public void goPlaceABet(int spec_id);
}

