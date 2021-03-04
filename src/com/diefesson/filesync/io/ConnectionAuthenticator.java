package com.diefesson.filesync.io;

import java.io.IOException;

import com.diefesson.filesync.user.UserManager;

/**
 * Uses a {@link #userManager} to realize the connection handshake
 * 
 * @author Diefesson de Sousa Silva
 *
 */
public class ConnectionAuthenticator {

	private final UserManager userManager;

	public ConnectionAuthenticator(UserManager userManager) {
		this.userManager = userManager;
	}

	public boolean authenticate(SyncConnection connection) throws IOException {
		var in = connection.getIn();
		var out = connection.getOut();
		out.writeUTF(userManager.getUsername());
		out.writeUTF(userManager.getPassword());
		var allowed = in.readBoolean();
		return allowed;
	}

	public boolean verify(SyncConnection connection) throws IOException {
		var in = connection.getIn();
		var out = connection.getOut();
		var username = in.readUTF();
		var password = in.readUTF();
		var allowed = userManager.verify(username, password);
		out.writeBoolean(allowed);
		return allowed;
	}

}
