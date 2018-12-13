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
public class PairStrInt implements Comparable {
    public String first;
    public Integer second;
    
    public PairStrInt(String f, int s) {
        this.first = f;
        this.second = s;
    }
    
    @Override
    public int compareTo(Object o) {
        return this.second - ((PairStrInt)o).second;
    }
    
    public void print() {
        System.out.println(this.first + "\t\t\t" + this.second);
    }
    
    @Override
    public String toString() {
        return StrUtil.normalizeStr(this.first);
    }
}
