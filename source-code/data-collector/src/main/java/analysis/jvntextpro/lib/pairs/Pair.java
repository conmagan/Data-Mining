/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package analysis.jvntextpro.lib.pairs;

/**
 *
 * @author hieupx
 */
public class Pair<F, S> {
    public F first;
    public S second;
    
    public Pair(F f, S s) {
        this.first = f;
        this.second = s;
    }
}
