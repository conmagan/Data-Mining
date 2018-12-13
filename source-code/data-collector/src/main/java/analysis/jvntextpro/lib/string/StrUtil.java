/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package analysis.jvntextpro.lib.string;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

/**
 *
 * @author hieupx
 */
public class StrUtil {
    public static List tokenizeStr(String str) {
        return StrUtil.tokenizeStr(str, " \t\r\n");
    }
    
    public static List<String> tokenizeStr(String str, String delim) {
        List res = new ArrayList();
        
	StringTokenizer strtok = new StringTokenizer(str, delim);
	int len = strtok.countTokens();
	
	for (int i = 0; i < len; i++) {
	    res.add(strtok.nextToken());
	}
        
        return res;
    }
    
    public static List<String> tokenizeStrAll(String str) {
        return StrUtil.tokenizeStr(str, " \t\r\n.,;:?!~^*-$%#@().…'\"=/");
    }
    
    public static String normalizeStr(String str) {
        String res = "";
        
        StringTokenizer strtok = new StringTokenizer(str, " \t\r\n");
        
        int len = strtok.countTokens();
        if (len > 0) {
            res = strtok.nextToken();
        }
        for (int i = 1; i < len; i++) {
            res += " " + strtok.nextToken();
        }

        return res;
    }
    
    public static String join(List<String> lst, int start, int end) {
        return join(lst, start, end, " ");
    }
    
    public static String join(List<String> lst, int start, int end, String delim) {
        String res = "";
        
        if (start < 0) {
            start = 0;
        }
        
        if (end >= lst.size()) {
            end = lst.size() - 1;
        }
        
        if (start > end) {
            return res;
        } else {
            res = (String)lst.get(start);
            for (int i = start + 1; i <= end; i++) {
                res += delim + (String)lst.get(i);
            }
            return res;
        }
    }
    
    public static String join(List<String> lst, String delim) {
        String res = "";
        
        if (lst.size() > 0) {
            res = lst.get(0);
        }
        for (int i = 1; i < lst.size(); i++) {
            res += delim + lst.get(i);
        }
        
        return res;
    }
    
    public static String join(List<String> lst) {
        return join(lst, " ");
    }
    
    public static String removeLastChar(String str) {
        if (str == null || str.length() == 0) {
            return str;
        } else {
            return str.substring(0, str.length() - 1);
        }
    }
    
    public static String removeFirstChar(String str) {
        if (str == null || str.length() == 0) {
            return str;
        } else {
            return str.substring(1, str.length());
        }
    }
    
    public static String toFirstCap(String str) {
        if (str == null || str.length() <= 0) {
            return str;
        }

        String firstChar = str.substring(0, 1);
        String remaining = str.substring(1, str.length());
        
        return firstChar.toUpperCase() + remaining.toLowerCase();
    }
    
    public static String toInitCap(String str) {
        List tokens = StrUtil.tokenizeStr(str);
        
        List<String> normalizedTokens = new ArrayList();
        for (int i = 0; i < tokens.size(); i++) {
            normalizedTokens.add(toFirstCap((String)tokens.get(i)));
        }
        
        return StrUtil.join(normalizedTokens);
    }
}
