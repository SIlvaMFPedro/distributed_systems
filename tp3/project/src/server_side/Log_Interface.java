/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server_side;

import client_side.mythreadbroker.MyThreadBroker;
import client_side.mythreadhorses.MyThreadHorses;
import client_side.mythreadspec.MyThreadSpec;

import commonInfo.VectorClock;
import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * @author pedro
 * @author franciscoteixeira
 */
public interface Log_Interface extends Remote{
    public void printFirst() throws RemoteException;
    public void changeLog() throws RemoteException;
    public int getRaceNumber() throws RemoteException;
    public int getTravelDistance() throws RemoteException;
    public void setBrokerState(MyThreadBroker.Broker_States state) throws RemoteException;
    public void setSpectatorState(int spec_id, MyThreadSpec.Spec_States state) throws RemoteException;
    public void setSpectatorMoney(int spec_id, int money) throws RemoteException;
    public void setSpectatorMoneyToBet(int spec_id, int horse_id, int money_to_bet) throws RemoteException;
    public void setSpectatorHorseSel(int spec_id, int horse_id) throws RemoteException;
    public void setHorseState(int horse_id, MyThreadHorses.Horse_States state) throws RemoteException;
    public void setHorseLength(int horse_id, int length) throws RemoteException;
    public void setHorseOdds(int horse_id, int odds) throws RemoteException;
    public void setHorseIT(int horse_id, int horse_it) throws RemoteException;
    public void setHorsePos(int[] horse_p) throws RemoteException;
    public void setHorsesAtTheEnd(int[] positions) throws RemoteException;
    public void setRaceNumber(int currentRace) throws RemoteException;
    public void finishLog() throws RemoteException;
    public void terminate() throws RemoteException;
}
