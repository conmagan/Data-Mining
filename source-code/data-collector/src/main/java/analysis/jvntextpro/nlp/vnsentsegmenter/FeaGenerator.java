/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package analysis.jvntextpro.nlp.vnsentsegmenter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import analysis.jvntextpro.lib.pairs.Pair;
import analysis.jvntextpro.lib.string.StrUtil;
import analysis.jvntextpro.resources.dicts.abbreviations.HonotificTitlesDict;
import analysis.jvntextpro.resources.dicts.abbreviations.StopAbbrsDict;
import analysis.jvntextpro.resources.dicts.vndicts.VnDict;
import analysis.jvntextpro.resources.dicts.vndicts.VnFirstWordsDict;
import analysis.jvntextpro.resources.regexes.PunctuationsMatcher;
import analysis.jvntextpro.resources.regexes.Regex;
import analysis.jvntextpro.resources.regexes.StrFormChecker;

/**
 *
 * @author hieupx
 */
public class FeaGenerator {
    /**
     * Scanning features for new data
     * @param tokens
     * @param data
     * @param majorIdxes
     * @param minorIdxes 
     */
    public static void scanFeatures(List tokens, List data, 
            List majorIdxes, List<Pair<Integer, Integer>> minorIdxes) {

        data.clear();
        majorIdxes.clear();
        minorIdxes.clear();
        
        int nTokens = tokens.size();
        for (int i = 0; i < nTokens; i++) {
            
            String tk = (String)tokens.get(i);
            
            List sentEnds = RegexSentEnd.findAllSentEnds(tk);
            
            int nSentEnds = sentEnds.size();
            for (int j = 0; j < nSentEnds; j++) {
                SentEnd se = (SentEnd)sentEnds.get(j);
                
                String tkM2 = null;
                if (i - 2 >= 0) {
                    tkM2 = (String)tokens.get(i - 2);
                }
                
                String tkM1 = null;
                if (i - 1 >= 0) {
                    tkM1 = (String)tokens.get(i - 1);
                }
                
                String tkP1 = null;
                if (i + 1 < nTokens) {
                    tkP1 = (String)tokens.get(i + 1);
                }
                
                String tkP2 = null;
                if (i + 2 < nTokens) {
                    tkP2 = (String)tokens.get(i + 2);
                }
                
                String tkP3 = null;
                if (i + 3 < nTokens) {
                    tkP3 = (String)tokens.get(i + 3);
                }
                
                String cps;
                if (j == nSentEnds - 1) {
                    cps = scanAllCPs(tkM2, tkM1, tk, tkP1, tkP2, tkP3,
                            se, nSentEnds, true);
                } else {
                    cps = scanAllCPs(tkM2, tkM1, tk, tkP1, tkP2, tkP3,
                            se, nSentEnds, false);
                }
                
                data.add(cps);
                majorIdxes.add(i);
                minorIdxes.add(new Pair(se.start, se.end));
            }
        }
    }
    
    /**
     * Scanning features for training model for sentence segmentation
     * @param doc
     * @param data 
     */
    public static void scanFeaturesForTraining(List doc, List data) {
        // going through all sentences in the document
        int nSents = doc.size();
        for (int i = 0; i < nSents; i++) {
            // current sentence
            List currentSentTokens = StrUtil.tokenizeStr((String)doc.get(i));
            
            // next sentence
            List nextSentTokens = null;
            if (i + 1 < nSents) {
                nextSentTokens = StrUtil.tokenizeStr((String)doc.get(i + 1));
            }
            
            // last sentence
            List lastSentTokens = null;
            if (i - 1 >= 0) {
                lastSentTokens = StrUtil.tokenizeStr((String)doc.get(i - 1));
            }
            
            // going through all tokens in the sentences
            int nTokens = currentSentTokens.size();
            for (int j = 0; j < nTokens; j++) {
                // the current token
                String token = (String)currentSentTokens.get(j);
                
                // get a list of sentence-end marks
                List sentEnds = RegexSentEnd.findAllSentEnds(token);
                
                // going through all sentence-end marks
                int noSentEnds = sentEnds.size();
                for (int k = 0; k < noSentEnds; k++) {
                    // the current sentence-end
                    SentEnd se = (SentEnd)sentEnds.get(k);
                    
                    // token at position: -2
                    String tkM2 = null;
                    if (j - 2 >= 0) {
                        tkM2 = (String)currentSentTokens.get(j - 2);
                    } else {
                        if (j == 1) {
                            if (lastSentTokens != null && lastSentTokens.size() > 0) {
                                tkM2 = (String)lastSentTokens.get(lastSentTokens.size() - 1);
                            }
                        } else if (j == 0) {
                            if (lastSentTokens != null && lastSentTokens.size() > 1) {
                                tkM2 = (String)lastSentTokens.get(lastSentTokens.size() - 2);
                            }
                        }
                    }
                    
                    // token at position: -1
                    String tkM1 = null;
                    if (j - 1 >= 0) {
                        tkM1 = (String)currentSentTokens.get(j - 1);
                    } else {
                        if (j == 0) {
                            if (lastSentTokens != null && lastSentTokens.size() > 0) {
                                tkM1 = (String)lastSentTokens.get(lastSentTokens.size() - 1);
                            }
                        }
                    }
                    
                    // token at position: +1
                    String tkP1 = null;
                    if (j + 1 < nTokens) {
                        tkP1 = (String)currentSentTokens.get(j + 1);
                    } else {
                        // if the current token is the last one, get the first token of the next sentence
                        if (nextSentTokens != null && nextSentTokens.size() > 0) {
                            tkP1 = (String)nextSentTokens.get(0);
                        }
                    }
                    
                    // token at position: +2
                    String tkP2 = null;
                    if (j + 2 < nTokens) {
                        tkP2 = (String)currentSentTokens.get(j + 2);
                    } else if (j + 2 == nTokens) {
                        // maybe the first token of the next sentence
                        if (nextSentTokens != null && nextSentTokens.size() > 0) {
                            tkP2 = (String)nextSentTokens.get(0);
                        }
                    } else if (j + 2 > nTokens) {
                        // maybe the second token of the next sentence
                        if (nextSentTokens != null && nextSentTokens.size() > 1) {
                            tkP2 = (String)nextSentTokens.get(1);
                        }
                    }
                    
                    // token at position: +3
                    String tkP3 = null;
                    if (j + 3 < nTokens) {
                        tkP3 = (String)currentSentTokens.get(j + 3);
                    } else if (j + 3 == nTokens) {
                        // maybe the first token of the next sentence
                        if (nextSentTokens != null && nextSentTokens.size() > 0) {
                            tkP3 = (String)nextSentTokens.get(0);
                        }
                    } else if (j + 3 == nTokens + 1) {
                        // maybe the second token of the next sentence
                        if (nextSentTokens != null && nextSentTokens.size() > 1) {
                            tkP3 = (String)nextSentTokens.get(1);
                        }
                    } else if (j + 3 > nTokens + 1) {
                        // maybe the third token of the next sentence
                        if (nextSentTokens != null && nextSentTokens.size() > 2) {
                            tkP3 = (String)nextSentTokens.get(2);
                        }
                    }
                    
                    /**
                     * Generating context predicates
                     */
                    String cps;
                    if (k == noSentEnds - 1) {
                        // is the token-end
                        cps = scanAllCPs(tkM2, tkM1,
                            token, tkP1, tkP2, tkP3, se, noSentEnds, true);
                    } else {
                        // is not the token end
                        cps = scanAllCPs(tkM2, tkM1,
                            token, tkP1, tkP2, tkP3, se, noSentEnds, false);
                    }

                    /**
                     * Adding label
                     */
                    if (j == nTokens - 1) {
                        if (se.end >= token.length()) {
                            cps += " " + SentSegConstants.positiveLabel;
                        } else {
                            String sPart = token.substring(se.end);
                            if (PunctuationsMatcher.match("right-quotation", sPart)) {
                                cps += " " + SentSegConstants.positiveLabel;
                            } else {
                                cps += " " + SentSegConstants.negativeLabel;
                            }
                        }
                    } else {
                        cps += " " + SentSegConstants.negativeLabel;
                    }
                    
                    /**
                     * Adding list of context predicates to data set
                     */
                    if (cps.length() > 0) {
                        data.add(cps);
                    }
                }
            }
        }
    }
    
    /**
     * Scanning all context predicates
     * @param tkM2
     * @param tkM1
     * @param tk
     * @param tkP1
     * @param tkP2
     * @param tkP3
     * @param se
     * @param noSentEnds
     * @param isLastSentEnd 
     */
    public static String scanAllCPs(
            String tkM2, String tkM1, 
            String tk, 
            String tkP1, String tkP2, String tkP3,
            SentEnd se, int noSentEnds, boolean isLastSentEnd) {
        
        List cpsList = new ArrayList();
        Set cpsSet = new HashSet();
        
        scanTkM1RelatedCPs(tkM1, cpsList, cpsSet);
        scanTkRelatedCPs(tk, cpsList, cpsSet);
        scanTkP1RelatedCPs(tkP1, cpsList, cpsSet);
        scanTokenCombinationRelatedCPs(tkM2, tkM1, tk, tkP1, tkP2, tkP3, cpsList, cpsSet);
        scanFPartRelatedCPs(tk, se, cpsList, cpsSet);
        scanSPartRelatedCPs(tk, tkP1, tkP2, se, cpsList, cpsSet);
        scanSentEndRelatedCPs(tk, tkP1, se, noSentEnds, isLastSentEnd, cpsList, cpsSet);
        scanFPartSERelatedCPs(tk, se, cpsList, cpsSet);
        scanNamesRelatedCPs(tkM2, tkM1, tk, tkP1, tkP2, tkP3, cpsList, cpsSet);
        scanOrderedListRelatedCPs(tkM1, tk, tkP1, cpsList, cpsSet);
        
        List cps = new ArrayList();
        for (int idx = 0; idx < cpsList.size(); idx++) {
            String cp = (String)cpsList.get(idx);
            if (cpsSet.contains(cp)) {
                cps.add(cp);
                cpsSet.remove(cp);
            }
        }
        
        return StrUtil.join(cps);
    }
    
    public static void scanTkM1RelatedCPs(String tkM1, List cpsList, Set cpsSet) {
        if (tkM1 != null && !tkM1.isEmpty()) {
            cpsList.add("tk:-1:" + tkM1);
            cpsSet.add("tk:-1:" + tkM1);
            
            String loweredTkM1 = tkM1.toLowerCase();
            if (!loweredTkM1.equals(tkM1)) {
                cpsList.add("tk:-1:" + loweredTkM1);
                cpsSet.add("tk:-1:" + loweredTkM1);
            }
            
            /*
            if (Regex.isFirstCap(tkM1)) {
                cpsList.add("firstcap:-1");
                cpsSet.add("firstcap:-1");
            }
            */

            if (StrFormChecker.isFirstCap(tkM1)) {
                cpsList.add("firstcap:-1");
                cpsSet.add("firstcap:-1");
            }
            
        } else {
            cpsList.add("null:-1");
            cpsSet.add("null:-1");
        }
    }
    
    public static void scanTkRelatedCPs(String tk, List cpsList, Set cpsSet) {

        if (tk.endsWith(",") || tk.endsWith(";")) {
            cpsList.add("sentend:false");
            cpsSet.add("sentend:false");
        }
        
        cpsList.add("tk:0:" + tk);
        cpsSet.add("tk:0:" + tk);
        String loweredTk = tk.toLowerCase();
        if (!loweredTk.equals(tk)) {
            cpsList.add("tk:0:" + loweredTk);
            cpsSet.add("tk:0:" + loweredTk);
        }
        
        /*
        if (Regex.isFirstCap(tk)) {
            cpsList.add("firstcap:0");
            cpsSet.add("firstcap:0");
        }
        */

        if (StrFormChecker.isFirstCap(tk)) {
            cpsList.add("firstcap:0");
            cpsSet.add("firstcap:0");
        }
        
        /*
        if (Regex.isFirstLower(tk)) {
            cpsList.add("firstlower:0");
            cpsSet.add("firstlower:0");
        }
        */

        if (StrFormChecker.isFirstLow(tk)) {
            cpsList.add("firstlower:0");
            cpsSet.add("firstlower:0");
        }
        
        scanRegexRelatedCPs(tk, "0", cpsList, cpsSet);
        
        scanStopAbbrsRelatedCPs(tk, "0", cpsList, cpsSet);
    }
    
    public static void scanTkP1RelatedCPs(String tkP1, List cpsList, Set cpsSet) {
        if (tkP1 != null && !tkP1.isEmpty()) {
            cpsList.add("tk:+1:" + tkP1);
            cpsSet.add("tk:+1:" + tkP1);

            String loweredTkP1 = tkP1.toLowerCase();
            if (!loweredTkP1.equals(tkP1)) {
                cpsList.add("tk:+1:" + loweredTkP1);
                cpsSet.add("tk:+1:" + loweredTkP1);
            }
            
            /*
            if (Regex.isFirstCap(tkP1)) {
                cpsList.add("firstcap:+1");
                cpsSet.add("firstcap:+1");
            }
            */

            if (StrFormChecker.isFirstCap(tkP1)) {
                cpsList.add("firstcap:+1");
                cpsSet.add("firstcap:+1");
            }
            
            /*
            if (Regex.isFirstLower(tkP1)) {
                cpsList.add("firstlower:+1");
                cpsSet.add("firstlower:+1");
            }
            */

            if (StrFormChecker.isFirstLow(tkP1)) {
                cpsList.add("firstlower:+1");
                cpsSet.add("firstlower:+1");
            }
            
            if (Regex.isFirstNumber(tkP1)) {
                cpsList.add("firstnumber:+1");
                cpsSet.add("firstnumber:+1");
            }
            
            scanRegexRelatedCPs(tkP1, "+1", cpsList, cpsSet);

        } else {
            cpsList.add("null:+1");
            cpsSet.add("null:+1");
        }
    }
    
    public static void scanTokenCombinationRelatedCPs(
            String tkM2, String tkM1,
            String tk,
            String tkP1, String tkP2, String tkP3, List cpsList, Set cpsSet) {
        
        /*
         * <token:-1><token>
         */
        if (tkM1 != null) {
            String tkM1Tk = tkM1 + ":" + tk;
            cpsList.add("tk:-1:0:" + tkM1Tk);
            cpsSet.add("tk:-1:0:" + tkM1Tk);
            
            String loweredTkM1Tk = tkM1Tk.toLowerCase();
            if (!loweredTkM1Tk.equals(tkM1Tk)) {
                cpsList.add("tk:-1:0:" + loweredTkM1Tk);
                cpsSet.add("tk:-1:0:" + loweredTkM1Tk);
            }
        }
        
        /*
         * <token><token:+1>
         */
        if (tkP1 != null) {
            String tkTkP1 = tk + ":" + tkP1;
            cpsList.add("tk:0:+1:" + tkTkP1);
            cpsSet.add("tk:0:+1:" + tkTkP1);
            
            String loweredTkTkP1 = tkTkP1.toLowerCase();
            if (!loweredTkTkP1.equals(tkTkP1)) {
                cpsList.add("tk:-1:0:" + loweredTkTkP1);
                cpsSet.add("tk:-1:0:" + loweredTkTkP1);
            }
            
            scanHonotificTitlesRelatedCPs(tk, tkP1, "0", cpsList, cpsSet);
        }
        
        /*
         * <token:+1><token:+2>
         */
        if (tkP1 != null && tkP2 != null) {
            String p1p2 = tkP1 + ":" + tkP2;
            cpsList.add("com:+1:+2:" + p1p2);
            cpsSet.add("com:+1:+2:" + p1p2);
            
            String loweredP1P2 = p1p2.toLowerCase();
            if (!loweredP1P2.equals(p1p2)) {
                cpsList.add("com:+1:+2:" + loweredP1P2);
                cpsSet.add("com:+1:+2:" + loweredP1P2);
            }
            
            if (VnDict.contains(tkP1 + " " + tkP2)) {
                cpsList.add("indict:+1:+2");
                cpsSet.add("indict:+1:+2");
            }

            /*
            if (!Regex.isFirstCap(tkP1)) {
                if (Regex.isFirstCap(tkP1 + " " + tkP2)) {
                    cpsList.add("firstcap:+1");
                    cpsSet.add("firstcap:+1");
                }
            }
            */

            if (!StrFormChecker.isFirstCap(tkP1)) {
                if (StrFormChecker.isFirstCap(tkP1 + " " + tkP2)) {
                    cpsList.add("firstcap:+1");
                    cpsSet.add("firstcap:+1");
                }
            }
            
            if (!Regex.isFirstNumber(tkP1)) {
                if (Regex.isFirstNumber(tkP1 + " " + tkP2)) {
                    cpsList.add("firstnumber:+1");
                    cpsSet.add("firstnumber:+1");
                }
            }
            
            if (VnFirstWordsDict.contains(tkP1 + " " + tkP2)) {
                cpsList.add("firstword:+1");
                cpsSet.add("firstword:+1");
            }
        }
        
        /*
         * <token:+1><token:+2><token:+3>
         */
        if (tkP1 != null && tkP2 != null && tkP3 != null) {
            if (VnFirstWordsDict.contains(tkP1 + " " + tkP2 + " " + tkP3)) {
                cpsList.add("firstword:+1");
                cpsSet.add("firstword:+1");
            }
        }
    }
    
    public static void scanFPartRelatedCPs(
            String tk, SentEnd se,
            List cpsList, Set cpsSet) {
        String fPart;
        if (se.start > 0) {
            fPart = tk.substring(0, se.start);
            cpsList.add("fp:" + fPart);
            cpsSet.add("fp:" + fPart);
            
            String loweredFPart = fPart.toLowerCase();
            if (!loweredFPart.equals(fPart)) {
                cpsList.add("fp:" + loweredFPart);
                cpsSet.add("fp:" + loweredFPart);
            }

            scanRegexRelatedCPs(fPart, "fp", cpsList, cpsSet);
        }
    }
    
    public static void scanSPartRelatedCPs(
            String tk, String tkP1, String tkP2, 
            SentEnd se,
            List cpsList, Set cpsSet) {

        if (se.end <= tk.length() - 1) {
            String sPart = tk.substring(se.end);
            
            cpsList.add("sp:" + sPart);
            cpsSet.add("sp:" + sPart);
            
            String loweredSPart = sPart.toLowerCase();
            if (!loweredSPart.equals(sPart)) {
                cpsList.add("sp:" + loweredSPart);
                cpsSet.add("sp:" + loweredSPart);
            }
            
            /*
            if (Regex.isFirstCap(sPart)) {
                cpsList.add("firstcap:sp");
                cpsSet.add("firstcap:sp");
            }
            */

            if (StrFormChecker.isFirstCap(sPart)) {
                cpsList.add("firstcap:sp");
                cpsSet.add("firstcap:sp");
            }
            
            /*
            if (Regex.isFirstLower(sPart)) {
                cpsList.add("firstlower:sp");
                cpsSet.add("firstlower:sp");
            }
            */

            if (StrFormChecker.isFirstLow(sPart)) {
                cpsList.add("firstlower:sp");
                cpsSet.add("firstlower:sp");
            }
            
            scanRegexRelatedCPs(sPart, "sp", cpsList, cpsSet);
            
            if (PunctuationsMatcher.match("right-quotation", sPart)) {
                cpsList.add("quotation:sp");
                cpsSet.add("quotation:sp");
            } else {
                if (sPart.length() > 0) {
                    cpsList.add("is-middle");
                    cpsSet.add("is-middle");
                }
            }
            
            if (tkP1 != null) {
                if (VnDict.contains(sPart + " " + tkP1)) {
                    cpsList.add("indict:+1:+2");
                    cpsSet.add("indict:+1:+2");
                }
                
                if (VnFirstWordsDict.contains(sPart + " " + tkP1)) {
                    cpsList.add("firstword:+1");
                    cpsSet.add("firstword:+1");
                }
                
                if (tkP2 != null) {
                    if (VnFirstWordsDict.contains(sPart + " " + tkP1 + " " + tkP2)) {
                        cpsList.add("firstword:+1");
                        cpsSet.add("firstword:+1");
                    }
                }
            }
        }
    }
    
    public static void scanFPartSERelatedCPs(
            String tk, SentEnd se, List cpsList, Set cpsSet) {
        
        String fPartSE = tk.substring(0, se.end);
        if (fPartSE != null && !fPartSE.equals(tk)) {
            cpsList.add("fpse:" + fPartSE);
            cpsSet.add("fpse:" + fPartSE);

            String loweredFPartSE = fPartSE.toLowerCase();
            if (!loweredFPartSE.equals(fPartSE)) {
                cpsList.add("fpse:" + loweredFPartSE);
                cpsSet.add("fpse:" + loweredFPartSE);
            }
            
            scanRegexRelatedCPs(fPartSE, "fpse", cpsList, cpsSet);
            
            scanStopAbbrsRelatedCPs(fPartSE, "fpse", cpsList, cpsSet);
        }
    }
    
    public static void scanRegexRelatedCPs(String str, String suffix, 
            List cpsList, Set cpsSet) {

        boolean regexMatched = false;
        
        if (Regex.isCurrency(str)) {
            cpsList.add("currency:" + suffix);
            cpsSet.add("currency:" + suffix);
            regexMatched = true;
        }
        
        if (Regex.isCurrencyUnit(str)) {
            cpsList.add("currencyunit:" + suffix);
            cpsSet.add("currencyunit:" + suffix);
            regexMatched = true;
        }
        
        if (Regex.isDate(str)) {
            cpsList.add("date:" + suffix);
            cpsSet.add("date:" + suffix);
            regexMatched = true;
        }
        
        if (Regex.isDegree(str)) {
            cpsList.add("degree:" + suffix);
            cpsSet.add("degree:" + suffix);
            regexMatched = true;
        }
        
        if (Regex.isEmail(str)) {
            cpsList.add("email:" + suffix);
            cpsSet.add("email:" + suffix);
            regexMatched = true;
        }
        
        if (Regex.isIPAddress(str)) {
            cpsList.add("ip:" + suffix);
            cpsSet.add("ip:" + suffix);
            regexMatched = true;
        }
        
        if (Regex.isNumber(str)) {
            cpsList.add("number:" + suffix);
            cpsSet.add("number:" + suffix);
            regexMatched = true;
        }
        
        if (Regex.isPercent(str)) {
            cpsList.add("percent:" + suffix);
            cpsSet.add("percent:" + suffix);
            regexMatched = true;
        }
        
        if (Regex.isTime(str)) {
            cpsList.add("time:" + suffix);
            cpsSet.add("time:" + suffix);
            regexMatched = true;
        }
        
        if (Regex.isURL(str)) {
            cpsList.add("url:" + suffix);
            cpsSet.add("url:" + suffix);
            regexMatched = true;
        }
        
        if (regexMatched) {
            cpsList.add("regex:" + suffix);
            cpsSet.add("regex:" + suffix);
        }
    }
    
    public static void scanSentEndRelatedCPs(
            String tk, String tkP1, SentEnd se, int noSentEnds, boolean isLastSentEnd,
            List cpsList, Set cpsSet) {
     
        if (noSentEnds > 1 && !isLastSentEnd) {
            cpsList.add("tokenend:false");
            cpsSet.add("tokenend:false");
        }
        
        if (se.end <= tk.length() - 1) {
            String sPart = tk.substring(se.end);
            
            if (PunctuationsMatcher.match("right-quotation", sPart) ||
                    PunctuationsMatcher.match("right-bracket", sPart)) {
                cpsList.add("tokenend:true");
                cpsSet.add("tokenend:true");
            }
        }
        
        String seText = se.instance;
        if (seText.matches("\\.{2,}|…{1,}")) {
            /*
            if (tkP1 != null && !Regex.isFirstCap(tkP1)) {
                cpsList.add("se:threedot");
                cpsSet.add("se:threedot");
            } 
            */

            if (tkP1 != null && !StrFormChecker.isFirstCap(tkP1)) {
                cpsList.add("se:threedot");
                cpsSet.add("se:threedot");
            }            
        }
    }
    
    public static void scanNamesRelatedCPs(
            String tkM2, String tkM1,
            String tk,
            String tkP1, String tkP2, String tkP3, List cpsList, Set cpsSet) {

        // <token:-2><token:-1><token>
        if (tkM2 != null && tkM1 != null) {
            if (Regex.isName(tkM2 + " " + tkM1 + " " + tk)) {
                cpsList.add("part-of-a-name");
                cpsSet.add("part-of-a-name");
                return;
            }
        }
        
        // <token:-2><token:-1><token><token:+1>
        if (tkM2 != null && tkM1 != null && tkP1 != null) {
            if (Regex.isName(tkM2 + " " + tkM1 + " " + tk + " " + tkP1)) {
                cpsList.add("part-of-a-name");
                cpsSet.add("part-of-a-name");
                return;
            }
        }
        
        // <token:-1><token>
        if (tkM1 != null) {
            if (Regex.isName(tkM1 + " " + tk)) {
                cpsList.add("part-of-a-name");
                cpsSet.add("part-of-a-name");
                return;
            }
        }
        
        // <token:-1><token><token:+1>
        if (tkM1 != null && tkP1 != null) {
            if (Regex.isName(tkM1 + " " + tk + " " + tkP1)) {
                cpsList.add("part-of-a-name");
                cpsSet.add("part-of-a-name");
                return;
            }
        }
        
        // <token:-1><token><token:+1><token:+2>
        if (tkM1 != null && tkP1 != null && tkP2 != null) {
            if (Regex.isName(tkM1 + " " + tk + " " + tkP1 + " " + tkP2)) {
                cpsList.add("part-of-a-name");
                cpsSet.add("part-of-a-name");
                return;
            }
        }
        
        // <token><token:+1>
        if (tkP1 != null) {
            if (Regex.isName(tk + " " + tkP1)) {
                cpsList.add("part-of-a-name");
                cpsSet.add("part-of-a-name");
                return;
            }
        }
        
        // <token><token:+1><token:+2>
        if (tkP1 != null && tkP2 != null) {
            if (Regex.isName(tk + " " + tkP1 + " " + tkP2)) {
                cpsList.add("part-of-a-name");
                cpsSet.add("part-of-a-name");
                // return;
            }
        }
    }
    
    public static void scanOrderedListRelatedCPs(
            String tkM1, String tk, String tkP1,
            List cpsList, Set cpsSet) {
        /*
         * Checking order number like 1., 2., 3., etc.
         */
        /*
        if (tkM1 == null && tk.matches("[1-9]\\.") && tkP1 != null && 
                Regex.isFirstCap(tkP1)) {
            cpsList.add("first-sent");
            cpsSet.add("first-sent");
            
            cpsSet.remove("firstcap:+1");
        } 
        */
        
        if (tkM1 == null && tk.matches("[1-9]\\.") && tkP1 != null && 
                StrFormChecker.isFirstCap(tkP1)) {
            cpsList.add("first-sent");
            cpsSet.add("first-sent");
            
            cpsSet.remove("firstcap:+1");
        } 
        
        if (tkM1 != null && tk.matches("[1-9]\\.")) {
            if (tkM1.endsWith(".") || tkM1.endsWith("!") || tkM1.endsWith("?") || 
                    tkM1.endsWith("…") || tkM1.endsWith(".\"") || tkM1.endsWith("!\"") || 
                    tkM1.endsWith("?\"") || tkM1.endsWith(".”") || tkM1.endsWith("!”") || 
                    tkM1.endsWith("?”") || tkM1.endsWith("…”")) {
                    
                /*
                if (tkP1 != null && Regex.isFirstCap(tkP1)) {
                    cpsList.add("first-sent");
                    cpsSet.add("first-sent");
                    
                    cpsSet.remove("firstcap:+1");
                }
                */
                
                if (tkP1 != null && StrFormChecker.isFirstCap(tkP1)) {
                    cpsList.add("first-sent");
                    cpsSet.add("first-sent");
                    
                    cpsSet.remove("firstcap:+1");
                }
            }
        }
    }
    
    public static void scanStopAbbrsRelatedCPs(
            String str, String suffix, List cpsList, Set cpsSet) {

        if (StopAbbrsDict.vnContainsCaseSensitive(str) ||
                StopAbbrsDict.enContainsCaseSensitive(str)) {
            cpsList.add("abbr:" + suffix);
            cpsSet.add("abbr:" + suffix);
        }
    }
    
    public static void scanHonotificTitlesRelatedCPs(
            String tk, String tkP1, String suffix, List cpsList, Set cpsSet) {
        if (HonotificTitlesDict.contains(tk)) {
            /*
            if (tkP1 != null && Regex.isFirstCap(tkP1)) {
                cpsList.add("honotific:" + suffix);
                cpsSet.add("honotific:" + suffix);
            }
            */
            
            if (tkP1 != null && StrFormChecker.isFirstCap(tkP1)) {
                cpsList.add("honotific:" + suffix);
                cpsSet.add("honotific:" + suffix);
            }
        }
    }
}
