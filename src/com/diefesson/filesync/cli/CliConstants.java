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
	public static final String SYNC = "sync";
	public static final String RESCAN = "rescan";
	public static final String LIST_FILES = "list_files";
	public static final String LIST_REMOTE_FILES = "list_remote_files";
	public static final String LIST_DIFFS = "list_diffs";

	// User management commands
	public static final String ADD_USER = "add_user";
	public static final String LIST_USERS = "list_users";
	public static final String REMOVE_USER = "remove_user";

	// Authentication commands
	public static final String AUTH = "auth";
	public static final String GO_ANONYMOUS = "go_anonymous";

	// Credential persistence
	public static final String SAVE_PREFS = "save_prefs";
	public static final String LOAD_PREFS = "load_prefs";

	// Exit command
	public static final String EXIT = "exit";
}
