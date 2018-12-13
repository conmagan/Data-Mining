import analysis.jgibbslda.LDA;

import java.util.ArrayList;

public class Main {
    public static void main(String args[]) {
        for (String c : Configs.categories) Crawler.crawl(c);
        exportTrainingData();
        LDA.trainModel();
        Vectorization.vectorize();
    }


    private static void exportTrainingData() {
        ArrayList<String> wholeArticles = new ArrayList<>();
        for (String c : Configs.categories) {
            String dir = "data/" + c + "/normalized";
            FileUtils.listFiles(dir, s -> {
                if (!s.startsWith(".")) {
                    String content = FileUtils.readToString(dir + "/" + s);
                    wholeArticles.add(content);
                }
            });
        }
        StringBuilder sb = new StringBuilder();
        sb.append(wholeArticles.size()).append("\n");
        for (String s : wholeArticles) {
            sb.append(s);
        }
        FileUtils.write("data/news.dat", sb.toString());
    }
}
