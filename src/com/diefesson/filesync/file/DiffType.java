package com.diefesson.filesync.file;

public enum DiffType {
	LOCAL(0), REMOTE(1), CONFLICT(2);
	
	public final int id;
	
	private DiffType(int id) {
		this.id = id;
	}
	
	public static final DiffType fromId(int id) {
		for(var dt : values()) {
			if(dt.id == id) {
				return dt;
			}
		}
		throw new IllegalArgumentException("Unknow DiffType id: " + id);
	}

}
