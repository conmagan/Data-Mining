/**
 * 
 */

package analysis.jgibbslda;

import jmdn.base.struct.pair.PairIntDouble;
import jmdn.base.util.filesystem.FileSaver;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @author Xuan-Hieu Phan (pxhieu@gmail.com)
 * @version 1.1
 * @since 29-08-2014
 */
public class Inferencer {
	private CmdOption ldaCmdOption; // reference to command-line option
									// object
	private Model ldaModel; // reference to the previously estimated LDA
							// model
	private Dictionary dictionary; // reference to the model dictionary
	private NewData newData; // reference to the new (previously unseen) data

	// private int M; // the number of documents (of the model)
	private int V; // the number of words (i.e., vocabulary size) of the model
	private int K; // the number of topics
	private double[] p;
	private int[] nwsum;
	private int[][] nw;
	double alpha;
	double beta;

	private int newM; // the number of documents of the new data
	private int newV; // the number of words of the new data
	private int nIters; // the number of sampling iterations for inference

	private int[][] newZ;
	private int[][] newNW;
	private int[][] newND;
	private int[] newNWSum;
	private int[] newNDSum;
	private double[][] newTheta;
	private double[][] newPhi;

	/**
	 * Default class constructor.
	 */
	public Inferencer() {
		setDefaultValues();
	}

	/**
	 * Class constructor.
	 * 
	 * @param ldaCmdOption
	 *            the reference to the command line option object
	 * @param ldaModel
	 *            the trained LDA model
	 */
	public Inferencer(CmdOption ldaCmdOption, Model ldaModel) {
		this();

		this.ldaCmdOption = ldaCmdOption;
		this.ldaModel = ldaModel;
		dictionary = ldaModel.getDictionary();
		newData = new NewData(dictionary);

		// M = ldaModel.getNoDocuments();
		V = ldaModel.getVocabularySize();
		K = ldaModel.getNoTopics();

		p = new double[K];
		nwsum = ldaModel.getNWSum();
		nw = ldaModel.getNW();
		alpha = ldaModel.getAlpha();
		beta = ldaModel.getBeta();
	}

	/**
	 * Setting default values for class properties.
	 */
	private void setDefaultValues() {
		ldaCmdOption = null;
		ldaModel = null;
		dictionary = null;
		newData = null;

		// M = 0;
		V = 0;
		K = 0;

		p = null;
		nwsum = null;
		nw = null;
		alpha = 0.0;
		beta = 0.0;

		newM = 0;
		newV = 0;
		nIters = 0;

		newZ = null;
		newNW = null;
		newND = null;
		newNWSum = null;
		newNDSum = null;
		newTheta = null;
		newPhi = null;
	}

	/**
	 * Initializing for new data.
	 * 
	 * @return True if initializing successfully, false otherwise
	 */
	public synchronized boolean init() {
		newM = newData.size();
		newV = newData.mapSize();

		int m, n, w, k;

		newNW = new int[newV][K];
		for (w = 0; w < newV; w++) {
			for (k = 0; k < K; k++) {
				newNW[w][k] = 0;
			}
		}

		newND = new int[newM][K];
		for (m = 0; m < newM; m++) {
			for (k = 0; k < K; k++) {
				newND[m][k] = 0;
			}
		}

		newNWSum = new int[K];
		for (k = 0; k < K; k++) {
			newNWSum[k] = 0;
		}

		newNDSum = new int[newM];
		for (m = 0; m < newM; m++) {
			newNDSum[m] = 0;
		}

		newZ = new int[newM][];
		for (m = 0; m < newM; m++) {
			Document iDoc = newData.getIDocument(m);
			int[] iWords = iDoc.getWords();
			int N = iDoc.length();

			newZ[m] = new int[N];
			for (n = 0; n < N; n++) {
				int iw = iWords[n];
				int topic = (int) Math.floor(Math.random() * K);
				newZ[m][n] = topic;

				/* number of instances of word i assigned to topic j */
				newNW[iw][topic] += 1;
				/* number of words in document i assigned to topic j */
				newND[m][topic] += 1;
				/* total number of words assigned to topic j */
				newNWSum[topic] += 1;
			}

			/* total number of words in document i */
			newNDSum[m] = N;
		}

		newTheta = new double[newM][];
		for (m = 0; m < newM; m++) {
			newTheta[m] = new double[K];
		}

		newPhi = new double[K][];
		for (k = 0; k < K; k++) {
			newPhi[k] = new double[newV];
		}

		return true;
	}

	/**
	 * Parsing documents for topic inference.
	 * 
	 * @param documents
	 *            the list of the documents to be parsed
	 * @return True if parsing successfully, false otherwise
	 */
	public synchronized boolean praseDocuments(List<String> documents) {
		if (!newData.parseNewData(documents)) {
			return false;
		}

		return init();
	}

	/**
	 * Loading documents from file.
	 * 
	 * @param dataFile
	 *            the data file
	 * @return True if loading documents successfully, false otherwise
	 */
	public synchronized boolean loadDocuments(String dataFile) {
		if (!newData.readNewData(dataFile)) {
			return false;
		}

		return init();
	}

	/**
	 * Doing topic inference (for the parsed documents).
	 * 
	 * @param nIters
	 *            the number of Gibbs sampling iterations
	 */
	protected synchronized void inference(int nIters) {
		this.nIters = nIters;

		for (int iter = 0; iter < nIters; iter++) {
			for (int m = 0; m < newM; m++) {
				Document doc = newData.getDocument(m);
				Document iDoc = newData.getIDocument(m);
				int N = doc.length();
				int[] words = doc.getWords();
				int[] iWords = iDoc.getWords();
				for (int n = 0; n < N; n++) {
					newZ[m][n] = sampling(words[n], iWords[n], m, n);
				}
			}
		}
	}

	/**
	 * Sampling a topic for a given word.
	 * 
	 * @param w
	 *            the word id in the LDA model space
	 * @param iw
	 *            the word id in the new data space
	 * @param m
	 *            the document id
	 * @param n
	 *            the word order in the document "m"
	 * @return The sampled (resulting) topic for the word
	 */
	protected synchronized int sampling(int w, int iw, int m, int n) {
		int topic = newZ[m][n];
		int k;

		newNW[iw][topic] -= 1;
		newND[m][topic] -= 1;
		newNWSum[topic] -= 1;
		newNDSum[m] -= 1;

		double Vbeta = V * beta;
		// double Kalpha = K * alpha;
		double newNDSumMKAlpha = newNDSum[m] + K * alpha;

		/* do multinomial sampling via cumulative method */
		for (k = 0; k < K; k++) {
			p[k] = (nw[w][k] + newNW[iw][k] + beta) / (nwsum[k] + newNWSum[k] + Vbeta) * (newND[m][k] + alpha)
					/ newNDSumMKAlpha;
		}

		/* cumulate multinomial parameters */
		for (k = 1; k < K; k++) {
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
		newNW[iw][topic] += 1;
		newND[m][topic] += 1;
		newNWSum[topic] += 1;
		newNDSum[m] += 1;

		return topic;
	}

	/**
	 * Doing topic inference for a list of documents for some number of Gibbs
	 * sampling iterations.
	 * 
	 * @param documents
	 *            the list of documents to be inferred
	 * @param nIters
	 *            the number of Gibbs sampling iterations
	 * @return True if topic inference is successful, false otherwise
	 */
	public synchronized boolean doInference(List<String> documents, int nIters) {
		if (praseDocuments(documents)) {
			inference(nIters);
			computeNewTheta();
			return true;
		}

		return false;
	}

	/**
	 * Getting the topic distributions of the parsed documents.
	 * 
	 * @return A list of topic distributions corresponding to the parsed
	 *         documents
	 */
	public synchronized List<List<Double>> getTopicDistributionsOfDocuments() {
		List<List<Double>> results = new ArrayList<List<Double>>();

		if (newTheta == null) {
			return results;
		}

		int nDocs = newTheta.length;
		for (int m = 0; m < nDocs; m++) {
			List<Double> distribution = new ArrayList<Double>();
			for (int k = 0; k < K; k++) {
				distribution.add(newTheta[m][k]);
			}
			results.add(distribution);
		}

		return results;
	}

	/**
	 * Getting words of topics of the parsed documents.
	 * 
	 * @return A list of lists of words per each topic of the parsed documents.
	 */
	public synchronized List<List<List<String>>> getWordsOfTopicsOfDocuments() {
		List<List<List<String>>> results = new ArrayList<List<List<String>>>();

		if (newZ == null) {
			return results;
		}

		for (int m = 0; m < newM; m++) {
			List<List<String>> topicsWords = new ArrayList<List<String>>();
			for (int k = 0; k < K; k++) {
				topicsWords.add(new ArrayList<String>());
			}
			results.add(topicsWords);
		}

		for (int m = 0; m < newM; m++) {
			List<List<String>> topicsWords = results.get(m);

			for (int n = 0; n < newZ[m].length; n++) {
				int topic = newZ[m][n];
				List<String> topicWords = topicsWords.get(topic);

				String word = dictionary.getWord(newData.getDocument(m).getWord(n));

				topicWords.add(word);
			}
		}

		return results;
	}

	/**
	 * Getting the counts of topics of the parsed documents.
	 * 
	 * @return A list of lists of topics-counts corresponding the parsed
	 *         documents
	 */
	public synchronized List<List<Integer>> getTopicsCountsOfDocuments() {
		List<List<Integer>> results = new ArrayList<List<Integer>>();

		if (newZ == null) {
			return results;
		}

		for (int m = 0; m < newM; m++) {
			int[] temp = new int[K];
			for (int k = 0; k < K; k++) {
				temp[k] = 0;
			}

			int N = newZ[m].length;
			for (int n = 0; n < N; n++) {
				temp[newZ[m][n]]++;
			}

			List<Integer> topicsCountsOfDoc = new ArrayList<Integer>();
			for (int k = 0; k < K; k++) {
				topicsCountsOfDoc.add(temp[k]);
			}

			results.add(topicsCountsOfDoc);
		}

		return results;
	}

	/**
	 * Performing topic inference and getting the topic distributions of the
	 * input documents.
	 * 
	 * @param documents
	 *            the list of input documents
	 * @param nIters
	 *            the number of Gibbs sampling iterations for topic inference
	 * @return A list of topic distributions corresponding to the input
	 *         documents
	 */
	public synchronized List<List<Double>> doInferenceAndGetTopicDistributionsOfDocuments(List<String> documents,
			int nIters) {
		List<List<Double>> results = new ArrayList<List<Double>>();

		if (doInference(documents, nIters)) {
			results = getTopicDistributionsOfDocuments();
		}

		return results;
	}

	/**
	 * Performing topic inference and getting a list of normalized topic
	 * distributions for the input documents.
	 * 
	 * @param documents
	 *            the list of input documents
	 * @param nIters
	 *            the number of Gibbs sampling iterations for topic inference
	 * @return A list of normalized topic distributions corresponding to the
	 *         input documents
	 */
	public synchronized List<List<Double>> doInferenceAndGetNormTopicDistributionsOfDocuments(List<String> documents,
			int nIters) {
		List<List<Double>> results = new ArrayList<List<Double>>();

		List<List<Double>> temp = doInferenceAndGetTopicDistributionsOfDocuments(documents, nIters);
		for (int i = 0; i < temp.size(); i++) {
			double sum = 0.0;
			List<Double> distribution = temp.get(i);
			int len = distribution.size();
			for (int j = 0; j < len; j++) {
				sum += distribution.get(j).doubleValue();
			}

			List<Double> normalizedDistribution = new ArrayList<Double>();
			for (int j = 0; j < len; j++) {
				if (sum > 0.0) {
					normalizedDistribution.add(distribution.get(j).doubleValue() / sum);
				} else {
					normalizedDistribution.add(distribution.get(j));
				}
			}

			results.add(normalizedDistribution);
		}

		return results;
	}

	/**
	 * Performing topic inference and getting the words of topics of input
	 * documents.
	 * 
	 * @param documents
	 *            the list of input documents.
	 * @param nIters
	 *            the number of Gibbs sampling iterations for topic inference
	 * @return A list of list of topics-words corresponding to the input
	 *         documents.
	 */
	public synchronized List<List<List<String>>> doInferenceAndGetWordsOfTopicsOfDocuments(List<String> documents,
			int nIters) {
		List<List<List<String>>> results = new ArrayList<List<List<String>>>();

		if (doInference(documents, nIters)) {
			results = getWordsOfTopicsOfDocuments();
		}

		return results;
	}

	/**
	 * Performing topic inference and getting the counts of topics for the input
	 * documents.
	 * 
	 * @param documents
	 *            the list of input documents
	 * @param nIters
	 *            the number of Gibbs sampling iterations for topic inference
	 * @return A list of list of topics-counts corresponding to the input
	 *         documents
	 */
	public synchronized List<List<Integer>> doInferenceAndGetTopicsCountsOfDocuments(List<String> documents, int nIters) {
		List<List<Integer>> results = new ArrayList<List<Integer>>();

		if (doInference(documents, nIters)) {
			results = getTopicsCountsOfDocuments();
		}

		return results;
	}

	/**
	 * Performing topic inference and getting the normalized counts of topics
	 * for the input documents.
	 * 
	 * @param documents
	 *            the list of input documents
	 * @param nIters
	 *            the number of Gibbs sampling iterations for topic inference
	 * @return A list of list of normalized topics-counts corresponding to the
	 *         input documents
	 */
	public synchronized List<List<Double>> doInferenceAndGetNormTopicsCountsOfDocuments(List<String> documents,
			int nIters) {
		List<List<Double>> results = new ArrayList<List<Double>>();

		List<List<Integer>> temp = doInferenceAndGetTopicsCountsOfDocuments(documents, nIters);
		for (int i = 0; i < temp.size(); i++) {
			double sum = 0.0;
			List<Integer> topicsCounts = temp.get(i);
			int len = topicsCounts.size();
			for (int j = 0; j < len; j++) {
				sum += topicsCounts.get(j).doubleValue();
			}

			List<Double> normalizedTopicsCounts = new ArrayList<Double>();
			for (int j = 0; j < len; j++) {
				if (sum > 0.0) {
					normalizedTopicsCounts.add(topicsCounts.get(j).doubleValue() / sum);
				} else {
					normalizedTopicsCounts.add(topicsCounts.get(j).doubleValue());
				}
			}

			results.add(normalizedTopicsCounts);
		}

		return results;
	}

	public synchronized List<List<InferredTopic>> doInferenceAndGetInferredTopicsOfDocuments(List<String> documents,
			int nIters) {
		List<List<InferredTopic>> results = new ArrayList<List<InferredTopic>>();
		
		if (doInference(documents, nIters)) {
			List<List<Double>> topicDistributionsOfDocuments = getTopicDistributionsOfDocuments();
			List<List<List<String>>> topicsWordsOfDocuments = getWordsOfTopicsOfDocuments();
			
			if (topicDistributionsOfDocuments.size() != topicsWordsOfDocuments.size()) {
				return results;
			}
			
			int nDocs = topicDistributionsOfDocuments.size();
			for (int i = 0; i < nDocs; i++) {
				List<InferredTopic> inferredTopics = new ArrayList<InferredTopic>();
				
				List<Double> topicWeights = topicDistributionsOfDocuments.get(i);
				List<List<String>> topicsWords = topicsWordsOfDocuments.get(i);
				
				for (int j = 0; j < topicWeights.size(); j++) {
					InferredTopic inferredTopic = new InferredTopic();
					inferredTopic.setTopicId(j);
					inferredTopic.setTopicWeight(topicWeights.get(j));
					
					List<String> topicWords = topicsWords.get(j);
					for (int w = 0; w <topicWords.size(); w++) {
						inferredTopic.addTopicWord(topicWords.get(w));
					}
					
					inferredTopics.add(inferredTopic);
				}
				
				results.add(inferredTopics);
			}
		}
		
		return results;
	}

	/**
	 * Computing the theta matrix for the new data.
	 */
	public synchronized void computeNewTheta() {
		for (int m = 0; m < newM; m++) {
			for (int k = 0; k < K; k++) {
				newTheta[m][k] = (newND[m][k] + alpha) / (newNDSum[m] + K * alpha);
			}
		}
	}

	/**
	 * Computing the phi matrix for the new data.
	 */
	public synchronized void computeNewPhi() {
		for (int k = 0; k < K; k++) {
			for (int iw = 0; iw < newV; iw++) {
				int w = newData.getIId2IdMap().get(iw).intValue();
				newPhi[k][iw] = (nw[w][k] + newNW[iw][k] + beta) / (nwsum[k] + newNWSum[k] + V * beta);
			}
		}
	}

	/**
	 * Saving the inferred model (of the new data).
	 * 
	 * @param modelName
	 *            the model name of the new data
	 * @return True if saving successfully, false otherwise
	 */
	public boolean saveInferredModel(String modelName) {
		computeNewTheta();
		computeNewPhi();

		saveInferredModelTAssign(ldaCmdOption.modelDirectory + modelName + ldaModel.getTAssignSuffix());
		saveInferredModelNewTheta(ldaCmdOption.modelDirectory + modelName + ldaModel.getThetaSuffix());
		saveInferredModelNewPhi(ldaCmdOption.modelDirectory + modelName + ldaModel.getPhiSuffix());
		saveInferredModelOthers(ldaCmdOption.modelDirectory + modelName + ldaModel.getOthersSuffix());
		saveInferredModelTWords(ldaCmdOption.modelDirectory + modelName + ldaModel.getTWordsSuffix());

		return true;
	}

	/**
	 * Saving the topic assignment of the new data to a file.
	 * 
	 * @param filename
	 *            the name of the file to store the topic assignment of the new
	 *            data
	 * @return True if saving successfully, false otherwise
	 */
	public boolean saveInferredModelTAssign(String filename) {
		try {
			BufferedWriter bufferedWriter = FileSaver.openBufferedWriter(filename, "UTF8");
			if (bufferedWriter == null) {
				return false;
			}

			for (int i = 0; i < newZ.length; i++) {
				int[] words = newData.getDocument(i).getWords();
				for (int j = 0; j < newZ[i].length; j++) {
					bufferedWriter.write("" + words[j] + ":" + newZ[i][j] + " ");
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
	 * Saving the theta matrix of the new data to a file.
	 * 
	 * @param filename
	 *            the name of the file to store the theta matrix
	 * @return True if saving successfully, false otherwise
	 */
	public boolean saveInferredModelNewTheta(String filename) {
		try {
			BufferedWriter bufferedWriter = FileSaver.openBufferedWriter(filename, "UTF8");
			if (bufferedWriter == null) {
				return false;
			}

			for (int i = 0; i < newM; i++) {
				for (int j = 0; j < K; j++) {
					bufferedWriter.write("" + newTheta[i][j] + " ");
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
	 * Saving the phi matrix of the new data to a file.
	 * 
	 * @param filename
	 *            the name of the file to store the phi matrix
	 * @return True if saving successfully, false otherwise
	 */
	public boolean saveInferredModelNewPhi(String filename) {
		try {
			BufferedWriter bufferedWriter = FileSaver.openBufferedWriter(filename, "UTF8");
			if (bufferedWriter == null) {
				return false;
			}

			for (int i = 0; i < K; i++) {
				for (int j = 0; j < newV; j++) {
					bufferedWriter.write("" + newPhi[i][j] + " ");
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
	 * Saving the other information about the inferred model for the new data to
	 * a file.
	 * 
	 * @param filename
	 *            the name of the file to store the other information
	 * @return True if saving successfully, false otherwise
	 */
	public boolean saveInferredModelOthers(String filename) {
		try {
			BufferedWriter bufferedWriter = FileSaver.openBufferedWriter(filename, "UTF8");
			if (bufferedWriter == null) {
				return false;
			}

			bufferedWriter.write("alpha=" + alpha + "\n");
			bufferedWriter.write("beta=" + beta + "\n");
			bufferedWriter.write("ntopics=" + K + "\n");
			bufferedWriter.write("ndocs=" + newM + "\n");
			bufferedWriter.write("nwords=" + newV + "\n");
			bufferedWriter.write("liter=" + nIters + "\n");

			bufferedWriter.close();

		} catch (IOException ex) {
			System.err.println(ex.toString());
			return false;
		}

		return true;
	}

	/**
	 * Saving top words for each topics of the new data to a file.
	 * 
	 * @param filename
	 *            the name of the file to store topic words
	 * @return True if saving successfully, false otherwise
	 */
	public boolean saveInferredModelTWords(String filename) {
		Map<Integer, Integer> iId2Id = newData.getIId2IdMap();

		try {
			BufferedWriter bufferedWriter = FileSaver.openBufferedWriter(filename, "UTF8");
			if (bufferedWriter == null) {
				return false;
			}

			int tempTWords = ldaCmdOption.tWords;
			if (tempTWords > newV) {
				tempTWords = newV;
			}

			for (int k = 0; k < K; k++) {
				List<PairIntDouble> wordsProbs = new ArrayList<PairIntDouble>();

				for (int w = 0; w < newV; w++) {
					wordsProbs.add(new PairIntDouble(w, newPhi[k][w]));
				}

				Collections.sort(wordsProbs);

				bufferedWriter.write("Topic: " + k + "th:\n");
				for (int i = 0; i < tempTWords; i++) {
					PairIntDouble wordProb = wordsProbs.get(V - i - 1);
					Integer id = iId2Id.get(wordProb.first);
					String word = dictionary.getWord(id);
					if (word != null) {
						bufferedWriter.write("\t" + word + "   " + wordProb.second + "\n");
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
