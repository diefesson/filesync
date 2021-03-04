package com.diefesson.filesync.server;

import java.io.IOException;
import java.net.Inet4Address;
import java.net.ServerSocket;

import com.diefesson.filesync.io.ConnectionAuthenticator;
import com.diefesson.filesync.io.SyncConnection;

/**
 * 
 * @author Diefesson de Sousa Silva
 *
 */
public class Server implements Runnable {

	private final ConnectionAuthenticator authenticator;
	private ServerSocket serverSocket;
	private boolean run = false;
	private OnConnectListener onConnect;

	public Server(ConnectionAuthenticator authenticator) {
		this.authenticator = authenticator;
	}

	public void setOnConnect(OnConnectListener onConnect) {
		this.onConnect = onConnect;
	}

	public void start(String address, int port) throws IOException {
		if (run == true)
			throw new IllegalStateException("Server already is running");
		serverSocket = new ServerSocket(port, 0, Inet4Address.getByName(address));
		new Thread(this).start();
	}

	public void stop() {
		run = false;
	}

	@Override
	public void run() {
		run = true;
		while (run) {
			if (onConnect != null) {
				try {
					var connection = new SyncConnection(serverSocket.accept());
					if (authenticator.verify(connection))
						onConnect.onConnect(connection);
					else
						connection.close();
				} catch (IOException e) {
					var address = serverSocket.getInetAddress().getHostAddress();
					var port = serverSocket.getLocalPort();
					System.err.print("Server %s %d: could not accept connection".formatted(address, port));
					e.printStackTrace();
				}
			}
		}
	}

	public interface OnConnectListener {
		void onConnect(SyncConnection connection);
	}

}
