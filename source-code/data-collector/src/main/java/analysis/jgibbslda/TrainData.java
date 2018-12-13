/**
 * 
 */

package analysis.jgibbslda;

import jmdn.base.util.filesystem.FileLoader;
import jmdn.base.util.string.StrUtil;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Xuan-Hieu Phan (pxhieu@gmail.com)
 * @version 1.1
 * @since 29-08-2014
 */
public class TrainData {
	private List<Document> documents;
	private Dictionary dictionary;

	/**
	 * Default class constructor.
	 */
	public TrainData() {
		documents = new ArrayList<Document>();
		dictionary = new Dictionary();
	}

	/**
	 * Class constructor.
	 * 
	 * @param dictionary
	 *            the reference to the dictionary object
	 */
	public TrainData(Dictionary dictionary) {
		this();

		if (dictionary != null) {
			this.dictionary = dictionary;
		}
	}

	/**
	 * Getting the size of the training data.
	 * 
	 * @return The total number of documents in the training data
	 */
	public int size() {
		return documents.size();
	}

	/**
	 * Clearing all the documents.
	 */
	protected void clearAllDocs() {
		documents.clear();
	}

	/**
	 * Getting the dictionary.
	 * 
	 * @return The reference to the dictionary
	 */
	public Dictionary getDictionary() {
		return dictionary;
	}

	/**
	 * Getting the dictionary's size.
	 * 
	 * @return The size of the dictionary
	 */
	public int dictSize() {
		return dictionary.size();
	}

	/**
	 * Adding a document to the training data.
	 * 
	 * @param document
	 *            the document to be added
	 */
	public void addDocument(Document document) {
		documents.add(document);
	}

	/**
	 * Getting a document at a given index.
	 * 
	 * @param index
	 *            the index at which the document is fetched
	 * @return The fetched document
	 */
	public Document getDocument(int index) {
		return documents.get(index);
	}

	/**
	 * Reading data from file.
	 * 
	 * @param dataFile
	 *            the file storing the data
	 * @return True if reading successfully, false otherwise
	 */
	public boolean readData(String dataFile) {
		try {
			BufferedReader bufferedReader = FileLoader.getBufferredReader(dataFile, "UTF8");
			if (bufferedReader == null) {
				return false;
			}

			String line;
			/* read documents */
			while ((line = bufferedReader.readLine()) != null) {
				List<String> tokens = StrUtil.tokenizeString(line);

				if (tokens.isEmpty()) {
					continue;
				}

				int docLen = tokens.size();
				Document doc = new Document(docLen);
				int[] words = doc.getWords();
				for (int i = 0; i < docLen; i++) {
					words[i] = dictionary.addWord(tokens.get(i));
				}

				documents.add(doc);
			}

			bufferedReader.close();

		} catch (UnsupportedEncodingException ex) {
			System.err.println(ex.toString());
			return false;
		} catch (IOException ex) {
			System.err.println(ex.toString());
			return false;
		}

		return (documents.size() > 0);
	}
}
