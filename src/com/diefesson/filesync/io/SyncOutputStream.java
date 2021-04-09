package com.diefesson.filesync.io;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import com.diefesson.filesync.file.FileStructure;
import com.diefesson.filesync.file.FileType;

/**
 * 
 * @author Diefesson de Sousa Silva
 *
 */
public class SyncOutputStream extends DataOutputStream {

	public SyncOutputStream(OutputStream out) {
		super(out);
	}
	
	public void writeFileStructure(FileStructure fileStructure) throws IOException{
		var vfs = fileStructure.recurse().iterator();
		while(vfs.hasNext()) {
			var vf = vfs.next();
			writeByte(vf.getType().id);
			writeUTF(vf.getPath().toString());
		}
		writeByte(-1);
	}
	
	public void writeFileType(FileType fileType) throws IOException{
		writeByte(fileType.id);
	}

}
