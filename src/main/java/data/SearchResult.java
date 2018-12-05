package data;

import java.util.Collection;
import java.util.HashSet;

/**
 * This class is made for simplifying how I store search results for looking up
 * a {@link ViewingObject} by tags
 * 
 * @author Pedram
 *
 */
public class SearchResult implements Comparable<SearchResult>{
	private final ViewingObject viewObj;
	private final HashSet<String> matchTags;

	/**
	 * Initializes SearchResult with the ViewingObject and numMatches = 0
	 * 
	 * @param viewingObject The object this class focuses on
	 */
	public SearchResult(ViewingObject viewingObject) {
		viewObj = viewingObject;
		matchTags = new HashSet<>();
	}

	/**
	 * Initializes SearchResult with the ViewingObject and has matchTags contain the
	 * tag specified
	 * 
	 * @param viewingObject The object this class focuses on
	 * @param tag           The initial tag related to the ViewingObject
	 */
	public SearchResult(ViewingObject viewingObject, String tag) {
		viewObj = viewingObject;
		matchTags = new HashSet<>();
		matchTags.add(tag);
	}

	/**
	 * Initializes SearchResult with the ViewingObject and has matchTags contain all
	 * of the tags given
	 * 
	 * @param viewingObject The object this class focuses on
	 * @param tags          The initial tags related to the ViewingObject
	 */
	public SearchResult(ViewingObject viewingObject, Collection<String> tags) {
		viewObj = viewingObject;
		matchTags = new HashSet<>();
		matchTags.addAll(tags);
	}

	/**
	 * Adds specified tag to the list of matched tags
	 * 
	 * @param tag Tag to be added to list of matched tags
	 */
	public void addMatch(String tag) {
		matchTags.add(tag);
	}

	/**
	 * Returns a mutable copy of the ViewingObject this object is bound to
	 * 
	 * @return Mutable copy of the ViewingObject
	 */
	public ViewingObject getObject() {
		return viewObj;
	}

	/**
	 * Returns the number of matches this ViewingObject has.
	 * 
	 * @return Number of matches this ViewingObject has
	 */
	public int numMatches() {
		return matchTags.size();
	}

	/**
	 * Returns a copy of the tags that this ViewingObject has matched with
	 * 
	 * @return A copy of the list of matched with tags
	 */
	public HashSet<String> getMatches() {
		return new HashSet<>(matchTags);
	}
	
	@Override
	public int compareTo(SearchResult other) {
		return other.matchTags.size() - this.matchTags.size();	
	}

	@Override
	public String toString() {
		StringBuilder build = new StringBuilder();
		
		build.append(viewObj.getLocation() + " {\n");
		
		for (String tag : matchTags) {
			build.append("\t" + tag + "\n");
		}
		
		build.append("}");
		
		return build.toString();
	}
}
