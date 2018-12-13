/*
    Copyright (C) 2006 by
    
    Xuan-Hieu Phan
	
	Email:	hieuxuan@ecei.tohoku.ac.jp
		pxhieu@gmail.com
	URL:	http://www.hori.ecei.tohoku.ac.jp/~hieuxuan
	
	Graduate School of Information Sciences,
	Tohoku University
*/

package analysis.jvntextpro.mlearning.maxent;

import java.util.List;
import java.util.Map;

public class Observation {
    
    public String originalData = "";
    public int[] cps = null;
    public int humanLabel = -1;
    public int modelLabel = -1;
    
    public Observation() {
	// do nothing	
    }
    
    public Observation(int[] cps) {
	this.cps = new int[cps.length];
        System.arraycopy(cps, 0, this.cps, 0, cps.length);
    }
    
    public Observation(List intCps) {
	this.cps = new int[intCps.size()];
	
	for (int i = 0; i < intCps.size(); i++) {
	    Integer intCp = (Integer)intCps.get(i);
	    
	    this.cps[i] = intCp.intValue();
	}
    }
    
    public Observation(int humanLabel, int[] cps) {
	this.humanLabel = humanLabel;
	this.cps = new int[cps.length];
        System.arraycopy(cps, 0, this.cps, 0, cps.length);
    }    
    
    public String toString(Map lbInt2Str) {
	String res = originalData;

	String modelLabelStr = (String)lbInt2Str.get(new Integer(modelLabel));
	if (modelLabelStr != null) {
	    res += Option.labelSeparator + modelLabelStr;
	}
	
	return res;
    }
    
} // end of class Observation

