package com.diefesson.filesync.file;

import java.nio.file.Path;

public class Diff {
	
	private final Path path;
	private final FileType fileType;
	private final DiffType diffType;
	
	public Diff(Path path, FileType fileType, DiffType diffType) {
		this.path = path;
		this.fileType = fileType;
		this.diffType = diffType;
	}

	public Path getPath() {
		return path;
	}
	
	public FileType getFileType() {
		return fileType;
	}

	public DiffType getDiffType() {
		return diffType;
	}

}
