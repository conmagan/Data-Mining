/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package analysis.jvntextpro.lib.pairs;

import analysis.jvntextpro.lib.string.StrUtil;

/**
 *
 * @author hieupx
 */
public class PairStrDouble implements Comparable {
    public String first;
    public Double second;
    
    public PairStrDouble(String f, double s) {
        this.first = f;
        this.second = s;
    }
    
    @Override
    public int compareTo(Object o) {
        double val1 = second.doubleValue();
        double val2 = ((PairStrDouble)o).second.doubleValue();
        
        if (val1 > val2) {
            return 1;
        } else if (val1 == val2) {
            return 0;
        } else {
            return -1;
        }
    }
    
    public void print() {
        System.out.println(this.first + "\t\t\t" + this.second);
    }
    
    @Override
    public String toString() {
        return StrUtil.normalizeStr(this.first);
    }
}
