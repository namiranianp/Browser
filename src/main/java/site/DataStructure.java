package site;

import outsideCode.ReadWriteLock;
import outsideCode.WorkQueue;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeMap;

/**
 * This class is meant to hold the data structure of all items and another data structure where it maps the object
 * to all the tags it has
 */
public class DataStructure {
	private final WorkQueue workers;

	private final ReadWriteLock objLock;
	private final ArrayList<ViewingObject> objects;

	private final ReadWriteLock sortLock;
	private final TreeMap<String, ArrayList<ViewingObject>> sorted;

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
	 * Adds a single {@link ViewingObject} to this data structure
	 *
	 * @param obj {@link ViewingObject} being added
	 */
	public void addObject(ViewingObject obj) {
		objLock.lockReadWrite();
		objects.add(obj);
		objLock.unlockReadWrite();

		workers.execute(new tagWorker(obj));
	}

	/**
	 * Adds multiple {@link ViewingObject} objects to this data structure
	 *
	 * @param objs
	 */
	public void addObjects(List<ViewingObject> objs) {
		objLock.lockReadWrite();
		objects.addAll(objs);
		objLock.unlockReadWrite();

		for (ViewingObject obj : objs) {
			workers.execute(new tagWorker(obj));
		}
	}

	/**
	 * @return ArrayList containing all {@link ViewingObject}s
	 */
	public ArrayList<ViewingObject> getObjects() {
		return objects;
	}

	/**
	 * @return ArrayList containing what is to be displayed
	 */
	public List<ViewingObject> getDisplay() {
		return display;
	}

	private class tagWorker implements Runnable {
		private final ViewingObject sorting;

		public tagWorker(ViewingObject target) {
			sorting = target;
		}

		@Override
		public void run() {
			for (String tag : sorting.getTags()) {
				sortLock.lockReadWrite();
				if (!sorted.containsKey(tag)) {
					sorted.put(tag, new ArrayList<>());
				}
				sortLock.unlockReadWrite();

				sorted.get(tag).add(sorting);
			}
		}
	}
}

