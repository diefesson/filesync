package com.diefesson.filesync.file;

/**
 * 
 * @author Diefesson de Sousa Silva
 *
 */
public enum FileType {
	NORMAL(0), FOLDER(1);
	
	public final int id;
	
	private FileType(int id) {
		this.id = id;
	}
	
	public static FileType fromId(int id) {
		for(var ft : values()) {
			if(ft.id == id)
				return ft;
		}
		throw new IllegalArgumentException("Invalid ID: " + id);
	}
}
