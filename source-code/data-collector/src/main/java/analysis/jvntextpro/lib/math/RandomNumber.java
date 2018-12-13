/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package analysis.jvntextpro.lib.math;

import java.util.Random;

/**
 * Generating random numbers
 * @author hieupx
 */
public class RandomNumber {
    private Random randomGenerator = null;
    
    /**
     * RandomNumber default constructor
     */
    public RandomNumber() {
        randomGenerator = new Random();
    }
    
    /**
     * This method generates the next random integer in [0..MAX-1], MAX depends on platform, machine architecture
     * @return the random integer
     */
    public int genNextInt() {
        return randomGenerator.nextInt();
    }
    
    /**
     * This method generates the next random integer in [0..max]
     * @param max
     * @return the random integer
     */
    public int genNextInt(int max) {
        return randomGenerator.nextInt(max);
    }
}
