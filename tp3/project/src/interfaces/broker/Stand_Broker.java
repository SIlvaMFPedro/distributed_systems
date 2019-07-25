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
public interface Stand_Broker extends Remote{
    public void goReportResults(VectorClock vector_clk) throws RemoteException;
    public void entertain(VectorClock vector_clk) throws RemoteException;
    public void honourBets(VectorClock vector_clk) throws RemoteException;
    public void horsesToPaddock(VectorClock vector_clk) throws RemoteException;
    public boolean checkSpectators(VectorClock vector_clk) throws RemoteException;
    public int checkCelSpectators(VectorClock vector_clk) throws RemoteException;
    public void theEnd(VectorClock vector_clk) throws RemoteException;
    public void requestFinishLog(VectorClock vector_clk) throws RemoteException;
    public void terminate() throws RemoteException;
}
