package analysis.jgibbslda;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Xuan-Hieu Phan (pxhieu@gmail.com)
 * @version 1.1
 * @since 29-06-2015
 */
public class InferredTopic {
	private Integer topicId;
	private Double topicWeight;
	private List<String> topicWords;

	/**
	 * Default class constructor.
	 */
	public InferredTopic() {
		topicId = -1;
		topicWeight = 0.0;
		topicWords = new ArrayList<String>();
	}

	@Override
	public String toString() {
		return "InferredTopic{" +
				"topicId=" + topicId +
				", topicWeight=" + topicWeight +
				", topicWords=" + topicWords +
				'}';
	}

	public InferredTopic(int topicId) {
		this();
		
		this.topicId = topicId;
	}
	
	public void clear() {
		topicId = -1;
		topicWeight = 0.0;
		topicWords.clear();
	}
	
	public void setTopicId(int topicId) {
		this.topicId = topicId;
	}
	
	public int getTopicId() {
		return topicId;
	}
	
	public void setTopicWeight(double topicWeight) {
		this.topicWeight = topicWeight;
	}
	
	public double getTopicWeight() {
		return topicWeight;
	}
	
	public void addTopicWord(String word) {
		topicWords.add(word);
	}
	
	public String getTopicWord(int index) {
		if (index >= 0 && index < topicWords.size()) {
			return topicWords.get(index);
		} else {
			return "";
		}
	}
	
	public List<String> getTopicWords() {
		return topicWords;
	}
	
	public void clearTopicWords() {
		topicWords.clear();
	}
}
