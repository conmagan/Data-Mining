/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package analysis.jvntextpro.resources.regexes;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import analysis.jvntextpro.lib.properties.Parameters;
import analysis.jvntextpro.lib.pairs.Pair;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 *
 * @author hieupx
 */
public class PunctuationsLoader {
    private static Map<String, String> type2RegexMap = null;
    private static List<Pair<String, String>> regexList = null;

    static {
        type2RegexMap = new HashMap();
        regexList = new ArrayList();
        readRegexXMLFile(Parameters.getRegexPunctuationMarksFile());
    }
    
    public static void readRegexXMLFile(String regexXMLFile) {
        try {
            File xmlFile = new File(regexXMLFile);
            
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = (Document)dBuilder.parse(xmlFile);
            doc.getDocumentElement().normalize();
            
            NodeList puncList = doc.getElementsByTagName("punctuation");
            
            for (int i = 0; i < puncList.getLength(); i++) {
                Node puncNode = puncList.item(i);
                Element puncElement = (Element)puncNode;
                
                String regexType = puncElement.getAttribute("category");
                
                Node puncTextNode = puncNode.getFirstChild();
                if (puncTextNode != null) {
                    String regexValue = puncTextNode.getNodeValue();
                    
                    if (!regexValue.isEmpty()) {
                        // put to regex map
                        type2RegexMap.put(regexType, regexValue);
                    
                        // add to regex list
                        regexList.add(new Pair(regexType, regexValue));
                    }
                }
            }
        } catch (ParserConfigurationException ex) {
            System.err.println(ex.toString());
            System.exit(1);
        } catch (SAXException ex) {
            System.err.println(ex.toString());
            System.exit(1);
        } catch (IOException ex) {
            System.err.println(ex.toString());
            System.exit(1);
        }
    }
    
    public static Map getType2RegexMap() {
        return type2RegexMap;
    }
    
    public static List<Pair<String, String>> getRegexList() {
        return regexList;
    }
    
    public static void print() {
        for (int i = 0; i < regexList.size(); i++) {
            Pair<String, String> pair = (Pair<String, String>)regexList.get(i);
            
            System.out.println(pair.first + ": " + pair.second);
        }
    }
}
