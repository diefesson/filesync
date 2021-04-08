package com.diefesson.filesync.cli;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Scanner;
import java.util.Set;
import java.util.concurrent.ExecutionException;

import com.diefesson.filesync.App;
import com.diefesson.filesync.file.Diff;
import com.diefesson.filesync.file.FileStructure;
import com.diefesson.filesync.file.VirtualFile;
import com.diefesson.filesync.io.AuthException;

/**
 * 
 * @author Diefesson de Sousa Silva
 *
 */
public class Cli implements Runnable {

	private final App app;
	private final Scanner scanner;

	public Cli(App app) {
		this.app = app;
		scanner = new Scanner(System.in);
	}

	private void handleListen(Command c) {
		try {
			c.ensureArgCount(1);
			var port = c.getIntArg(0);
			app.listen(port);
		} catch (CommandException e) {
			System.out.println("Usage: listen <port>");
		}
	}

	private void handleUnlisten(Command c) {
		try {
			c.ensureArgCount(1);
			var port = c.getIntArg(0);
			app.unlisten(port);
		} catch (CommandException e) {
			System.out.println("Usage: unlisten <port>");
		}
	}

	private void handlePorts(Command c) {
		try {
			c.ensureArgCount(0);
			for (var p : app.getPorts())
				System.out.println(p);
		} catch (CommandException e) {
			System.out.println("Usage: ports");
		}
	}

	private void handleDownload(Command c) {
		try {
			c.ensureArgCount(3);
			var address = c.getArg(0);
			var port = c.getIntArg(1);
			var path = c.getArg(2);
			app.download(address, port, Path.of(path)).get();
		} catch (CommandException e) {
			System.out.println("Usage: download <address> <port> <path>");
		} catch (AuthException e) {
			System.out.println("Authentication error");
		} catch (IOException | ExecutionException | InterruptedException e) {
			System.out.println("Download error: " + e.getMessage());
			e.printStackTrace(System.out);
		}
	}

	private void handleUpload(Command c) {
		try {
			c.ensureArgCount(3);
			var address = c.getArg(0);
			var port = c.getIntArg(1);
			var path = c.getArg(2);
			app.upload(address, port, Path.of(path)).get();
		} catch (CommandException e) {
			System.out.println("Usage: upload <address> <port> <path>");
		} catch (AuthException e) {
			System.out.println("Authentication error");
		} catch (IOException | ExecutionException | InterruptedException e) {
			System.out.println("Upload error: " + e.getMessage());
			e.printStackTrace(System.out);
		}
	}

	private void handleAddUser(Command c) {
		try {
			c.ensureArgCount(2);
			var username = c.getArg(0);
			var password = c.getArg(1);
			app.getUserManager().addUser(username, password);
		} catch (CommandException e) {
			System.out.println("Usage: add_user <username> <password>");
		}
	}

	private void handleListUsers(Command c) {
		try {
			c.ensureArgCount(0);
			for (var username : app.getUserManager().getUsernames())
				System.out.println(username);
		} catch (CommandException e) {
			System.out.println("Usage: list_users");
		}
	}

	private void handleRemoveUser(Command c) {
		try {
			c.ensureArgCount(1);
			app.getUserManager().removeUser(c.getArg(0));
		} catch (CommandException e) {
			System.out.println("Usage: remove_user <username>");
		}
	}

	private void handleAuth(Command c) {
		try {
			c.ensureArgCount(2);
			app.getUserManager().setUsername(c.getArg(0));
			app.getUserManager().setPassword(c.getArg(1));
		} catch (CommandException e) {
			System.out.println("Usage: auth <username> <password>");
		}
	}

	private void handleGoAnonymous(Command c) {
		try {
			c.ensureArgCount(0);
			app.getUserManager().goAnonymous();
		} catch (CommandException e) {
			System.out.println("Usage: go_anonymous");
		}
	}

	private void handleSavePrefs(Command c) {
		try {
			c.ensureArgCount(0);
			app.getUserManager().savePrefs();
		} catch (CommandException e) {
			System.out.println("Usage: save_prefs");
		} catch (IOException e) {
			System.out.println("Save error: " + e.getMessage());
			e.printStackTrace(System.out);
		}
	}

	private void handleLoadPrefs(Command c) {
		try {
			c.ensureArgCount(0);
			app.getUserManager().loadPrefs();;
		} catch (CommandException e) {
			System.out.println("Usage: load_prefs");
		} catch (IOException e) {
			System.out.println("Load error: " + e.getMessage());
		}
	}

	private void handleListFiles(Command c) {
		try {
			c.ensureArgCount(0);
			var fileStructure = app.getFileSystemBridge().getFileStructure();
			printFileStructure(fileStructure);
		} catch (CommandException e) {
			System.out.println("usage: list_files");
		}
	}

	private void handleListRemoteFiles(Command c) {
		try {
			c.ensureArgCount(2);
			var address = c.getArg(0);
			var port = c.getIntArg(1);
			var future = app.listRemote(address, port);
			printFileStructure(future.get());
		} catch (CommandException e) {
			System.out.println("usage: list_remote_files <address> <port>");
		} catch (AuthException e) {
			System.out.println("Authentication error");
		} catch (IOException | ExecutionException | InterruptedException e) {
			System.out.println("List error: " + e.getMessage());
			e.printStackTrace(System.out);
		}
	}

	private void handleRescan(Command c) {
		try {
			c.ensureArgCount(0);
			app.getFileSystemBridge().scan();
		} catch (CommandException e) {
			System.out.println("usage: list_files");
		} catch (IOException e) {
			System.out.println("Rescan error: " + e.getMessage());
			e.printStackTrace(System.out);
		}
	}

	private void handleListDiffs(Command c) {
		try {
			c.ensureArgCount(2);
			var future = app.listRemote(c.getArg(0), c.getIntArg(1));
			var remoteFS = future.get();
			var localFS = app.getFileSystemBridge().getFileStructure();
			var diffs = localFS.compare(remoteFS);
			printDiffs(diffs);
		} catch (CommandException e) {
			System.out.println("usage: list_diffs: <adress> <port>");
		} catch (AuthException e) {
			System.out.println("Authentication error");
		} catch (IOException | ExecutionException | InterruptedException e) {
			System.out.println("List diffs error: " + e.getMessage());
			e.printStackTrace(System.out);
		}
	}

	private void handleExit() {
		System.out.println("Bye bye");
		System.exit(0);
	}

	private void printFileStructure(FileStructure fileStructure) {
		for (var vf : fileStructure) {
			printVirtualFile(vf);
		}
	}

	private void printVirtualFile(VirtualFile virtualFile) {
		System.out.println(virtualFile.getType() + " " + virtualFile.getPath());
		for (var vf : virtualFile) {
			printVirtualFile(vf);
		}
	}

	private void printDiffs(Set<Diff> diffs) {
		for (var d : diffs) {
			System.out.println(d.getType() + " " + d.getPath());
		}
	}

	@Override
	public void run() {
		while (true) {
			System.out.print("> ");
			var c = nextCommand();
			switch (c.getName()) {
			case CliConstants.LISTEN -> handleListen(c);
			case CliConstants.UNLISTEN -> handleUnlisten(c);
			case CliConstants.PORTS -> handlePorts(c);
			case CliConstants.DOWNLOAD -> handleDownload(c);
			case CliConstants.UPLOAD -> handleUpload(c);
			case CliConstants.ADD_USER -> handleAddUser(c);
			case CliConstants.LIST_USERS -> handleListUsers(c);
			case CliConstants.REMOVE_USER -> handleRemoveUser(c);
			case CliConstants.AUTH -> handleAuth(c);
			case CliConstants.GO_ANONYMOUS -> handleGoAnonymous(c);
			case CliConstants.SAVE_PREFS -> handleSavePrefs(c);
			case CliConstants.LOAD_PREFS -> handleLoadPrefs(c);
			case CliConstants.LIST_FILES -> handleListFiles(c);
			case CliConstants.LIST_REMOTE_FILES -> handleListRemoteFiles(c);
			case CliConstants.LIST_DIFFS -> handleListDiffs(c);
			case CliConstants.RESCAN -> handleRescan(c);
			case CliConstants.EXIT -> handleExit();
			case "" -> {
			}
			default -> System.out.println("Unknow comand: " + c.getName());
			}
		}
	}

	private Command nextCommand() {
		return Command.parse(scanner.nextLine());
	}

}
