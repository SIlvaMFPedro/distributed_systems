/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package commonInfo;

import java.io.Serializable;
import java.util.Arrays;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class is used to represent a vector clock;
 */
/**
 *
 * @author pedro
 * @author franciscoteixeira
 */
public class VectorClock implements Serializable, Cloneable{
    private static final long serialVersionUID = 1001L;
    private final ReentrantLock rl;
    
    public int[] vector_clks;
    private int index;
    
    public VectorClock(int size, int index){
        rl = new ReentrantLock(true);
        this.index = index;
        this.vector_clks = new int[size];
        
        for(int i = 0; i < size; i++){
            this.vector_clks[i] = 0;
        }
    }
    
    public VectorClock(int size) {
        rl = new ReentrantLock(true);
        this.index = -1;
        this.vector_clks = new int[size];
        
        for(int i = 0; i < size; i++)
            this.vector_clks[i] = 0;
    }
    
    /**
     * Increments the local index declared on the constructor.
     */
    public void increment() {
        rl.lock();
        try{
            vector_clks[index]++;
        }finally{
            rl.unlock();
        }
    }
    
    /**
     * Updates the vector clock.
     * @param vector the Vector clock
     */
    public void update(VectorClock vector) {
        rl.lock();
        try{
            for (int i = 0; i < vector_clks.length; i++) {
                vector_clks[i] = Math.max(vector.vector_clks[i], this.vector_clks[i]);
            }
        }finally{
            rl.unlock();
        }
    }
    
    /**
     * Returns a deep copy of the object.
     * @return deep copy of the object
     */
    public VectorClock getCopy() {
        rl.lock();
        VectorClock copy = new VectorClock(5);
        try{
            copy =  this.clone();
        } catch (CloneNotSupportedException ex) {
            Logger.getLogger(VectorClock.class.getName()).log(Level.SEVERE, null, ex);
        }finally{
            rl.unlock();
        }
        return copy;
    }
    
    /**
     * Returns the vector clock as an integer array.
     * @return integer array containing the vector clock
     */
    public int[] toIntArray() {
        rl.lock();
        try{
            return vector_clks;
        }finally{
            rl.unlock();
        }
    }
    
    @Override
    public synchronized VectorClock clone() throws CloneNotSupportedException {
        rl.lock();
        try{
            VectorClock copy = null;        
            try { 
                copy = (VectorClock) super.clone ();
            } catch (CloneNotSupportedException e) {   
                System.err.println(Arrays.toString(e.getStackTrace()));
                System.exit(1);
            }
            copy.index = index;
            copy.vector_clks = vector_clks.clone();
            return copy;
        }finally{
            rl.unlock();
        }
    }
    
}
