package com.diefesson.filesync.server;

import java.io.IOException;
import java.util.concurrent.ExecutorService;

import com.diefesson.filesync.file.FileSystemBridge;
import com.diefesson.filesync.io.SyncConnection;
import com.diefesson.filesync.server.Server.OnConnectListener;
import com.diefesson.filesync.server.task.ServerDownloadTask;
import com.diefesson.filesync.server.task.ServerListTask;
import com.diefesson.filesync.server.task.ServerUploadTask;

/**
 * 
 * @author Diefesson de Sousa Silva
 *
 */
public class ConnectionDispatcher implements OnConnectListener {

	private final ExecutorService executorService;
	private final FileSystemBridge fileSystemBridge;

	public ConnectionDispatcher(ExecutorService executorService, FileSystemBridge fileSystemBridge) {
		this.executorService = executorService;
		this.fileSystemBridge = fileSystemBridge;
	}

	@Override
	public void onConnect(SyncConnection connection) {
		executorService.execute(() -> {
			try {
				int r = connection.getIn().read();
				switch (r) {
				case ProtocolConstants.DOWNLOAD_REQUEST:
					executorService.execute(new ServerDownloadTask(connection, fileSystemBridge));
					break;
				case ProtocolConstants.UPLOAD_REQUEST:
					executorService.execute(new ServerUploadTask(connection, fileSystemBridge));
					break;
				case ProtocolConstants.LIST_FILES_REQUEST:
					executorService.execute(new ServerListTask(connection, fileSystemBridge));
					break;
				default:
					System.err.println("Received unknow request: " + r);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		});
	}

}
