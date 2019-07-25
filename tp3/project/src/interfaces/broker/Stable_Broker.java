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
public interface Stable_Broker extends Remote{
    public void openEvent(VectorClock vector_clk) throws RemoteException;
    public void summonHorsesToPaddock(VectorClock vector_clk) throws RemoteException;
    public boolean checkHorses(VectorClock vector_clk) throws RemoteException;
    public boolean checkHorsesAway(VectorClock vc) throws RemoteException;
    public void SendHorsesAway(VectorClock vc) throws RemoteException;
    public void terminate() throws RemoteException;
    
}
