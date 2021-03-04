package com.diefesson.filesync;

import com.diefesson.filesync.cli.Cli;

/**
 * 
 * @author Diefesson de Sousa Silva
 *
 */
public class Main {
	public static void main(String[] args) {
		var root = (args.length > 0) ? args[0] : "C:/teste";
		var app = new App(root);
		app.scan();
		new Cli(app).run();
	}
}
