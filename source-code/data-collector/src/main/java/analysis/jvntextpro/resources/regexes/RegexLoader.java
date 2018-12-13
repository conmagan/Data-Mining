/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package analysis.jvntextpro.resources.regexes;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import analysis.jvntextpro.lib.properties.Parameters;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 *
 * @author hieupx
 */
public class RegexLoader {
    private static Map<String, Map> type2RegexMap = null;

    static {
        type2RegexMap = new HashMap();
        readRegexXMLFile(Parameters.getRegexFile());
    }
    
    public static void readRegexXMLFile(String regexXMLFile) {
        try {
            File xmlFile = new File(regexXMLFile);
            
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = (Document)dBuilder.parse(xmlFile);
            doc.getDocumentElement().normalize();
            
            NodeList regexTypeNList = doc.getElementsByTagName("regextype");
            
            for (int i = 0; i < regexTypeNList.getLength(); i++) {
                Node regexTypeNode = regexTypeNList.item(i);
                Element regexTypeElement = (Element)regexTypeNode;
                
                String regexType = regexTypeElement.getAttribute("category");
                
                Map<String, String> regexMap = new HashMap();
                
                NodeList regexNList = regexTypeElement.getElementsByTagName("regex");
                for (int j = 0; j < regexNList.getLength(); j++) {
                    Node regexNode = regexNList.item(j);
                    
                    Element regexElement = (Element)regexNode;
                    String regexID = regexElement.getAttribute("id");
                    
                    Node regexTextNode = regexNode.getFirstChild();
                    
                    if (regexTextNode != null) {
                        String regexValue = regexTextNode.getNodeValue();
                        
                        regexMap.put(regexID, regexValue);
                    }
                }
                
                type2RegexMap.put(regexType, regexMap);
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
    
    public static void print() {
        Set keys = type2RegexMap.entrySet();
        Iterator it = keys.iterator();
        
        while (it.hasNext()) {
            Map.Entry me = (Map.Entry)it.next();
            
            String regexType = (String)me.getKey();
            Map<String, String> regexMap = (Map<String, String>)me.getValue();

            System.out.println("\nRegular expression type: " + regexType);
            
            Set regexKeys = regexMap.entrySet();
            Iterator regexIt = regexKeys.iterator();
            while (regexIt.hasNext()) {
                Map.Entry regexME = (Map.Entry)regexIt.next();
                
                String regexID = (String)regexME.getKey();
                String regexValue = (String)regexME.getValue();
                
                System.out.println("\t" + regexID + ": " + regexValue);
            }
        }
    }
}

