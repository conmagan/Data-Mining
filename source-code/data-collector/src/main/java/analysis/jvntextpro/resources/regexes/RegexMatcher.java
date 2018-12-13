/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package analysis.jvntextpro.resources.regexes;

import java.util.*;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

/**
 *
 * @author hieupx
 */
public class RegexMatcher {
    private static Map<String, Map> type2CompiledRegexMap = null;
    
    static {
        type2CompiledRegexMap = new HashMap();
        compile();
    }
    
    public static void compile() {
        Set keys = RegexLoader.getType2RegexMap().entrySet();
        Iterator it = keys.iterator();
        
        while (it.hasNext()) {
            Map.Entry me = (Map.Entry)it.next();
            
            String regexType = (String)me.getKey();
            Map<String, String> regexMap = (Map<String, String>)me.getValue();
            
            Map<String, Pattern> regexCompiledMap = new HashMap();

            Set regexKeys = regexMap.entrySet();
            Iterator regexIt = regexKeys.iterator();
            
            while (regexIt.hasNext()) {
                Map.Entry regexME = (Map.Entry)regexIt.next();
                
                String regexID = (String)regexME.getKey();
                String regexValue = (String)regexME.getValue();
                
                try {
                    Pattern regexCompiledPattern = Pattern.compile(regexValue);
                    
                    regexCompiledMap.put(regexID, regexCompiledPattern);
                    
                } catch (PatternSyntaxException ex) {
                    System.err.println(ex.getMessage());
                    System.exit(1);
                }
            }
            
            if (regexCompiledMap.size() > 0) {
                type2CompiledRegexMap.put(regexType, regexCompiledMap);
            }
        }
    }
    
    public static String addStartEndLineChars(String pattern) {
        String res = pattern;
        
        if (!res.startsWith("^")) {
            res = "^" + res;
        }
        
        if (!res.endsWith("$")) {
            res += "$";
        }
        
        return res;
    }
    
    public static Map getPatterns(String regexType) {
        return type2CompiledRegexMap.get(regexType);
    }
    
    public static Pattern getPattern(String regexType, String regexID) {
        Map<String, Pattern> regexCompiledMap = type2CompiledRegexMap.get(regexType);
        return (Pattern)regexCompiledMap.get(regexID);
    }
    
    public static boolean matchAnything(String str) {
        Set keys = type2CompiledRegexMap.entrySet();
        Iterator it = keys.iterator();
        
        while (it.hasNext()) {
            Map.Entry me = (Map.Entry)it.next();            
            Map<String, Pattern> regexCompiledMap = (Map<String, Pattern>)me.getValue();
            
            Set regexKeys = regexCompiledMap.entrySet();
            Iterator regexIt = regexKeys.iterator();
            
            while (regexIt.hasNext()) {
                Map.Entry regexME = (Map.Entry)regexIt.next();
                
                Pattern regexCompiledPattern = (Pattern)regexME.getValue();
                
                if (regexCompiledPattern.matcher(str).matches()) {
                    return true;
                }            
            }
        }
        
        return false;
    }
    
    public static List match(String str) {
        return match(str, false);
    }
    
    public static List match(String str, boolean find) {
        List<String> res = new ArrayList();

        Set keys = type2CompiledRegexMap.entrySet();
        Iterator it = keys.iterator();
        
        while (it.hasNext()) {
            Map.Entry me = (Map.Entry)it.next();            
            Map<String, Pattern> regexCompiledMap = (Map<String, Pattern>)me.getValue();
            
            Set regexKeys = regexCompiledMap.entrySet();
            Iterator regexIt = regexKeys.iterator();
            
            while (regexIt.hasNext()) {
                Map.Entry regexME = (Map.Entry)regexIt.next();
                
                String regexID = (String)regexME.getKey();
                Pattern regexCompiledPattern = (Pattern)regexME.getValue();

                if (find) {
                    if (regexCompiledPattern.matcher(str).find()) {
                        res.add(regexID);
                    }
                } else {
                    if (regexCompiledPattern.matcher(str).matches()) {
                        res.add(regexID);
                    }
                }
            }
        }

        return res;
    }
    
    public static List match(String regexType, String str) {
        return match(regexType, str, false);
    }
    
    public static List match(String regexType, String str, boolean find) {
        List<String> res = new ArrayList();

        Map<String, Pattern> regexCompiledMap = getPatterns(regexType);
        
        Set regexKeys = regexCompiledMap.entrySet();
        Iterator regexIt = regexKeys.iterator();
        
        while (regexIt.hasNext()) {
            Map.Entry regexME = (Map.Entry)regexIt.next();
            
            String regexID = (String)regexME.getKey();
            Pattern regexCompiledPattern = (Pattern)regexME.getValue();
            
            if (find) {
                if (regexCompiledPattern.matcher(str).find()) {
                    res.add(regexID);
                }
            } else {
                if (regexCompiledPattern.matcher(str).matches()) {
                    res.add(regexID);
                }
            }
        }

        return res;
    }

    public static boolean matchAnyThing(String regexType, String str) {
        return matchAnyThing(regexType, str, false);
    }
    
    public static boolean matchAnyThing(String regexType, String str, boolean find) {
        Map<String, Pattern> regexCompiledMap = getPatterns(regexType);
        
        Set regexKeys = regexCompiledMap.entrySet();
        Iterator regexIt = regexKeys.iterator();
        
        while (regexIt.hasNext()) {
            Map.Entry regexME = (Map.Entry)regexIt.next();
            
            String regexID = (String)regexME.getKey();
            Pattern regexCompiledPattern = (Pattern)regexME.getValue();
            
            if (find) {
                if (regexCompiledPattern.matcher(str).find()) {
                    return true;
                }
            } else {
                if (regexCompiledPattern.matcher(str).matches()) {
                    return true;
                }
            }
        }

        return false;
    }
    
    public static boolean match(String regexType, String regexID, String str) {
        return match(regexType, regexID, str, false);
    }
    
    public static boolean match(String regexType, String regexID, String str, boolean find) {
        Pattern regexCompiledPattern = getPattern(regexType, regexID);
        if (regexCompiledPattern != null) {
            if (find) {
                return regexCompiledPattern.matcher(str).find();
            } else {
                return regexCompiledPattern.matcher(str).matches();
            }
        } else {
            return false;
        }
    }
}
