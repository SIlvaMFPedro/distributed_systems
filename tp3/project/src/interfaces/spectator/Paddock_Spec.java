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
public interface Paddock_Spec extends Remote{
    public void waitForNextRace(int spec_id, VectorClock vector_clk) throws RemoteException;
    public int[] appraiseHorses(int spec_id, VectorClock vector_clk) throws RemoteException;
    public void goPlaceABet(int spec_id, VectorClock vector_clk) throws RemoteException;
}

