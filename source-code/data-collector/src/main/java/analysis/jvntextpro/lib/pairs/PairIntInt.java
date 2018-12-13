/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package analysis.jvntextpro.lib.pairs;

/**
 *
 * @author hieupx
 */
public class PairIntInt implements Comparable {
    public Integer first;
    public Integer second;
    
    public PairIntInt(int f, int s) {
        this.first = f;
        this.second = s;
    }
    
    @Override
    public int compareTo(Object o) {
        return this.second - ((PairIntInt)o).second;
    }
}
