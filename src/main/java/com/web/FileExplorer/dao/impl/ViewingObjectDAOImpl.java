package com.web.FileExplorer.dao.impl;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.web.FileExplorer.dao.ViewingObjectDAO;
import com.web.FileExplorer.dto.ViewingObject;

public class ViewingObjectDAOImpl implements ViewingObjectDAO {

	public static final String regex = ViewingObject.OVERALLREGEX;

	@Override
	public ViewingObject viewingObjectFromString(String viewingObject) {
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(viewingObject);
		ViewingObject obj = null;

		if (m.find()) {
			obj = new ViewingObject(m.group(2), m.group(1));
			obj.addTagsFromString(m.group(4));
			obj.setFileType(m.group(3));
		}

		return obj;
	}

	@Override
	public HashSet<ViewingObject> loadViewingObjects(String file) {
		HashSet<ViewingObject> objects = new HashSet<>();

		try (BufferedReader reader = new BufferedReader(new FileReader(file));) {
			ViewingObject obj = null;
			String line = null;

			while ((line = reader.readLine()) != null) {
				obj = viewingObjectFromString(line);
				objects.add(obj);
			}
		} catch (IOException e) {
			//TODO
			e.printStackTrace();
		}

		return objects;
	}
}
