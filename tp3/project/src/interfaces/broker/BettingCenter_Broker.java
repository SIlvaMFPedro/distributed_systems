/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package interfaces.broker;

import commonInfo.VectorClock;
import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 *
 * @author pedro
 * @author franciscoteixeira
 */
public interface BettingCenter_Broker extends Remote{
    public void waitForBets(VectorClock vector_clk) throws RemoteException;
    public void goWaitForHorses(VectorClock vector_clk) throws RemoteException;
    public void settlingAccounts(VectorClock vector_clk) throws RemoteException;
    public void horsesToPaddock(VectorClock vector_clk) throws RemoteException;
    public void entertain(VectorClock vector_clk) throws RemoteException;
    public boolean areThereAnyWinners(int[] a, VectorClock vector_clk) throws RemoteException;
    public int getRelaxCount(VectorClock vector_clk) throws RemoteException;
    public void terminate() throws RemoteException;
}
