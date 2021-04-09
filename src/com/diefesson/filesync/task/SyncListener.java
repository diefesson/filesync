package com.diefesson.filesync.task;

import com.diefesson.filesync.file.DiffSet;
import com.diefesson.filesync.file.FileStructure;

public interface SyncListener {
	
	void onRemoteListFetch(FileStructure remote);
	
	void onDiff(DiffSet diffSet);
	
	void onProgress(int completed, int total);
	
	void onError(Exception e);

}
