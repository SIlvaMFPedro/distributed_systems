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
public interface Paddock_Broker extends Remote{
    public void announceNextRace(VectorClock vector_clk) throws RemoteException;
    public void acceptTheBets(VectorClock vector_clk) throws RemoteException;
    public boolean checkSpecCount(VectorClock vector_clk) throws RemoteException;
    public void terminate() throws RemoteException;
}
