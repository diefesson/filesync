package com.diefesson.filesync.io;

import java.io.IOException;
import java.net.Socket;

/**
 * 
 * @author Diefesson de Sousa Silva
 *
 */
public class SyncConnection implements AutoCloseable {

	private final Socket socket;
	private final SyncInputStream in;
	private final SyncOutputStream out;

	public SyncConnection(String address, int port) throws IOException {
		this(new Socket(address, port));
	}

	public SyncConnection(Socket socket) throws IOException {
		this.socket = socket;
		this.in = new SyncInputStream(socket.getInputStream());
		this.out = new SyncOutputStream(socket.getOutputStream());
	}

	public SyncInputStream getIn() {
		return in;
	}

	public SyncOutputStream getOut() {
		return out;
	}

	/**
	 * Closes the underlying socket and streams
	 * 
	 * @throws IOException
	 */
	@Override
	public void close() throws IOException {
		try (socket; in; out) {

		}
	}

}
