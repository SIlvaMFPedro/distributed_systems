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
public interface Stand_Broker {
    public void goReportResults();
    public void entertain();
    public void honourBets();
    public void horsesToPaddock();
    public boolean checkSpectators();
    public int checkCelSpectators();
    public void theEnd();
    public void requestFinishLog();
}
