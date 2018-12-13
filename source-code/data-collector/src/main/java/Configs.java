public class Configs {
    public static int DELAY = 2000;
    public static int TIMEOUT = 3600000;
    public static String USER_AGENT = "Mozilla";

    public static String ROOT_DIR = "/Users/source-code/data-collector";
    public static String VALID_CHARS = "aàáạảãăằắặẳãâầấậẩẫbcdđeèéẹẻẽêềếệểễghiìíịỉĩklmnoòóọỏõôồốộổỗơờớợởỡpqrstuùúụủũưừứựửữvxyỳýỷỹỵ_ ";
    public static String STOPWORDS_PATH = ROOT_DIR + "/data/stopwords.data";

    public static String LDAMODEL_DIRECTORY = ROOT_DIR + "/data/ldamodel";
    public static String LDAMODEL_NAME = "model-final";
    public static int LDA_ITERS = 2000;

    public static String[] categories = new String[]{"suc-khoe", "kinh-doanh", "phap-luat", "the-thao", "o-to-xe-may"};

    public static String TRAIN = "data/train.csv";

}
