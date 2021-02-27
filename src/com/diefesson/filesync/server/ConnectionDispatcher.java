package com.diefesson.filesync.server;

import java.io.IOException;
import java.util.concurrent.Executor;

import com.diefesson.filesync.file.FileSynchronizer;
import com.diefesson.filesync.io.SyncConnection;
import com.diefesson.filesync.server.Server.OnConnectListener;
import com.diefesson.filesync.server.task.ServerDownloadTask;
import com.diefesson.filesync.server.task.ServerUploadTask;

/**
 * 
 * @author Diefesson de Sousa Silva
 *
 */
public class ConnectionDispatcher implements OnConnectListener {

	private final FileSynchronizer synchronizer;
	private final Executor executor;

	public ConnectionDispatcher(FileSynchronizer synchronizer, Executor executor) {
		this.synchronizer = synchronizer;
		this.executor = executor;
	}

	@Override
	public void onConnect(SyncConnection connection) {
		executor.execute(() -> {
			try {
				int r = connection.getIn().read();
				if (r == Constants.DOWNLOAD_REQUEST) {
					executor.execute(new ServerDownloadTask(connection, synchronizer));
				} else if (r == Constants.UPLOAD_REQUEST) {
					executor.execute(new ServerUploadTask(connection, synchronizer));
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		});
	}

}
