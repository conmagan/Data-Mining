/**
 * 
 */

package analysis.jgibbslda;

import jmdn.base.struct.pair.PairIntDouble;
import jmdn.base.struct.pair.PairStrDouble;
import jmdn.base.util.filesystem.FileLoader;
import jmdn.base.util.filesystem.FileSaver;
import jmdn.base.util.string.StrUtil;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Xuan-Hieu Phan (pxhieu@gmail.com)
 * @version 1.1
 * @since 29-08-2014
 */
public class Model {
	private String modelDirectory; // the directory where the model files are
									// stored
	private String dataFile; // the data file
	private String wordMapFile; // the word map file
	private String tAssignSuffix; // the suffix for topic assignment file
	private String thetaSuffix; // the suffix for theta file
	private String phiSuffix; // the suffix for phi file
	private String othersSuffix; // the suffix for file containing other model
									// parameters
	private String tWordsSuffix; // the suffix for file containing
									// words-per-topics

	private String modelName; // the model name
	private int modelStatus; // the model status, can be one of the following:
								// MODEL_STATUS_UNKNOWN, MODEL_STATUS_EST,
								// MODEL_STATUS_ESTC, or MODEL_STATUS_INF

	private int K; // the number of LDA topics
	private double alpha, beta; // LDA hyperparameters
	private int nIters; // the number of Gibbs sampling iterations for model
						// estimation
	private int saveStep; // model saving period
	private int tWords; // print out tWords top words per each topic
	private int M; // the number of documents (training data)
	private int V; // the number of words (vocabulary size of training data)
	private int lastIter; // the iteration at which the model was saved

	private TrainData data; // training data
	private Dictionary dictionary; // dictionary

	// variables for sampling and model estimation
	private double[] p; // temporary variable for sampling
	private int[][] z; // topic assignments for words, size M x doc.size()
	private int[][] nw; // nw[i][j]: the number of instances of word/term i
						// assigned to topic j, size V x K
	private int[][] nd; // nd[i][j]: the number of words in document i assigned
						// to topic j, size M x K
	private int[] nwsum; // nwsum[j]: the total number of words assigned to
							// topic j, size K
	private int[] ndsum; // ndsum[i]: the total number of words in document i,
							// size M

	private double[][] theta; // document-topic distributions, size M x K
	private double[][] phi; // topic-word distributions, size K x V

	/**
	 * Default class constructor.
	 */
	public Model() {
		setDefaultValues();
	}

	/**
	 * Setting default values for all the variables and parameters.
	 */
	private void setDefaultValues() {
		modelDirectory = "./";
		dataFile = "trndocs.txt";

		wordMapFile = "wordmap.txt";
		tAssignSuffix = ".tassign";
		thetaSuffix = ".theta";
		phiSuffix = ".phi";
		othersSuffix = ".others";
		tWordsSuffix = ".twords";

		modelName = "model-final";
		modelStatus = Constants.MODEL_STATUS_UNKNOWN;

		K = 100;
		alpha = 50.0 / K;
		beta = 0.1;
		nIters = 2000;
		saveStep = 200;
		tWords = 500;

		M = 0;
		V = 0;
		lastIter = 0;

		data = null;
		dictionary = null;

		p = null;
		z = null;
		nw = null;
		nd = null;
		nwsum = null;
		ndsum = null;

		theta = null;
		phi = null;
	}

	/**
	 * Getting the dictionary.
	 * 
	 * @return The dictionary
	 */
	protected Dictionary getDictionary() {
		return dictionary;
	}

	/**
	 * Getting the total number of training documents.
	 * 
	 * @return The total number of documents used to train the model
	 */
	protected int getNoDocuments() {
		return M;
	}

	/**
	 * Getting the vocabulary size.
	 * 
	 * @return The total number of words in the training data
	 */
	public int getVocabularySize() {
		return V;
	}

	/**
	 * Getting the number of topics of the LDA model.
	 * 
	 * @return The number of topics
	 */
	public int getNoTopics() {
		return K;
	}

	/**
	 * Getting the reference to the temporary memory for sampling.
	 * 
	 * @return The reference to the temporary memory
	 */
	protected double[] getP() {
		return p;
	}

	/**
	 * Getting the reference to the nw variable.
	 * 
	 * @return The reference to nw
	 */
	protected int[][] getNW() {
		return nw;
	}

	/**
	 * Getting the reference to the nwsum variable.
	 * 
	 * @return The reference to nwsum
	 */
	protected int[] getNWSum() {
		return nwsum;
	}

	/**
	 * Getting the hyper-parameter alpha of the LDA model.
	 * 
	 * @return The alpha value
	 */
	protected double getAlpha() {
		return alpha;
	}

	/**
	 * Getting the hyper-parameter beta of the LDA model.
	 * 
	 * @return The beta value
	 */
	protected double getBeta() {
		return beta;
	}

	/**
	 * Getting the name of the file storing the word map.
	 * 
	 * @return The name of the word map file
	 */
	protected String getWordMapFile() {
		return wordMapFile;
	}

	/**
	 * Getting the name suffix of the file storing the topic assignment of the
	 * model.
	 * 
	 * @return The name suffix of the file containing the topic assignment
	 */
	protected String getTAssignSuffix() {
		return tAssignSuffix;
	}

	/**
	 * Getting the name suffix of the file storing theta matrix.
	 * 
	 * @return The name suffix of the file containing the theta matrix
	 */
	protected String getThetaSuffix() {
		return thetaSuffix;
	}

	/**
	 * Getting the name suffix of the file storing the phi matrix.
	 * 
	 * @return The name suffix of the file containing the phi matrix
	 */
	protected String getPhiSuffix() {
		return phiSuffix;
	}

	/**
	 * Getting the name suffix of the file storing the other information of the
	 * model.
	 * 
	 * @return The name suffix of the file containing the other information of
	 *         the model
	 */
	protected String getOthersSuffix() {
		return othersSuffix;
	}

	/**
	 * Getting the name suffix of the file storing the top words per each topic.
	 * 
	 * @return The name suffix of the file containing the top words per each
	 *         topic
	 */
	protected String getTWordsSuffix() {
		return tWordsSuffix;
	}

	/**
	 * Reading the command-line options from the CmdOption object.
	 * 
	 * @param cmdOption
	 *            the CmdOption object
	 */
	public void readOptions(CmdOption cmdOption) {
		modelDirectory = cmdOption.modelDirectory;
		if (!modelDirectory.endsWith(File.separator)) {
			modelDirectory += File.separator;
		}

		dataFile = cmdOption.dataFile;

		modelName = cmdOption.modelName;

		if (cmdOption.est) {
			modelStatus = Constants.MODEL_STATUS_EST;
		}

		if (cmdOption.estc) {
			modelStatus = Constants.MODEL_STATUS_ESTC;
		}

		if (cmdOption.inf) {
			modelStatus = Constants.MODEL_STATUS_INF;
		}

		alpha = cmdOption.alpha;
		beta = cmdOption.beta;
		nIters = cmdOption.nIters;
		K = cmdOption.K;
		saveStep = cmdOption.saveStep;
		tWords = cmdOption.tWords;
	}

	/**
	 * Loading options from the file containing the other information about the
	 * model.
	 * 
	 * @return True if loading successfully, false otherwise
	 */
	public boolean loadOptions() {
		try {
			BufferedReader bufferedReader = FileLoader.getBufferredReader(modelDirectory, modelName + othersSuffix,
					"UTF8");
			if (bufferedReader == null) {
				return false;
			}

			String line;

			/* read documents */
			while ((line = bufferedReader.readLine()) != null) {
				List<String> tokens = StrUtil.tokenizeString(line, "= \t\r\n");

				if (tokens.isEmpty() || tokens.size() != 2) {
					continue;
				}

				String optStr = tokens.get(0);
				String optVal = tokens.get(1);

				if (optStr.equals("alpha")) {
					alpha = Double.parseDouble(optVal);
				}

				if (optStr.equals("beta")) {
					beta = Double.parseDouble(optVal);
				}

				if (optStr.equals("ntopics")) {
					K = Integer.parseInt(optVal);
				}

				if (optStr.equals("ndocs")) {
					M = Integer.parseInt(optVal);
				}

				if (optStr.equals("nwords")) {
					V = Integer.parseInt(optVal);
				}

				if (optStr.equals("liter")) {
					lastIter = Integer.parseInt(optVal);
				}
			}

			bufferedReader.close();

		} catch (UnsupportedEncodingException ex) {
			System.err.println(ex.toString());
			return false;
		} catch (IOException ex) {
			System.err.println(ex.toString());
			return false;
		}

		return true;
	}

	/**
	 * Initializing the model for estimation or inference.
	 * 
	 * @param cmdOption
	 *            the CmdOption object
	 * @return True if initializing successfully, false otherwise
	 */
	public boolean init(CmdOption cmdOption) {
		readOptions(cmdOption);

		if (modelStatus == Constants.MODEL_STATUS_EST) {
			return initForEst();

		} else if (modelStatus == Constants.MODEL_STATUS_ESTC) {
			loadOptions();
			return initForEstC();

		} else if (modelStatus == Constants.MODEL_STATUS_INF) {
			loadOptions();
			return initForInf();

		} else {
			System.out.println("No model initialization!");
			return false;
		}
	}

	/**
	 * Initializing the model for estimating the model from scratch.
	 * 
	 * @return True if initializing successfully, false otherwise
	 */
	protected boolean initForEst() {
		System.out.println("Model initialization for estimation from scratch...");

		data = new TrainData();

		/* read training data */
		if (!data.readData(dataFile)) {
			System.out.println("Reading data failed or no documents read!");
			return false;
		}

		/* get dictionary */
		dictionary = data.getDictionary();
		dictionary.writeWordMaps(modelDirectory + wordMapFile);

		M = data.size();
		V = dictionary.size();

		p = new double[K];

		nw = new int[V][K];
		for (int w = 0; w < V; w++) {
			for (int k = 0; k < K; k++) {
				nw[w][k] = 0;
			}
		}

		nd = new int[M][K];
		for (int m = 0; m < M; m++) {
			for (int k = 0; k < K; k++) {
				nd[m][k] = 0;
			}
		}

		nwsum = new int[K];
		for (int k = 0; k < K; k++) {
			nwsum[k] = 0;
		}

		ndsum = new int[M];
		for (int m = 0; m < M; m++) {
			ndsum[m] = 0;
		}

		z = new int[M][];
		for (int m = 0; m < M; m++) {
			Document doc = data.getDocument(m);
			int[] words = doc.getWords();

			int N = doc.length();
			z[m] = new int[N];

			/* initialization for z */
			for (int n = 0; n < N; n++) {
				int topic = (int) Math.floor(Math.random() * K);

				z[m][n] = topic;

				/* number of instances of word n assigned to topic "topic" */
				nw[words[n]][topic] += 1;

				/* number of words in document m assigned to topic "topic" */
				nd[m][topic] += 1;

				/* total number of words assigned to topic "topic" */
				nwsum[topic] += 1;
			}

			/* total number of words in document m */
			ndsum[m] = N;
		}

		theta = new double[M][K];
		phi = new double[K][V];

		System.out.println("Model initialization for estimation from scratch: completed!");

		return true;
	}

	/**
	 * Initializing the model for continuing estimating the from a previously
	 * estimated model.
	 * 
	 * @return True if initializing successfully, false otherwise
	 */
	protected boolean initForEstC() {
		System.out.println("Model initialization to continue estimation...");

		data = new TrainData();
		dictionary = data.getDictionary();
		dictionary.readWordMaps(modelDirectory, wordMapFile);

		/* load LDA model */
		if (!loadModel()) {
			System.out.println("Failed to load LDA model!");
			return false;
		}

		p = new double[K];

		nw = new int[V][K];
		for (int w = 0; w < V; w++) {
			for (int k = 0; k < K; k++) {
				nw[w][k] = 0;
			}
		}

		nd = new int[M][K];
		for (int m = 0; m < M; m++) {
			for (int k = 0; k < K; k++) {
				nd[m][k] = 0;
			}
		}

		nwsum = new int[K];
		for (int k = 0; k < K; k++) {
			nwsum[k] = 0;
		}

		ndsum = new int[M];
		for (int m = 0; m < M; m++) {
			ndsum[m] = 0;
		}

		for (int m = 0; m < M; m++) {
			Document doc = data.getDocument(m);
			int[] words = doc.getWords();

			int N = doc.length();

			/* initialization for z */
			for (int n = 0; n < N; n++) {
				int topic = z[m][n];

				/* number of instances of word n assigned to topic "topic" */
				nw[words[n]][topic] += 1;

				/* number of words in document m assigned to topic "topic" */
				nd[m][topic] += 1;

				/* total number of words assigned to topic "topic" */
				nwsum[topic] += 1;
			}

			/* total number of words in document m */
			ndsum[m] = N;
		}

		theta = new double[M][K];
		phi = new double[K][V];

		computeTheta();
		computePhi();

		System.out.println("Model initialization to continue estimation: completed!");

		return true;
	}

	/**
	 * Initializing the model for doing inference.
	 * 
	 * @return True if initializing successfully, false otherwise
	 */
	protected boolean initForInf() {
		System.out.println("Model initialization for inference...");

		data = new TrainData();
		dictionary = data.getDictionary();
		dictionary.readWordMaps(modelDirectory, wordMapFile);

		/* load LDA model */
		if (!loadModel()) {
			System.out.println("Failed to load LDA model!");
			return false;
		}

		p = new double[K];

		nw = new int[V][K];
		for (int w = 0; w < V; w++) {
			for (int k = 0; k < K; k++) {
				nw[w][k] = 0;
			}
		}

		nd = new int[M][K];
		for (int m = 0; m < M; m++) {
			for (int k = 0; k < K; k++) {
				nd[m][k] = 0;
			}
		}

		nwsum = new int[K];
		for (int k = 0; k < K; k++) {
			nwsum[k] = 0;
		}

		ndsum = new int[M];
		for (int m = 0; m < M; m++) {
			ndsum[m] = 0;
		}

		for (int m = 0; m < M; m++) {
			Document doc = data.getDocument(m);
			int[] words = doc.getWords();

			int N = doc.length();

			/* initialization for z */
			for (int n = 0; n < N; n++) {
				int topic = z[m][n];

				/* number of instances of word n assigned to topic "topic" */
				nw[words[n]][topic] += 1;

				/* number of words in document m assigned to topic "topic" */
				nd[m][topic] += 1;

				/* total number of words assigned to topic "topic" */
				nwsum[topic] += 1;
			}

			/* total number of words in document m */
			ndsum[m] = N;
		}

		theta = new double[M][K];
		phi = new double[K][V];

		computeTheta();
		computePhi();

		/* clear all docs because they are no longer needed */
		data.clearAllDocs();

		System.out.println("Model initialization for inference: completed!");

		return true;
	}

	/**
	 * Loading the model from the model file and related files.
	 * 
	 * @return True if loading the model successfully, false otherwise
	 */
	public boolean loadModel() {
		System.out.println("Loading LDA model to continue estimation or inference ...");

		z = new int[M][];

		try {
			BufferedReader bufferedReader = FileLoader.getBufferredReader(modelDirectory, modelName + tAssignSuffix,
					"UTF8");
			if (bufferedReader == null) {
				return false;
			}

			String line;

			/* read documents */
			for (int m = 0; m < M; m++) {
				line = bufferedReader.readLine();

				List<String> tokens = StrUtil.tokenizeString(line);

				if (tokens.isEmpty()) {
					continue;
				}

				int N = tokens.size();
				List<Integer> words = new ArrayList<Integer>();
				List<Integer> topics = new ArrayList<Integer>();

				for (int n = 0; n < N; n++) {
					List<String> wt = StrUtil.tokenizeString(tokens.get(n), ":");

					if (wt.size() != 2) {
						System.out.println("Invalid word-topic assignment line!");
						bufferedReader.close();
						return false;
					}

					words.add(Integer.parseInt(wt.get(0)));
					topics.add(Integer.parseInt(wt.get(1)));
				}

				/* add document to data set */
				Document doc = new Document(words);
				data.addDocument(doc);

				/* assign values for z */
				z[m] = new int[N];
				for (int n = 0; n < N; n++) {
					z[m][n] = topics.get(n);
				}
			}

			bufferedReader.close();

		} catch (UnsupportedEncodingException ex) {
			System.err.println(ex.toString());
			return false;
		} catch (IOException ex) {
			System.err.println(ex.toString());
			return false;
		}

		System.out.println("Loading LDA model to continue estimation or inference: completed!");

		return true;
	}

	/**
	 * Performing model estimation.
	 */
	protected void estimate() {
		System.out.println("Sampling " + nIters + " iterations!");

		long startTime = System.currentTimeMillis();

		int iter;
		int startIter = lastIter + 1;
		int totalNIters = nIters + lastIter;
		for (iter = startIter; iter <= totalNIters; iter++) {
			System.out.println("Iteration " + iter + " ...");

			for (int m = 0; m < M; m++) {
				Document doc = data.getDocument(m);
				int docLen = doc.length();
				int[] words = doc.getWords();
				for (int n = 0; n < docLen; n++) {
					z[m][n] = sampling(words[n], m, n);
				}
			}

			if (saveStep > 0) {
				if (iter % saveStep == 0) {
					/* saving the model */
					System.out.println("Saving the model at iteration " + iter + " ...");
					computeTheta();
					computePhi();
					lastIter = iter;
					saveModel(generateModelName(iter));
				}
			}
		}

		System.out.println("Gibbs sampling completed!");
		System.out.println("Estimation time: " + (System.currentTimeMillis() - startTime) / 1000 + " seconds.");

		computeTheta();
		computePhi();
		lastIter = iter - 1;
		System.out.println("Saving the final model!");
		saveModel(generateModelName(-1));
	}

	/**
	 * Sampling a random topic for a given word.
	 * 
	 * @param w
	 *            the word that needs a sampled topic
	 * @param m
	 *            the document id
	 * @param n
	 *            the word order in the document
	 * @return The sampled (resulting) topic
	 */
	protected int sampling(int w, int m, int n) {
		/* remove z_i from the count variables */
		int topic = z[m][n];
		nw[w][topic] -= 1;
		nd[m][topic] -= 1;
		nwsum[topic] -= 1;
		ndsum[m] -= 1;

		double Vbeta = V * beta;
		// double Kalpha = K * alpha;
		double ndSumMKAlpha = ndsum[m] + K * alpha;

		/* do multinomial sampling via cumulative method */
		for (int k = 0; k < K; k++) {
			p[k] = (nw[w][k] + beta) / (nwsum[k] + Vbeta) * (nd[m][k] + alpha) / ndSumMKAlpha;
		}

		/* cumulate multinomial parameters */
		for (int k = 1; k < K; k++) {
			p[k] += p[k - 1];
		}

		/* scaled sample because of unnormalized p[] */
		double u = Math.random() * p[K - 1];

		for (topic = 0; topic < K; topic++) {
			if (p[topic] > u) {
				break;
			}
		}

		/* add newly estimated z_i to count variables */
		nw[w][topic] += 1;
		nd[m][topic] += 1;
		nwsum[topic] += 1;
		ndsum[m] += 1;

		return topic;
	}

	/**
	 * Computing the theta matrix.
	 */
	protected void computeTheta() {
		for (int m = 0; m < M; m++) {
			for (int k = 0; k < K; k++) {
				theta[m][k] = (nd[m][k] + alpha) / (ndsum[m] + K * alpha);
			}
		}
	}

	/**
	 * Computing the phi matrix.
	 */
	protected void computePhi() {
		for (int k = 0; k < K; k++) {
			for (int w = 0; w < V; w++) {
				phi[k][w] = (nw[w][k] + beta) / (nwsum[k] + V * beta);
			}
		}
	}

	/**
	 * Generating the model name based on the current iteration.
	 * 
	 * @param iter
	 *            the current iteration
	 * @return The model name
	 */
	protected String generateModelName(int iter) {
		if (iter < 0) {
			return "model-final";
		} else if (0 <= iter && iter < 10) {
			return "model-0000" + iter;
		} else if (10 <= iter && iter < 100) {
			return "model-000" + iter;
		} else if (100 <= iter && iter < 1000) {
			return "model-00" + iter;
		} else if (1000 <= iter && iter < 10000) {
			return "model-0" + iter;
		} else {
			return "model-" + iter;
		}
	}

	/**
	 * Saving the trained model to the model file and related files.
	 * 
	 * @param modelName
	 *            the model name
	 * @return True if saving the model successfully, false otherwise
	 */
	protected boolean saveModel(String modelName) {
		if (!saveModelTAssign(modelDirectory + modelName + tAssignSuffix)) {
			return false;
		}

		if (!saveModelOthers(modelDirectory + modelName + othersSuffix)) {
			return false;
		}

		if (!saveModelTheta(modelDirectory + modelName + thetaSuffix)) {
			return false;
		}

		if (!saveModelPhi(modelDirectory + modelName + phiSuffix)) {
			return false;
		}

		if (!saveModelTWords(modelDirectory + modelName + tWordsSuffix)) {
			return false;
		}

		return true;
	}

	/**
	 * Saving the topic assignment to file.
	 * 
	 * @param filename
	 *            the file storing the topic assignment
	 * @return True if saving successfully, false otherwise
	 */
	protected boolean saveModelTAssign(String filename) {
		try {
			BufferedWriter bufferedWriter = FileSaver.openBufferedWriter(filename, "UTF8");
			if (bufferedWriter == null) {
				return false;
			}

			for (int m = 0; m < M; m++) {
				Document doc = data.getDocument(m);
				int[] words = doc.getWords();
				int N = doc.length();
				for (int n = 0; n < N; n++) {
					bufferedWriter.write(words[n] + ":" + z[m][n] + " ");
				}
				bufferedWriter.write("\n");
			}

			bufferedWriter.close();

		} catch (IOException ex) {
			System.err.println(ex.toString());
			return false;
		}

		return true;
	}

	/**
	 * Saving the other information of the model to file.
	 * 
	 * @param filename
	 *            the file storing the other information of the trained model
	 * @return True if saving successfully, false otherwise
	 */
	protected boolean saveModelOthers(String filename) {
		try {
			BufferedWriter bufferedWriter = FileSaver.openBufferedWriter(filename, "UTF8");
			if (bufferedWriter == null) {
				return false;
			}

			bufferedWriter.write("alpha=" + alpha + "\n");
			bufferedWriter.write("beta=" + beta + "\n");
			bufferedWriter.write("ntopics=" + K + "\n");
			bufferedWriter.write("ndocs=" + M + "\n");
			bufferedWriter.write("nwords=" + V + "\n");
			bufferedWriter.write("liter=" + lastIter + "\n");

			bufferedWriter.close();

		} catch (IOException ex) {
			System.err.println(ex.toString());
			return false;
		}

		return true;
	}

	/**
	 * Saving the theta matrix to file.
	 * 
	 * @param filename
	 *            the file storing the theta matrix
	 * @return True if saving successfully, false otherwise
	 */
	protected boolean saveModelTheta(String filename) {
		try {
			BufferedWriter bufferedWriter = FileSaver.openBufferedWriter(filename, "UTF8");
			if (bufferedWriter == null) {
				return false;
			}

			for (int m = 0; m < M; m++) {
				for (int k = 0; k < K; k++) {
					bufferedWriter.write(theta[m][k] + " ");
				}
				bufferedWriter.write("\n");
			}

			bufferedWriter.close();

		} catch (IOException ex) {
			System.err.println(ex.toString());
			return false;
		}

		return true;
	}

	/**
	 * Saving the phi matrix to file.
	 * 
	 * @param filename
	 *            the file storing the phi matrix
	 * @return True if saving successfully, false otherwise
	 */
	protected boolean saveModelPhi(String filename) {
		try {
			BufferedWriter bufferedWriter = FileSaver.openBufferedWriter(filename, "UTF8");
			if (bufferedWriter == null) {
				return false;
			}

			for (int k = 0; k < K; k++) {
				for (int w = 0; w < V; w++) {
					bufferedWriter.write(phi[k][w] + " ");
				}
				bufferedWriter.write("\n");
			}

			bufferedWriter.close();

		} catch (IOException ex) {
			System.err.println(ex.toString());
			return false;
		}

		return true;
	}

	/**
	 * Getting the top words per each topic.
	 * 
	 * @param topicId
	 *            the topic id of which the top words are selected
	 * @param nWords
	 *            the number of top words per topic
	 * @return A list of top words and their weight values of the topic
	 */
	public List<PairStrDouble> getWordsOfTopic(int topicId, int nWords) {
		List<PairStrDouble> results = new ArrayList<PairStrDouble>();

		int tempNo = nWords;
		if (tempNo > V) {
			tempNo = V;
		}

		if (topicId >= 0 && topicId < K) {
			List<PairIntDouble> wordsProbs = new ArrayList<PairIntDouble>();

			for (int w = 0; w < V; w++) {
				wordsProbs.add(new PairIntDouble(w, phi[topicId][w]));
			}

			Collections.sort(wordsProbs);

			for (int i = 0; i < tempNo; i++) {
				PairIntDouble wordProb = wordsProbs.get(V - i - 1);
				String wordStr = dictionary.getWord(wordProb.first);
				if (wordStr != null) {
					results.add(new PairStrDouble(wordStr, wordProb.second));
				}
			}
		}

		return results;
	}

	/**
	 * Saving top words per each topic to file.
	 * 
	 * @param filename
	 *            the file storing top words per each topic
	 * @return True if saving successfully, false otherwise
	 */
	protected boolean saveModelTWords(String filename) {
		try {
			BufferedWriter bufferedWriter = FileSaver.openBufferedWriter(filename, "UTF8");
			if (bufferedWriter == null) {
				return false;
			}

			int tempTWords = tWords;
			if (tempTWords > V) {
				tempTWords = V;
			}

			for (int k = 0; k < K; k++) {
				List<PairIntDouble> wordsProbs = new ArrayList<PairIntDouble>();

				for (int w = 0; w < V; w++) {
					wordsProbs.add(new PairIntDouble(w, phi[k][w]));
				}

				Collections.sort(wordsProbs);

				bufferedWriter.write("Topic: " + k + "th:\n");
				for (int i = 0; i < tempTWords; i++) {
					PairIntDouble wordProb = wordsProbs.get(V - i - 1);
					String wordStr = dictionary.getWord(wordProb.first);
					if (wordStr != null) {
						bufferedWriter.write("\t" + wordStr + "   " + wordProb.second + "\n");
					}
				}
			}

			bufferedWriter.close();

		} catch (IOException ex) {
			System.err.println(ex.toString());
			return false;
		}

		return true;
	}
}
