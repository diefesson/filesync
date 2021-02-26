package com.diefesson.filesync.task;

import java.io.IOException;

import com.diefesson.filesync.file.FileSynchronizer;
import com.diefesson.filesync.io.SyncConnection;
import com.diefesson.filesync.server.Constants;

public class UploadTask implements Runnable {
	
	private final String address;
	private final int port;
	private final String path;
	private final FileSynchronizer synchronizer;
	
	public UploadTask(String address, int port, String path, FileSynchronizer synchronizer) {
		this.address = address;
		this.port = port;
		this.path = path;
		this.synchronizer = synchronizer;
	}
	
	@Override
	public void run() {
		try(var connection = new SyncConnection(address, port)){
			var in = connection.getIn();
			var out = connection.getOut();
			out.write(Constants.UPLOAD_REQUEST);
			out.writeUTF(path);
			in.readToFile(synchronizer.solvePath(path));
		} catch (IOException e) {
			var mes = "UploadTask %s %d %s: could not upload file".formatted(address, port, path);
			System.err.println(mes);
			e.printStackTrace();
		}
	}

}
