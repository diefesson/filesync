package com.diefesson.filesync.file;

import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * 
 * @author Diefesson de Sousa Silva
 *
 */
public class FileEntry {

	private final ReadWriteLock lock = new ReentrantReadWriteLock();
	private final String path;

	public FileEntry(String path) {
		this.path = path;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof FileEntry)
			return ((FileEntry) obj).path.equals(path);
		return false;
	}

	public String getPath() {
		return path;
	}

	public void lockRead() {
		lock.readLock().lock();
	}

	public void lockWrite() {
		lock.writeLock().lock();
	}

	public void unlockRead() {
		lock.readLock().unlock();
	}

	public void unlockWrite() {
		lock.writeLock().unlock();
	}

}
