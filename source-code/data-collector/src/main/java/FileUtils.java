import java.io.*;
import java.nio.charset.StandardCharsets;

public class FileUtils {

    public static void listFiles(String path, OnResult onResult) {
        File folder = new File(path);
        File[] listOfFiles = folder.listFiles();
        if (listOfFiles != null) {
            for (File file : listOfFiles) {
                if (file.isFile()) {
                    onResult.onSuccess(file.getName());
                }
            }
        } else {
            Logs.infoLn("List files null");
        }
    }

    public static void write(String path, String message) {
        try (BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(path), StandardCharsets.UTF_8))) {
            bw.write(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String readToString(String path) {
        StringBuilder str = new StringBuilder();
        File fileDir = new File(path);
        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(fileDir), StandardCharsets.UTF_8))) {
            String line;
            while ((line = br.readLine()) != null) str.append(line).append("\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return str.toString();
    }

}