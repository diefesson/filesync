package com.diefesson.filesync.file;

import java.nio.file.Path;
import java.util.Iterator;

/**
 * 
 * @author Diefesson de Sousa Silva
 *
 */
public class FileStructure implements Iterable<VirtualFile> {

	private VirtualFile root;

	public FileStructure() {
		root = new VirtualFile(null, FileType.FOLDER);
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

	private VirtualFile get(Path path) {
		var current = root;
		System.out.println("path is " + path);
		if (path != null) {
			for (var name : path) {
				current = current.get(name.toString());
				if (current == null)
					return null;
			}
		}
		return current;
	}

	@Override
	public Iterator<VirtualFile> iterator() {
		return root.iterator();
	}

}
