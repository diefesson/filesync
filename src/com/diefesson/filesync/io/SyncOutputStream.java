package com.diefesson.filesync.io;

import java.io.DataOutputStream;
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

}
