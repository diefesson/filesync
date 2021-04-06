package com.diefesson.filesync.file;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * 
 * @author Diefesson de Sousa Silva
 *
 */
public class VirtualFile implements Iterable<VirtualFile> {

	private String name;
	private VirtualFile parent;
	private final FileType type;
	private final Map<String, VirtualFile> subFiles;

	public VirtualFile(String name, FileType type) {
		this.name = name;
		this.type = type;
		subFiles = new HashMap<>();
	}

	public String getName() {
		return name;
	}

	public FileType getType() {
		return type;
	}

	public void add(VirtualFile file) {
		subFiles.put(file.name, file);
		file.parent = this;
	}

	public void remove(String name) {
		var file = subFiles.remove(name);
		file.parent = null;
	}

	public VirtualFile get(String name) {
		return subFiles.get(name);
	}

	public void removeAll() {
		subFiles.clear();
	}

	@Override
	public String toString() {
		var str = (name == null) ? "" : name;
		if (parent != null && parent.name != null) {
			return parent.toString() + "/" + str;
		} else {
			return str;
		}
	}

	@Override
	public Iterator<VirtualFile> iterator() {
		return subFiles.values().iterator();
	}

}
