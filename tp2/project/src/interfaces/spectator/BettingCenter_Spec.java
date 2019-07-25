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
public interface BettingCenter_Spec {
    public void placeABet(int spec_id, int horse_id, int bet_val, int currentMoney);
    public void goWatchRace(int spec_id);
    public int collectGains(int spec_id, int currentMoney);
    public void goRelax(int spec_id);
    public void waitForNextRace(int spec_id);
    
}
