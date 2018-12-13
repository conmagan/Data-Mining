/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package analysis.jvntextpro.nlp.vnwordsegmenter;

import java.util.ArrayList;
import java.util.List;
import analysis.jvntextpro.lib.properties.Parameters;
import analysis.jvntextpro.lib.string.StrUtil;
import analysis.jvntextpro.mlearning.flexcrfs.Prediction;

/**
 *
 * @author hieupx
 */
public class VnWordSegmenter {
    private Prediction predictor = null;
    
    public VnWordSegmenter() {
        predictor = new Prediction(Parameters.getNlpVnWordSegmenterModelDir());
    }
    
    public void init() {
	predictor.init();
    }
    
    public List correctTags(List<String> tags) {
        List results = new ArrayList();
        
        String tag = tags.get(0);
        if (tag.equals("I")) {
            results.add("B");
        } else {
            results.add(tag);
        }
        
        int len = tags.size();        
        for (int i = 1; i < len; i++) {
            String previousTag = tags.get(i - 1);
            String currentTag = tags.get(i);            
            if (currentTag.equals("I") && previousTag.equals("O")) {
                results.add("B");
            } else {
                results.add(currentTag);
            }
        }
        
        return results;
    }
    
    public List predict(List<String> sent) {
        List data = FeaGenerator9886F1.scanFeatures(sent);
        return predictor.predict(data);
    }
    
    public String predict(String sent) {
        return StrUtil.join(predict(StrUtil.tokenizeStr(sent)));
    }
    
    public List segment(List<String> sent) {
        List words = new ArrayList();

        List tags = correctTags(predict(sent));
        
        String word = "";
        int i = 0;
        while (i < sent.size()) {
            String tag = (String)tags.get(i);
            String token = (String)sent.get(i);
            
            if (tag.equals("B")) {
                if (!word.isEmpty()) {
                    words.add(word);
                }
                word = token;
                
            } else if (tag.equals("I")) {
                word += "_" + token;
                
            } else if (tag.equals("O")) {
                if (!word.isEmpty()) {
                    words.add(word);
                    word = "";
                }
                
                words.add(token);
            }
            
            i++;
        } 
        
        // for the last word (if any)
        if (!word.isEmpty()) {
            words.add(word);
        }
        
        return words;
    }

    public String segment(String sent) {
        return StrUtil.join(segment(StrUtil.tokenizeStr(sent)));
    }
}
