package site;

import outsideCode.WorkQueue;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class SeedWorker implements Runnable {
	private final WorkQueue workers;
	private final String source;
	private final DataStructure struct;

	public SeedWorker(WorkQueue workQueue, String startDir, DataStructure dataStructure) {
		workers = workQueue;
		source = startDir;
		struct = dataStructure;
	}

	@Override
	public void run() {
		Path startPath = Paths.get(source);
		if (!Files.isDirectory(startPath) || !Files.isReadable(startPath)){
			return;
		}

		try (DirectoryStream<Path> pathway = Files.newDirectoryStream(startPath)) {
			for (Path path : pathway) {
				if (Files.isDirectory(path)) {
					workers.execute(new SeedWorker(workers, path.toAbsolutePath().toString(), struct));
				} else if (path.toAbsolutePath().toString().endsWith(".mp4")) {
					ViewingObject newObject = new ViewingObject(path.toAbsolutePath().toString(),
							path.getFileName().toString(), null);
					struct.addObject(newObject);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
			//TODO
		}
	}
}
