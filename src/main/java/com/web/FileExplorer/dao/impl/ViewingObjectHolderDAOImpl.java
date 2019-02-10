package com.web.FileExplorer.dao.impl;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.web.FileExplorer.dao.ViewingObjectHolderDAO;
import com.web.FileExplorer.dto.ViewingObject;
import com.web.FileExplorer.dto.ViewingObjectHolder;

public class ViewingObjectHolderDAOImpl implements ViewingObjectHolderDAO {

	@Override
	public ViewingObjectHolder getHolder(ViewingObject obj) {
		ViewingObjectHolder holder = new ViewingObjectHolder();

		if (obj != null) {
			holder.addObject(obj);
		}

		return holder;
	}

	@Override
	public ViewingObjectHolder getHolderFromFormattedFile(String path) {
		
		// TODO include what happens if we're given a path to a directory
		ViewingObjectHolder holder = new ViewingObjectHolder();
		ViewingObject obj = null;
		File file = new File(path);
		String line = null;
		Pattern pat = Pattern.compile(ViewingObjectHolder.fileRegex);
		Matcher matcher;

		if (!file.exists()) {
			// TODO
			return holder;
		}

		try (FileReader reader = new FileReader(file); BufferedReader buf = new BufferedReader(reader);) {
			while ((line = buf.readLine()) != null) {
				matcher = pat.matcher(line);
				if (matcher.matches()) {
					obj = new ViewingObject(matcher.group(2), matcher.group(1));
					obj.addTagsFromString(matcher.group(4));
					holder.addObject(obj);
				}
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return holder;
	}

	@Override
	public ViewingObjectHolder getHolderFromFiles(String path) {
		// TODO Auto-generated method stub
		return new ViewingObjectHolder();
	}

}
