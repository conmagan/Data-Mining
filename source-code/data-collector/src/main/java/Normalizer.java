import analysis.jvntextpro.nlp.vntextpro.VnTextPro;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Normalizer {

    public static VnTextPro vnTextPro = null;
    public static Map<Character, Boolean> charMap = new HashMap<>();
    public static HashMap<String, Boolean> stopwords;

    static {
        initVnTextPro();
        initNormalized();
    }

    public static String segment(String content) {
        String segmentedStr = vnTextPro.segmentText(content.trim().toLowerCase()).toString();
        String result = segmentedStr.substring(1, segmentedStr.length() - 1);
        return result;
    }


    public static String normalize(String content) {
        String normalizeStr = content.replaceAll("[0-9]", "");
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < normalizeStr.length(); i++) {
            char ch = normalizeStr.charAt(i);
            if (isValidChar(ch)) {
                sb.append(ch);
            }
        }
        StringBuilder result = new StringBuilder();
        String[] arr = sb.toString().trim().split(" ");
        for (int i = 0; i < arr.length; i++) {
            String str = arr[i];
            if (stopwords.get(str) == null && str.length() > 1) {
                result.append(str);
                result.append(" ");
            }
        }
        return result.toString().trim();
    }

    private static boolean isValidChar(char s) {
        return charMap.get(s) != null;
    }

    public static void initVnTextPro() {
        vnTextPro = new VnTextPro(true, true, true);
        vnTextPro.init();
    }

    public static void initNormalized() {
        Logs.infoLn("Initializing for text normalization...");
        stopwords = new HashMap<>();
        getStopwords(Configs.STOPWORDS_PATH);
        for (int i = 0; i < Configs.VALID_CHARS.length(); i++) {
            charMap.put(Configs.VALID_CHARS.charAt(i), true);
        }
    }

    public static void getStopwords(String path) {
        BufferedReader br;
        try {
            br = new BufferedReader(new FileReader(path));
            String w = br.readLine();
            while (w != null) {
                stopwords.put(w.trim(), true);
                w = br.readLine();
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String normalizeContent(String s) {
        String segmented = segment(s);
        return normalize(segmented);
    }
}

