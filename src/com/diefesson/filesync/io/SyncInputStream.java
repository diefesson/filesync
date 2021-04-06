package com.diefesson.filesync.io;

import java.io.DataInputStream;
import java.io.InputStream;

/**
 * 
 * @author Diefesson de Sousa Silva
 *
 */
public class SyncInputStream extends DataInputStream {

	public SyncInputStream(InputStream in) {
		super(in);
	}

}