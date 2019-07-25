/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package interfaces.horses;

import commonInfo.VectorClock;
import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 *
 * @author pedro
 * @author franciscoteixeira
 */
public interface Paddock_Horses extends Remote{
    public void proceedToPaddock(int id, int agi, VectorClock vector_clk) throws RemoteException;
    public void proceedToStartLine(int id, VectorClock vector_clk) throws RemoteException;
    public boolean checkForSpectators(VectorClock vector_clk) throws RemoteException;
}
