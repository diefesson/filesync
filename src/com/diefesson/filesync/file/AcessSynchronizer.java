package com.diefesson.filesync.file;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * 
 * @author Diefesson de Sousa Silva
 *
 */
public class AcessSynchronizer {

	private final Map<String, ReadWriteLock> locks = new HashMap<>();

	public synchronized Lock readLock(String path) {
		return getLock(path).readLock();
	}

	public synchronized Lock writeLock(String path) {
		return getLock(path).writeLock();
	}

	private synchronized ReadWriteLock getLock(String path) {
		var lock = locks.get(path);
		if (lock == null) {
			lock = new ReentrantReadWriteLock();
			locks.put(path, lock);
		}
		return lock;
	}

}
