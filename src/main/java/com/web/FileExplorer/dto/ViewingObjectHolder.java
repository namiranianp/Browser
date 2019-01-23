package com.web.FileExplorer.dto;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
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

	public static final String fileRegex = "(?is)\\{ " + ViewingObject.OVERALLREGEX + " \\}";

	/** Sorts {@link ViewingObject} based on the tags they have */
	private final TreeMap<String, HashSet<ViewingObject>> byTags;

	/** Stores {@link ViewingObjet} paired with their locations for easy lookup **/
	private final HashMap<String, ViewingObject> byLoc;

	/**
	 * initializes the {@link DataStructure} object
	 */
	public ViewingObjectHolder() {
		byTags = new TreeMap<>();
		byLoc = new HashMap<>();
	}

	/**
	 * Adds a single {@link ViewingObject} to this data structure and sorts it based
	 * on tags. This method assumes the object you are adding is not already in the
	 * DS, adding an object already in the DS will overwrite the older object
	 *
	 * @param obj {@link ViewingObject} being added
	 */
	public void addObject(ViewingObject obj) {
		String lower;
		String objLoc = obj.getLocation();
		String[] tags = obj.getTags();
		ViewingObject target = obj;

		if (byLoc.containsKey(objLoc)) {
			// obj is a duplicate
			target = byLoc.get(objLoc);
			target.addTag(Arrays.asList(tags));
			target.setFileType(obj.getFileType());
		} else {
			// a brand new viewing object
			byLoc.put(objLoc, obj);
		}
		for (String tag : tags) {
			lower = tag.toLowerCase().strip();
			if (!byTags.containsKey(lower)) {
				byTags.put(lower, new HashSet<ViewingObject>());
			}
			byTags.get(lower).add(target);
		}
	}

	/**
	 * Adds multiple {@link ViewingObject} objects to this data structure and sorts
	 * them by tag automatically. This method assumes the objects you are adding are
	 * not already in the DS, adding objects already in the DS will overwrite the
	 * older objects.
	 *
	 * @param objs {@link List} of {@link ViewingObject}s
	 */
	public void addObjects(List<ViewingObject> objs) {
		for (ViewingObject obj : objs) {
			addObject(obj);
		}
	}

	/**
	 * Finds the {@link ViewingObject} with the given location and adds the tag to
	 * it, if no object with this location is present this returns false
	 * 
	 * @param location Location of the target object as an absolute path
	 * @param tag      The tag to be added
	 * @return If a ViewingObject with the location specified is present in the data
	 *         structure
	 */
	public boolean addTagToObject(String location, String tag) {
		ViewingObject target = byLoc.get(location);
		String lower = tag.toLowerCase().strip();

		if (target == null) {
			// if there is no object in the structure, no need to keep working
			return false;
		}
		target.addTag(lower);

		// Add the ViewingObject to appropriate tag
		if (!byTags.containsKey(lower)) {
			byTags.put(lower, new HashSet<ViewingObject>());
		}

		byTags.get(lower).add(target);

		return true;
	}

	/**
	 * Finds the {@link ViewingObject} with the given location and adds the
	 * collection of tags to it, if no object with this location is present this
	 * returns false
	 * 
	 * @param location Location of the target object as an absolute path
	 * @param tag      Collection of strings containing all of the tags to be added
	 * @return If a ViewingObject with the location specified is present in the data
	 *         structure
	 */
	public boolean addTagToObject(String location, Collection<String> tagList) {

		if (byLoc.containsKey(location)) {
			for (String tag : tagList) {
				this.addTagToObject(location, tag);
			}
		}

		ViewingObject target = byLoc.get(location);
		String lower;

		if (target == null) {
			// if there is no object in the structure, no need to keep working
			return false;
		}
		target.addTag(tagList); // TODO clean these tags?

		// Add the ViewingObject to appropriate tags
		for (String tag : tagList) {
			lower = tag.toLowerCase().strip();

			if (!byTags.containsKey(lower)) {
				byTags.put(lower, new HashSet<ViewingObject>());
			}

			byTags.get(lower).add(target);
		}

		return true;
	}

	/**
	 * Removes a specific {@link ViewingObject} from the data structure.
	 * 
	 * @param location The location as an absolute path of the object to be removed
	 * @return The {@link ViewingObject} removed or null if nothing was removed
	 */
	public ViewingObject removeObject(String location) {
		ViewingObject target = byLoc.remove(location);

		if (target != null) {
			// remove the object from all of its associated tags
			for (String tag : target.getTags()) {
				byTags.get(tag.toLowerCase().strip()).remove(target);
			}
		}

		return target;
	}

	/**
	 * Returns a mutable copy of a {@link ViewingObject} by its location
	 * 
	 * @param location The location of the ViewingObject wanted
	 * @return The ViewingObject with the desired location
	 */
	public ViewingObject getByLocation(String location) {
		return byLoc.get(location);
	}

	/**
	 * 
	 * @return A mutable copy of every object stored in this data structure
	 */
	public List<ViewingObject> getMutables() {
		ArrayList<ViewingObject> objects = new ArrayList<>();

		for (String loc : byLoc.keySet()) {
			objects.add(byLoc.get(loc));
		}

		return objects;
	}

	/**
	 * Returns a HashSet with a copy of all of the ViewingObjects this data
	 * structure contains
	 * 
	 * @return HashSet containing all {@link ViewingObject}
	 */
	public List<ViewingObject> getClonedList() {
		ArrayList<ViewingObject> copy = new ArrayList<>();

		for (String loc : byLoc.keySet()) {
			copy.add(byLoc.get(loc).clone());
		}

		return copy;
	}

	/**
	 * TODO A complex and helpful search algorithm, Currently a very simple and
	 * basic search algorithm, more of a proof of concept. Also have to sanitize
	 * user input, this is really just proof of concept
	 * 
	 * @param searchTerm The single term the you are searching for
	 * @return A list containing, in order of best matches, all the matches for this
	 *         search term
	 */
	public List<ViewingObject> search(String searchTerm) {
		System.out.println(this.byTags.keySet());
		String query = searchTerm.toLowerCase().trim().split(" ")[0].trim();
		String viewLoc;
		ArrayList<ViewingObject> finalResults = new ArrayList<>();
		ArrayList<SearchResult> toSort = new ArrayList<>();
		TreeMap<String, SearchResult> results = new TreeMap<>();
		SearchResult tempResult;

		if (byLoc.containsKey(query)) {
			SearchResult perfect = new SearchResult(byLoc.get(query), query);
			perfect.setPerfectMatch();
			results.put(query, perfect);
			toSort.add(perfect);
		}

		for (ViewingObject view : getByTag(query)) {
			viewLoc = view.getLocation();
			if (!results.containsKey(viewLoc)) {
				tempResult = new SearchResult(view);
				results.put(viewLoc, tempResult);
				toSort.add(tempResult);
			}
			results.get(viewLoc).addMatch(query);
		}

		Collections.sort(toSort, Collections.reverseOrder());

		for (int i = 0; i < toSort.size(); i++) {
			finalResults.add(i, toSort.get(i).getObject());
		}

		return finalResults;
	}

	/**
	 * Returns to the user a ArrayList filled with copies of the ViewingObjects that
	 * contain this tag. The tag must be an exact match, no partial search is
	 * currently implemented
	 * 
	 * @param str The tag being looked for
	 * @return A ArrayList containing copies of the relevant ViewingObjects
	 */
	public List<ViewingObject> getByTag(String str) {
		ArrayList<ViewingObject> result = new ArrayList<>();
		HashSet<ViewingObject> related = byTags.get(str.toLowerCase());

		// if statement is here to avoid null pointer exception
		if (related != null) {
			for (ViewingObject view : related) {
				result.add(view.clone());
			}
		}

		return result;
	}

	/**
	 * Takes in tags as an argument and returns all the viewing objects that match
	 * the tag in the form of an ArrayList<{@link SearchResult}>, sorts it by most
	 * relevant. What is most relevant is decided based on how many tags each object
	 * contains. The tags must be an exact match, no partial search is currently
	 * implemented
	 * 
	 * @param tags a list of tags that will be used
	 * @return {@link ArrayList} of {@link ViewingObject} that contains the sorted
	 *         results
	 */
	public List<ViewingObject> getByTags(List<String> searchTags) {
		ArrayList<ViewingObject> sorted = new ArrayList<>();
		ArrayList<SearchResult> sorting = new ArrayList<>();
		HashMap<String, SearchResult> results = new HashMap<>(); // key is location cause locations are unique
		HashSet<ViewingObject> nullCheck;
		SearchResult temp;
		String objLoc;
		String lower;

		// iterate over each search term
		for (String tag : searchTags) {
			lower = tag.toLowerCase().strip();
			nullCheck = byTags.get(lower);
			if (nullCheck != null) {

				// iterate over each object mapped to each search term
				for (ViewingObject obj : nullCheck) {
					objLoc = obj.getLocation();

					if (results.containsKey(objLoc)) {
						results.get(objLoc).addMatch(lower);
					} else {
						// adds the SearchResult object to the map and array, both reference same obj
						temp = new SearchResult(obj, lower);
						results.put(objLoc, temp);
						sorting.add(temp);
					}
				}
			}
		}

		Collections.sort(sorting);

		for (SearchResult res : sorting) {
			sorted.add(res.getObject());
		}

		return sorted;
	}

	/**
	 * Given a path to a file, will either append the current
	 * {@link ViewingObjectHolder} to the existing or create a new file at the
	 * location if none exists
	 * 
	 * @param path The path to the file where this holder will be stored
	 * @return True if the write was completed successfully, otherwise false
	 */
	public boolean writeToFile(String path) {
		File target = new File(path);
		FileWriter writer = null;
		BufferedWriter buf = null;

		try {
			if (target.isFile()) {
				// the file already exists, we need to append
				writer = new FileWriter(path, true);
			} else {
				// the file doesn't exist, create a file and start writing
				writer = new FileWriter(path);
			}

			buf = new BufferedWriter(writer);
			buf.write(toString());
			buf.flush();
			buf.close();
			writer.close();

			return true;
		} catch (IOException e) {
			// TODO:
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public String toString() {
		StringBuilder build = new StringBuilder();

		for (String loc : byLoc.keySet()) {
			build.append("{ " + byLoc.get(loc).toString() + " }\n");
		}

		return build.toString();
	}
}
