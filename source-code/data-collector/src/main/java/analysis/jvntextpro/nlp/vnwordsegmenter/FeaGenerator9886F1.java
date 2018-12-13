/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package analysis.jvntextpro.nlp.vnwordsegmenter;

import java.util.ArrayList;
import java.util.List;
import analysis.jvntextpro.mlearning.data.sequence.Sequence;
import analysis.jvntextpro.resources.dicts.abbreviations.PrefixTitlesDict;
import analysis.jvntextpro.resources.dicts.vndicts.VnDict;
import analysis.jvntextpro.resources.dicts.vndicts.VnExtendedDict;
import analysis.jvntextpro.resources.dicts.vndicts.VnFamilyNamesDict;
import analysis.jvntextpro.resources.dicts.vndicts.VnMiddleLastNamesDict;
import analysis.jvntextpro.resources.regexes.Regex;
import analysis.jvntextpro.resources.regexes.StrFormChecker;

/**
 *
 * @author hieupx
 */
public class FeaGenerator9886F1 {
    /**
     * Scan features for word segmentation
     * @param sent
     * @return 
     */
    public static List scanFeatures(List<String> sent) {
        List seq = new ArrayList();
        
	for (int i = 0; i < sent.size(); i++) {
	    seq.add(scanAllCPs(sent, i));
	}
        
        return seq;
    }
    
    /**
     * Scan features for training the word segmentation model
     * @param sent
     * @return 
     */
    public static List scanFeaturesForTraining(Sequence sent) {
        List seq = new ArrayList();
        
        List<String> tokens = new ArrayList();
        List tags = new ArrayList();
        
        int len = sent.size();
        for (int i = 0; i < len; i++) {
            List texts = sent.getTextAt(i);
            tokens.add((String)texts.get(0));
            tags.add(sent.getTagAt(i));
        }
        
        for (int i = 0; i < len; i++) {
            List cps = scanAllCPs(tokens, i);
            cps.add((String)tags.get(i));
            seq.add(cps);
        }
        
        return seq;
    }
    
    /**
     * Scan all context predicates at a specific position of a sentence
     * @param sent
     * @param i
     * @return 
     */
    public static List<String> scanAllCPs(List<String> sent, int i) {
        List<String> cps = new ArrayList();

        /*
         * -2 -1 0 +1 +2
         * for normal word lookup
         */
        scanWordsRelatedCPs(cps, sent, i);

        /*
         * -3 -2 -1 0 +1 +2 +3
         * for named entity lookup
         */
        scanNamesRelatedCPs(cps, sent, i);
        
        /*
         * -5 -4 -3 -2 -1 0 +1 +2 +3 +4 +5
         * for idiom dictionary lookup
         */
        // scanIdiomsRelatedCPs(cps, sent, i);
        
        return cps;
    }
    
    public static void scanWordsRelatedCPs(List<String> cps, List<String> sent, int i) {
        int len = sent.size();        
        int j;
        
        /*
         * individual tokens
         */
        for (j = -2; j <= 2; j++) {
            if (i + j >= 0 && i + j < len) {
                String tk = sent.get(i + j);
                String idx = Integer.toString(j);
                
                String cp = "t:";
                cp += idx + ":" + tk;
                cps.add(cp.toLowerCase());
                
                /*
                 * punctuation checking
                 */
                if (j >= -1 && j <= 1) {
                    if (Regex.isSpecialCharsChunk(tk)) {
                        cps.add("sk:" + idx);
                    }
                }
                
                /*
                 * regular expression checking
                 */
                if (j >= -1 && j <= 1) {
                    /*
                    if (Regex.isFirstCap(tk)) {
                        cps.add("fc:" + idx);
                    }
                    */
                    
                    if (StrFormChecker.isFirstCap(tk)) {
                        cps.add("fc:" + idx);
                    }
                    
                    /*
                    if (Regex.isAllCap(tk)) {
                        cps.add("ac:" + idx);
                    }
                    */

                    if (StrFormChecker.isAllCap(tk)) {
                        cps.add("ac:" + idx);
                    }
                    
                    boolean regexMatched = false;
                    
                    if (Regex.isNumber(tk)) {
                        cps.add("nm:" + idx);
                        regexMatched = true;
                    }

                    if (Regex.isDate(tk)) {
                        cps.add("dt:" + idx);
                        regexMatched = true;
                    }

                    if (Regex.isTime(tk)) {
                        cps.add("tm:" + idx);
                        regexMatched = true;
                    }
                    
                    if (Regex.isDegree(tk)) {
                        cps.add("dg:" + idx);
                        regexMatched = true;
                    }

                    if (Regex.isPercent(tk)) {
                        cps.add("pc:" + idx);
                        regexMatched = true;
                    }

                    if (Regex.isEmail(tk)) {
                        cps.add("em:" + idx);
                        regexMatched = true;
                    }

                    if (Regex.isIPAddress(tk)) {
                        cps.add("ip:" + idx);
                        regexMatched = true;
                    }
                    
                    if (Regex.isURL(tk)) {
                        cps.add("url:" + idx);
                        regexMatched = true;
                    }
                    
                    if (regexMatched) {
                        cps.add("rg:" + idx);
                    }

                    /*
                    if (Regex.isNumber(tk) || Regex.isDate(tk) || 
                            Regex.isTime(tk) || Regex.isDegree(tk) ||
                            Regex.isPercent(tk) || Regex.isEmail(tk) ||
                            Regex.isIPAddress(tk) || Regex.isURL(tk)) {
                        cps.add("regex:" + idx);
                    }
                    */
                }
            }
        }

        /*
         * combination of two consecutive tokens
         */
        for (j = -2; j <= 1; j++) {
            if (i + j >= 0 && i + j + 1 < len) {
                String tk1 = sent.get(i + j);
                String tk2 = sent.get(i + j + 1);
                String tk12 = tk1 + " " + tk2;
                String idx1 = Integer.toString(j);
                String idx2 = Integer.toString(j + 1);
                
                String cp = "t:";
                cp += idx1 + ":" + idx2 + ":" + tk1 + ":" + tk2;
                cps.add(cp.toLowerCase());
                
                /*
                 * dictionary checking for -1:0, 0:+1
                 */
                if (j >= -1 && j <= 0) {
                    if (VnDict.contains(tk12) || VnExtendedDict.contains(tk12)) {
                        cps.add("dc:" + idx1 + ":" + idx2);
                    } 
                }
                
                /*
                 * regular expression checking
                 */
                if (j >= -1 && j <= 0) {
                    /*
                    if (Regex.isInitCap(tk12)) {
                        cps.add("ic:" + idx1 + ":" + idx2);
                    }
                    */
                    
                    if (StrFormChecker.isInitCap(tk12)) {
                        cps.add("ic:" + idx1 + ":" + idx2);
                    }

                    /*
                    if (Regex.isAllCap(tk12)) {
                        cps.add("ac:" + idx1 + ":" + idx2);
                    }
                    */

                    if (StrFormChecker.isAllCap(tk12)) {
                        cps.add("ac:" + idx1 + ":" + idx2);
                    }
                }
                
                /*
                 * repeated and duplicate word checking
                 */
                if (j >= -1 && j <= 0) {
                    // xanh xanh
                    if (tk1.equalsIgnoreCase(tk2)) {
                        cps.add("fd:" + idx1 + ":" + idx2);
                    }
                    
                    // long lanh, xao xuyen, cham chap, xung xinh
                    if (tk1.length() >= 2 && tk2.length() >= 2) {
                        if (tk1.substring(0, 1).equalsIgnoreCase(tk2.substring(0, 1))) {
                            cps.add("pd:" + idx1 + ":" + idx2);
                        }
                    }
                    
                    // lu'ng tu'ng, long tong
                    if (tk1.length() >= 3 && tk2.length() >= 3) {
                        if (tk1.substring(tk1.length() - 2).equalsIgnoreCase(tk2.substring(tk2.length() - 2))) {
                            cps.add("pd:" + idx1 + ":" + idx2);
                        }
                    }
                }
            }
        }
        
        /*
         * combination of three consecutive tokens
         */
        for (j = -2; j <= 0; j++) {
            if (i + j >= 0 && i + j + 2 < len) {
                String tk1 = sent.get(i + j);
                String tk2 = sent.get(i + j + 1);
                String tk3 = sent.get(i + j + 2);
                String tk123 = tk1 + " " + tk2 + " " + tk3;
                String idx1 = Integer.toString(j);
                String idx2 = Integer.toString(j + 1);
                String idx3 = Integer.toString(j + 2);
                
                String cp = "t:";
                cp += idx1 + ":" + idx2 + ":" + idx3 + ":" + 
                        tk1 + ":" + tk2 + ":" + tk3;
                cps.add(cp.toLowerCase());
                
                /*
                 * dictionary checking for -2:-1:0, -1:0:+1, 0:+1:+2
                 */
                if (VnDict.contains(tk123) || VnExtendedDict.contains(tk123)) {
                    cps.add("dc:" + idx1 + ":" + idx2 + ":" + idx3);
                }
                
                /*
                 * regular expression checking
                 */
                /*
                if (Regex.isInitCap(tk123)) {
                    cps.add("ic:" + idx1 + ":" + idx2 + ":" + idx3);
                }
                */

                if (StrFormChecker.isInitCap(tk123)) {
                    cps.add("ic:" + idx1 + ":" + idx2 + ":" + idx3);
                }
                
                /*
                if (Regex.isAllCap(tk123)) {
                    cps.add("ac:" + idx1 + ":" + idx2 + ":" + idx3);
                }
                */

                if (StrFormChecker.isAllCap(tk123)) {
                    cps.add("ac:" + idx1 + ":" + idx2 + ":" + idx3);
                }
                
                /*
                 * repeated and duplicate word checking
                 */
                // ha ha ha, he he he
                if (tk1.equalsIgnoreCase(tk2) && tk1.equalsIgnoreCase(tk3)) {
                    cps.add("fd:" + idx1 + ":" + idx2 + ":" + idx3);
                }
                
                // sach sanh sanh
                if (tk1.substring(0, 1).equalsIgnoreCase(tk2.substring(0, 1)) &&
                        tk1.substring(0, 1).equalsIgnoreCase(tk3.substring(0, 1))) {
                    cps.add("pd:" + idx1 + ":" + idx2 + ":" + idx3);
                }
            }
        }
        
        /*
         * combination of four sonsecutive tokens
         */
        for (j = -3; j <= 0; j++) {
            if (i + j >= 0 && i + j + 3 < len) {
                String tk1 = sent.get(i + j);
                String tk2 = sent.get(i + j + 1);
                String tk3 = sent.get(i + j + 2);
                String tk4 = sent.get(i + j + 3);
                String idx1 = Integer.toString(j);
                String idx2 = Integer.toString(j + 1);
                String idx3 = Integer.toString(j + 2);
                String idx4 = Integer.toString(j + 3);
                String tk1234 = tk1 + " " + tk2 + " " + tk3 + " " + tk4;
                
                /*
                 * regular expression checking
                 */
                /*
                if (Regex.isInitCap(tk1234)) {
                    cps.add("ic:" + idx1 + ":" + idx2 + ":" + idx3 + ":" + idx4);
                }
                */

                if (StrFormChecker.isInitCap(tk1234)) {
                    cps.add("ic:" + idx1 + ":" + idx2 + ":" + idx3 + ":" + idx4);
                }
                
                /*
                if (Regex.isAllCap(tk1234)) {
                    cps.add("ac:" + idx1 + ":" + idx2 + ":" + idx3 + ":" + idx4);
                }
                */

                if (StrFormChecker.isAllCap(tk1234)) {
                    cps.add("ac:" + idx1 + ":" + idx2 + ":" + idx3 + ":" + idx4);
                }
                
                /*
                 * repeated and duplicate word checking
                 */
                // dung da dung dinh, nhi nha nhi nhanh, long la long lanh, am a am uc
                if (tk1.equalsIgnoreCase(tk3)) {
                    cps.add("pd:" + idx1 + ":" + idx2 + ":" + idx3 + ":" + idx4);
                }
                
                // di di lai lai, hu hu thuc thuc, cuoi cuoi noi noi
                if (tk1.equalsIgnoreCase(tk2) && tk3.equalsIgnoreCase(tk4)) {
                    cps.add("pd:" + idx1 + ":" + idx2 + ":" + idx3 + ":" + idx4);
                }
                
            }
        }
    }
    
    public static void scanNamesRelatedCPs(List<String> cps, List<String> sent, int i) {
        int len = sent.size();
        int j; 

        /*
         * prefix titles checking
         */
        for (j = -1; j <= 1; j++) {
            if (i + j >= 0 && i + j + 1 < len) {
                String tk = sent.get(i + j);
                String tk1 = sent.get(i + j + 1);
                String idx = Integer.toString(j);

                /*
                if (PrefixTitlesDict.contains(tk) && Regex.isFirstCap(tk1)) {
                    cps.add("px:" + idx);
                    
                } else {
                    if (i + j - 1 >= 0) {
                        String tkM1 = sent.get(i + j - 1);
                        if (PrefixTitlesDict.contains(tkM1 + " " + tk) && 
                                Regex.isFirstCap(tk1)) {
                            cps.add("px:" + idx);
                        }
                    }
                }
                */
                
                if (PrefixTitlesDict.contains(tk) && StrFormChecker.isFirstCap(tk1)) {
                    cps.add("px:" + idx);
                    
                } else {
                    if (i + j - 1 >= 0) {
                        String tkM1 = sent.get(i + j - 1);
                        if (PrefixTitlesDict.contains(tkM1 + " " + tk) && 
                                StrFormChecker.isFirstCap(tk1)) {
                            cps.add("px:" + idx);
                        }
                    }
                }
            }
        }
        
        /*
         * initial title checking (honotific title)
         */
        /*
        for (j = -1; j <= 1; j++) {
            if (i + j >= 0 && i + j < len) {
                String tk = sent.get(i + j);
                String idx = Integer.toString(j);
                
                if (HonotificTitlesDict.contains(tk)) {
                    cps.add("tl:" + idx);
                }
            }
        }
        */
        
        /*
         * family name checking
         */
        for (j = -1; j <= 1; j++) {
            if (i + j >= 0 && i + j < len) {
                String tk = sent.get(i + j);
                String idx = Integer.toString(j);

                /*
                if (Regex.isFirstCap(tk) && VnFamilyNamesDict.contains(tk)) {
                    cps.add("fa:" + idx);
                } 
                */
                
                if (StrFormChecker.isFirstCap(tk) && VnFamilyNamesDict.contains(tk)) {
                    cps.add("fa:" + idx);
                }                
            }
        }
        
        /*
         * middle name and last name checking
         */
        for (j = -2; j <= 0; j++) {
            if (i + j >= 0 && i + j + 1 < len) {
                String tk1 = sent.get(i + j);
                String tk2 = sent.get(i + j + 1);
                // String idx1 = Integer.toString(j);
                String idx2 = Integer.toString(j + 1);

                /*
                if (Regex.isFirstCap(tk1) && 
                        Regex.isFirstCap(tk2) && 
                        VnMiddleLastNamesDict.contains(tk2)) {
                    cps.add("na:" + idx2);
                }
                */
                
                if (StrFormChecker.isFirstCap(tk1) && 
                        StrFormChecker.isFirstCap(tk2) && 
                        VnMiddleLastNamesDict.contains(tk2)) {
                    cps.add("na:" + idx2);
                }
            }
        }
        
        /*
         * fullname checking
         */
        String tkM2 = null;
        if (i - 2 >= 0) {
            tkM2 = sent.get(i - 2);
        }
        String tkM1 = null;
        if (i - 1 >= 0) {
            tkM1 = sent.get(i - 1);
        }
        String tk = sent.get(i);
        String tkP1 = null;
        if (i + 1 < len) {
            tkP1 = sent.get(i + 1);
        }
        String tkP2 = null;
        if (i + 2 < len) {
            tkP2 = sent.get(i + 2);
        }

        boolean isPartOfAName = false; 
        
        // <token:-2><token:-1><token>
        if (!isPartOfAName && tkM2 != null && tkM1 != null) {
            if (Regex.isName(tkM2 + " " + tkM1 + " " + tk)) {
                isPartOfAName = true;
            }
        }
        
        // <token:-2><token:-1><token><token:+1>
        if (!isPartOfAName && tkM2 != null && tkM1 != null && tkP1 != null) {
            if (Regex.isName(tkM2 + " " + tkM1 + " " + tk + " " + tkP1)) {
                isPartOfAName = true;
            }
        }
        
        // <token:-1><token>
        if (!isPartOfAName && tkM1 != null) {
            if (Regex.isName(tkM1 + " " + tk)) {
                isPartOfAName = true;
            }
        }
        
        // <token:-1><token><token:+1>
        if (!isPartOfAName && tkM1 != null && tkP1 != null) {
            if (Regex.isName(tkM1 + " " + tk + " " + tkP1)) {
                isPartOfAName = true;
            }
        }
        
        // <token:-1><token><token:+1><token:+2>
        if (!isPartOfAName && tkM1 != null && tkP1 != null && tkP2 != null) {
            if (Regex.isName(tkM1 + " " + tk + " " + tkP1 + " " + tkP2)) {
                isPartOfAName = true;
            }
        }
        
        // <token><token:+1>
        if (!isPartOfAName && tkP1 != null) {
            if (Regex.isName(tk + " " + tkP1)) {
                isPartOfAName = true;
            }
        }
        
        // <token><token:+1><token:+2>
        if (!isPartOfAName && tkP1 != null && tkP2 != null) {
            if (Regex.isName(tk + " " + tkP1 + " " + tkP2)) {
                isPartOfAName = true;
            }
        }
        
        if (isPartOfAName) {
            cps.add("pna:0");
        }
    }

    public static void scanIdiomsRelatedCPs(List<String> cps, List<String> sent, int i) {
        /*
         * Add more code here
         */
    }
}
