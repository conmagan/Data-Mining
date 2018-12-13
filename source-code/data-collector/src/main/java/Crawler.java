import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class Crawler extends BaseCrawler {

    private static String getURL(String category) {
        return "http://dantri.com.vn/" + category;
    }

    static void crawl(String category) {
        mState = new State();
        int pageIndex = mState.getCurrentPage() + 1;
        while (true) {
            String URL = getURL(category) + "/trang-" + pageIndex + ".htm";
            Logs.infoLn("Crawling page: " + pageIndex + " " + URL);
            Document d = Utils.getDoc(URL);
            if (d == null) return;
            Elements e = d.getElementsByClass("mr1");
            if (e.toString().equals("")) break;
            for (int j = mState.getCurrentTopic() + 1; j < e.size(); j++) {
                int jj = j + 1;
                String link = e.get(j).select("a").attr("abs:href");
                Logs.infoLn("Topic: " + jj + "/" + e.size() + " " + link);
                Document topicDoc = Utils.getDoc(link);
                String title = topicDoc.getElementsByClass("mgb15").text();
                Element e1 = topicDoc.getElementsByClass("sapo").first();
                String content1 = "";
                String content2 = "";
                if (e1 != null) {
                    e1.select("a").remove();
                    content1 = e1.text();
                }
                Element e2 = topicDoc.getElementById("divNewsContent");
                if (e2 != null && e2.select("div.news-tag") != null) {
                    e2.select("div.news-tag").remove();
                    content2 = e2.text();
                }
                title = title.replace("/", " ");
                FileUtils.write("data/" + category + "/raw/" + title + ".txt", content1 + content2);
                FileUtils.write("data/" + category + "/normalized/" + title + ".txt",
                        Normalizer.normalizeContent(content1 + content2));
            }
            mState.setCurrentPage(pageIndex);
            mState.setCurrentTopic(-1);
            pageIndex++;
        }
    }
}
