/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package analysis.jvntextpro.nlp.vnsentsegmenter;

/**
 *
 * @author hieupx
 */
public class SentEnd {
    public String name = null;
    public String instance = null;
    public int start;
    public int end;
    
    public SentEnd(String nm, String ins, int st, int ed) {
        this.name = nm;
        this.instance = ins;
        this.start = st;
        this.end = ed;
    }
    
    @Override
    public String toString() {
        return "[" + name + ", " + instance + ", " + start + ", " + end + "]";
    }
    
    public void print() {
        System.out.println(toString());
    }
}
