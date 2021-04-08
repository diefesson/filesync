package com.diefesson.filesync.file;

import java.nio.file.Path;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

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
	
	public Map<String, VirtualFile> getSubFiles(){
		return Collections.unmodifiableMap(subFiles);
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
	
	public Path getPath() {
		if(parent != null) {
			return parent.getPath().resolve(name);
		} else {
			return Path.of(name);
		}
	}
	
	public Set<Pair<VirtualFile, VirtualFile>> subFilesPairs(VirtualFile other){
		var pairs = new HashSet<Pair<VirtualFile, VirtualFile>>();
		var names = new HashSet<String>();
		names.addAll(subFiles.keySet());
		names.addAll(other.subFiles.keySet());
		for(var n : names) {
			pairs.add(new Pair<>(get(n), other.get(n)));
		}
		return pairs;
	}

	@Override
	public String toString() {
		return getPath().toString();
	}

	@Override
	public Iterator<VirtualFile> iterator() {
		return subFiles.values().iterator();
	}

}
