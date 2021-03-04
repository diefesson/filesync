package com.diefesson.filesync.cli;

/**
 * 
 * @author Diefesson de Sousa Silva
 *
 */
public class CliConstants {
	// Server management commands
	public static final String LISTEN = "listen";
	public static final String PORTS = "ports";
	public static final String UNLISTEN = "unlisten";

	// Sync commands
	public static final String DOWNLOAD = "download";
	public static final String UPLOAD = "upload";
	public static final String SYNC = "list";

	// User management commands
	public static final String ADD_USER = "add_user";
	public static final String LIST_USERS = "list_users";
	public static final String REMOVE_USER = "remove_user";

	// Authentication commands
	public static final String LOGIN = "login";
	public static final String GO_ANONYMOUS = "go_anonymous";

	// Credential persistence
	public static final String SAVE_CREDENTIALS = "save_credentials";
	public static final String LOAD_CREDENTIALS = "load_credentials";

	// Exit command
	public static final String EXIT = "exit";
}
