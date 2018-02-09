package site;

import java.util.ArrayList;
import java.util.List;

/**
 * Stores search results in an easily organized an accessable manner
 *
 * @author Pedram
 */
public class SearchResult implements Comparable<SearchResult> {
	private ViewingObject obj;
	private int numTags;
	private ArrayList<String> tags;

	/**
	 * Initializes Search Result with the given location, count, and index
	 *
	 * @param viewingObject the viewing object that this SearchResult is bound to
	 *                      s	 * @param index         of search result
	 */
	public SearchResult(ViewingObject viewingObject, String tag) {
		obj = viewingObject;
		tags = new ArrayList<String>();
		tags.add(tag);
	}

	/**
	 * Takes in a single tag and adds it to the list of tags this {@link SearchResult} object has and increments the
	 * value of numTags
	 *
	 * @param tag the tag to be added to the list
	 */
	public void addTag(String tag) {
		tags.add(tag);
		numTags++;
	}

	public void addTags(List<String> listOfTags) {
		tags.addAll(listOfTags);
		numTags += listOfTags.size();
	}

	/**
	 * @return the {@link ViewingObject} that this class is bound to
	 */
	public ViewingObject getObj() {
		return obj;
	}

	/**
	 * @return the number of tags that are the same between the search and the {@link ViewingObject}
	 */
	public int getNumTags() {
		return numTags;
	}

	public String[] getTags() {
		String[] tagsCopy = new String[tags.size()];

		int i = 0;
		for (; i < tags.size(); ++i) {
			tagsCopy[i] = tags.get(i);
		}

		return tagsCopy;
	}

	//TODO should have override here

	public int compareTo(SearchResult other) {
		return this.numTags - other.numTags;
	}

	@Override
	public String toString() {
		//TODO
		return null;
	}
}
