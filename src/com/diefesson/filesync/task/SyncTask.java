package com.diefesson.filesync.task;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;

import com.diefesson.filesync.App;
import com.diefesson.filesync.file.Diff;
import com.diefesson.filesync.file.DiffSet;
import com.diefesson.filesync.file.DiffType;
import com.diefesson.filesync.file.FileStructure;
import com.diefesson.filesync.file.FileType;
import com.diefesson.filesync.io.AuthException;

public class SyncTask implements Runnable {

	private final App app;
	private final SyncListener syncListener;
	private final String address;
	private final int port;

	public SyncTask(App app, String address, int port, SyncListener syncListener) {
		this.app = app;
		this.syncListener = syncListener;
		this.address = address;
		this.port = port;
	}

	@Override
	public void run() {
		try {
			var local = app.getFileSystemBridge().getFileStructure();
			var remote = getRemoteFileStructure();
			syncListener.onRemoteListFetch(remote);
			var diffSet = new DiffSet(local, remote);
			syncListener.onDiff(diffSet);
			var completed = 0;
			for(var f : syncFolders(diffSet)) {
				f.get();
				syncListener.onProgress(++completed, diffSet.size());
			}
			for(var f : syncFiles(diffSet)) {
				f.get();
				syncListener.onProgress(++completed, diffSet.size());
			}
		} catch (Exception e) {
			syncListener.onError(e);
		}

	}

	private FileStructure getRemoteFileStructure() throws IOException, AuthException {
		return new ListTask(app.createConnection(address, port)).call();
	}

	private List<Future<Void>> syncFolders(DiffSet diffSet) throws IOException, AuthException {
		var futures = new ArrayList<Future<Void>>();
		var folders = diffSet.stream().filter((d) -> d.getFileType() == FileType.FOLDER).iterator();
		while(folders.hasNext()) {
			futures.add(syncDiff(folders.next()));
		}
		return futures;
	}

	private List<Future<Void>> syncFiles(DiffSet diffSet) throws IOException, AuthException {
		var futures = new ArrayList<Future<Void>>();
		var files = diffSet.stream().filter((d) -> d.getFileType() == FileType.NORMAL).iterator();
		while(files.hasNext()) {
			futures.add(syncDiff(files.next()));
		}
		return futures;
	}

	private Future<Void> syncDiff(Diff diff) throws IOException, AuthException {
		var connection = app.createConnection(address, port);
		if (diff.getDiffType() == DiffType.LOCAL) {
			return app.getExecutorService()
					.submit(new UploadTask(connection, app.getFileSystemBridge(), diff.getPath()));
		} else {
			return app.getExecutorService()
					.submit(new DownloadTask(connection, app.getFileSystemBridge(), diff.getPath()));
		}
	}

}
