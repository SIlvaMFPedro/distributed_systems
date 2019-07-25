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
 * @author pedro
 * @author franciscoteixeira
 */
public interface Repository_Horses extends Remote{
    public boolean checkFinalRace(int spec_id, VectorClock vector_clk) throws RemoteException;
}
