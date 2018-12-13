import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

public class Utils {
    static Document getDoc(String URL) {
        try {
            Thread.sleep(Configs.DELAY);
            return Jsoup.connect(URL).userAgent(Configs.USER_AGENT).timeout(Configs.TIMEOUT).get();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }
}
