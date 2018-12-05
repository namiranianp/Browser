package data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.TreeMap;

/**
 * This class is meant to hold the data structure of all items and another data
 * structure where it maps the object to all the tags it has
 */
public class ViewingObjectHolder {
	/** Contains a list of all the {@link ViewingObject} */
	private final HashSet<ViewingObject> objects;

	/** Sorts {@link ViewingObject} based on the tags they have */
	private final TreeMap<String, HashSet<ViewingObject>> sorted;

	/**
	 * initializes the {@link DataStructure} object
	 */
	public ViewingObjectHolder() {
		objects = new HashSet<>();
		sorted = new TreeMap<>();
	}

	/**
	 * Adds a single {@link ViewingObject} to this data structure and sorts it based
	 * on tags
	 *
	 * @param obj {@link ViewingObject} being added
	 */
	public void addObject(ViewingObject obj) {
		objects.add(obj);

		for (String tag : obj.getTags()) {
			if (!sorted.containsKey(tag)) {
				sorted.put(tag, new HashSet<ViewingObject>());
			}
			sorted.get(tag).add(obj);
		}
	}

	/**
	 * Adds multiple {@link ViewingObject} objects to this data structure and sorts
	 * them by tag automatically
	 *
	 * @param objs {@link List} of {@link ViewingObject}s
	 */
	public void addObject(List<ViewingObject> objs) {
		objects.addAll(objs);

		// sort the viewing objects by tag as they are added
		for (ViewingObject obj : objs) {
			for (String tag : obj.getTags()) {
				if (!sorted.containsKey(tag)) {
					sorted.put(tag, new HashSet<ViewingObject>());
				}
				sorted.get(tag).add(obj);
			}
		}
	}

	/**
	 * Returns a HashSet with a copy of all of the ViewingObjects this data
	 * structure contains
	 * 
	 * @return HashSet containing all {@link ViewingObject}
	 */
	public HashSet<ViewingObject> getObjects() {
		HashSet<ViewingObject> copy = new HashSet<>();

		for (ViewingObject view : objects) {
			copy.add(view.clone());
		}

		return copy;
	}

	/**
	 * Returns to the user a HashSet filled with copies of the ViewingObjects that
	 * contain this tag.
	 * 
	 * @param str The tag being looked for
	 * @return A HashSet containing copies of the relevant ViewingObjects
	 */
	public ArrayList<ViewingObject> getByTag(String str) {
		ArrayList<ViewingObject> result = new ArrayList<>();

		for (ViewingObject view : sorted.get(str)) {
			result.add(view.clone());
		}

		return result;
	}

	/**
	 * Takes in tags as an argument and returns all the viewing objects that match
	 * the tag in the form of an ArrayList<{@link SearchResult}>, sorts it by most
	 * relevant. What is most relevant is decided based on how many tags each object
	 * contains.
	 * 
	 * @param tags a list of tags that will be used
	 * @return {@link ArrayList} of {@link ViewingObject} that contains the sorted
	 *         results
	 */
	public ArrayList<SearchResult> getByTags(List<String> searchTags) {
		ArrayList<SearchResult> sorting = new ArrayList<>();
		HashMap<String, SearchResult> results = new HashMap<>(); // key is location cause locations are unique
		HashSet<ViewingObject> nullCheck;
		SearchResult temp;
		String objLoc;

		// iterate over each search term
		for (String tag : searchTags) {
			nullCheck = sorted.get(tag);
			if (nullCheck != null) {

				// iterate over each object mapped to each search term
				for (ViewingObject obj : nullCheck) {
					objLoc = obj.getLocation();

					if (results.containsKey(objLoc)) {
						results.get(objLoc).addMatch(tag);
					} else {
						// adds the SearchResult object to the map and array, both reference same obj
						temp = new SearchResult(obj, tag);
						results.put(objLoc, temp);
						sorting.add(temp);
					}
				}
			}
		}

		Collections.sort(sorting);

		return sorting;
	}

	@Override
	public String toString() {
		StringBuilder build = new StringBuilder();

		for (ViewingObject view : objects) {
			build.append("{ " + view.toString() + " }\n");
		}

		return build.toString();
	}
}
