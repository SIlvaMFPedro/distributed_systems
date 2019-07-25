/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package interfaces.broker;


/**
 *
 * @author pedro
 * @author franciscomclt
 */
public interface Paddock_Broker {
    public void announceNextRace();
    public void acceptTheBets();
    public boolean checkSpecCount();
}
