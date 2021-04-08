package com.diefesson.filesync.file;

import java.nio.file.Path;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * 
 * @author Diefesson de Sousa Silva
 *
 */
public class FileStructure implements Iterable<VirtualFile> {

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
	
	public Set<Diff> compare(FileStructure other){
		var diffs = new HashSet<Diff>();
		var pairs = root.subFilesPairs(other.root);
		for(var p : pairs) {
			compare(p.left, p.right, diffs);
		}
		return diffs;
	}
	
	private void compare(VirtualFile vf1, VirtualFile vf2, Set<Diff> diffs) {
		if(vf1 == null) {
			diffs.add(new Diff(vf2.getPath(), DiffType.REMOTE));
		} else if(vf2 == null) {
			diffs.add(new Diff(vf1.getPath(), DiffType.LOCAL));
		} else if(vf1.getType() != vf2.getType()) {
			diffs.add(new Diff(vf1.getPath(), DiffType.CONFLICT));
		} else if(vf1.getType() == FileType.FOLDER) {
			for(var p : vf1.subFilesPairs(vf2)) {
				compare(p.left, p.right, diffs);
			}
		}
	}
	
	private VirtualFile get(Path path) {
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

	@Override
	public Iterator<VirtualFile> iterator() {
		return root.iterator();
	}

}
