package com.web.FileExplorer.dao;

import java.util.HashSet;

import com.web.FileExplorer.dto.ViewingObject;

public interface ViewingObjectDAO {

	/**
	 * Given a path to a file, will create a viewing object that represents the file
	 * given
	 * 
	 * @param file The path to the file which you want to represent
	 * @return {@link ViewingObject} representation of the file
	 */
	public HashSet<ViewingObject> loadViewingObjects(String file);

	/**
	 * Given a correctly formatted string, will translate the string into a viewing
	 * object
	 * 
	 * @param viewingObject A correctly formatted string for a ViewingObject
	 * @return {@link ViewingObject} representation of the string passed in
	 */
	public ViewingObject viewingObjectFromString(String viewingObject);
}
