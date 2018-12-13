/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package analysis.jvntextpro.nlp.vnsentsegmenter;

/**
 *
 * @author hieupx
 */
public class SentSegPrepro {
    public static String preprocessText(String text) {
        text = text.replaceAll("\\.{4,}", "...");
        text = text.replaceAll("…{2,}", "…");
        text = text.replaceAll("\\!{2,}", "!");
        text = text.replaceAll("\\?{2,}", "?");
        
        return text;
    } 
}
