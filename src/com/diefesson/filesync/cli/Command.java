package com.diefesson.filesync.cli;

/**
 * 
 * @author Diefesson de Sousa Silva
 *
 */
public class Command {
	private String name;
	private String[] args;

	public Command(String name, String[] args) {
		this.name = name;
		this.args = args;
	}

	public String getName() {
		return name;
	}

	public String[] getArgs() {
		return args;
	}

	public String getArg(int index) {
		return args[index];
	}

	public boolean is(String name) {
		return this.name.equals(name);
	}

	public int argCount() {
		return args.length;
	}

	public static Command parse(String line) {
		var split = line.split(" ");
		var name = split[0];
		var args = new String[split.length - 1];
		for (int i = 0; i < args.length; i++) {
			args[i] = split[i + 1];
		}
		return new Command(name, args);
	}

}