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
public interface Repository_Broker {
    public void reportResults(int race_id, int[] winner_id);
    public void alertWinners();
    public boolean checkFinalRace();
    
}
