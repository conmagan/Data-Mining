/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package analysis.jvntextpro.lib.filesystem;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import analysis.jvntextpro.lib.pairs.PairStrInt;
import analysis.jvntextpro.lib.string.StrUtil;

/**
 * Utilities for directory and file processing
 * @author hieupx
 */
public class DirFileUtil {
    /**
     * This method list all files in a directory
     * This method calls method listDir(dirName, ext) with ext = "*"
     * @param dir - directory name
     * @return a list of filenames
     */
    public static List<String> listDir(String dir) {
        return listDir(dir, "*");
    }
    
    /**
     * This method lists all files in a directory
     * @param dir - the directory name
     * @param ext - the file extension
     * @return a list of filenames
     */
    public static List<String> listDir(String dir, String ext) {
        List<String> res = null;
        
        File d = new File(dir);
        String[] children = d.list();
        
        if (children == null) {
            System.out.println("Specified directory does not exist or is not a directory!");
            return res;
        } else {
            res = new ArrayList();
            
            if (ext.equals("*") || ext.equals(".*")) {
                res.addAll(Arrays.asList(children));
            } else {
                for (int i = 0; i < children.length; i++) {
                    if (children[i].endsWith(ext)) {
                        res.add(children[i]);
                    }
                }
            }
        }
        
        return res;
    }
    
    /**
     * Get full filenames (including directory) from directory "dir"
     * @param dir
     * @param ext
     * @return 
     */
    public static List<String> getFilenamesFromDir(String dir, String ext) {
        List<String> res = null;
        List files = listDir(dir, ext);
        
        if (files.size() > 0) {
            res = new ArrayList();
            for (int i = 0; i < files.size(); i++) {
                res.add(getFullFilename(dir, (String)files.get(i)));
            }
        }

        return res;
    }
    
    /**
     * This method combines a directory and a filename to return a full name part
     * @param dir - the directory name
     * @param file - the filename
     * @return a full name part
     */
    public static String getFullFilename(String dir, String file) {
        return normalizeDir(dir) + File.separator + file;
    }
    
    /**
     * This method normalizes a directory path, removing path separator if needed
     * @param dir - the input directory
     * @return the directory path without path separator
     */
    public static String normalizeDir(String dir) {
        if (dir.endsWith(File.separator)) {
            return StrUtil.removeLastChar(dir);
        } else {
            return dir;
        }
    }
    
    public static void saveListString(List<String> list, String filename) {
        BufferedWriter out;        
        try {
            out = new BufferedWriter(
                         new OutputStreamWriter(
                         new FileOutputStream(filename), "UTF8"));

            for (int i = 0; i < list.size(); i++) {
                out.write((String)list.get(i) + "\n");
            }
            
            out.close();
            
        } catch (IOException ex) {
            System.err.println(ex.toString());
            System.exit(1);
        }
    }
    
    public static void saveListStrInt(List<PairStrInt> list, String filename) {
        BufferedWriter out;
        try {
            out = new BufferedWriter(
                         new OutputStreamWriter(
                         new FileOutputStream(filename), "UTF8"));
            
            for (int i = list.size() - 1; i >= 0; i--) {
                out.write(list.get(i).toString() + "\n");
            }
            
            out.close();
            
        } catch (IOException ex) {
            System.err.println(ex.toString());
            System.exit(1);
        }
    }
}
