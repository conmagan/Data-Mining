/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package analysis.jvntextpro.lib.properties;

import analysis.jvntextpro.lib.string.StrUtil;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author pxhieu
 */
public class Parameters {
    private static String rootDir = null;
    private static String propertiesFile = null;
    private static Properties properties = null;
    private static Map propertiesMap = null;

    static {
        properties = new Properties();
        rootDir = Path.getRootDir();
        propertiesFile = Path.getPropertiesFile();

        loadProperties();
        processAndStoreProperties();
    }


    private static void loadProperties() {
        FileInputStream proInputStream;
        try {
            proInputStream = new FileInputStream(propertiesFile);
            properties.load(proInputStream);
            proInputStream.close();

        } catch (IOException ex) {
            Logger.getLogger(Parameters.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private static void processAndStoreProperties() {
        propertiesMap = new HashMap();

        for (Map.Entry<Object, Object> proItem : properties.entrySet()) {
            String key = (String) proItem.getKey();
            String value = (String) proItem.getValue();

            // convert to os-independent path
            if (value.indexOf("/") >= 0) {
                value = StrUtil.join(StrUtil.tokenizeStr(value, "/"), File.separator);
            }

            propertiesMap.put(key, value);
        }
    }

    public static String getNlpVnSentSegmenterDataDir() {
        String dir = (String) propertiesMap.get("nlpVnSentSegmenterDataDir");
        if (dir != null) {
            return rootDir + File.separator + dir;
        } else {
            return "";
        }
    }

    public static String getNlpVnWordSegmenterDataDir() {
        String dir = (String) propertiesMap.get("nlpVnWordSegmenterDataDir");
        if (dir != null) {
            return rootDir + File.separator + dir;
        } else {
            return "";
        }
    }

    public static String getNlpVnPOSTaggerDataDir() {
        String dir = (String) propertiesMap.get("nlpVnPOSTaggerDataDir");
        if (dir != null) {
            return rootDir + File.separator + dir;
        } else {
            return "";
        }
    }

    public static String getDmBlogClassificationModelDir() {
        String dir = (String) propertiesMap.get("dmBlogClassificationModelDir");
        if (dir != null) {
            return rootDir + File.separator + dir;
        } else {
            return "";
        }
    }

    public static String getNlpEnSentSegmenterModelDir() {
        String dir = (String) propertiesMap.get("nlpEnSentSegmenterModelDir");
        if (dir != null) {
            return rootDir + File.separator + dir;
        } else {
            return "";
        }
    }

    public static String getNlpEnPOSTaggerModelDir() {
        String dir = (String) propertiesMap.get("nlpEnPOSTaggerModelDir");
        if (dir != null) {
            return rootDir + File.separator + dir;
        } else {
            return "";
        }
    }

    public static String getNlpEnChunkerModelDir() {
        String dir = (String) propertiesMap.get("nlpEnChunkerModelDir");
        if (dir != null) {
            return rootDir + File.separator + dir;
        } else {
            return "";
        }
    }

    public static String getNlpVnSentSegmenterModelDir() {
        String dir = (String) propertiesMap.get("nlpVnSentSegmenterModelDir");
        if (dir != null) {
            return rootDir + File.separator + dir;
        } else {
            return "";
        }
    }

    public static String getNlpVnTokenizerModelDir() {
        String dir = (String) propertiesMap.get("nlpVnTokenizerModelDir");
        if (dir != null) {
            return rootDir + File.separator + dir;
        } else {
            return "";
        }
    }

    public static String getNlpVnWordSegmenterModelDir() {
        String dir = (String) propertiesMap.get("nlpVnWordSegmenterModelDir");
        if (dir != null) {
            return rootDir + File.separator + dir;
        } else {
            return "";
        }
    }

    public static String getNlpVnPOSTaggerModelDir() {
        String dir = (String) propertiesMap.get("nlpVnPOSTaggerModelDir");
        if (dir != null) {
            return rootDir + File.separator + dir;
        } else {
            return "";
        }
    }

    public static String getNlpVnChunkerModelDir() {
        String dir = (String) propertiesMap.get("nlpVnChunkerModelDir");
        if (dir != null) {
            return rootDir + File.separator + dir;
        } else {
            return "";
        }
    }

    public static String getNlpVnNERModelDir() {
        String dir = (String) propertiesMap.get("nlpVnNERModelDir");
        if (dir != null) {
            return rootDir + File.separator + dir;
        } else {
            return "";
        }
    }

    public static String getRegexDir() {
        String dir = (String) propertiesMap.get("regexDir");
        if (dir != null) {
            return rootDir + File.separator + dir;
        } else {
            return "";
        }
    }

    public static String getNamesDictDir() {
        String dir = (String) propertiesMap.get("namesDictDir");
        if (dir != null) {
            return rootDir + File.separator + dir;
        } else {
            return "";
        }
    }

    public static String getLexiconsDictDir() {
        String dir = (String) propertiesMap.get("lexiconsDictDir");
        if (dir != null) {
            return rootDir + File.separator + dir;
        } else {
            return "";
        }
    }

    public static String getSpecialcharsDictDir() {
        String dir = (String) propertiesMap.get("specialCharsDictDir");
        if (dir != null) {
            return rootDir + File.separator + dir;
        } else {
            return "";
        }
    }

    public static String getAbbreviationsDir() {
        String dir = (String) propertiesMap.get("abbreviationsDir");
        if (dir != null) {
            return rootDir + File.separator + dir;
        } else {
            return "";
        }
    }

    public static String getEnDictDir() {
        String dir = (String) propertiesMap.get("enDictDir");
        if (dir != null) {
            return rootDir + File.separator + dir;
        } else {
            return "";
        }
    }

    public static String getVnDictDir() {
        String dir = (String) propertiesMap.get("vnDictDir");
        if (dir != null) {
            return rootDir + File.separator + dir;
        } else {
            return "";
        }
    }

    public static String getHumanTopicMapDir() {
        String dir = (String) propertiesMap.get("humanTopicMapDir");
        if (dir != null) {
            return rootDir + File.separator + dir;
        } else {
            return "";
        }
    }

    public static String getConceptsDir() {
        String dir = (String) propertiesMap.get("conceptsDir");
        if (dir != null) {
            return rootDir + File.separator + dir;
        } else {
            return "";
        }
    }

    public static String getConceptsFilesDir() {
        String dir = (String) propertiesMap.get("conceptsFilesDir");
        if (dir != null) {
            return rootDir + File.separator + dir;
        } else {
            return "";
        }
    }

    public static String getRegexFile() {
        String file = (String) propertiesMap.get("regexFile");
        if (file != null) {
            return getRegexDir() + File.separator + file;
        } else {
            return "";
        }
    }

    public static String getRegexPunctuationMarksFile() {
        String file = (String) propertiesMap.get("regexPunctuationMarksFile");
        if (file != null) {
            return getRegexDir() + File.separator + file;
        } else {
            return "";
        }
    }

    public static String getNamesPersonsFile() {
        String file = (String) propertiesMap.get("namesPersonsFile");
        if (file != null) {
            return getNamesDictDir() + File.separator + file;
        } else {
            return "";
        }
    }

    public static String getNamesLocationsFile() {
        String file = (String) propertiesMap.get("namesLocationsFile");
        if (file != null) {
            return getNamesDictDir() + File.separator + file;
        } else {
            return "";
        }
    }

    public static String getNamesOrganizationsFile() {
        String file = (String) propertiesMap.get("namesOrganizationsFile");
        if (file != null) {
            return getNamesDictDir() + File.separator + file;
        } else {
            return "";
        }
    }

    public static String getNamesProductsFile() {
        String file = (String) propertiesMap.get("namesProductsFile");
        if (file != null) {
            return getNamesDictDir() + File.separator + file;
        } else {
            return "";
        }
    }

    public static String getLexiconsVnUnitsFile() {
        String file = (String) propertiesMap.get("lexiconsVnUnitsFile");
        if (file != null) {
            return getLexiconsDictDir() + File.separator + file;
        } else {
            return "";
        }
    }

    public static String getLexiconsEnUnitsFile() {
        String file = (String) propertiesMap.get("lexiconsEnUnitsFile");
        if (file != null) {
            return getLexiconsDictDir() + File.separator + file;
        } else {
            return "";
        }
    }

    public static String getStopWordsFile() {
        String file = (String) propertiesMap.get("stopWordsFile");
        if (file != null) {
            return getLexiconsDictDir() + File.separator + file;
        } else {
            return "";
        }
    }

    public static String getStopWordsTextClassificationFile() {
        String file = (String) propertiesMap.get("stopWordsTextClassificationFile");
        if (file != null) {
            return getLexiconsDictDir() + File.separator + file;
        } else {
            return "";
        }
    }

    public static String getStopNamesFile() {
        String file = (String) propertiesMap.get("stopNamesFile");
        if (file != null) {
            return getLexiconsDictDir() + File.separator + file;
        } else {
            return "";
        }
    }

    public static String getSpecialcharsFile() {
        String file = (String) propertiesMap.get("specialCharsFile");
        if (file != null) {
            return getSpecialcharsDictDir() + File.separator + file;
        } else {
            return "";
        }
    }

    public static String getNonAlphaCharsFile() {
        String file = (String) propertiesMap.get("nonAlphaCharsFile");
        if (file != null) {
            return getSpecialcharsDictDir() + File.separator + file;
        } else {
            return "";
        }
    }

    public static String getVnStopAbbrsFile() {
        String file = (String) propertiesMap.get("vnStopAbbrsFile");
        if (file != null) {
            return getAbbreviationsDir() + File.separator + file;
        } else {
            return "";
        }
    }

    public static String getEnStopAbbrsFile() {
        String file = (String) propertiesMap.get("enStopAbbrsFile");
        if (file != null) {
            return getAbbreviationsDir() + File.separator + file;
        } else {
            return "";
        }
    }

    public static String getHonotificTitlesFile() {
        String file = (String) propertiesMap.get("honotificTitlesFile");
        if (file != null) {
            return getAbbreviationsDir() + File.separator + file;
        } else {
            return "";
        }
    }

    public static String getPrefixTitlesFile() {
        String file = (String) propertiesMap.get("prefixTitlesFile");
        if (file != null) {
            return getAbbreviationsDir() + File.separator + file;
        } else {
            return "";
        }
    }

    public static String getVnDictFile() {
        String file = (String) propertiesMap.get("vnDictFile");
        if (file != null) {
            return getVnDictDir() + File.separator + file;
        } else {
            return "";
        }
    }

    public static String getVnExtendedDictFile() {
        String file = (String) propertiesMap.get("vnExtendedDictFile");
        if (file != null) {
            return getVnDictDir() + File.separator + file;
        } else {
            return "";
        }
    }

    public static String getVnIdiomsDictFile() {
        String file = (String) propertiesMap.get("vnIdiomsDictFile");
        if (file != null) {
            return getVnDictDir() + File.separator + file;
        } else {
            return "";
        }
    }

    public static String getVnFirstWordsDictFile() {
        String file = (String) propertiesMap.get("vnFirstWordsDictFile");
        if (file != null) {
            return getVnDictDir() + File.separator + file;
        } else {
            return "";
        }
    }

    public static String getVnFamilyNamesDictFile() {
        String file = (String) propertiesMap.get("vnFamilyNamesDictFile");
        if (file != null) {
            return getVnDictDir() + File.separator + file;
        } else {
            return "";
        }
    }

    public static String getVnMiddleLastNamesDictFile() {
        String file = (String) propertiesMap.get("vnMiddleLastNamesDictFile");
        if (file != null) {
            return getVnDictDir() + File.separator + file;
        } else {
            return "";
        }
    }

    public static String getVnLocationsDictFile() {
        String file = (String) propertiesMap.get("vnLocationsDictFile");
        if (file != null) {
            return getVnDictDir() + File.separator + file;
        } else {
            return "";
        }
    }

    public static String getVnBaomoiWordListFile() {
        String file = (String) propertiesMap.get("vnBaomoiWordListFile");
        if (file != null) {
            return getVnDictDir() + File.separator + file;
        } else {
            return "";
        }
    }

    public static String getVnBaomoiNameListFile() {
        String file = (String) propertiesMap.get("vnBaomoiNameListFile");
        if (file != null) {
            return getVnDictDir() + File.separator + file;
        } else {
            return "";
        }
    }

    public static String getVnBaomoiNameListForConceptLookupFile() {
        String file = (String) propertiesMap.get("vnBaomoiNameListForConceptLookupFile");
        if (file != null) {
            return getVnDictDir() + File.separator + file;
        } else {
            return "";
        }
    }

    public static String getHumanTopicMapFile() {
        String file = (String) propertiesMap.get("humanTopicMapFile");
        if (file != null) {
            return getHumanTopicMapDir() + File.separator + file;
        } else {
            return "";
        }
    }

    public static String getMappedTopicsFile() {
        String file = (String) propertiesMap.get("mappedTopicsFile");
        if (file != null) {
            return getHumanTopicMapDir() + File.separator + file;
        } else {
            return "";
        }
    }
}
