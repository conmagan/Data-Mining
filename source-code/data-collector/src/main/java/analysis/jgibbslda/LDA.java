package analysis.jgibbslda;

public class LDA {

    public static void trainModel() {
        CmdOption ldaCmdOption = new CmdOption();
        ldaCmdOption.K = 100;
        ldaCmdOption.alpha = 0.5;
        ldaCmdOption.beta = 0.1;
        ldaCmdOption.nIters = 2000;
        ldaCmdOption.modelName = "model";
        ldaCmdOption.saveStep = 200;
        ldaCmdOption.dataFile = "data/news.dat";
        ldaCmdOption.tWords = 20;
        ldaCmdOption.est = true;

        Model ldaModel = new Model();
        if (!ldaModel.init(ldaCmdOption)) {
            System.out.println("Model initialization failed!");
            return;
        }

        if (ldaCmdOption.est || ldaCmdOption.estc) {
            ldaModel.estimate();
        }
    }
}
