/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package analysis.jvntextpro.nlp.vntokenizer;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import analysis.jvntextpro.lib.string.StrUtil;
import analysis.jvntextpro.resources.dicts.specialchars.SpecialChars;
import analysis.jvntextpro.resources.regexes.Regex;

/**
 *
 * @author hieupx
 */
public class VnTokenizer {
    private static String sentEndRegex = "^(((…)+|(\\.){2,}|\\.v(\\.v){1,}\\.)+(\\!)+|\\.(\\!)+|(\\!)+\\.|(\\?)+(\\!)+|(\\!)+(\\?)+|(…)+|(\\.){2,}|\\.v(\\.v){1,}\\.|(\\./)?\\.|(\\!)+|(\\?)+)";
    private static String sentEndWithSuffixRegex = "(((…)+|(\\.){2,}|\\.v(\\.v){1,}\\.)+(\\!)+|\\.(\\!)+|(\\!)+\\.|(\\?)+(\\!)+|(\\!)+(\\?)+|(…)+|(\\.){2,}|\\.v(\\.v){1,}\\.|(\\./)?\\.|(\\!)+|(\\?)+)(('|’|\"|”|\\)|\\]|\\}|⟩|»)*)$";
    private static Pattern sentEndPattern = null;
    private static Pattern sentEndWithSuffixPattern = null;

    static {
        compiles();
    }
    public static void compiles() {
        sentEndPattern = Pattern.compile(sentEndRegex);
        sentEndWithSuffixPattern = Pattern.compile(sentEndWithSuffixRegex);
    }
    
    /**
     * NOTE: the following order is important:
     * ellipsis-exclamation: (((…)+|(\\.){2,}|\\.v(\\.v){1,}\\.)+(\\!)+)
     * fullstop-exclamation: (\\.(\\!)+)
     * exclamation-fullstop: ((\\!)+\\.)
     * question-exclamation: ((\\?)+(\\!)+)
     * exclamation-question: ((\\!)+(\\?)+)
     * ellipsis: ((…)+|(\\.){2,}|\\.v(\\.v){1,}\\.)
     * fullstop: ((\\./)?\\.)
     * exclamation: ((\\!)+)
     * question: ((\\?)+)
     * 
     * Suffix can be: (('|’|"|”|\\)|\\]|\\}|⟩|»)*)
     */
    public static String tokenizeLastToken(String str) {
        String res;
        
        Matcher matcher = sentEndWithSuffixPattern.matcher(str);
        
        if (matcher.find()) {
            int start = matcher.start();
            int end;
  
            res = tokenizeToken(str.substring(0, start));
            String sPart = str.substring(start);
            
            matcher = sentEndPattern.matcher(sPart);
            if (matcher.find()) {
                start = matcher.start();
                end = matcher.end();
                
                res += " " + sPart.substring(start, end);
                
                String suffix = sPart.substring(end);
                if (suffix.length() > 0) {
                    res += " " + tokenizeToken(suffix);
                }
            }
            
        } else {
            res = tokenizeToken(str);
        }
        
        return res;
    }
    
    public static String tokenizeToken(String str) {
        str = replaceBrackets(str);
        str = replaceQuotationMarks(str);
        str = replaceComma(str);
        str = replaceColon(str);
        str = replaceSemicolon(str);
        str = replaceThreeDots(str);
        str = replaceSlash(str);
        
        return str;
    }
    
    /*
     * <<PXH>>
     */
    
    public static String normalizeThreeDots(String str) {
        String res = str.replaceAll("\\.+(\\s+\\.+)+", "...");
        res = res.replaceAll("(\\.){2,}", "...");
        return res;
    }

    /**
     * Brackets: 
     * (parentheses) [square brackets] {curly brackets} ⟨chevrons⟩
     * @param str
     * @return 
     */
    public static String replaceBrackets(String str) {
        str = str.replaceAll("\\(", " \\( ").replaceAll("\\)", " \\) ");
        str = str.replaceAll("\\[", " \\[ ").replaceAll("\\]", " \\] ");
        str = str.replaceAll("\\{", " \\{ ").replaceAll("\\}", " \\} ");
        str = str.replaceAll("⟨", " ⟨ ").replaceAll("⟩", " ⟩ ");
        str = str.replaceAll("«", " « ").replaceAll("»", " » ");
        
        return str;
    }
    
    /**
     * Quotation marks: 
     * `|'|"|``|''|''''|````|""
     * ‘|’|“|”|‘‘|’’|““|””
     * @param str
     * @return 
     */
    public static String replaceQuotationMarks(String str) {
        str = str.replaceAll("````", " <<!qm1!>> <<!qm1!>> ");
        str = str.replaceAll("''''", " <<!qm2!>> <<!qm2!>> ");
        str = str.replaceAll("``", " <<!qm1!>> ");
        str = str.replaceAll("''", " <<!qm2!>> ");

        str = str.replaceAll("‘‘", " <<!qm3!>> ");
        str = str.replaceAll("’’", " <<!qm4>!> ");
        
        str = str.replaceAll("`", " ` ");
        str = str.replaceAll("'", " ' ");
        str = str.replaceAll("\"", " \" ");
        
        str = str.replaceAll("‘", " ‘ ");
        str = str.replaceAll("’", " ’ ");
        str = str.replaceAll("“", " “ ");
        str = str.replaceAll("”", " ” ");
        
        str = str.replaceAll("<<!qm1!>>", "``");
        str = str.replaceAll("<<!qm2!>>", "''");
        str = str.replaceAll("<<!qm3!>>", "‘‘");
        str = str.replaceAll("<<!qm3!>>", "’’");
        
        return str;
    }
    
    /**
     * Comma:
     * @param str
     * @return 
     */
    public static String replaceComma(String str) {
        return str.replaceAll(",", " , ");
    }
    
    /**
     * Semicolon:
     * @param str
     * @return 
     */
    public static String replaceSemicolon(String str) {
        return str.replaceAll(";", " ; ");
    }
    
    /**
     * Colon:
     * @param str
     * @return 
     */
    public static String replaceColon(String str) {
        return str.replaceAll(":", " : ");
    }
    
    /**
     * Three dots: 
     * (normalizeThreeDots should be called first)
     * ..., …, etc.
     * @param str
     * @return 
     */
    public static String replaceThreeDots(String str) {
        return str.replaceAll("…", " … ").replaceAll("\\.\\.\\.", " ... ");
    }
    
    /**
     * Slash:
     * @param str
     * @return 
     */
    public static String replaceSlash(String str) {
        return str.replaceAll("/$", " / ").replaceAll("⁄$", " ⁄ ");
    }
    
    /**
     * Tokenizing a sentence
     * @param str
     * @return 
     */
    public static String tokenize(String str) {
        String res = "";
        
        // do some normalization first
        str = normalizeThreeDots(str);
        
        List iTokens = StrUtil.tokenizeStr(str);
        int len = iTokens.size();
        
        for (int i = 0; i < len; i++) {
            String iToken = (String)iTokens.get(i);
            
            if (i == len - 1) {
                res += " " + tokenizeLastToken(iToken);
            
            } else {
                if (!SpecialChars.checkSpecialCharIn(iToken)) {
                    res += " " + iToken;
                
                } else {
                    if (Regex.matchAnyRegex(iToken)) {
                        res += " " + iToken;
                    } else {
                        res += " " + tokenizeToken(iToken);
                    }
                }
            }
        }
        
        return StrUtil.normalizeStr(res);
    }
}
