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
public interface RaceTrack_Broker extends Remote{
    public void superviseTheRace(VectorClock vector_clk) throws RemoteException;
    public void waitForHorses(VectorClock vector_clk) throws RemoteException;
    public void startRace(VectorClock vector_clk) throws RemoteException;
    public int[] reportResults(VectorClock vector_clk) throws RemoteException;
    public boolean checkOnHorses(VectorClock vector_clk) throws RemoteException;
    public void terminate() throws RemoteException;
}
