package com.web.FileExplorer.services;

import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;

import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.web.FileExplorer.dto.ViewingObject;
import com.web.FileExplorer.dto.ViewingObjectHolder;

public interface DirectoryTraverserService {

	/**
	 * Given a path to a file or directory and a {@link ViewingObjectHolder}, if the
	 * path given is a directory this method will recursively travel the directory
	 * and create {@link ViewingObject} representations of every file and story it
	 * in the given ViewingObjectHolder
	 * 
	 * @param path
	 * @param struct
	 */
	public static void traverseDirectory(Path path, ViewingObjectHolder struct) {
		try (DirectoryStream<Path> pathway = Files.newDirectoryStream(path)) {
			for (Path location : pathway) {
				if (Files.isDirectory(location)) {
					traverseDirectory(location, struct);
				} else {
					struct.addObject(new ViewingObject(location.toString(), location.getFileName().toString()));
				}
			}
		} catch (IOException e) {
			System.err.println("The path " + path + " is not a valid directory, check the directory and try again");
		}
	}

	public static void traverseMultipartFile(MultipartFile file, ViewingObjectHolder struct) {
		String filename = StringUtils.cleanPath(file.getOriginalFilename());
		File target = new File(filename);
		System.err.println("The filename is:" + filename);
		System.err.println("The filename is:" + target.getAbsolutePath());
		
		if (!target.isDirectory()) {
			struct.addObject(new ViewingObject(target.getAbsolutePath(), target.getName()));
		} else {
			traverseDirectory(target.toPath(), struct);
		}
	}
}
