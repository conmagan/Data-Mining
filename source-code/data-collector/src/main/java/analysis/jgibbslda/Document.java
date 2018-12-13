/**
 * 
 */

package analysis.jgibbslda;

import java.util.List;

/**
 * @author Xuan-Hieu Phan (pxhieu@gmail.com)
 * @version 1.1
 * @since 29-08-2014
 */
public class Document {
	private int[] words;
	private String rawText;
	private int length;

	/**
	 * Default class constructor.
	 */
	public Document() {
		words = null;
		rawText = "";
		length = 0;
	}

	/**
	 * Class constructor.
	 * 
	 * @param length
	 *            the length of the document
	 */
	public Document(int length) {
		this.length = length;
		rawText = "";
		words = new int[length];
	}

	/**
	 * Class constructor.
	 * 
	 * @param length
	 *            the length of the document
	 * @param words
	 *            the document content (an array of identifiers of the
	 *            document's words)
	 */
	public Document(int length, int[] words) {
		this(length);
		System.arraycopy(words, 0, this.words, 0, length);
	}

	/**
	 * Class constructor.
	 * 
	 * @param length
	 *            the length of the document
	 * @param words
	 *            the document content (an array of identifiers of the
	 *            document's words)
	 * @param rawText
	 *            the document in the raw text form
	 */
	public Document(int length, int[] words, String rawText) {
		this(length, words);
		this.rawText = rawText;
	}

	/**
	 * Class (copy) constructor.
	 * 
	 * @param document
	 *            the document object to be copied
	 */
	public Document(List<Integer> document) {
		this(document.size());

		for (int i = 0; i < document.size(); i++) {
			this.words[i] = document.get(i);
		}
	}

	/**
	 * Class (copy) constructor.
	 * 
	 * @param document
	 *            the document to be copied
	 * @param rawText
	 *            the document in the raw text form
	 */
	public Document(List<Integer> document, String rawText) {
		this(document);
		this.rawText = rawText;
	}

	/**
	 * Getting a specific word in the document.
	 * 
	 * @param index
	 *            the index of the word
	 * @return The word at the index "index"
	 */
	public int getWord(int index) {
		return words[index];
	}

	/**
	 * Getting all the words in the document.
	 * 
	 * @return An array of the identifiers of all words in the document
	 */
	public int[] getWords() {
		return words;
	}

	/**
	 * Getting the raw text of the document.
	 * 
	 * @return A text string that is the raw text of that document
	 */
	public String getRawText() {
		return rawText;
	}

	/**
	 * Getting the document' length.
	 * 
	 * @return The length of the document
	 */
	public int length() {
		return length;
	}

	/**
	 * Displaying the document.
	 */
	public void print() {
		if (rawText != null && !rawText.isEmpty()) {
			System.out.println(rawText);
		}
	}
}
