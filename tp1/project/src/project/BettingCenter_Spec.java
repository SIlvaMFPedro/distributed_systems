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
public interface BettingCenter_Spec {
    public void placeABet(int spec_id, int horse_id, int bet_val);
    public void goWatchRace();
    public void collectGains(int id);
    public void goRelax();
    public void waitForNextRace();
    
}
