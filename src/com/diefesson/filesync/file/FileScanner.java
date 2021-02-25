package com.diefesson.filesync.file;

import java.io.File;

/**
 * 
 * @author Diefesson de Sousa Silva
 *
 */
public class FileScanner implements Runnable {

	private final File root;
	private OnFileListener onFile;
	private OnEndListener onEnd;

	public FileScanner(String root) {
		this.root = new File(root);
	}

	private void scanFile(File file) {
		if (onFile != null) {
			var path = root.toPath().relativize(file.toPath()).toString();
			onFile.onFile(path);
		}
		if (file.isDirectory()) {
			for (var f : file.listFiles()) {
				scanFile(f);
			}
		}
	}

	public void setOnFile(OnFileListener onFile) {
		this.onFile = onFile;
	}

	public void setOnEnd(OnEndListener onEnd) {
		this.onEnd = onEnd;
	}

	@Override
	public void run() {
		scanFile(root);
		if (onEnd != null)
			onEnd.onEnd();
	}

	public interface OnFileListener {
		void onFile(String path);
	}

	public interface OnEndListener {
		void onEnd();
	}

}
