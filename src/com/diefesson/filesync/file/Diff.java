package com.diefesson.filesync.file;

import java.nio.file.Path;

public class Diff {
	
	private final Path path;
	private final DiffType type;
	
	public Diff(Path path, DiffType type) {
		this.path = path;
		this.type = type;
	}

	public Path getPath() {
		return path;
	}

	public DiffType getType() {
		return type;
	}

}
