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
public interface Repository_Spec {
    public boolean checkWin(int spec_id, int horse_id);
    public void goCollectGains(int spec_id); 
    public boolean checkFinalRace(int spec_id);
}
