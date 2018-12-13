/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package analysis.jvntextpro.mlearning.data.sequence;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

/**
 * @author hieupx
 */
public class Observation {
    private List<String> tokens = null;
    private String tag = null;

    public Observation() {
        tokens = new ArrayList();
    }

    public Observation(String tok, String tg) {
        tokens = new ArrayList();
        tokens.add(tok);
        tag = tg;
    }

    public Observation(List<String> toks, String tg) {
        tokens = new ArrayList();

        for (int i = 0; i < toks.size(); i++) {
            tokens.add(toks.get(i));
        }

        tag = tg;
    }

    public Observation(List<String> toks, boolean hasTag) {
        tokens = new ArrayList();

        int len = toks.size();

        if (hasTag) {
            len--;
        }

        for (int i = 0; i < len; i++) {
            tokens.add(toks.get(i));
        }

        if (hasTag) {
            tag = toks.get(len);
        }
    }

    public Observation(List<String> toks) {
        this(toks, false);
    }

    public Observation(String str, boolean hasTag) {
        tokens = new ArrayList();

        StringTokenizer strtok = new StringTokenizer(str, " \t\r\n");
        int len = strtok.countTokens();

        if (hasTag) {
            len--;
        }

        for (int i = 0; i < len; i++) {
            tokens.add(strtok.nextToken());
        }

        if (hasTag) {
            tag = strtok.nextToken();
        }
    }

    public Observation(String str) {
        this(str, false);
    }

    public String getToken(int idx) {
        String res = null;

        if (idx >= 0 && idx < tokens.size()) {
            res = tokens.get(idx);
        }

        return res;
    }

    public List<String> getTokens() {
        return tokens;
    }

    public void setTokens(List<String> toks) {
        tokens.clear();

        for (int i = 0; i < toks.size(); i++) {
            tokens.add(toks.get(i));
        }
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tg) {
        tag = tg;
    }

    @Override
    public String toString() {
        String res = "";
        if (tokens.size() > 0) {
            res = tokens.get(0);
        }

        for (int i = 1; i < tokens.size(); i++) {
            res += "\t" + tokens.get(i);
        }

        if (tag != null) {
            res += "\t" + tag;
        }

        return res;
    }

    public void write() {
        System.out.println(toString());
    }

    public void write(Writer out) throws IOException {
        out.write(toString() + "\n");
    }
}

