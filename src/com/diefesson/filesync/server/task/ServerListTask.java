package com.diefesson.filesync.server.task;

import java.io.IOException;

import com.diefesson.filesync.file.FileSystemBridge;
import com.diefesson.filesync.io.SyncConnection;

public class ServerListTask implements Runnable{

	private final SyncConnection syncConnection;
	private final FileSystemBridge fileSystemBridge;
	
	public ServerListTask(SyncConnection syncConnection, FileSystemBridge fileSystemBridge) {
		this.syncConnection = syncConnection;
		this.fileSystemBridge = fileSystemBridge;
	}
	
	@Override
	public void run() {
		try(syncConnection){
			var out = syncConnection.getOut();
			var fileStructure = fileSystemBridge.getFileStructure();
			out.writeFileStructure(fileStructure);
		} catch (IOException e) {
			System.err.println("Error while serving list");
			e.printStackTrace();
		}
	}
	
}
