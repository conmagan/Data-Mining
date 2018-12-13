/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package analysis.jvntextpro.resources.regexes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import analysis.jvntextpro.lib.pairs.Pair;

/**
 *
 * @author hieupx
 */
public class PunctuationsMatcher {
    private static Map<String, Pattern> type2CompiledRegexMap = null;
    private static List<Pair<String, Pattern>> compiledRegexList = null;
    
    static {
        type2CompiledRegexMap = new HashMap();
        compiledRegexList = new ArrayList();
        compile();
    }
    
    public static void compile() {
        List<Pair<String, String>> regexList = PunctuationsLoader.getRegexList();
        for (int i = 0; i < regexList.size(); i++) {
            Pair<String, String> pair = (Pair<String, String>)regexList.get(i);
            
            Pattern pattern = Pattern.compile(pair.second);
            
            type2CompiledRegexMap.put(pair.first, pattern);
            compiledRegexList.add(new Pair(pair.first, pattern));
        }
    }
    
    public static String addStartEndLineChars(String pattern) {
        return RegexMatcher.addStartEndLineChars(pattern);
    }
    
    private static Pattern getPattern(String regexType) {
        return type2CompiledRegexMap.get(regexType);
    }
    
    public static String getMatchedType(String str) {
        String res = "";
        
        for (int i = 0; i < compiledRegexList.size(); i++) {
            Pair<String, Pattern> pair = (Pair<String, Pattern>)compiledRegexList.get(i);
            
            if (pair.second.matcher(str).matches()) {
                res = pair.first;
                break;
            }
        }
        
        return res;
    }
    
    public static List match(String str) {
        return match(str, false);
    }
    
    public static List match(String str, boolean find) {
        List<String> res = new ArrayList();
        
        for (int i = 0; i < compiledRegexList.size(); i++) {
            Pair<String, Pattern> pair = (Pair<String, Pattern>)compiledRegexList.get(i);
            
            if (find) {
                if (pair.second.matcher(str).find()) {
                    res.add(pair.first);
                }
            } else {
                if (pair.second.matcher(str).matches()) {
                    res.add(pair.first);
                }
            }
        }
        
        return res;
    }
    
    public static boolean match(String regexType, String str) {
        return match(regexType, str, false);
    }
    
    public static boolean match(String regexType, String str, boolean find) {
        Pattern pattern = getPattern(regexType);
        
        if (pattern != null) {
            if (find) {
                return pattern.matcher(str).find();
            } else {
                return pattern.matcher(str).matches();
            }
        }
        
        return false;
    }
}
