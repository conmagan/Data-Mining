/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package analysis.jvntextpro.resources.regexes;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Utilities for regular expression finding/matching
 * @author pxhieu
 */
public class Regex {
    private static Map<String, Pattern> numberPtrns = null;
    private static Map<String, Pattern> datePtrns = null;
    private static Map<String, Pattern> timePtrns = null;
    private static Map<String, Pattern> currencyPtrns = null;
    private static Map<String, Pattern> percentPtrns = null;
    private static Map<String, Pattern> emailPtrns = null;
    private static Map<String, Pattern> urlPtrns = null;
    private static Map<String, Pattern> ipPtrns = null;
    private static Map<String, Pattern> dates = null;
    private static Map<String, Pattern> schkPtrns = null;
    private static Map<String, Pattern> degreePtrns = null;
    private static Map<String, Pattern> currencyunitPtrns = null;
    private static Map<String, Pattern> firstnumberPtrns = null;
    private static Map<String, Pattern> firstcapPtrns = null;
    private static Map<String, Pattern> firstlowerPtrns = null;
    private static Map<String, Pattern> initcapPtrns = null;
    private static Map<String, Pattern> allcapPtrns = null;
    private static Map<String, Pattern> alllowerPtrns = null;
    private static Map<String, Pattern> namePtrns = null;
    private static Map<String, Pattern> shortformPtrns = null;

    static {
        schkPtrns = RegexMatcher.getPatterns("specialcharschunk");
        datePtrns = RegexMatcher.getPatterns("date");
        timePtrns = RegexMatcher.getPatterns("time");
        numberPtrns = RegexMatcher.getPatterns("number");
        percentPtrns = RegexMatcher.getPatterns("percent");
        degreePtrns = RegexMatcher.getPatterns("degree");
        currencyunitPtrns = RegexMatcher.getPatterns("currencyunit");
        currencyPtrns = RegexMatcher.getPatterns("currency");
        emailPtrns = RegexMatcher.getPatterns("email");
        ipPtrns = RegexMatcher.getPatterns("ipaddress");
        urlPtrns = RegexMatcher.getPatterns("url");
        firstnumberPtrns = RegexMatcher.getPatterns("firstnumber");
        firstcapPtrns = RegexMatcher.getPatterns("firstcap");
        firstlowerPtrns = RegexMatcher.getPatterns("firstlower");
        initcapPtrns = RegexMatcher.getPatterns("initcap");
        allcapPtrns = RegexMatcher.getPatterns("allcap");
        alllowerPtrns = RegexMatcher.getPatterns("alllower");
        namePtrns = RegexMatcher.getPatterns("name");
        shortformPtrns = RegexMatcher.getPatterns("shortform");
    }
    
    public static List match(Map<String, Pattern> mapPtrns, String str) {
        return match(mapPtrns, str, false);
    }
    
    public static List match(Map<String, Pattern> mapPtrns, String str, boolean find) {
        List<String> res = new ArrayList();
        
        for (Map.Entry<String, Pattern> entry : mapPtrns.entrySet()) {
            String regexID = entry.getKey();
            Pattern regexPattern = entry.getValue();
            
            if (find) {
                if (regexPattern.matcher(str).find()) {
                    res.add(regexID);
                }
            } else {
                if (regexPattern.matcher(str).matches()) {
                    res.add(regexID);
                }
            }
        }

        return res;
    }

    public static boolean matchAnyThing(Map<String, Pattern> mapPtrns, String str) {
        return matchAnyThing(mapPtrns, str, false);
    }
    
    public static boolean matchAnyThing(Map<String, Pattern> mapPtrns, String str, boolean find) {
        for (Map.Entry<String, Pattern> entry : mapPtrns.entrySet()) {
            Pattern regexPattern = entry.getValue();
            
            if (find) {
                if (regexPattern.matcher(str).find()) {
                    return true;
                }
            } else {
                if (regexPattern.matcher(str).matches()) {
                    return true;
                }
            }
        }
        
        return false;
    }
    
    public static boolean matchAnyRegex(String str) {
        if (isNumber(str)) {
            return true;
        }
        
        if (isDate(str)) {
            return true;
        }
        
        if (isTime(str)) {
            return true;
        }
        
        if (isCurrency(str)) {
            return true;
        }
        
        if (isPercent(str)) {
            return true;
        }
        
        if (isEmail(str)) {
            return true;
        }
        
        if (isURL(str)) {
            return true;
        }
        
        if (isIPAddress(str)) {
            return true;
        }
        
        return false;
    }

    public static List<String> getSpecialCharsChunk(String str) {
        return match(schkPtrns, str);
        // return RegexMatcher.match("specialcharschunk", str);
    }

    public static boolean isSpecialCharsChunk(String str) {
        return matchAnyThing(schkPtrns, str);
        // return RegexMatcher.matchAnyThing("specialcharschunk", str);
    }
    
    public static boolean containsSpecialCharsChunk(String str) {
        return (match(schkPtrns, str, true).size() > 0);
        // return (RegexMatcher.match("specialcharschunk", str, true).size() > 0);
    }
    
    public static List<String> getDates(String str) {
        return match(datePtrns, str);
        // return RegexMatcher.match("date", str);
    }

    public static boolean isDate(String str) {
        return matchAnyThing(datePtrns, str);
        // return RegexMatcher.matchAnyThing("date", str);
    }
    
    public static boolean containsDate(String str) {
        return (match(datePtrns, str, true).size() > 0);
        // return (RegexMatcher.match("date", str, true).size() > 0);
    }

    public static List<String> getTimes(String str) {
        return match(timePtrns, str);
        // return RegexMatcher.match("time", str);
    }

    public static boolean isTime(String str) {
        return matchAnyThing(timePtrns, str);
        // return RegexMatcher.matchAnyThing("time", str);
    }
    
    public static boolean containsTime(String str) {
        return (match(timePtrns, str, true).size() > 0);
        // return (RegexMatcher.match("time", str, true).size() > 0);
    }
    
    public static List<String> getNumbers(String str) {
        return match(numberPtrns, str);
        // return RegexMatcher.match("number", str);
    }

    public static boolean isNumber(String str) {
        return matchAnyThing(numberPtrns, str);
        // return RegexMatcher.matchAnyThing("number", str);
    }
    
    public static boolean containsNumber(String str) {
        return (match(numberPtrns, str, true).size() > 0);
        // return (RegexMatcher.match("number", str, true).size() > 0);
    }
    
    public static List<String> getPercents(String str) {
        return match(percentPtrns, str);
        // return RegexMatcher.match("percent", str);
    }

    public static boolean isPercent(String str) {
        return matchAnyThing(percentPtrns, str);
        // return RegexMatcher.matchAnyThing("percent", str);
    }
    
    public static boolean containsPercent(String str) {
        return (match(percentPtrns, str, true).size() > 0);
        // return (RegexMatcher.match("percent", str, true).size() > 0);
    }
    
    public static List<String> getDegrees(String str) {
        return match(degreePtrns, str);
        // return RegexMatcher.match("degree", str);
    }

    public static boolean isDegree(String str) {
        return matchAnyThing(degreePtrns, str);
        // return RegexMatcher.matchAnyThing("degree", str);
    }
    
    public static boolean containsDegree(String str) {
        return (match(degreePtrns, str, true).size() > 0);
        // return (RegexMatcher.match("degree", str, true).size() > 0);
    }
    
    public static List<String> getCurrencyUnits(String str) {
        return match(currencyunitPtrns, str);
        // return RegexMatcher.match("currencyunit", str);
    }

    public static boolean isCurrencyUnit(String str) {
        return matchAnyThing(currencyunitPtrns, str);
        // return RegexMatcher.matchAnyThing("currencyunit", str);
    }
    
    public static boolean containsCurrencyUnit(String str) {
        return (match(currencyunitPtrns, str, true).size() > 0);
        // return (RegexMatcher.match("currencyunit", str, true).size() > 0);
    }
    
    public static List<String> getCurrencies(String str) {
        return match(currencyPtrns, str);
        // return RegexMatcher.match("currency", str);
    }

    public static boolean isCurrency(String str) {
        return matchAnyThing(currencyPtrns, str);
        // return RegexMatcher.matchAnyThing("currency", str);
    }
    
    public static boolean containsCurrency(String str) {
        return (match(currencyPtrns, str, true).size() > 0);
        // return (RegexMatcher.match("currency", str, true).size() > 0);
    }
    
    public static List<String> getEmails(String str) {
        return match(emailPtrns, str);
        // return RegexMatcher.match("email", str);
    }
    
    public static boolean isEmail(String str) {
        return matchAnyThing(emailPtrns, str);
        // return RegexMatcher.matchAnyThing("email", str);
    }
    
    public static boolean containsEmail(String str) {
        return (match(emailPtrns, str, true).size() > 0);
        // return (RegexMatcher.match("email", str, true).size() > 0);
    }
    
    public static List<String> getIPAddresses(String str) {
        return match(ipPtrns, str);
        // return RegexMatcher.match("ipaddress", str);
    }

    public static boolean isIPAddress(String str) {
        return matchAnyThing(ipPtrns, str);
        // return RegexMatcher.matchAnyThing("ipaddress", str);
    }
    
    public static boolean containsIPAddress(String str) {   
        return (match(ipPtrns, str, true).size() > 0);
        // return (RegexMatcher.match("ipaddress", str, true).size() > 0);
    }
    
    public static List<String> getURLs(String str) {
        return match(urlPtrns, str);
        // return RegexMatcher.match("url", str);
    }

    public static boolean isURL(String str) {
        return matchAnyThing(urlPtrns, str);
        // return RegexMatcher.matchAnyThing("url", str);
    }
    
    public static boolean containsURL(String str) {
        return (match(urlPtrns, str, true).size() > 0);
        // return (RegexMatcher.match("url", str, true).size() > 0);
    }
    
    public static List<String> getFirstNumbers(String str) {
        return match(firstnumberPtrns, str);
        // return RegexMatcher.match("firstnumber", str);
    }

    public static boolean isFirstNumber(String str) {
        return matchAnyThing(firstnumberPtrns, str);
        // return RegexMatcher.matchAnyThing("firstnumber", str);
    }
    
    public static boolean containsFirstNumber(String str) {
        return (match(firstnumberPtrns, str, true).size() > 0);
        // return (RegexMatcher.match("firstnumber", str, true).size() > 0);
    }

    public static List<String> getFirstCaps(String str) {
        return match(firstcapPtrns, str);
        // return RegexMatcher.match("firstcap", str);
    }

    public static boolean isFirstCap(String str) {
        return matchAnyThing(firstcapPtrns, str);
        // return RegexMatcher.matchAnyThing("firstcap", str);
    }
    
    public static boolean containsFirstCap(String str) {
        return (match(firstcapPtrns, str, true).size() > 0);
        // return (RegexMatcher.match("firstcap", str, true).size() > 0);
    }

    public static List<String> getFirstLowers(String str) {
        return match(firstlowerPtrns, str);
        // return RegexMatcher.match("firstlower", str);
    }

    public static boolean isFirstLower(String str) {
        return matchAnyThing(firstlowerPtrns, str);
        // return RegexMatcher.matchAnyThing("firstlower", str);
    }
    
    public static boolean containsFirstLower(String str) {
        return (match(firstlowerPtrns, str, true).size() > 0);
        // return (RegexMatcher.match("firstlower", str, true).size() > 0);
    }
    
    public static List<String> getInitCaps(String str) {
        return match(initcapPtrns, str);
        // return RegexMatcher.match("initcap", str);
    }

    public static boolean isInitCap(String str) {
        return matchAnyThing(initcapPtrns, str);
        // return RegexMatcher.matchAnyThing("initcap", str);
    }
    
    public static boolean containsInitCap(String str) {
        return (match(initcapPtrns, str, true).size() > 0);
        // return (RegexMatcher.match("initcap", str, true).size() > 0);
    }
    
    public static List<String> getAllCaps(String str) {
        return match(allcapPtrns, str);
        // return RegexMatcher.match("allcap", str);
    }

    public static boolean isAllCap(String str) {
        return matchAnyThing(allcapPtrns, str);
        // return RegexMatcher.matchAnyThing("allcap", str);
    }
    
    public static boolean containsAllCap(String str) {
        return (match(allcapPtrns, str, true).size() > 0);
        // return (RegexMatcher.match("appcap", str, true).size() > 0);
    }
    
    public static List<String> getAllLowers(String str) {
        return match(alllowerPtrns, str);
        // return RegexMatcher.match("alllower", str);
    }

    public static boolean isAllLower(String str) {
        return matchAnyThing(alllowerPtrns, str);
        // return RegexMatcher.matchAnyThing("alllower", str);
    }
    
    public static boolean containsAllLower(String str) {
        return (match(alllowerPtrns, str, true).size() > 0);
        // return (RegexMatcher.match("alllower", str, true).size() > 0);
    }
    
    public static List<String> getNames(String str) {
        return match(namePtrns, str);
        // return RegexMatcher.match("name", str);
    }

    public static boolean isName(String str) {
        return matchAnyThing(namePtrns, str);
        // return RegexMatcher.matchAnyThing("name", str);
    }
    
    public static boolean containsName(String str) {
        return (match(namePtrns, str, true).size() > 0);
        // return (RegexMatcher.match("name", str, true).size() > 0);
    }
    
    public static List<String> getShortForms(String str) {
        return match(shortformPtrns, str);
        // return RegexMatcher.match("shortform", str);
    }
    
    public static boolean isShortForm(String str) {
        return matchAnyThing(shortformPtrns, str);
        // return RegexMatcher.matchAnyThing("shortform", str);
    }
    
    public static boolean containsShortForm(String str) {
        return (match(shortformPtrns, str, true).size() > 0);
        // return (RegexMatcher.match("shortform", str, true).size() > 0);
    }    
}
