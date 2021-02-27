package com.diefesson.filesync.io;

import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;

/**
 * 
 * @author Diefesson de Sousa Silva
 *
 */
public class SyncOutputStream extends DataOutputStream {

	public SyncOutputStream(OutputStream out) {
		super(out);
	}

	/**
	 * Writes from a file to this stream
	 * 
	 * @param path
	 * @throws IOException
	 */
	public void writeFromFile(String path) throws IOException {
		try (var in = new FileInputStream(path)) {
			var buffer = new byte[1024];
			int readed;
			while ((readed = in.read(buffer)) != -1) {
				write(buffer, 0, readed);
			}
		}
	}

}
