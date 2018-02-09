package site;

import java.util.ArrayList;

public class ViewingObject {
	private String objLoc;
	private String name;
	private ArrayList<String> tags;

	public ViewingObject(String objectLocation, String objectName, ArrayList<String> initialTags) {
		objLoc = objectLocation;
		name = objectName;
		tags = (initialTags == null) ? new ArrayList<String>() : initialTags;
	}

	/**
	 * Copys all of the tags of this {@link ViewingObject} and returns it in an array form
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
}
