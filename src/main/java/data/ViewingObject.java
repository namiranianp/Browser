package data;

import java.util.Collection;
import java.util.Iterator;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * This class is meant to hold each object to be viewed and information
 * regarding it
 * 
 * @author Pedram
 *
 */
public class ViewingObject {
	public static final String tagReg = "(?s)\\|(.+?)\\|";

	private String objLoc;
	private String name;
	private final TreeSet<String> tags;

	/**
	 * Constructor for this class, idk what else to write here
	 * 
	 * @param objectLocation location of the thing this object is representing, this
	 *                       MUST be the absolute path cause relative paths suck
	 * @param objectName     name given to the object
	 * @param initialTags    any tags you wish to give to the object, or null if no
	 *                       tags are given
	 */
	public ViewingObject(String objectLocation, String objectName, Collection<String> initialTags) {
		objLoc = objectLocation;
		name = objectName;
		tags = new TreeSet<>();
		if (initialTags != null) {
			tags.addAll(initialTags);
		}
	}

	/**
	 * Constructor for this class without giving a list of initial tags
	 * 
	 * @param objectLocation location of the thing this object is representing, this
	 *                       MUST be the absolute path cause relative paths suck
	 * @param objectName     name you wish to give the object
	 */
	public ViewingObject(String objectLocation, String objectName) {
		this(objectLocation, objectName, null);
	}

	/**
	 * Changes the name of the viewing object
	 * 
	 * @param newName the new name of this object
	 */
	public void setName(String newName) {
		name = newName;
	}

	/**
	 * Use this method if your object changes location and you need to update the
	 * location instead of creating a new object
	 * 
	 * @param newLocation the new location
	 */
	public void setObjLoc(String newLocation) {
		objLoc = newLocation;
	}

	/**
	 * Adds the specific tag to the list of tags for this object
	 * 
	 * @param tag Tag to be added
	 * @return True if the tag is not already in the list of tags, False otherwise
	 */
	public boolean addTag(String tag) {
		return tags.add(tag);
	}

	/**
	 * Overloaded method to allow you to add a single tag or a collection of
	 * multiple tags
	 * 
	 * @param newTags collection of tags to be added
	 * @return whether this list changed as a result of this function call
	 */
	public boolean addTag(Collection<String> newTags) {
		return tags.addAll(newTags);
	}

	/**
	 * Given a string, splits it up on "|" and adds text in between "|" as a tag
	 * 
	 * @param tagList The string containing a list of tags separated by "|"
	 * @return Returns true because all other addTag functions return a boolean
	 */
	public boolean addTagsFromString(String tagList) {
		Pattern p = Pattern.compile(tagReg);
		Matcher m = p.matcher(tagList);

		while (m.find()) {
			this.tags.add(m.group(1));
		}

		return true;
	}

	/**
	 * Copies all of the tags of this {@link ViewingObject} and returns it in an
	 * array form
	 *
	 * @return An array of Strings containing all of the tags
	 */
	public String[] getTags() {
		String[] tagsCopy = new String[tags.size()];
		Iterator<String> iter = tags.iterator();
		int i = 0;

		while (iter.hasNext()) {
			tagsCopy[i++] = iter.next();
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

	/**
	 * Returns an exact copy of this ViewingObject, changing the new object should
	 * have no effect on the current object
	 */
	public ViewingObject clone() {
		return new ViewingObject(this.objLoc, this.name, this.tags);
	}

	@Override
	public String toString() {
		StringBuilder buff = new StringBuilder();
		buff.append("name: " + name + ", loc: " + objLoc + ", tags: ");

		for (String tag : tags) {
			buff.append("|" + tag + "|");
		}

		return buff.toString();
	}
}
