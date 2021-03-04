package com.diefesson.filesync.task;

import java.io.IOException;
import java.util.concurrent.Callable;

import com.diefesson.filesync.file.AcessSynchronizer;
import com.diefesson.filesync.io.SyncConnection;
import com.diefesson.filesync.server.Constants;

/**
 * 
 * @author Diefesson de Sousa Silva
 *
 */
public class DownloadTask implements Callable<Void> {

	private final SyncConnection connection;
	private final String path;
	private final AcessSynchronizer synchronizer;

	public DownloadTask(String path, SyncConnection connection, AcessSynchronizer synchronizer) {
		this.path = path;
		this.connection = connection;
		this.synchronizer = synchronizer;
	}

	public Void call() throws IOException {
		try (connection) {
			var in = connection.getIn();
			var out = connection.getOut();
			out.write(Constants.DOWNLOAD_REQUEST);
			out.writeUTF(path);
			in.readToFile(synchronizer.solvePath(path));
			return null;
		}
	}

}
