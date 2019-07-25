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
public interface Stand_Spec {
    public void waitForNextRace(int spec_id);
    public void goCheckHorses(int spec_id, int currentMoney);
    public void watchingTheRace(int spec_id);
    public void goCheckWin(int spec_id);
    public void celebrate(int spec_id);
}
