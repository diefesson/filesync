package com.diefesson.filesync.cli;

import java.util.Scanner;

import com.diefesson.filesync.App;

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
			if (c.argCount() != 1)
				throw new IllegalArgumentException();
			var port = Integer.parseInt(c.getArg(0));
			app.listen(port);
		} catch (IllegalArgumentException e) {
			System.out.println("Usage: listen <port>");
		}
	}

	private void handleUnlisten(Command c) {
		try {
			if (c.argCount() != 1)
				throw new IllegalArgumentException();
			var port = Integer.parseInt(c.getArg(0));
			app.unlisten(port);
		} catch (IllegalArgumentException e) {
			System.out.println("Usage: unlisten <port>");
		}
	}

	private void handlePorts(Command c) {
		try {
			if (c.argCount() != 0)
				throw new IllegalArgumentException();
			for (var p : app.getPorts())
				System.out.println("port " + p);
		} catch (IllegalArgumentException e) {
			System.out.println("Usage: ports");
		}
	}

	private void handleDownload(Command c) {
		try {
			if (c.argCount() != 3)
				throw new IllegalArgumentException();
			var address = c.getArg(0);
			var port = Integer.parseInt(c.getArg(1));
			var path = c.getArg(2);
			app.download(address, port, path);
		} catch (IllegalArgumentException e) {
			System.out.println("Usage: listen <port>");
		}
	}

	private void handleUpload(Command c) {
		try {
			if (c.argCount() != 3)
				throw new IllegalArgumentException();
			var address = c.getArg(0);
			var port = Integer.parseInt(c.getArg(1));
			var path = c.getArg(2);
			app.upload(address, port, path);
		} catch (IllegalArgumentException e) {
			System.out.println("Usage: listen <port>");
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
			case CliConstants.EXIT -> System.exit(0);
			default -> System.out.println("Unknow comand: " + c.getName());
			}
		}
	}

	private Command nextCommand() {
		return Command.parse(scanner.nextLine());
	}

}
