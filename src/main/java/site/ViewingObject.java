package site;

import java.util.ArrayList;

/**
 * This class is meant to hold each object to be viewed and information
 * regarding it
 * 
 * @author Pedram
 *
 */
public class ViewingObject {
	private String objLoc;
	private String name;
	private ArrayList<String> tags;

	/**
	 * Constructor for this class, idk what else to write here
	 * 
	 * @param objectLocation
	 *            location of the thing this object is representing, this MUST be
	 *            the absolute path cause relative paths suck
	 * @param objectName
	 *            name given to the object
	 * @param initialTags
	 *            any tags you wish to give to the object, or null if no tags are
	 *            given
	 */
	public ViewingObject(String objectLocation, String objectName, ArrayList<String> initialTags) {
		objLoc = objectLocation;
		name = objectName;
		tags = (initialTags == null) ? new ArrayList<String>() : initialTags;
	}

	/**
	 * Changes the name of the viewing object
	 * 
	 * @param newName
	 *            the new name of this object
	 */
	public void setName(String newName) {
		name = newName;
	}

	/**
	 * Use this method if your object changes location and you need to update the
	 * location instead of creating a new object
	 * 
	 * @param newLocation
	 *            the new location
	 */
	public void setObjLoc(String newLocation) {
		objLoc = newLocation;
	}

	/**
	 * Copies all of the tags of this {@link ViewingObject} and returns it in an
	 * array form
	 *
	 * @return An array of Strings containing all of the tags
	 */
	public String[] getTags() {
		String[] tagsCopy = new String[tags.size()];
		int i = 0;

		for (; i < tags.size(); ++i) {
			tagsCopy[i] = tags.get(i);
		}
		return tagsCopy;
	}

	/**
	 * @return The name of this object in String format
	 */
	public String getName() {
		return name;
	}

	/**
	 * @return the absolute location of this object in String format
	 */
	public String getLocation() {
		return objLoc;
	}

	@Override
	public String toString() {
		StringBuffer buff = new StringBuffer();
		buff.append("name: " + name + ", loc: " + objLoc + ", tags: ");

		for (String tag : tags) {
			buff.append("|" + tag + "|");
		}

		return buff.toString();
	}
}
