package com.web.FileExplorer.dao;

import com.web.FileExplorer.dto.ViewingObject;
import com.web.FileExplorer.dto.ViewingObjectHolder;

public interface ViewingObjectHolderDAO {

	/**
	 * Given a single {@link ViewingObject}, or null, will create a new
	 * {@link ViewingObjectHolder} with either one Viewing object or empty
	 * 
	 * @param obj The ViewingObject to be added to the list, or null if nothing is
	 *            to be added
	 * @return A ViewingObjectHolder containing the desired item, or nothing
	 */
	public ViewingObjectHolder getHolder(ViewingObject obj);

	/**
	 * Given a path to a formatted file, or directory with formatted files, will
	 * translate these files into {@linkplain ViewingObject}s and return a
	 * {@link ViewingObjectHolder} containing these
	 * 
	 * @param path Path to the formatted file or directory with formatted files
	 * @param key  The key to be used to decrypt the file, enter an empty String if
	 *             the file is not encrypted
	 * @return A ViewingObjectHolder containing the items specified
	 */
	public ViewingObjectHolder getHolderFromFormattedFile(String path);

	/**
	 * Given a path to a file or directory will create a {@link ViewingObject}
	 * representation for each file encountered, if a directory it will recursively
	 * traverse it
	 * 
	 * @param path Path to a file or directory
	 * @return {@link ViewingObjectHolder} containing a ViewingObject representation
	 *         for every file in the specified path
	 */
	public ViewingObjectHolder getHolderFromFiles(String path);
}
