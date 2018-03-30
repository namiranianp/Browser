package site;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

import outsideCode.ReadWriteLock;
import outsideCode.WorkQueue;

/**
 * This class is meant to hold the data structure of all items and another data
 * structure where it maps the object to all the tags it has
 */
public class DataStructure {
	private final WorkQueue workers;

	/** Contains a list of all the {@link ViewingObject} */
	private final ArrayList<ViewingObject> objects;
	private final ReadWriteLock objLock;

	/** Sorts {@link ViewingObject} based on the tags they have */
	private final TreeMap<String, ArrayList<ViewingObject>> sorted;
	private final ReadWriteLock sortLock;

	private final ReadWriteLock displayLock;
	private final List<ViewingObject> display;

	/**
	 * initializes the {@link DataStructure} object
	 */
	public DataStructure(WorkQueue workQueue) {
		workers = workQueue;

		objLock = new ReadWriteLock();
		objects = new ArrayList<>();

		sortLock = new ReadWriteLock();
		sorted = new TreeMap<>();

		displayLock = new ReadWriteLock();
		display = new ArrayList<>();
	}

	/**
	 * Adds a single {@link ViewingObject} to this data structure and sorts it based
	 * on tags
	 *
	 * @param obj
	 *            {@link ViewingObject} being added
	 */
	public void addObject(ViewingObject obj) {
		objLock.lockReadWrite();
		objects.add(obj);
		objLock.unlockReadWrite();

		for (String tag : obj.getTags()) {
			sortLock.lockReadWrite();
			if (!sorted.containsKey(tag)) {
				sorted.put(tag, new ArrayList<ViewingObject>());
			}
			sorted.get(tag).add(obj);
			sortLock.unlockReadWrite();
		}
	}

	/**
	 * Adds multiple {@link ViewingObject} objects to this data structure and sorts
	 * them by tag automatically
	 *
	 * @param objs
	 *            {@link List} of {@link ViewingObject}s
	 */
	public void addObjects(List<ViewingObject> objs) {
		objLock.lockReadWrite();
		objects.addAll(objs);
		objLock.unlockReadWrite();

		// sort the viewing objects by tag as they are added
		for (ViewingObject obj : objs) {
			for (String tag : obj.getTags()) {
				sortLock.lockReadWrite();
				if (!sorted.containsKey(tag)) {
					sorted.put(tag, new ArrayList<ViewingObject>());
				}
				sorted.get(tag).add(obj);
				sortLock.unlockReadWrite();
			}
		}
	}

	/**
	 * @return ArrayList containing all {@link ViewingObject}s
	 */
	public ArrayList<ViewingObject> getObjects() {
		return objects;
	}

	/**
	 * Takes in tags as an argument and returns all the viewing objects that match
	 * the tag, sorts it by most relevant
	 * 
	 * @param tags
	 *            a list of tags that will be used
	 * @return {@link ArrayList} of {@link ViewingObject} that contains the sorted
	 *         results
	 */
	public List<ViewingObject> getByTags(List<String> tags) {
		ArrayList<ViewingObject> results = new ArrayList<>();

		return results;
	}
}
