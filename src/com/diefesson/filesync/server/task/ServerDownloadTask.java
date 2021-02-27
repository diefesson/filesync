package com.diefesson.filesync.server.task;

import java.io.IOException;

import com.diefesson.filesync.file.FileSynchronizer;
import com.diefesson.filesync.io.SyncConnection;

/**
 * Resolves download requests
 * 
 * @author Diefesson de Sousa Silva
 *
 */
public class ServerDownloadTask implements Runnable {

	private final SyncConnection connection;
	private final FileSynchronizer synchronizer;

	public ServerDownloadTask(SyncConnection connection, FileSynchronizer synchronizer) {
		this.connection = connection;
		this.synchronizer = synchronizer;
	}

	@Override
	public void run() {
		String path = null;
		try (connection) {
			var in = connection.getIn();
			var out = connection.getOut();
			path = in.readUTF();
			synchronizer.lockRead(path);
			out.writeFromFile(synchronizer.solvePath(path));
			out.flush();
		} catch (IOException e) {
			System.err.println("ServerDownloadTask %s: error uploading file to client".formatted(path));
			e.printStackTrace();
		} finally {
			synchronizer.unlockRead(path);
		}
	}
}
