/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package analysis.jvntextpro.lib.properties;

import analysis.jvntextpro.lib.string.StrUtil;

import java.io.File;
import java.net.URL;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * @author hieupx
 */
public class Path {
    private static String rootDir = moduleDir() + "/";

    private static String moduleDir() {
        String projectDir = Paths.get(".").toAbsolutePath().normalize().toString();
        String libsDir = "/src/main/java/analysis/";
        String moduleName = "/jvntextpro/data";
        return projectDir + libsDir + moduleName;
    }

//    static {
//        rootDir = DirFileUtil.normalizeDir(findRootDir());
//    }

    private static String findRootDir() {
        String res;

        URL root = new Path().getClass().getProtectionDomain().getCodeSource().getLocation();
        System.out.println(root);
        String rootStr = root.toString();
        List<String> tokens = StrUtil.tokenizeStr(rootStr, "/");

        int idx = tokens.size() - 1;
        while (idx >= 0) {
            String token = tokens.get(idx).toLowerCase();

            if (token.endsWith("jar")) {
                idx--;
                continue;
            }

            if (token.startsWith("vietnamesetextpro")) {
                break;
            }

            idx--;
        }

        List pathTokens = new ArrayList();
        for (int i = 0; i <= idx; i++) {
            String token = tokens.get(i);

            if (token.startsWith("file:") ||
                    token.startsWith("http:") ||
                    token.startsWith("ftp:")) {
                continue;
            }

            pathTokens.add(token);
        }

        String osName = System.getProperty("os.name");
        if (osName.toLowerCase().indexOf("windows") >= 0) {
            res = StrUtil.join(pathTokens, File.separator);
        } else {
            res = File.separator + StrUtil.join(pathTokens, File.separator);
        }

        return res;
    }

    public static String getRootDir() {
        return rootDir;
    }

    public static String getPropertiesFile() {
        return rootDir + File.separator + "vietnamesetextpro.properties";
    }
}
