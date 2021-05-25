package fr.wonder.commons.utils;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * Utility class to parse launch arguments // FIX rework pretty much everything
 */
public class ArgumentParser {
	
	public static final String TRUE_VALUE = "true";
	public static final String FALSE_VALUE = "false";
	
	private final Map<String, String> args;
	
	public ArgumentParser(String[] args) {
		this.args = parseArgs(args);
	}
	
	public static Map<String, String> parseArgs(String[] args) {
		Map<String, String> arguments = new HashMap<>();
		for(String a : args) {
			if(a.isBlank())
				continue;
			
			int dot = a.indexOf(':');
			if(dot == -1)
				arguments.put(a, null);
			else
				arguments.put(a.substring(0, dot), a.substring(dot+1));
		}
		return arguments;
	}
	
	public boolean isSet(String arg) {
		return args.containsKey(arg);
	}
	
	public boolean isTrue(String arg) {
		return TRUE_VALUE.equals(args.get(arg));
	}
	
	public boolean isFalse(String arg) {
		return FALSE_VALUE.equals(args.get(arg));
	}
	
	public String getString(String arg) {
		return args.get(arg);
	}
	
	public File getFile(String arg) {
		String path = getString(arg);
		return path == null ? null : new File(path);
	}

	public int getInt(String arg) throws NumberFormatException {
		return Integer.parseInt(getString(arg));
	}
	
}
