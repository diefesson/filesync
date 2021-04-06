package com.diefesson.filesync;

import java.io.IOException;
import java.nio.file.Path;

import com.diefesson.filesync.cli.Cli;

/**
 * 
 * @author Diefesson de Sousa Silva
 *
 */
public class Main {
	public static void main(String[] args) {
		var root = (args.length >= 1) ? Path.of(args[0]) : Path.of("./");
		try {
			createBasicFiles(root);
			var app = new App(root);
			new Cli(app).run();
		} catch (IOException e) {
			System.out.println("IOException during initialization");
			e.printStackTrace();
		}
	}

	private static void createBasicFiles(Path root) throws IOException {
		root.resolve("files").toFile().mkdir();
		root.resolve("users.txt").toFile().createNewFile();
	}
}
