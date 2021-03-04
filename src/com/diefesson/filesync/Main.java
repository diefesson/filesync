package com.diefesson.filesync;

import java.io.IOException;
import java.nio.file.Paths;

import com.diefesson.filesync.cli.Cli;
import com.diefesson.filesync.file.AcessSynchronizer;
import com.diefesson.filesync.user.UserManager;

/**
 * 
 * @author Diefesson de Sousa Silva
 *
 */
public class Main {
	public static void main(String[] args) {
		var root = (args.length >= 1) ? args[0] : "./";
		var synchronizer = new AcessSynchronizer(Paths.get(root, "files").toString());
		var userManager = new UserManager();
		try {
			userManager.loadFromFile(Paths.get(root, "users.txt").toString());
		} catch (IOException e) {
			System.out.println("No users.txt found");
		}
		var app = new App(synchronizer, userManager);
		new Cli(app).run();
	}
}
