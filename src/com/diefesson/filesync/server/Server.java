package com.diefesson.filesync.server;

import java.io.IOException;
import java.net.Inet4Address;
import java.net.ServerSocket;

import com.diefesson.filesync.io.SyncConnection;

/**
 * 
 * @author Diefesson de Sousa Silva
 *
 */
public class Server implements Runnable {

	private ServerSocket serverSocket;
	private boolean run = false;
	private OnConnectListener onConnect;

	public void setOnConnect(OnConnectListener onConnect) {
		this.onConnect = onConnect;
	}

	public void start(String address, int port) throws IOException {
		if (run == true)
			return;
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

					var s = serverSocket.accept();
					onConnect.onConnect(new SyncConnection(s));
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
