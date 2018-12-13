/**
 * 
 */

package analysis.jgibbslda;

import jmdn.base.util.filesystem.FileLoader;
import jmdn.base.util.filesystem.FileSaver;
import jmdn.base.util.string.StrUtil;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Xuan-Hieu Phan (pxhieu@gmail.com)
 * @version 1.1
 * @since 29-08-2014
 */
public class Dictionary {
	private Map<String, Integer> word2Id;
	private Map<Integer, String> id2Word;

	/**
	 * Default class constructor.
	 */
	public Dictionary() {
		word2Id = new HashMap<String, Integer>();
		id2Word = new HashMap<Integer, String>();
	}

	/**
	 * Getting the dictionary size.
	 * 
	 * @return The size of the dictionary
	 */
	public int size() {
		return word2Id.size();
	}

	/**
	 * Indicating whether the dictionary is empty or not.
	 * 
	 * @return True if the dictionary is empty, false otherwise
	 */
	public boolean isEmpty() {
		return word2Id.isEmpty();
	}

	/**
	 * Looking up a word by its identifier.
	 * 
	 * @param wordId
	 *            the id of the word
	 * @return The word corresponding with the input word identifier
	 */
	public String getWord(int wordId) {
		return id2Word.get(wordId);
	}

	/**
	 * Looking up the word id of a given word.
	 * 
	 * @param word
	 *            the word that needs to be looked up
	 * @return The id of the word
	 */
	public Integer getWordId(String word) {
		return word2Id.get(word);
	}

	/**
	 * Checking if the dictionary contains a particular word.
	 * 
	 * @param word
	 *            the word that needs to be looked up
	 * @return True if the dictionary contains the word, false otherwise
	 */
	public boolean containsWord(String word) {
		return word2Id.containsKey(word);
	}

	/**
	 * Checking if the dictionary contains a particular word id.
	 * 
	 * @param wordId
	 *            the input word id
	 * @return True if the dictionary contains the word id, false otherwise
	 */
	public boolean containsWordId(int wordId) {
		return id2Word.containsKey(wordId);
	}

	/**
	 * Adding a word to the dictionary.
	 * 
	 * @param word
	 *            the word to be added
	 * @return The id of the word
	 */
	public int addWord(String word) {
		if (!containsWord(word)) {
			int id = word2Id.size();

			word2Id.put(word, id);
			id2Word.put(id, word);

			return id;

		} else {
			return word2Id.get(word);
		}
	}

	/**
	 * Reading the dictionary from the dictionary file.
	 * 
	 * @param modelDirectory
	 *            the model directory
	 * @param wordMapFile
	 *            the word map file
	 * @return True if reading successfully, failse otherwise
	 */
	public boolean readWordMaps(String modelDirectory, String wordMapFile) {
		try {
			BufferedReader bufferedReader = FileLoader.getBufferredReader(modelDirectory, wordMapFile, "UTF8");
			if (bufferedReader == null) {
				return false;
			}

			String line;

			/* read number of words */
			line = bufferedReader.readLine();
			int nWords = Integer.parseInt(line);

			if (nWords <= 0) {
				bufferedReader.close();
				return false;
			}

			/* read word2Id map */
			for (int i = 0; i < nWords; i++) {
				line = bufferedReader.readLine();

				List<String> tokens = StrUtil.tokenizeString(line);
				if (tokens.size() != 2) {
					continue;
				}

				String word = tokens.get(0);
				int id = Integer.parseInt(tokens.get(1));

				word2Id.put(word, id);
				id2Word.put(id, word);
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
	 * Saving the dictionary to a file.
	 * 
	 * @param wordMapFile
	 *            the file to which the dictionary will be saved
	 * @return True if saving successfully, false otherwise
	 */
	public boolean writeWordMaps(String wordMapFile) {
		try {
			BufferedWriter bufferedWriter = FileSaver.openBufferedWriter(wordMapFile, "UTF8");
			if (bufferedWriter == null) {
				return false;
			}

			/* write number of words */
			bufferedWriter.write(word2Id.size() + "\n");

			/* write word2id map */
			for (Map.Entry<String, Integer> entry : word2Id.entrySet()) {
				bufferedWriter.write(entry.getKey() + " " + entry.getValue() + "\n");
			}

			bufferedWriter.close();

		} catch (IOException ex) {
			System.err.println(ex.toString());
			return false;
		}

		return false;
	}
}
