package com.diefesson.filesync.file;

import java.io.File;
import java.nio.file.Paths;
import java.util.HashSet;
import java.util.Set;

/**
 * 
 * @author Diefesson de Sousa Silva
 *
 */
public class FileSynchronizer {

	private final String root;
	private final Set<FileEntry> files = new HashSet<>();

	public FileSynchronizer(String rootDir) {
		this.root = rootDir;
	}

	private FileEntry getEntry(String path) {
		for (var fe : files) {
			if (fe.getPath() == path)
				return fe;
		}
		return null;
	}

	public String getRoot() {
		return root;
	}

	public String solvePath(String path) {
		return Paths.get(root, path).toString();
	}

	public synchronized boolean hasFile(String path) {
		return getEntry(path) != null;
	}

	public synchronized boolean add(String path) {
		return add(path, false);
	}

	public synchronized boolean add(String path, boolean lockWrite) {
		var entry = new FileEntry(path);
		if (files.add(entry)) {
			if (lockWrite)
				entry.lockWrite();
			return true;
		}
		return false;
	}

	public synchronized boolean remove(String path) {
		var entry = getEntry(path);
		if (entry != null) {
			entry.lockWrite();
			return new File(root + "/" + path).delete();
		}
		return false;
	}

	public synchronized boolean lockRead(String path) {
		var entry = getEntry(path);
		if (entry != null) {
			entry.lockRead();
			return true;
		}
		return false;
	}

	public synchronized boolean lockWrite(String path) {
		var entry = getEntry(path);
		if (entry != null) {
			entry.lockWrite();
			return true;
		}
		return false;
	}

	public synchronized void unlockRead(String path) {
		var entry = getEntry(path);
		if (entry != null) {
			entry.unlockRead();
		}
	}

	public synchronized void unlockWrite(String path) {
		var entry = getEntry(path);
		if (entry != null) {
			entry.unlockWrite();
		}
	}

}
