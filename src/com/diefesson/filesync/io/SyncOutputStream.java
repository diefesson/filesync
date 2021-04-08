package com.diefesson.filesync.io;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import com.diefesson.filesync.file.FileStructure;
import com.diefesson.filesync.file.VirtualFile;

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
		for(var vf : fileStructure) {
			writeVirtualFile(vf);
		}
		writeByte(-1);
	}
	
	private void writeVirtualFile(VirtualFile virtualFile) throws IOException {
		write(virtualFile.getType().id);
		writeUTF(virtualFile.toString());
		for(var vf : virtualFile) {
			writeVirtualFile(vf);
		}
	}

}
