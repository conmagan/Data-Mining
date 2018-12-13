/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package analysis.jvntextpro.lib.collection;

import java.util.*;
import analysis.jvntextpro.lib.math.RandomNumber;

/**
 * CollectionUtil: utilities for processing collection data structures
 * @author hieupx
 */
public class CollectionUtil {
    /**
     * Randomly swap elements of an input list
     * This static method calls randomSwap(lst, times) with times = 1
     * @param lst - the input list of elements
     */
    public static void randomSwap(List lst) {
        randomSwap(lst, 1);
    }
    
    /**
     * Randomly swap elements of an input list.
     * The random swap repeats sizeof(lst) * times
     * @param lst - the input list of elements
     * @param times - random swap will be repeated (sizeof(lst) * times) times
     */
    public static void randomSwap(List lst, int times) {
        RandomNumber randNum = new RandomNumber();
        int lstSize = lst.size();

        int repeat = times * lstSize;
        for (int i = 0; i < repeat; i++) {
            int idx1 = randNum.genNextInt(lstSize);
            int idx2 = randNum.genNextInt(lstSize);
            if (idx1 != idx2) {
                Collections.swap(lst, idx1, idx2);
            }
        }
    }
    
    /**
     * This method randomly partitions the input list (orgLst) into noParts parts
     * @param orgLst - the input list of elements
     * @param firstLst - the list that contains the first part
     * @param secondLst - the list that contains the remaing parts
     * @param noParts - the number of partitions
     * @return 1 if no partitioning is performed, otherwise return 0
     */
    public static int randomPartition(List orgLst, List firstLst, List secondLst, int noParts) {
        if (noParts < 2) {
            return 1;
        }
        
        randomSwap(orgLst, 4);
        
        int orgSize = orgLst.size();
        int partSize = orgSize / noParts;
        
        if (partSize <= 0) {
            return 1;
        }
        
        firstLst.clear();
        secondLst.clear();
        
        Set<Integer> idxes = new HashSet();
        RandomNumber randNum = new RandomNumber();
        
        while (idxes.size() < partSize) {
            int randInt = randNum.genNextInt(orgSize);
            if (idxes.contains(randInt)) {
                continue;
            } else {
                idxes.add(randInt);
            }
        }
        
        for (int i = 0; i < orgSize; i++) {
            if (idxes.contains(i)) {
                firstLst.add(orgLst.get(i));
            } else {
                secondLst.add(orgLst.get(i));
            }
        }
        
        return 0;
    }
    
    /**
     * This method randomly partitions an input list of element into n folds
     * @param orgLst - the input list of elements
     * @param nFolds - the number of data folds
     * @return a list of n folds, each fold contain a list of elements
     */
    public static List randomNFoldsPartition(List orgLst, int nFolds) {
        List res = null;

        int orgSize = orgLst.size();
        if (orgSize <= 0 || nFolds < 2 || orgSize < nFolds) {
            return res;
        }
        
        res = new ArrayList();
        for (int i = 0; i < nFolds; i++) {
            res.add(new ArrayList());
        }
        
        randomSwap(orgLst);
        
        for (int i = 0; i < orgSize; i++) {
            int idx = i % nFolds;
            List lst = (List)res.get(idx);
            lst.add(orgLst.get(i));
        }
        
        return res;
    }
}
