package com.diefesson.filesync.file;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * 
 * @author Diefesson de Sousa Silva
 *
 */
public class FileSystemBridge {

	private final Path root;
	private final FileStructure fileStructure = new FileStructure();
	// private final AcessSynchronizer acessSynchronizer = new AcessSynchronizer();

	public FileSystemBridge(Path root) throws IOException {
		this.root = root;
		scan();
	}

	public void scan() throws IOException {
		fileStructure.reset();
		Files.walk(root).skip(1).forEach((p) -> {
			var type = Files.isDirectory(p) ? FileType.FOLDER : FileType.NORMAL;
			fileStructure.add(root.relativize(p), type);
		});
	}

	public FileStructure getFileStructure() {
		return fileStructure;
	}

	public void createFolder(Path path) throws IOException {
		var abs = root.resolve(path);
		Files.createDirectory(abs);
		fileStructure.add(path, FileType.FOLDER);
	}

	public OutputStream writeFile(Path path) throws IOException {
		var abs = root.resolve(path);
		abs.toFile().createNewFile();
		fileStructure.add(path, FileType.NORMAL);
		return new FileOutputStream(abs.toFile());
	}

	public InputStream readFile(Path path) throws IOException {
		var abs = root.resolve(path);
		return new FileInputStream(abs.toFile());
	}

	public void deleteFile(Path path) throws IOException {
		var abs = root.resolve(path);
		fileStructure.remove(path);
		Files.delete(abs);
	}

	public long getSize(Path path) throws IOException {
		var abs = root.resolve(path);
		return Files.size(abs);
	}
	
	public FileType getFileType(Path path) {
		return fileStructure.get(path).getType();
	}
}
