package com.web.FileExplorer.dto;

import java.nio.file.Paths;
import java.util.Collection;
import java.util.HashSet;
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
	/**
	 * This pattern is for identifying individual tags from a list
	 */
	public static final String tagReg = "(?s)\\|(.+?)\\|";

	/**
	 * These patterns are for being able to turn the toString() back into a
	 * ViewingObject
	 */
	public static final String tagRegex = "tags: (.*?)";
	public static final String nameRegex = "name: (.*?), ";
	public static final String locRegex = "loc: (.*?), ";
	public static final String typeRegex = "type: (.*?), ";
	public static final String OVERALLREGEX = nameRegex + locRegex + typeRegex + tagRegex;

	private String objLoc;
	private String name;
	private String fileType;
	private final TreeSet<String> tags;

	/**
	 * Constructor for this class.
	 * 
	 * @param objectLocation location of the thing this object is representing, this
	 *                       MUST be the absolute path cause relative paths suck
	 * @param objectName     name given to the object
	 * @param initialTags    any tags you wish to give to the object, or null if no
	 *                       tags are given
	 */
	public ViewingObject(String objectLocation, String objectName, Collection<String> initialTags) {
		objLoc = objectLocation.strip();
		name = objectName.strip();
		tags = new TreeSet<>();
		if (initialTags != null) {
			tags.addAll(initialTags);
		}
		String type = (objectLocation.contains(".")) ? objectLocation.substring(objectLocation.lastIndexOf("."))
				: "unk";

		if (type.equalsIgnoreCase(".png")) {
			type = ".img";
		}
		
		this.fileType = type;
	}

	/**
	 * Constructor for this class, that takes one initial tag instead of a
	 * collection
	 * 
	 * @param objectLocation location of the thing this object is representing, this
	 *                       MUST be the absolute path cause relative paths suck
	 * @param objectName     name given to the object
	 * @param initialTags    Single initial tag for this object
	 */
	public ViewingObject(String objectLocation, String objectName, String initialTag) {
		objLoc = objectLocation;
		name = objectName;
		tags = new TreeSet<>();
		tags.add(initialTag);
		this.fileType = objectLocation.substring(objectLocation.lastIndexOf(".") + 1);
	}

	/**
	 * Constructor for this class without giving a list of initial tags
	 * 
	 * @param objectLocation location of the thing this object is representing, this
	 *                       MUST be the absolute path cause relative paths suck
	 * @param objectName     name you wish to give the object
	 */
	public ViewingObject(String objectLocation, String objectName) {
		this(objectLocation, objectName, new HashSet<String>());
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
	 * Sets the fileType for this ViewingObject
	 * 
	 * @param type A string containing the the fileType
	 */
	public void setFileType(String type) {
		this.fileType = type;
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
	 * Overloaded method to allow you to add a collection of tags
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
	 * Removes all of the tags from this ViewingObject
	 */
	public void clearTags() {
		tags.clear();
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
	 * @return the fileType for this object as a String
	 */
	public String getFileType() {
		return fileType;
	}

	/**
	 * Given a path, will return the relative path between the path given and the
	 * location of the object this ViewingObject represents. Important: it will call
	 * otherPath.relativize(objLoc); the order matters!
	 * 
	 * @param otherPath Path in a string format of a directory
	 * @return String containing relative path
	 */

	public String getRelPath(String otherPath) {
		return Paths.get(otherPath).relativize(Paths.get(objLoc)).toString();
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
		buff.append("name: " + name + ", loc: " + objLoc + ", type: " + fileType + ", tags: ");

		for (String tag : tags) {
			buff.append("|" + tag + "|");
		}

		return buff.toString();
	}
}
