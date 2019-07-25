/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package interfaces.horses;

/**
 *
 * @author pedro
 * @author franciscomclt
 */
public interface Paddock_Horses {
    public void proceedToPaddock(int id, int agi);
    public void proceedToStartLine(int id);
    public boolean checkForSpectators();
}
