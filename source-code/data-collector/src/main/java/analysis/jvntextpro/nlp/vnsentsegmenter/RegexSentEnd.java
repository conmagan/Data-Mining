/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package analysis.jvntextpro.nlp.vnsentsegmenter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;
import analysis.jvntextpro.lib.pairs.PairIntInt;

/**
 *
 * @author hieupx
 */
public class RegexSentEnd {
    /**
     * NOTE: the following order is important:
     * 1: ellipsis-exclamation: (((…)+|(\\.){2,}|\\.v(\\.v){1,}\\.)(\\!)+)
     * 2: fullstop-exclamation: (\\.(\\!)+)
     * 3: exclamation-fullstop: ((\\!)+\\.)
     * 4: question-exclamation: ((\\?)+(\\!)+)
     * 5: exclamation-question: ((\\!)+(\\?)+)
     * 6: ellipsis: ((…)+|(\\.){2,}|\\.v(\\.v){1,}\\.)
     * 7: fullstop: ((\\./)?\\.)
     * 8: exclamation: ((\\!)+)
     * 9: question: ((\\?)+)
     */
    private static final String regexSentEnd = 
            "(((…)+|(\\.){2,}|\\.v(\\.v){1,}\\.)+(\\!)+|\\.(\\!)+|(\\!)+\\.|(\\?)+(\\!)+|(\\!)+(\\?)+|(…)+|(\\.){2,}|\\.v(\\.v){1,}\\.|(\\./)?\\.|(\\!)+|(\\?)+)";
    
    private static final String regexSentEnd1 = "((…)+|(\\.){2,}|\\.v(\\.v){1,}\\.)(\\!)+";
    private static final String regexSentEnd2 = "\\.(\\!)+";
    private static final String regexSentEnd3 = "(\\!)+\\.";
    private static final String regexSentEnd4 = "(\\?)+(\\!)+";
    private static final String regexSentEnd5 = "(\\!)+(\\?)+";
    private static final String regexSentEnd6 = "((…)+|(\\.){2,}|\\.v(\\.v){1,}\\.)";
    private static final String regexSentEnd7 = "(\\./)?\\.";
    private static final String regexSentEnd8 = "(\\!)+";
    private static final String regexSentEnd9 = "(\\?)+";
    
    private static Pattern ptnSentEnd = null;
    
    private static final List<Pattern> patterns = new ArrayList();;
    
    static {
        try {
            ptnSentEnd = Pattern.compile(regexSentEnd);
            
            patterns.add(Pattern.compile(regexSentEnd1));
            patterns.add(Pattern.compile(regexSentEnd2));
            patterns.add(Pattern.compile(regexSentEnd3));
            patterns.add(Pattern.compile(regexSentEnd4));
            patterns.add(Pattern.compile(regexSentEnd5));
            patterns.add(Pattern.compile(regexSentEnd6));
            patterns.add(Pattern.compile(regexSentEnd7));
            patterns.add(Pattern.compile(regexSentEnd8));
            patterns.add(Pattern.compile(regexSentEnd9));
            
        } catch (PatternSyntaxException ex) {
            System.out.println(ex.toString());
            System.exit(1);
        }
    }
    
    public static String createTempStr(int len) {
        String res = "";
        for (int i = 0; i < len; i++) {
            res += "*";
        }
        return res;
    }
    
    public static String findSentEndsForPattern(int idx, 
            String str, List<SentEnd> ses) {

        String tempStr = str;        
        Matcher matcher = patterns.get(idx).matcher(str);
        
        while (matcher.find()) {
            int start = matcher.start();
            int end = matcher.end();

            String instance = str.substring(start, end);
            String name = getPatternName(instance);

            tempStr = tempStr.substring(0, start) + 
                    createTempStr(end - start) +
                    tempStr.substring(end);

            SentEnd se = new SentEnd(name, instance, start, end);
            ses.add(se);
        }
        
        return tempStr;
    }
    
    public static List<SentEnd> sort(List<SentEnd> ses) {
        if (ses.size() <= 1) {
            return ses;
        }
        
        List<SentEnd> results = new ArrayList();
        
        List<PairIntInt> tempList = new ArrayList();
        for (int i = 0; i < ses.size(); i++) {
            SentEnd se = ses.get(i);
            tempList.add(new PairIntInt(i, se.start));
        }
        
        Collections.sort(tempList);
        for (int i = 0; i < tempList.size(); i++) {
            PairIntInt pair = tempList.get(i);
            results.add(ses.get(pair.first));
        }
        
        return results;
    }
    
    public static List<SentEnd> findAllSentEnds(String str) {
        List<SentEnd> ses = new ArrayList();

        if ((str.indexOf(".") < 0) && 
                (str.indexOf("!") < 0) &&
                (str.indexOf("?") < 0) &&
                (str.indexOf("…") < 0)) {
            return ses;
        }
        
        String tempStr = str;
        for (int i = 0; i < patterns.size(); i++) {
            tempStr = findSentEndsForPattern(i, tempStr, ses);
        }
        
        return sort(ses);
    }
    
    /**
     * Get sentence-end pattern name
     * @param str
     * @return 
     */
    public static String getPatternName(String str) {
        /*
         * NOTE: the following order is important
         */
        
        if (str.matches("((…)+|(\\.){2,}|\\.v(\\.v){1,}\\.)(\\!)+")) {
            return "ellipsis-exclamation";
            
        } else if (str.matches("\\.(\\!)+")) {
            return "fullstop-exclamation";

        } else if (str.matches("(\\!)+\\.")) {
            return "exclamation-fullstop";
            
        } else if (str.matches("(\\?)+(\\!)+")) {
            return "question-exclamation";
            
        } else if (str.matches("(\\!)+(\\?)+")) {
            return "exclamation-question";
            
        } else if (str.matches("(…)+|(\\.){2,}|\\.v(\\.v){1,}\\.")) {
            return "ellipsis";

        } else if (str.matches("(\\./)?\\.")) {
            return "fullstop";
            
        } else if (str.matches("(\\!)+")) {
            return "exclamation";
            
        } else if (str.matches("(\\?)+")) {
            return "question";

        } else {
            return "";
        }        
    }
}
