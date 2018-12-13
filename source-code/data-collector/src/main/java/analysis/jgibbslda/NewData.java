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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Xuan-Hieu Phan (pxhieu@gmail.com)
 * @version 1.1
 * @since 29-08-2014
 */
public class NewData {
	private Dictionary dictionary; /* dictionary */
	private List<Document> docs; /* new data */
	private List<Document> iDocs; /* for inference */
	private Map<Integer, Integer> iId2Id; /* for inference */
	private Map<Integer, Integer> id2IId; /* for inference */

	/**
	 * Default class constructor.
	 */
	public NewData() {
		dictionary = null;
		docs = new ArrayList<>();
		iDocs = new ArrayList<>();
		iId2Id = new HashMap<>();
		id2IId = new HashMap<>();
	}

	/**
	 * Class constructor.
	 * 
	 * @param dictionary
	 *            the reference to the dictionary object
	 */
	public NewData(Dictionary dictionary) {
		this();
		this.dictionary = dictionary;
	}

	/**
	 * Clearing all the lists of documents and maps.
	 */
	public void clearAll() {
		docs.clear();
		iDocs.clear();
		iId2Id.clear();
		id2IId.clear();
	}

	/**
	 * Getting the size of the new data.
	 * 
	 * @return The total number of documents in the new data
	 */
	public int size() {
		return docs.size();
	}

	/**
	 * Getting the map of words from the inference space to the trained model
	 * space.
	 * 
	 * @return The map of words from the inference space to the trained model
	 *         space
	 */
	public Map<Integer, Integer> getIId2IdMap() {
		return iId2Id;
	}

	/**
	 * Getting the map of words from the trained model space to the inference
	 * space.
	 * 
	 * @return The map of words from the trained model space to the inference
	 *         space.
	 */
	public Map<Integer, Integer> getId2IIdMap() {
		return id2IId;
	}

	/**
	 * Getting the map's size, i.e., the number of words in the new data.
	 * 
	 * @return The map's size
	 */
	public int mapSize() {
		return id2IId.size();
	}

	/**
	 * Getting a document at a given index.
	 * 
	 * @param index
	 *            the index at which the document is fetched
	 * @return The fetched document
	 */
	public Document getDocument(int index) {
		return docs.get(index);
	}

	/**
	 * Getting a document (in the inference space) at a given index.
	 * 
	 * @param index
	 *            the index at which the document is fetched
	 * @return The fetched document
	 */
	public Document getIDocument(int index) {
		return iDocs.get(index);
	}

	/**
	 * Parsing documents in new data.
	 * 
	 * @param documents
	 *            the new documents (in the new data)
	 * @return True if parsing successfully, false otherwise
	 */
	public boolean parseNewData(List<String> documents) {
		if (documents.isEmpty()) {
			System.out.println("No documents provided!");
			return false;
		}

		clearAll(); /* reset variables for storing new data */

		for (int i = 0; i < documents.size(); i++) {
			List<String> tokens = StrUtil.tokenizeString(documents.get(i));

			if (tokens.isEmpty()) {
				continue;
			}

			List<Integer> tempList1 = new ArrayList<Integer>();
			List<Integer> tempList2 = new ArrayList<Integer>();

			int len = tokens.size();
			for (int j = 0; j < len; j++) {
				Integer id = dictionary.getWordId(tokens.get(j));

				if (id != null) {
					Integer iId = id2IId.get(id);

					if (iId == null) {
						iId = id2IId.size();
						id2IId.put(id, iId);
						iId2Id.put(iId, id);
					}

					tempList1.add(id);
					tempList2.add(iId);
				}
			}

			if (tempList1.isEmpty()) {
				continue;
			}

			Document doc = new Document(tempList1);
			Document iDoc = new Document(tempList2);

			docs.add(doc);
			iDocs.add(iDoc);
		}

		return true;
	}

	/**
	 * Reading new data from a file.
	 * 
	 * @param dataFile
	 *            the file storing new documents (new data)
	 * @return True if reading successfully, false otherwise
	 */
	public boolean readNewData(String dataFile) {
		List<String> documents = new ArrayList<String>();

		try {
			BufferedReader bufferedReader = FileLoader.getBufferredReader(dataFile, "UTF8");
			if (bufferedReader == null) {
				return false;
			}

			String line;
			/* read new documents */
			while ((line = bufferedReader.readLine()) != null) {
				documents.add(line);
			}

			bufferedReader.close();

		} catch (UnsupportedEncodingException ex) {
			System.err.println(ex.toString());
			return false;
		} catch (IOException ex) {
			System.err.println(ex.toString());
			return false;
		}

		return parseNewData(documents);
	}
}
