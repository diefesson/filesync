package com.diefesson.filesync.task;

import java.io.IOException;
import java.util.concurrent.Callable;

import com.diefesson.filesync.file.FileStructure;
import com.diefesson.filesync.io.SyncConnection;
import com.diefesson.filesync.server.ProtocolConstants;

public class ListTask implements Callable<FileStructure> {
	
	private final SyncConnection syncConnection;
	
	public ListTask(SyncConnection syncConnection) {
		this.syncConnection = syncConnection;
	}
	
	@Override
	public FileStructure call() throws IOException {
		try(syncConnection){
			var in = syncConnection.getIn();
			var out = syncConnection.getOut();
			out.write(ProtocolConstants.LIST_FILES_REQUEST);
			return in.readFileStructure();
		}
	}

}
