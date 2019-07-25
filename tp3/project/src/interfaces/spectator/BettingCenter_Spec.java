/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package interfaces.spectator;

import commonInfo.VectorClock;
import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 *
 * @author pedro
 * @author franciscoteixeira
 */
public interface BettingCenter_Spec extends Remote{
    public void placeABet(int spec_id, int horse_id, int bet_val, int currentMoney, VectorClock vector_clk) throws RemoteException;
    public void goWatchRace(int spec_id, VectorClock vector_clk) throws RemoteException;
    public int collectGains(int spec_id, int currentMoney, VectorClock vector_clk) throws RemoteException;
    public void goRelax(int spec_id, VectorClock vector_clk) throws RemoteException;
    public void waitForNextRace(int spec_id, VectorClock vector_clk) throws RemoteException;
    
}
