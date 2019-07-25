/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package server_side;

import client_side.mythreadbroker.MyThreadBroker;
import client_side.mythreadhorses.MyThreadHorses;
import client_side.mythreadspec.MyThreadSpec;

/**
 * @author pedro
 * @author franciscoteixeira
 */
public interface Log_Interface {
    public void printFirst();
    public void changeLog();
    public int getRaceNumber();
    public int getTravelDistance();
    public void setBrokerState(MyThreadBroker.Broker_States state);
    public void setSpectatorState(int spec_id, MyThreadSpec.Spec_States state);
    public void setSpectatorMoney(int spec_id, int money);
    public void setSpectatorMoneyToBet(int spec_id, int horse_id, int money_to_bet);
    public void setSpectatorHorseSel(int spec_id, int horse_id);
    public void setHorseState(int horse_id, MyThreadHorses.Horse_States state);
    public void setHorseLength(int horse_id, int length);
    public void setHorseOdds(int horse_id, int odds);
    public void setHorseIT(int horse_id, int horse_it);
    public void setHorsePos(int[] horse_p);
    public void setHorsesAtTheEnd(int[] positions);
    public void setRaceNumber(int currentRace);
    public void finishLog();
}
