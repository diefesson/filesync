package com.diefesson.filesync;

/**
 * 
 * @author Diefesson de Sousa Silva
 *
 */
public class Main {
	public static void main(String[] args) {
		var root = (args.length > 0) ? args[0] : "C:/teste";
		new Cli(root).run();
	}
}
