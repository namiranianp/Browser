package outsideCode;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

/**
 * Parses and stores an array of argument into flag, value pairs for easy access
 * later. Useful to parse command-line arguments.
 */
class ArgumentParser {

	/**
	 * Stores parsed flag, value pairs.
	 */
	private final Map<String, String> argumentMap;

	/**
	 * Creates a new and empty argument parser.
	 */
	public ArgumentParser() {
		argumentMap = new HashMap<String, String>();
	}

	/**
	 * Creates a new argument parser and immediately parses the passed
	 * arguments.
	 * 
	 * @param args
	 *            arguments to parse into flag, value pairs
	 * @see #parseArguments(String[])
	 */
	public ArgumentParser(String[] args) {
		this();
		parseArguments(args);
	}

	/**
	 * Parses the array of arguments into flag, value pairs. If an argument is a
	 * flag, tests whether the next argument is a value. If it is a value, then
	 * stores the flag, value pair. Otherwise, it stores the flag with no value.
	 * If the flag appears multiple times with different values, only the last
	 * value will be kept. Values without an associated flag are ignored.
	 * 
	 * @param args
	 *            arguments to parse into flag, value pairs
	 * @see #isFlag(String)
	 * @see #isValue(String)
	 */
	public void parseArguments(String[] args) {
		for (int i = 0; i < args.length; i++) {
			if (isFlag(args[i])) {
				if (i < args.length - 1 && isValue(args[i + 1])) {
					argumentMap.put(args[i], args[i + 1]);
					i++;
				} else {
					argumentMap.put(args[i], null);
				}
			}
		}
	}

	/**
	 * Tests whether the argument is a valid flag, i.e. it is not null, and
	 * after trimming it starts with a dash "-" character followed by at least 1
	 * character.
	 * 
	 * @param arg
	 *            argument to test
	 * @return {@code true} if the argument is a valid flag
	 */
	public static boolean isFlag(String arg) {
		return arg.trim() != null && arg.trim().length() > 1 && arg.trim().charAt(0) == '-';
	}

	/**
	 * Tests whether the argument is a valid value, i.e. it is not null, does
	 * not start with a dash "-" character, and is not empty after trimming.
	 * 
	 * @param arg
	 *            argument to test
	 * @return {@code true} if the argument is a valid value
	 */
	public static boolean isValue(String arg) {
		return !arg.trim().isEmpty() && arg.trim().charAt(0) != '-';
	}

	/**
	 * Goes through the ArrayList of the flag - value pairs and copies all of
	 * the flags into a new ArrayList.
	 * 
	 * @return ArrayList<String> of all flag values
	 */
	public HashSet<String> getKeys() {
		HashSet<String> keys = new HashSet<>();
		keys.addAll(argumentMap.keySet());
		return keys;
	}

	/**
	 * Checks if the flag exists.
	 * 
	 * @param flag
	 *            flag to check for
	 * @return {@code true} if flag exists
	 */
	public boolean hasFlag(String flag) {
		return flag != null && argumentMap.containsKey(flag);
	}

	/**
	 * 
	 * @param flag
	 * @return String containing the argument associated with the given flag
	 */
	public String getKeyValue(String flag) {
		return argumentMap.get(flag);
	}

	/**
	 * 
	 * @param flag
	 * @return Path of the argument paired with the given flag
	 */
	public Path getPath(String flag) {
		return Paths.get(argumentMap.get(flag));
	}

	/**
	 * Checks if the flag exists and has a non-null value.
	 * 
	 * @param flag
	 *            flag whose associated value is to be checked
	 * @return {@code true} if the flag exists and has a non-null value
	 */
	public boolean hasValue(String flag) {
		return flag != null && argumentMap.containsKey(flag.trim()) && argumentMap.get(flag.trim()) != null;
	}

	/**
	 * Returns the value associated with the specified flag. May be {@code null}
	 * if a {@code null} value was stored or if the flag does not exist.
	 * 
	 * @param flag
	 *            flag whose associated value is to be returned
	 * @return value associated with the flag or {@code null} if the flag does
	 *         not exist
	 */
	public String getValue(String flag) {
		return (flag != null && argumentMap.containsKey(flag)) ? argumentMap.get(flag) : null;
	}

	/**
	 * Returns the value for a flag. If the flag is missing or the value is
	 * {@code null}, returns the default value instead.
	 * 
	 * @param flag
	 *            flag whose associated value is to be returned
	 * @param defaultValue
	 *            the default mapping of the flag
	 * @return value of flag or {@code defaultValue} if the flag is missing or
	 *         the value is {@code null}
	 */
	public String getValue(String flag, String defaultValue) {
		return (flag != null && hasValue(flag)) ? argumentMap.get(flag) : defaultValue;
	}

	/**
	 * Returns the value for a flag as an integer. If the flag or value is
	 * missing or if the value cannot be converted to an integer, returns the
	 * default value instead.
	 * 
	 * @param flag
	 *            flag whose associated value is to be returned
	 * @param defaultValue
	 *            the default mapping of the flag
	 * @return value of flag as an integer or {@code defaultValue} if the value
	 *         cannot be returned as an integer
	 */
	public int getValue(String flag, int defaultValue) {
		if (flag != null && hasValue(flag)) {
			try {
				return Integer.parseInt(argumentMap.get(flag));
			} catch (IllegalArgumentException e) {
				System.err.println("An error occured, the value assigned to the flag " + flag + " is not an integer");
			}
		}
		return defaultValue;
	}

	@Override
	public String toString() {
		return argumentMap.toString();
	}
}