/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package analysis.jvntextpro.resources.dicts.abbreviations;

import java.io.*;
import java.util.HashSet;
import java.util.Set;
import analysis.jvntextpro.lib.properties.Parameters;
import analysis.jvntextpro.lib.string.StrUtil;

/**
 *
 * @author hieupx
 */
public class StopAbbrsDict {
    private static Set<String> vnDictSet = null;
    private static Set<String> enDictSet = null;

    static {
        vnDictSet = new HashSet();
        enDictSet = new HashSet();
        loadVnDict(Parameters.getVnStopAbbrsFile());
        loadEnDict(Parameters.getEnStopAbbrsFile());
    }
    
    private static void loadVnDict(String dictFile) {
        BufferedReader fin;
        try {
            fin = new BufferedReader(
                    new InputStreamReader(
                    new FileInputStream(dictFile), "UTF8"));

            String word;
            while ((word = fin.readLine()) != null) {
                word = StrUtil.normalizeStr(word);
                if (word.length() >= 2) {
                    vnDictSet.add(word);
                }
            }
            
            fin.close();
            
        } catch (UnsupportedEncodingException ex) {
            System.err.println(ex.toString());
            System.exit(1);
        } catch (IOException ex) {
            System.err.println(ex.toString());
            System.exit(1);
        }        
    }

    private static void loadEnDict(String dictFile) {
        BufferedReader fin;
        try {
            fin = new BufferedReader(
                    new InputStreamReader(
                    new FileInputStream(dictFile), "UTF8"));

            String word;
            while ((word = fin.readLine()) != null) {
                word = StrUtil.normalizeStr(word);
                if (word.length() >= 2) {
                    enDictSet.add(word);
                }
            }
            
            fin.close();
            
        } catch (UnsupportedEncodingException ex) {
            System.err.println(ex.toString());
            System.exit(1);
        } catch (IOException ex) {
            System.err.println(ex.toString());
            System.exit(1);
        }        
    }
    
    public static boolean vnContains(String word) {
        boolean res = vnDictSet.contains(word);
        
        if (!res) {
            String loweredWord = word.toLowerCase();
            
            if (!loweredWord.equals(word)) {
                res = vnDictSet.contains(loweredWord);
            }
        }
        
        return res;
    }

    public static boolean vnContainsCaseSensitive(String word) {
        return vnDictSet.contains(word);
    }
    
    public static boolean enContains(String word) {
        boolean res = enDictSet.contains(word);
        
        if (!res) {
            String loweredWord = word.toLowerCase();
            
            if (!loweredWord.equals(word)) {
                res = enDictSet.contains(loweredWord);
            }
        }
        
        return res;
    }
    
    public static boolean enContainsCaseSensitive(String word) {
        return enDictSet.contains(word);
    }
    
    public static void print() {
        Object vnArrs[] = vnDictSet.toArray();
        System.out.println("\nVietnamese Abbreviation Dictionary:\n");
        for (int i = 0; i < vnArrs.length; i++) {
            System.out.println((String)vnArrs[i]);
        }
        
        Object enArrs[] = enDictSet.toArray();
        System.out.println("\nEnglish Abbreviation Dictionary:\n");
        for (int i = 0; i < enArrs.length; i++) {
            System.out.println((String)enArrs[i]);
        }
    }
}
