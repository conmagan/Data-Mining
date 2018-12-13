/**
 * 
 */

package analysis.jgibbslda;

import jmdn.base.struct.pair.PairStrDouble;

import java.util.List;

/**
 * @author Xuan-Hieu Phan (pxhieu@gmail.com)
 * @version 1.1
 * @since 29-08-2014
 */
public class TopicLabeler {
	private CmdOption cmdOption;
	private Model model;
	private Inferencer inferencer;

	/**
	 * Default class constructor.
	 */
	public TopicLabeler() {
		cmdOption = new CmdOption();

		cmdOption.inf = true;
		cmdOption.modelDirectory = "./";
		cmdOption.modelName = "model-final";

		model = null;
		inferencer = null;
	}

	/**
	 * Class constructor.
	 * 
	 * @param modelDirectory
	 *            the directory containing the model and related files
	 * @param modelName
	 *            the model name
	 */
	public TopicLabeler(String modelDirectory, String modelName) {
		this();

		cmdOption.modelDirectory = modelDirectory;
		cmdOption.modelName = modelName;
	}

	/**
	 * Initializing the inferencer and related objects.
	 */
	public void init() {
		model = new Model();
		if (!model.init(cmdOption)) {
			System.out.println("Model initialization failed!");
			return;
		}

		inferencer = new Inferencer(cmdOption, model);
	}

	/**
	 * Getting top words of a given topic.
	 * 
	 * @param topicId
	 *            the topic of which the top words are selected
	 * @param nWords
	 *            the number of top words to be selected
	 * @return The list of top words of the topic
	 */
	public List<PairStrDouble> getWordsOfTopic(int topicId, int nWords) {
		return model.getWordsOfTopic(topicId, nWords);
	}

	/**
	 * Performing topic inference and getting the topic distributions of an
	 * input list of documents. The number of Gibbs sampling iterations for
	 * topic sampling is taken from the nInfIters constant (e.g., 200
	 * iterations).
	 * 
	 * @param documents
	 *            the list of input documents
	 * @return A list of topic distributions corresponding to the input
	 *         documents
	 */
	public synchronized List<List<Double>> doInferenceAndGetTopicDistributionsOfDocuments(List<String> documents) {
		return inferencer.doInferenceAndGetTopicDistributionsOfDocuments(documents, Constants.nInfIters);
	}

	/**
	 * Performing topic inference and getting the topic distributions of an
	 * input list of documents. The topic inference will perform nIters Gibbs
	 * sampling iterations.
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
		return inferencer.doInferenceAndGetTopicDistributionsOfDocuments(documents, nIters);
	}

	/**
	 * Performing topic inference and getting the normalized topic distributions
	 * of an input list of documents. The number of Gibbs sampling iterations
	 * for topic sampling is taken from the nInfIters constant (e.g., 200
	 * iterations).
	 * 
	 * @param documents
	 *            the list of input documents
	 * @return A list of normalized topic distributions corresponding to the
	 *         input documents
	 */
	public synchronized List<List<Double>> doInferenceAndGetNormTopicDistributionsOfDocuments(List<String> documents) {
		return inferencer.doInferenceAndGetNormTopicDistributionsOfDocuments(documents, Constants.nInfIters);
	}

	/**
	 * Performing topic inference and getting the normalized topic distributions
	 * of an input list of documents. The topic inference will perform nIters
	 * Gibbs sampling iterations.
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
		return inferencer.doInferenceAndGetNormTopicDistributionsOfDocuments(documents, nIters);
	}

	/**
	 * Performing topic inference and getting the list of list of topics-words
	 * for an input list of documents. The number of Gibbs sampling iterations
	 * for topic sampling is taken from the nInfIters constants (e.g., 200
	 * iterations).
	 * 
	 * @param documents
	 *            the input list of documents
	 * @return A list of list of topics-words corresponding to the list of input
	 *         documents.
	 */
	public synchronized List<List<List<String>>> doInferenceAndGetWordsOfTopicsOfDocuments(List<String> documents) {
		return inferencer.doInferenceAndGetWordsOfTopicsOfDocuments(documents, Constants.nInfIters);
	}

	/**
	 * Performing topic inference and getting the list of list of topics-words
	 * for an input list of documents. The topic inference will perform nIters
	 * Gibbs sampling iterations.
	 * 
	 * @param documents
	 *            the input list of documents
	 * @param nIters
	 *            the number of Gibbs sampling iterations
	 * @return A list of list of topics-words corresponding to the list of input
	 *         documents.
	 */
	public synchronized List<List<List<String>>> doInferenceAndGetWordsOfTopicsOfDocuments(List<String> documents,
			int nIters) {
		return inferencer.doInferenceAndGetWordsOfTopicsOfDocuments(documents, nIters);
	}

	/**
	 * Performing topic inference and getting the list of topics-counts for an
	 * input list of documents. The number of Gibbs sampling iterations for
	 * topic sampling is taken from the nInfIters constant (e.g., 200
	 * iterations).
	 * 
	 * @param documents
	 *            the input list of documents
	 * @return A list of lists of counts-of-topics corresponding to the list of
	 *         input documents
	 */
	public synchronized List<List<Integer>> doInferenceAndGetTopicsCountsOfDocuments(List<String> documents) {
		return inferencer.doInferenceAndGetTopicsCountsOfDocuments(documents, Constants.nInfIters);
	}

	/**
	 * Performing topic inference and getting the list of topics-counts for an
	 * input list of documents. The topic inference will perform nIters Gibbs
	 * sampling iterations.
	 * 
	 * @param documents
	 *            the input list of documents
	 * @param nIters
	 *            the number of Gibbs sampling iterations
	 * @return A list of lists of counts-of-topics corresponding to the list of
	 *         input documents
	 */
	public synchronized List<List<Integer>> doInferenceAndGetTopicsCountsOfDocuments(List<String> documents, int nIters) {
		return inferencer.doInferenceAndGetTopicsCountsOfDocuments(documents, nIters);
	}

	/**
	 * Performing topic inference and getting the list of normalized
	 * topics-counts for an input list of documents. The number of Gibbs
	 * sampling iterations for topic sampling is taken from the nInfIters
	 * constant (e.g., 200 iterations).
	 * 
	 * @param documents
	 *            the input list of documents
	 * @return A list of lists of normalized counts-of-topics corresponding to
	 *         the list of input documents
	 */
	public synchronized List<List<Double>> doInferenceAndGetNormTopicsCountsOfDocuments(List<String> documents) {
		return inferencer.doInferenceAndGetNormTopicsCountsOfDocuments(documents, Constants.nInfIters);
	}

	/**
	 * Performing topic inference and getting the list of normalized
	 * topics-counts for an input list of documents. The topic inference will
	 * perform nIters Gibbs sampling iterations.
	 * 
	 * @param documents
	 *            the input list of documents
	 * @param nIters
	 *            the number of Gibbs sampling iterations
	 * @return A list of lists of normalized counts-of-topics corresponding to
	 *         the list of input documents
	 */
	public synchronized List<List<Double>> doInferenceAndGetNormTopicsCountsOfDocuments(List<String> documents,
			int nIters) {
		return inferencer.doInferenceAndGetNormTopicsCountsOfDocuments(documents, nIters);
	}

	public synchronized List<List<InferredTopic>> doInferenceAndGetInferredTopicsOfDocuments(List<String> documents) {
		return inferencer.doInferenceAndGetInferredTopicsOfDocuments(documents, Constants.nInfIters);
	}

	public synchronized List<List<InferredTopic>> doInferenceAndGetInferredTopicsOfDocuments(List<String> documents,
			int nIters) {
		return inferencer.doInferenceAndGetInferredTopicsOfDocuments(documents, nIters);
	}
}
