package com.diefesson.filesync;

import java.util.Scanner;

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

	@Override
	public void run() {
		while (true) {
			System.out.print("> ");
			var c = nextCommand();
			if(c.is("listen")) {
				app.listen(Integer.parseInt(c.getArg(0)));
				System.out.println("Server started");
				continue;
			}
			if(c.is("unlisten")) {
				app.unlisten(Integer.parseInt(c.getArg(0)));
				System.out.println("Server stopped");
				continue;
			}
			if(c.is("ports")) {
				var ports = app.getPorts();
				System.out.print("Ports: ");
				if(ports.size() == 0)
					System.out.print("no ports");
				for(var p : ports) {
					System.out.print(p);
					System.out.print(" ");
				}
				System.out.println();
			}
		}
	}

	private Command nextCommand() {
		return Command.parse(scanner.nextLine());
	}

	private static class Command {
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

}
