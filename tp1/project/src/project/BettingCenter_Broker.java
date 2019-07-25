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
public interface BettingCenter_Broker {
    public void waitForBets();
    public void goWaitForHorses();
    public void settlingAccounts();
    public void horsesToPaddock();
    public void entertain();
    public boolean areThereAnyWinners(int[] a);
}
