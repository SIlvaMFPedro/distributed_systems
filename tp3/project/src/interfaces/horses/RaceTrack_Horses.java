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
public interface RaceTrack_Horses extends Remote{
    public void atTheStartLine(int id, VectorClock vector_clk) throws RemoteException;
    public void makeAMove(int id, int d, VectorClock vector_clk) throws RemoteException;
    public boolean[] hasFinishLineBeenCrossed(int id, VectorClock vector_clk) throws RemoteException;
    public void proceedToStable(int id, VectorClock vector_clk) throws RemoteException;
    
}
