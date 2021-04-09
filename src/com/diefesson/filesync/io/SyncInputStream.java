package com.diefesson.filesync.io;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;

import com.diefesson.filesync.file.FileStructure;
import com.diefesson.filesync.file.FileType;

/**
 * 
 * @author Diefesson de Sousa Silva
 *
 */
public class SyncInputStream extends DataInputStream {

	public SyncInputStream(InputStream in) {
		super(in);
	}

	public FileStructure readFileStructure() throws IOException {
		var fileStructure = new FileStructure();
		var typeId = readByte();
		while (typeId != -1) {
			var path = Path.of(readUTF());
			fileStructure.add(path, FileType.fromId(typeId));
			typeId = readByte();
		}
		return fileStructure;
	}

	public FileType readFileType() throws IOException {
		try {
			return FileType.fromId(readByte());
		} catch (IllegalArgumentException e) {
			throw new IOException(e);
		}
	}

}