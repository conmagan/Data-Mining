/*
    Copyright (C) 2006, Xuan-Hieu Phan
    
    Email:	hieuxuan@ecei.tohoku.ac.jp
		pxhieu@gmail.com
    URL:	http://www.hori.ecei.tohoku.ac.jp/~hieuxuan
    
    Graduate School of Information Sciences,
    Tohoku University
*/

package analysis.jvntextpro.mlearning.flexcrfs;

import java.util.ArrayList;
import java.util.List;

public class Prediction {

    public Option option = null;
    public Maps maps = null;
    public Dictionary dict = null;
    public FeatureGen feagen = null;
    public Viterbi viterbi = null;
    public Model model = null;

    public boolean initialized = false;

    public Prediction(String modelDir) {
        option = new Option(modelDir);
        option.readOptions();

        // init();
    }

    public boolean isInitialized() {
        return initialized;
    }

    public void init() {
        maps = new Maps();
        dict = new Dictionary();
        feagen = new FeatureGen(maps, dict);
        viterbi = new Viterbi();

        model = new Model(option, maps, dict, feagen, viterbi);
        if (!model.init()) {
            System.out.println("Couldn't load the model");
            System.out.println("Check the <model directory> and the <model file> again");
            return;
        }

        initialized = true;
    }

    public List predict(List sen) {

        List results = new ArrayList();
        int i, j;

        List seq = new ArrayList();

        for (i = 0; i < sen.size(); i++) {
            List strCps = (List) sen.get(i);
            List intCps = new ArrayList();

            for (j = 0; j < strCps.size(); j++) {
                String strCp = (String) strCps.get(j);
                Integer intCp = (Integer) maps.cpStr2Int.get(strCp);
                if (intCp != null) {
                    intCps.add(intCp);
                }
            }

            Observation obsr = new Observation(intCps);
            seq.add(obsr);
        }

        // predition
        model.inference(seq);

        for (i = 0; i < seq.size(); i++) {
            Observation obsr = (Observation) seq.get(i);

            String label = (String) maps.lbInt2Str.get(new Integer(obsr.modelLabel));
            if (label == null) {
                System.out.println("Invalid label id");
                return results;
            }
            results.add(label);
        }

        return results;
    }

} // end of class Prediction

