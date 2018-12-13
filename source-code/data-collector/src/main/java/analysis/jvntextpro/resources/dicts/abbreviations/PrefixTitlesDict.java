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
public class PrefixTitlesDict {
    private static Set<String> dictSet = null;
    
    static {
        dictSet = new HashSet();
        loadDict(Parameters.getPrefixTitlesFile());
    }
    
    private static void loadDict(String dictFile) {
        BufferedReader fin;

        try {
            fin = new BufferedReader(
                    new InputStreamReader(
                    new FileInputStream(dictFile), "UTF8"));

            String word;
            while ((word = fin.readLine()) != null) {
                word = StrUtil.normalizeStr(word);
                
                if (word.startsWith("#")) {
                    continue;
                }
                
                if (word.length() >= 2) {
                    dictSet.add(word);
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
    
    public static boolean contains(String word) {
        boolean res = dictSet.contains(word);
        
        if (!res) {
            String loweredWord = word.toLowerCase();
            res = dictSet.contains(loweredWord);
        }
        
        return res;
    }
    
    public static void print() {
        Object arrs[] = dictSet.toArray();
        System.out.println("\nPrefix titles dictionary:\n");
        for (int i = 0; i < arrs.length; i++) {
            System.out.println((String)arrs[i]);
        }
    }
}
