package com.diefesson.filesync.io;

import java.io.DataInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * 
 * @author Diefesson de Sousa Silva
 *
 */
public class SyncInputStream extends DataInputStream{
	
	public SyncInputStream(InputStream in) {
		super(in);
	}
	
	/**
	 * Reads from this stream to a file
	 * @param path
	 * @throws IOException
	 */
	public void readToFile(String path) throws IOException{
		try(var out = new FileOutputStream(path)){
			var buffer = new byte[1024];
			int readed;
			do {
				readed = read(buffer);
				out.write(buffer, 0 , readed);
			} while(readed != -1);
		}
	}
	
}