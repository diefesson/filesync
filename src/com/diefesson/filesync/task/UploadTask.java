package com.diefesson.filesync.task;

import java.io.IOException;
import java.util.concurrent.Callable;

import com.diefesson.filesync.file.AcessSynchronizer;
import com.diefesson.filesync.io.SyncConnection;
import com.diefesson.filesync.server.Constants;

public class UploadTask implements Callable<Void> {

	private final SyncConnection connection;
	private final AcessSynchronizer synchronizer;
	private final String path;

	public UploadTask(String path, SyncConnection connection, AcessSynchronizer synchronizer) {
		this.path = path;
		this.connection = connection;
		this.synchronizer = synchronizer;
	}

	@Override
	public Void call() throws IOException {
		try (connection) {
			var out = connection.getOut();
			out.write(Constants.UPLOAD_REQUEST);
			out.writeUTF(path);
			out.writeFromFile(synchronizer.solvePath(path));
			return null;
		}
	}

}
