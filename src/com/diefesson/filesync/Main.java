package com.diefesson.filesync;

import com.diefesson.filesync.file.FileScanner;

/**
 * 
 * @author Diefesson de Sousa Silva
 *
 */
public class Main {
	public static void main(String[] args) {
		var fs = new FileScanner("c:/trabalho/musicas");
		fs.setOnFile((path) -> {
			System.out.println(path);
		});
		fs.run();
	}
}
