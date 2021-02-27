package com.diefesson.filesync.server.task;

import com.diefesson.filesync.file.FileSynchronizer;
import com.diefesson.filesync.io.SyncConnection;

/**
 * 
 * @author Diefesson de Sousa Silva
 *
 */
public class ServerUploadTask implements Runnable {

	private final SyncConnection connection;
	private final FileSynchronizer synchronizer;

	public ServerUploadTask(SyncConnection connection, FileSynchronizer synchronizer) {
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
			in.readToFile(synchronizer.solvePath(path));
		} catch (Exception e) {
			System.err.println("ServerUploadTask %s: error donwloading file from client");
			e.printStackTrace();
		} finally {
			synchronizer.unlockWrite(path);
		}
	}

}
