/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package analysis.jvntextpro.mlearning.data.sequence;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

/**
 * @author hieupx
 */
public class Sequence {
    private List<Observation> obsrs = null;

    public Sequence() {
        obsrs = new ArrayList();
    }

    public void addObservation(Observation obs) {
        obsrs.add(obs);
    }

    public void addObservation(String str, boolean hasTag) {
        Observation obs = new Observation(str, hasTag);
        obsrs.add(obs);
    }

    public void addObservation(String str) {
        addObservation(str, false);
    }

    public String getTagAt(int pos) {
        return obsrs.get(pos).getTag();
    }

    public List<String> getTextAt(int pos) {
        return obsrs.get(pos).getTokens();
    }

    public void clear() {
        obsrs.clear();
    }

    public int size() {
        return obsrs.size();
    }

    public void write() {
        if (obsrs.size() <= 0) {
            return;
        }

        for (int i = 0; i < obsrs.size(); i++) {
            obsrs.get(i).write();
        }
        System.out.println();
    }

    public void write(Writer out) throws IOException {
        if (obsrs.size() <= 0) {
            return;
        }

        for (int i = 0; i < obsrs.size(); i++) {
            obsrs.get(i).write(out);
        }
        out.write("\n");
    }
}

