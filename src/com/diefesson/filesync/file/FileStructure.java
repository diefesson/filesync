package com.diefesson.filesync.file;

import java.nio.file.Path;
import java.util.stream.Stream;

/**
 * 
 * @author Diefesson de Sousa Silva
 *
 */
public class FileStructure {

	private VirtualFile root;

	public FileStructure() {
		root = new VirtualFile("", FileType.FOLDER);
	}

	public void add(Path path, FileType type) {
		var parentPath = path.getParent();
		var name = path.getFileName().toString();
		get(parentPath).add(new VirtualFile(name, type));
	}

	public void remove(Path path) {
		var parentPath = path.getParent();
		var name = path.getFileName().toString();
		get(parentPath).remove(name);
	}

	public void reset() {
		root.removeAll();
	}
	
	public Stream<VirtualFile> recurse(){
		Stream<VirtualFile> stream = Stream.empty();
		for(var vf : root.getSubFiles().values()) {
			stream = Stream.concat(stream, vf.recurse());
		}
		return stream;
	}
	
	public VirtualFile get(Path path) {
		var current = root;
		if (path != null) {
			for (var name : path) {
				current = current.get(name.toString());
				if (current == null)
					return null;
			}
		}
		return current;
	}

}
