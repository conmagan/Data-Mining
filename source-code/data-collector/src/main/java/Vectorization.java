import analysis.jgibbslda.TopicLabeler;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class Vectorization {

    public static TopicLabeler topicLabeler = null;

    static {
        initTopicLabeler();
    }

    public static void initTopicLabeler() {
        topicLabeler = new TopicLabeler(Configs.LDAMODEL_DIRECTORY, Configs.LDAMODEL_NAME);
        topicLabeler.init();
    }

    public static String infTopicsDistribution(String content, int iterations) {
        ArrayList<String> documents = new ArrayList<>();
        documents.add(content);
        List<List<Double>> ft = topicLabeler.doInferenceAndGetTopicDistributionsOfDocuments(documents, iterations);
        for (List<Double> lft : ft) {
            StringBuilder sb = new StringBuilder();
            for (int k = 0; k < lft.size(); k++) {
                sb.append(lft.get(k)).append(",");
            }
            return sb.toString().trim();
        }
        return null;
    }

    public static void vectorize() {
        Logs.infoLn("Generating training data ...");
        StringBuilder sb = new StringBuilder();
        AtomicInteger count = new AtomicInteger();
        for (int i = 0; i < Configs.categories.length; i++) {
            String c = Configs.categories[i];
            String dir = "data/" + c + "/normalized";
            int finalI = i;
            FileUtils.listFiles(dir, s -> {
                if (!s.startsWith(".")) {
                    String content = FileUtils.readToString(dir + "/" + s);
                    String topics = infTopicsDistribution(content, Configs.LDA_ITERS);
                    sb.append(topics).append(finalI).append("\n");
                    Logs.infoLn(s);
                    FileUtils.write(Configs.TRAIN, sb.toString());

                    Logs.info(count.getAndIncrement() + " ");
                }
            });
        }
        FileUtils.write(Configs.TRAIN, sb.toString());
    }
}
