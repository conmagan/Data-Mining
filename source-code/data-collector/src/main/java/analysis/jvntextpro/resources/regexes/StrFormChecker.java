/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package analysis.jvntextpro.resources.regexes;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import analysis.jvntextpro.lib.string.StrUtil;

/**
 *
 * @author hieupx
 */
public class StrFormChecker {
    public static final String allCapChars = "AÀẢÃÁẠĂẰẲẴẮẶÂẦẨẪẤẬBCDĐÐEÈẺẼÉẸÊỀỂỄẾỆFGHIÌỈĨÍỊJKLMNOÒỎÕÓỌÔỒỔỖỐỘƠỜỞỠỚỢPQRSTUÙỦŨÚỤƯỪỬỮỨỰVWXYỲỶỸÝỴZ";
    public static final String allLowChars = "aàảãáạăằẳẵắặâầẩẫấậbcdđeèẻẽéẹêềểễếệfghiìỉĩíịjklmnoòỏõóọôồổỗốộơờởỡớợpqrstuùủũúụưừửữứựvwxyỳỷỹýỵz";
    public static final String specialChars = ",.!;:-–? ";
    
    public static final Set<Character> allCapCharsSet = new HashSet();
    public static final Set<Character> allLowCharsSet = new HashSet();
    public static final Set<Character> specialCharsSet = new HashSet();
    
    static {
        for (int i = 0; i < allCapChars.length(); i++) {
            allCapCharsSet.add(allCapChars.charAt(i));
        }
        
        for (int i = 0; i < allLowChars.length(); i++) {
            allLowCharsSet.add(allLowChars.charAt(i));
        }
        
        for (int i = 0; i < specialChars.length(); i++) {
            specialCharsSet.add(specialChars.charAt(i));
        }
    }
    
    public static boolean hasAlpha(String str) {
        for (int i = 0; i < str.length(); i++) {
            char ch = str.charAt(i);
            if (allCapCharsSet.contains(ch) || allLowCharsSet.contains(ch)) {
                return true;
            }
        }
        
        return false;
    }
    
    public static boolean isTokenAllCap(String str) {
        if (str.length() <= 0) {
            return false;
        }
        
        if (!allCapCharsSet.contains(str.charAt(0))) {
            return false;
        }
        
        for (int i = 1; i < str.length(); i++) {
            char ch = str.charAt(i);
            if (!(allCapCharsSet.contains(ch) || specialCharsSet.contains(ch))) {
                return false;
            }
        } 
        
        return true;
    }
    
    public static boolean isAllCap(String str) {
        List<String> tokens = StrUtil.tokenizeStr(str);
        
        for (int i = 0; i < tokens.size(); i++) {
            if (!isTokenAllCap(tokens.get(i))) {
                return false;
            }
        }
        
        return true;
    }
    
    public static boolean isTokenInitCap(String str) {
        if (str.length() <= 0) {
            return false;
        }

        if (!allCapCharsSet.contains(str.charAt(0))) {
            return false;
        }

        for (int i = 1; i < str.length(); i++) {
            char ch = str.charAt(i);
            if (!(allLowCharsSet.contains(ch) || specialCharsSet.contains(ch))) {
                return false;
            }
        } 
        
        return true;
    }
    
    public static boolean isInitCap(String str) {
        List<String> tokens = StrUtil.tokenizeStr(str);
        
        for (int i = 0; i < tokens.size(); i++) {
            if (!isTokenInitCap(tokens.get(i))) {
                return false;
            }
        }
        
        return true;
    }
    
    public static boolean isTokenAllLow(String str) {
        if (str.length() <= 0) {
            return false;
        }
        
        if (!allLowCharsSet.contains(str.charAt(0))) {
            return false;
        }
        
        for (int i = 1; i < str.length(); i++) {
            char ch = str.charAt(i);
            if (!(allLowCharsSet.contains(ch) || specialCharsSet.contains(ch))) {
                return false;
            }
        } 
        
        return true;
    }
    
    public static boolean isAllLow(String str) {
        List<String> tokens = StrUtil.tokenizeStr(str);
        
        for (int i = 0; i < tokens.size(); i++) {
            if (!isTokenAllLow(tokens.get(i))) {
                return false;
            }
        }
        
        return true;
    }
    
    public static boolean isFirstCap(String str) {
        if (str.length() <= 0) {
            return false;
        }
        
        for (int i = 0; i < str.length(); i++) {
            char ch = str.charAt(i);
            
            if (allLowCharsSet.contains(ch)) {
                return false;
            }
            
            if (allCapCharsSet.contains(ch)) {
                return true;
            }
        }
        
        return false;
    }

    public static boolean isFirstLow(String str) {
        if (str.length() <= 0) {
            return false;
        }
        
        for (int i = 0; i < str.length(); i++) {
            char ch = str.charAt(i);

            if (allCapCharsSet.contains(ch)) {
                return false;
            }
            
            if (allLowCharsSet.contains(ch)) {
                return true;
            }
        }
        
        return false;
    }
}
