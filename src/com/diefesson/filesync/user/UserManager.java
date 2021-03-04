package com.diefesson.filesync.user;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Manages the credentials of the current user and the users that can access
 * the application
 * @author Diefesson de Sousa Silva
 *
 */
public class UserManager {

	private String username, password;
	private final Map<String, String> users;
	private static final String ANONYMOUS_USERNAME = "anonymous";
	private static final String ANONYMOUS_PASSWORD = "anonymous";
	private static final String DEFAULT_SAVE_PATH = "./users.txt";

	public UserManager() {
		this(ANONYMOUS_USERNAME, ANONYMOUS_PASSWORD);
	}

	public UserManager(String username, String password) {
		this.username = username;
		this.password = password;
		users = new HashMap<>();
		users.put(ANONYMOUS_USERNAME, ANONYMOUS_PASSWORD);
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public void goAnonymous() {
		username = ANONYMOUS_USERNAME;
		password = ANONYMOUS_PASSWORD;
	}

	public void addUser(String username, String password) {
		users.put(username, password);
	}

	public void removeUser(String username) {
		users.remove(username);
	}

	public boolean hasUser(String username) {
		return users.containsKey(username);
	}

	public Set<String> getUsernames() {
		return users.keySet();
	}

	public boolean verify(String username, String password) {
		return password.equals(users.get(username));
	}

	public void loadFromFile(String path) throws IOException {
		path = (path != null) ? path : DEFAULT_SAVE_PATH;
		try (var in = new BufferedReader(new FileReader(path))) {
			users.clear();
			var iterator = in.lines().map((it) -> {
				return it.split(" ");
			}).filter((it) -> {
				return it.length == 2;
			}).iterator();
			if (iterator.hasNext()) {
				var n = iterator.next();
				username = n[0];
				password = n[1];
			}
			iterator.forEachRemaining((it) -> {
				addUser(it[0], it[1]);
			});
		}
	}

	public void saveToFile(String path) throws IOException {
		path = (path != null) ? path : DEFAULT_SAVE_PATH;
		try (var out = new BufferedWriter(new FileWriter(path))) {
			out.write("%s %s\n".formatted(username, password));
			for (var kv : users.entrySet()) {
				out.write("%s %s\n".formatted(kv.getKey(), kv.getValue()));
			}
		}
	}

}
