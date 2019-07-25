/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package aux;

/**
 *
 * @author pedro
 * @author franciscomclt
 */
public class Bet {
    private int horse_id;
    private int value;
    
    /**
     * Bet Constructor;
     * @param horse_id bet horse id;
     * @param value bet amount value;
     */
    public Bet(int horse_id, int value) {
        this.horse_id = horse_id;
        this.value = value;
    }
    /**
     * Get Horse ID
     * @return bet horse id;
     */
    public int getHorse_id() {
        return horse_id;
    }
    /**
     * Get Value;
     * @return bet amount value;
     */
    public int getValue() {
        return value;
    }
    
}
