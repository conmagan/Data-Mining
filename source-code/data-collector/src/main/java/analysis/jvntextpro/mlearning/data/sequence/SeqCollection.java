/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package analysis.jvntextpro.mlearning.data.sequence;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import analysis.jvntextpro.lib.collection.CollectionUtil;
import analysis.jvntextpro.lib.string.StrUtil;

/**
 * @author hieupx
 */
public class SeqCollection {
    private List<Sequence> seqs = null;

    public SeqCollection() {
        seqs = new ArrayList();
    }

    public SeqCollection(List<Sequence> s) {
        seqs = new ArrayList();
        for (int i = 0; i < s.size(); i++) {
            seqs.add(s.get(i));
        }
    }

    public int size() {
        return seqs.size();
    }

    public void clear() {
        seqs.clear();
    }

    public void addSequence(Sequence seq) {
        seqs.add(seq);
    }

    public void addSequences(List<Sequence> s) {
        for (int i = 0; i < s.size(); i++) {
            seqs.add(s.get(i));
        }
    }

    public void setSequences(List<Sequence> s) {
        seqs.clear();
        addSequences(s);
    }

    public Sequence getSequenceAt(int idx) {
        Sequence res = null;
        if (idx >= 0 && idx < seqs.size()) {
            res = seqs.get(idx);
        }
        return res;
    }

    public void removeSequenceAt(int idx) {
        if (idx >= 0 && idx < seqs.size()) {
            seqs.remove(idx);
        }
    }

    public void write() {
        for (int i = 0; i < seqs.size(); i++) {
            seqs.get(i).write();
        }
    }

    public void write(Writer out) throws IOException {
        for (int i = 0; i < seqs.size(); i++) {
            seqs.get(i).write(out);
        }
    }

    public void write(String filename) {
        BufferedWriter out;
        try {
            out = new BufferedWriter(
                    new OutputStreamWriter(new FileOutputStream(filename), "UTF8"));
            write(out);

            out.close();
        } catch (IOException e) {
            System.out.println(e.toString());
        }
    }

    public void read(BufferedReader in, boolean hasTag) throws IOException {
        Sequence seq = new Sequence();
        String line;

        while ((line = in.readLine()) != null) {
            List tokens = StrUtil.tokenizeStr(line);

            if (tokens.isEmpty()) {
                // sequence delimiter 
                if (seq.size() > 0) {
                    // add this sequence to sequence collection
                    addSequence(seq);
                    // allocate a new sequence
                    seq = new Sequence();
                }
            } else {
                // a new observation
                Observation obs = new Observation(tokens, hasTag);
                // add this observation to the current sequence
                seq.addObservation(obs);
            }
        }

        // add the last sequence (if any)
        if (seq.size() > 0) {
            addSequence(seq);
        }
    }

    public void read(String filename, boolean hasTag) {
        BufferedReader in;
        try {
            in = new BufferedReader(new InputStreamReader(
                    new FileInputStream(filename), "UTF8"));

            read(in, hasTag);

            in.close();

        } catch (IOException e) {
            System.out.println(e.toString());
        }
    }

    public void read(String filename) {
        read(filename, true);
    }

    public void randomSwap() {
        CollectionUtil.randomSwap(seqs);
    }

    public void randomSwap(int times) {
        CollectionUtil.randomSwap(seqs, times);
    }

    public void randomPartition(SeqCollection firstPart, SeqCollection secondPart, int noParts) {
        List<Sequence> firstLst = new ArrayList();
        List<Sequence> secondLst = new ArrayList();

        CollectionUtil.randomPartition(seqs, firstLst, secondLst, noParts);

        firstPart.setSequences(firstLst);
        secondPart.setSequences(secondLst);
    }

    public List nFoldsRandomPartition(int nFolds) {
        List<SeqCollection> res = null;

        List lst = CollectionUtil.randomNFoldsPartition(seqs, nFolds);
        if (lst.size() > 0) {
            res = new ArrayList();
            for (int i = 0; i < lst.size(); i++) {
                SeqCollection seqColl = new SeqCollection((List<Sequence>) lst.get(i));
                res.add(seqColl);
            }
        }

        return res;
    }
}
