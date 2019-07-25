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
public interface RaceTrack_Horses {
    public void atTheStartLine(int id);
    public void makeAMove(int id, int d);
    public boolean hasFinishLineBeenCrossed(int id);
    public void proceedToStable(int id);
    
}
