package com.diefesson.filesync.task;

import java.io.IOException;

import com.diefesson.filesync.file.FileSynchronizer;
import com.diefesson.filesync.io.SyncConnection;
import com.diefesson.filesync.server.Constants;

/**
 * 
 * @author Diefesson de Sousa Silva
 *
 */
public class DownloadTask implements Runnable {

	private final String address;
	private final int port;
	private final String path;
	private final FileSynchronizer synchronizer;

	public DownloadTask(String address, int port, String path, FileSynchronizer synchronizer) {
		this.address = address;
		this.port = port;
		this.path = path;
		this.synchronizer = synchronizer;
	}

	@Override
	public void run() {
		try (var connection = new SyncConnection(address, port)) {
			var in = connection.getIn();
			var out = connection.getOut();
			out.writeByte(Constants.DOWNLOAD_REQUEST);
			out.writeUTF(path);
			in.readToFile(synchronizer.solvePath(path));
		} catch (IOException e) {
			var mes = "DonwloadTask %s %d %s: could not download file".formatted(address, port, path);
			System.err.println(mes);
			e.printStackTrace();
		}
	}

}
