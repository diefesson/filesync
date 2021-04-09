package com.diefesson.filesync.task;

import java.io.IOException;
import java.nio.file.Path;
import java.util.concurrent.Callable;

import com.diefesson.filesync.file.FileSystemBridge;
import com.diefesson.filesync.file.FileType;
import com.diefesson.filesync.io.SyncConnection;
import com.diefesson.filesync.server.ProtocolConstants;

public class UploadTask implements Callable<Void> {

	private final SyncConnection connection;
	private final FileSystemBridge fileSystemBridge;
	private final Path path;

	public UploadTask(SyncConnection connection, FileSystemBridge fileSystemBridge, Path path) {
		this.connection = connection;
		this.fileSystemBridge = fileSystemBridge;
		this.path = path;
	}

	@Override
	public Void call() throws IOException {
		try (connection) {
			var out = connection.getOut();
			out.write(ProtocolConstants.UPLOAD_REQUEST);
			out.writeUTF(path.toString());
			var fileType = fileSystemBridge.getFileType(path);
			out.writeFileType(fileType);
			if (fileType == FileType.NORMAL) {
				try (var fileIn = fileSystemBridge.readFile(path)) {
					fileIn.transferTo(out);
				}
			}
			return null;
		}
	}

}
