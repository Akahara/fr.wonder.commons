package fr.wonder.commons.files;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import fr.wonder.commons.loggers.Logger;

public final class ConfigFile {
	
	public static final String arrayPrefix = "***";
	private final File file;
	private final ConfigBranch root = new ConfigBranch();
	
	public ConfigFile(File file) throws IOException {
		this.file = file;
		
		String content = FilesUtils.read(file);
		String[] lines = content.split("\n");
		
		ConfigBranch currentBranch = root;
		int currentIndent = 0;
		for(String l : lines) {
			if(l.isBlank())
				continue;
			int dot = l.indexOf(':');
			if(dot == -1)
				continue;
			
			int indent = 0;
			while(l.startsWith("  ", indent*2))
				indent++;
			
			String keyName = l.substring(indent*2, dot);
			String keyValue = l.substring(dot+1).strip();
			
			if(keyName.isBlank() || keyValue.equals("-"))
				continue;
			
			while(indent < currentIndent) {
				currentBranch = currentBranch.parent;
				currentIndent--;
			}
			
			// avoid crashes, may produce unintended behaviors if the file
			// is incorrectly formated
			if(indent > currentIndent+1)
				continue;
			
			if(keyName.equals(arrayPrefix))
				keyName = arrayPrefix + currentBranch.children.size();
			
			if(keyValue.isEmpty()) {
				ConfigBranch branch = new ConfigBranch(currentBranch, keyName);
				currentBranch.put(branch);
				currentBranch = branch;
				currentIndent++;
			} else {
				ConfigValue value = new ConfigValue(currentBranch, keyName, keyValue);
				currentBranch.put(value);
			}
		}
	}
	
	@Override
	public String toString() {
		return root.toString();
	}
	
	/**
	 * Writes the content of this config file to the actual drive file it represents.
	 * @throws IOException if an IO error occurs while writing to the file
	 */
	public void save() throws IOException {
		StringBuilder sb = new StringBuilder();
		writeBranch(sb, root, 0);
		
		FilesUtils.write(file, sb.toString());
	}
	
	/**
	 * Returns the root of the config tree.
	 * @return the root branch
	 */
	public ConfigBranch getRoot() {
		return root;
	}
	
	/**
	 * Returns the config branch denoted by <code>path</code> with the dot <code>'.'</code>
	 * being the path separator. If the branch does not exist (or any of its parents for
	 * that matter), it is created on the fly.<br>
	 * If <code>path</code> is null, the root branch is returned.
	 * @param path the branch path, multiple branches are separated by dots
	 * @return the branch denoted by <code>path</code>
	 * @throws BranchingException if <code>path</code> is an invalid path or 
	 * 			{@link ConfigBranch#getBranch(String)} throws
	 * @see ConfigBranch#getBranch(String)
	 */
	public ConfigBranch getBranch(String path) throws BranchingException {
		if(path == null)
			return root;
		String[] branches = path.split("\\.");
		ConfigBranch branch = root;
		for(int i = 0; i < branches.length; i++) {
			if(branches[i].isBlank())
				throw new BranchingException("Invalid branch sequence \""+path+"\"");
			branch = branch.getBranch(branches[i]);
		}
		return branch;
	}
	
	/**
	 * Writes a complete branch to the string builder <code>sb</code>.
	 * @param sb the string output
	 * @param branch the branch to write
	 * @param indent the recursion level, a branch is indented by the number of its parents
	 */
	private static void writeBranch(StringBuilder sb, ConfigBranch branch, int indent) {
		String space = " ".repeat(indent*2);
		for(ConfigItem entry : branch.entries()) {
			sb.append(space);
			sb.append(entry.name.startsWith(arrayPrefix) ? arrayPrefix : entry.name);
			sb.append(": ");
			if(entry instanceof ConfigBranch) {
				sb.append('\n');
				writeBranch(sb, (ConfigBranch)entry, indent+1);
			} else {
				ConfigValue val = (ConfigValue)entry;
				try {
					sb.append(val.getSerialized());
				} catch (ParsingException e) {
					Logger.getDefault().warn("Unable to serialize a value: " + val.getPath() + " " + e.getMessage());
					sb.append('-');
				}
				sb.append('\n');
			}
		}
	}
	
	/**
	 * Unserialize and returns the value stored at <code>path</code>.
	 * @param <T> The type of the stored value
	 * @param path the branch followed by the key name
	 * @param serial the parser for the value, parsed values are cached so this won't necessarily
	 * 			call {@link ConfigType#unserialize(String)}, note that if the result of this method
	 * 			is modified, it will retroactively modify the result of further calls
	 * @return the unserialized value stored in <code>path</code>
	 * @throws ParsingException if no value has been cached and <code>serial.unserialize</code>
	 * 			throws an exception
	 * @throws BranchingException if <code>path</code> does not denote a valid path
	 * @see ConfigBranch#get(String, ConfigType)
	 */
	public <T> T get(String path, ConfigType<T> serial) throws ParsingException, BranchingException {
		int lastDot = path.lastIndexOf('.');
		if(lastDot == -1) {
			return root.get(path, serial);
		} else {
			String branch = path.substring(0, lastDot);
			String name = path.substring(lastDot+1);
			return getBranch(branch).get(name, serial);
		}
	}
	
	/**
	 * Unserialize and returns the value stored at <code>path</code>.
	 * @param <T> The type of the stored value
	 * @param path the branch followed by the key name
	 * @param serial the parser for the value, parsed values are cached so this won't necessarily
	 * 			call {@link ConfigType#unserialize(String)}, note that if the result of this method
	 * 			is modified, it will retroactively modify the result of further calls
	 * @param defaultValue the returned value if {@link #get(String, ConfigType)} throws
	 * @return the unserialized value stored in <code>path</code>
	 * @throws ParsingException if no value has been cached and <code>serial.unserialize</code>
	 * 			throws an exception
	 * @throws BranchingException if <code>path</code> does not denote a valid branch
	 * @see ConfigBranch#get(String, ConfigType)
	 * @see #getBranch(String)
	 */
	public <T> T get(String path, ConfigType<T> serial, T defaultValue) {
		int lastDot = path.lastIndexOf('.');
		if(lastDot == -1) {
			return root.get(path, serial, defaultValue);
		} else {
			String branch = path.substring(0, lastDot);
			String name = path.substring(lastDot+1);
			try {
				return getBranch(branch).get(name, serial, defaultValue);
			} catch (BranchingException e) {
				return defaultValue;
			}
		}
	}
	
	public <T> List<T> getAll(String path, ConfigType<T> serial) throws ParsingException, BranchingException {
		return getBranch(path).getAll(serial);
	}
	
	public int getInt(String path) throws BranchingException, ParsingException {
		return get(path, ConfigType.INT);
	}
	
	public int[] getInts(String path) throws BranchingException, ParsingException {
		return get(path, ConfigType.INTS);
	}
	
	public float getFloat(String path) throws BranchingException, ParsingException {
		return get(path, ConfigType.FLOAT);
	}
	
	public String getString(String path) throws BranchingException, ParsingException {
		return get(path, ConfigType.STRING);
	}
	
	/**
	 * Sets the value at <code>path</code> to <code>val</code>. If <code>val</code> is
	 * <code>null</code> the current one is removed (if any).
	 * @param <T> the type of the value to set
	 * @param path the path of the value, with dots <code>'.'</code> being path separators
	 * @param serial the serial to be used to save the value as a string
	 * @param val the actual value or null
	 * @throws BranchingException if <code>path</code> does not denote a valid path
	 */
	public <T> void set(String path, ConfigType<T> serial, T val) throws BranchingException {
		int lastDot = path.lastIndexOf('.');
		if(lastDot == -1) {
			root.set(path, serial, val);
		} else {
			String branch = path.substring(0, lastDot);
			String name = path.substring(lastDot+1);
			getBranch(branch).set(name, serial, val);
		}
	}
	
	/**
	 * Sets an array of values at the end of the branch <code>path</code>. All previous values
	 * of the branch (not sub-branches) are cleared.
	 * @param <T> the type of each individual value
	 * @param path the path to the branch to fill
	 * @param serial the serial to be used for each value
	 * @param values the actual values to be set
	 * @throws BranchingException if <code>path</code> does not denote a valid path to a branch
	 */
	public <T> void setArray(String path, ConfigType<T> serial, T[] values) throws BranchingException {
		ConfigBranch branch = getBranch(path);
		branch.clearValues();
		for(int i = 0; i < values.length; i++)
			branch.set(arrayPrefix+i, serial, values[i]);
	}
	
	public void setInt(String path, int val) throws BranchingException {
		set(path, ConfigType.INT, val);
	}
	
	public void setInts(String path, int[] val) throws BranchingException {
		set(path, ConfigType.INTS, val);
	}
	
	public void setFloat(String path, float val) throws BranchingException {
		set(path, ConfigType.FLOAT, val);
	}
	
	public void setString(String path, String val) throws BranchingException {
		set(path, ConfigType.STRING, val);
	}
	
	public abstract static class ConfigItem {
		
		protected final ConfigBranch parent;
		private final String name;
		
		private String path;
		
		/**
		 * Creates a new config item with parent <code>parent</code> and name <code>name</code>
		 * @param parent the parent of this item
		 * @param name the name of this item
		 * @throws NullPointerException if <code>name</code> is null
		 */
		private ConfigItem(ConfigBranch parent, String name) throws NullPointerException {
			this.parent = parent;
			this.name = name;
			if(name == null)
				throw new NullPointerException("Cannot create a config item with null name");
		}
		
		/**
		 * Root constructor, used by {@link ConfigBranch#ConfigBranch()} to create the root
		 * branch of the file. The path of the root branch is set by default to the empty string.
		 */
		private ConfigItem() {
			this.parent = null;
			this.name = null;
			this.path = "";
		}
		
		/**
		 * Returns the path of this config item, the path is computed once on the first call.
		 * The path is defined by <code>path = parent.getPath()+'.'+name</code>, only the root
		 * branch has a predefined path set to the empty string.<br>
		 * Note that an exception is to be made, if this element has the root as parent the first
		 * dot is removed and <code>path = name</code>
		 * @return the path of this config item
		 * @see ConfigBranch#ConfigBranch()
		 */
		public String getPath() {
			if(path != null)
				return path;
			if(parent.parent == null)
				return path = name;
			return path = parent.getPath() + "." + name;
		}
		
	}
	
	public static class ConfigBranch extends ConfigItem {
		
		/** All children of this branch, branches or values */
		private final Map<String, ConfigItem> children = new HashMap<>();
		
		/**
		 * Creates a new config branch with parent <code>parent</code> and name <code>name</code>
		 * @param parent the parent branch of this one
		 * @param name the name of this branch
		 */
		public ConfigBranch(ConfigBranch parent, String name) {
			super(parent, name);
		}
		
		/**
		 * Root branch constructor, called once per config file
		 * @see ConfigItem#ConfigItem()
		 */
		private ConfigBranch() {
			super();
		}
		
		/**
		 * Replaces the existing child with name <code>child.name</code> (if any) by <code>child</code>
		 */
		private void put(ConfigItem child) {
			children.put(child.name, child);
		}
		
		/**
		 * Returns the child branch with name <code>name</code>. If <code>name</code> is a value and not
		 * a branch a {@link BranchingException} will be thrown. If <code>name</code> does not exist it
		 * will be created on the fly.
		 * @param name the name of the branch, <b>path separators <code>'.'</code> are not accounted for</b>
		 * @return the child branch <code>name</code>
		 * @throws BranchingException if <code>name</code> exists and is not a branch
		 */
		public ConfigBranch getBranch(String name) throws BranchingException {
			if(name == null)
				throw new BranchingException("Cannot access null child");
			ConfigItem item = children.get(name);
			if(item == null)
				put(item = new ConfigBranch(this, name));
			if(!(item instanceof ConfigBranch))
				throw new BranchingException("Item " + getPath()+"."+name + " is not a branch");
			return (ConfigBranch) item;
		}
		
		/**
		 * Returns the child value with name <code>name</code>.
		 * @param name the name of the child value
		 * @return the config value associated with <code>name</code>
		 * @throws BranchingException if this branch has no child <code>name</code> or if it is not a value
		 */
		public ConfigValue getValue(String name) throws BranchingException {
			ConfigItem item = children.get(name);
			if(item == null)
				throw new BranchingException("Branch " + getPath() + " does not contain key " + name);
			if(!(item instanceof ConfigValue))
				throw new BranchingException("Item " + getPath() + "." + name + " is a branch");
			return (ConfigValue) item;
		}
		
		/**
		 * Returns true if this branch has a child named <code>name</code>.
		 * @param name the name to test
		 * @return true if this branch has a child named <code>name</code>
		 */
		public boolean hasChild(String name) {
			return children.containsKey(name);
		}
		
		/**
		 * Returns true if this branch has a branch child named <code>name</code>.
		 * @param name the name to test
		 * @return true if this branch has a branch child named <code>name</code>
		 */
		public boolean hasBranchChild(String name) {
			return hasChild(name) && children.get(name) instanceof ConfigBranch;
		}
		
		/**
		 * Returns true if this branch has a value child named <code>name</code>.
		 * @param name the name to test
		 * @return true if this branch has a value child named <code>name</code>
		 */
		public boolean hasValueChild(String name) {
			return hasChild(name) && children.get(name) instanceof ConfigValue;
		}
		
		/**
		 * Removes the child with name <code>name</code> of this branch.
		 * @param name the name of the child to remove
		 */
		private void remove(String name) {
			children.remove(name);
		}
		
		/**
		 * Remove all config values from this branch. Does not affect sub-branches.
		 */
		public void clearValues() {
			children.values().removeIf(item -> item instanceof ConfigItem);
		}
		
		/**
		 * Sets a value of this branch, or clears it if <code>value</code> is null.
		 * @param <T> the type of the value to set
		 * @param name the name of the child value
		 * @param serial the config serializer to store the value as a string
		 * @param value the actual value or null
		 * @throws BranchingException if <code>name</code> is already a branch
		 */
		public <T> void set(String name, ConfigType<T> serial, T value) throws BranchingException {
			if(hasBranchChild(name))
				throw new BranchingException("Cannot override branch " + getPath()+'.'+name + " with a value");
			if(hasValueChild(name)) {
				if(value == null) {
					remove(name);
				} else {
					ConfigValue val = getValue(name);
					if(val.value != value)
						val.set(serial, value);
				}
			} else if(value != null) {
				put(new ConfigValue(this, name, serial, value));
			}
		}
		
		public <T> void addToArray(ConfigType<T> serial, T value) throws BranchingException {
			int i = 0;
			while(hasChild(arrayPrefix+i))
				i++;
			set(arrayPrefix+i, serial, value);
		}

		public void removeFromArray(String stringValue) throws ParsingException {
			for(Entry<String, ConfigItem> child : children.entrySet()) {
				if(child.getValue() instanceof ConfigValue && ((ConfigValue)child.getValue()).getSerialized().equals(stringValue)) {
					children.remove(child.getKey());
					break;
				}
			}
		}
		
		/**
		 * Returns the parsed value of <code>name</code>.
		 * @param <T> the type of the value
		 * @param name the name of the child value
		 * @param serial the unserializer for the value, unserialized values are cached
		 * @return the unserialized value
		 * @throws ParsingException if <code>serial.unserialize</code> throws
		 * @throws BranchingException if <code>name</code> is not a value of this branch
		 * @see ConfigFile#get(String, ConfigType, Object)
		 * @see ConfigType#unserialize(String)
		 */
		public <T> T get(String name, ConfigType<T> serial) throws ParsingException, BranchingException {
			return getValue(name).get(serial);
		}
		
		/**
		 * Returns the parsed value of <code>name</code>, ignoring any exception that would be
		 * thrown by {@link #get(String, ConfigType)} returning <code>null</code> instead.
		 * @param <T> the type of the value
		 * @param name the name of the child value
		 * @param serial the unserializer for the value, unserialized values are cached
		 * @return the unserialized value or null if {@link #get(String, ConfigType)} throws
		 * @see #get(String, ConfigType)
		 */
		public <T> T getUnsafe(String name, ConfigType<T> serial) {
			try {
				return get(name, serial);
			} catch (BranchingException | ParsingException e) {
				return null;
			}
		}
		
		/**
		 * Returns the parsed value of <code>name</code>, ignoring any exception that would be
		 * thrown by {@link #get(String, ConfigType)} returning <code>defaultValue</code> instead.
		 * @param <T> the type of the value
		 * @param name the name of the child value
		 * @param serial the unserializer for the value, unserialized values are cached
		 * @param defaultValue the returned value if {@link #get(String, ConfigType)} throws
		 * @return the unserialized value or <code>defaultValue</code> if {@link #get(String, ConfigType)} throws
		 * @see #get(String, ConfigType)
		 */
		public <T> T get(String name, ConfigType<T> serial, T defaultValue) {
			try {
				return get(name, serial);
			} catch (BranchingException | ParsingException e) {
				return defaultValue;
			}
		}
		
		/**
		 * Returns a list of all values of this branch, sub-branches are ignored.
		 * @param <T> the type of an individual value
		 * @param serial the serial to be used for all child values
		 * @return a list of all child values parsed by <code>serial</code>
		 * @throws ParsingException if one of the values cannot be parsed
		 * @see #getAllUnsafe(ConfigType)
		 * @see #get(String, ConfigType)
		 */
		public <T> List<T> getAll(ConfigType<T> serial) throws ParsingException {
			List<T> values = new ArrayList<>();
			for(ConfigItem entry : entries()) {
				if(entry instanceof ConfigValue) {
					values.add(((ConfigValue)entry).get(serial));
				}
			}
			return values;
		}
		
		/**
		 * Returns a list of all values of this branch, sub-branches are ignored. Values that
		 * cannot be parsed / cause a ParsingException with <code>serial</code> are ignored.
		 * @param <T> the type of an individual value
		 * @param serial the serial to be used for all child values
		 * @return a list of all child values successfully parsed by <code>serial</code>
		 * @see #getAll(ConfigType)
		 * @see #get(String, ConfigType)
		 */
		public <T> List<T> getAllUnsafe(ConfigType<T> serial) {
			List<T> values = new ArrayList<>();
			for(ConfigItem entry : entries()) {
				if(entry instanceof ConfigValue) {
					try {
						values.add(((ConfigValue)entry).get(serial));
					} catch (ParsingException e) {
					}
				}
			}
			return values;
		}
		
		/** Returns all children of this branch */
		private Collection<ConfigItem> entries() {
			return children.values();
		}
		
		public int getInt(String path) throws BranchingException, ParsingException {
			return get(path, ConfigType.INT);
		}
		
		public int[] getInts(String path) throws BranchingException, ParsingException {
			return get(path, ConfigType.INTS);
		}
		
		public float getFloat(String path) throws BranchingException, ParsingException {
			return get(path, ConfigType.FLOAT);
		}
		
		public String getString(String path) throws BranchingException, ParsingException {
			return get(path, ConfigType.STRING);
		}
		
		public void setInt(String name, int val) throws BranchingException {
			set(name, ConfigType.INT, val);
		}
		
		public void setFloat(String name, float val) throws BranchingException {
			set(name, ConfigType.FLOAT, val);
		}
		
		public void setString(String name, String val) throws BranchingException {
			set(name, ConfigType.STRING, val);
		}
		
		@Override
		public String toString() {
			StringBuilder sb = new StringBuilder();
			writeBranch(sb, this, 0);
			return sb.toString();
		}
	}
	
	public static class ConfigValue extends ConfigItem {
		
		/** The cached serialized value or the default value taken from the original file */
		private String stringValue;
		/** The cached object value, depends on the serial used */
		private Object value;
		/** The serial charged of serializing and unserializing the value to/from a printable string */
		private ConfigType<?> serial;
		
		public ConfigValue(ConfigBranch parent, String name, String value) {
			super(parent, name);
			this.stringValue = value;
		}

		public <T> ConfigValue(ConfigBranch parent, String name, ConfigType<T> serial, T value) {
			super(parent, name);
			set(serial, value);
		}
		
		/**
		 * Sets the actual value of this config value.
		 * @param <T> the type of the value
		 * @param serial the parser that will be used to serialze <code>value</code> to a printable string
		 * @param value the actual value
		 */
		public <T> void set(ConfigType<T> serial, T value) {
			if(value == null)
				throw new IllegalArgumentException("A config value cannot be null");
			if(serial == null)
				throw new IllegalArgumentException("A config serial cannot be null");
			this.value = value;
			this.serial = serial;
			this.stringValue = null;
		}
		
		/**
		 * Returns the parsed value of <code>serial</code> or the cached value if <code>serial</code> did not
		 * change since the last call of this function. If <code>serial.unserialize</code> is used, the result
		 * is cached and the string cache is cleared.
		 * @param <T> the type of the value
		 * @param serial the unserializer
		 * @return the parsed value
		 * @throws ParsingException if <code>serial.unserialize</code> throws
		 * @see ConfigType#unserialize(String)
		 */
		@SuppressWarnings("unchecked")
		public <T> T get(ConfigType<T> serial) throws ParsingException {
			if(value != null && this.serial == serial)
				return (T)value;
			try {
				T val = serial.unserialize(stringValue);
				set(serial, val);
				return val;
			} catch(ParsingException e) {
				throw new ParsingException("Unable to parse " + getPath() + ": " + e.getMessage());
			}
		}
		
		/**
		 * Returns the parsed value of <code>serial</code> or the cached value if <code>serial</code> did not
		 * change since the last call of this function. If <code>serial.unserialize</code> is used, the result
		 * is cached and the string cache is cleared.<br>
		 * This method ignores exceptions and returns <code>defaultValue</code> if {@link #get(ConfigType)} would
		 * throw, leaving the previous cache state intact.
		 * @param <T> the type of the value
		 * @param serial the unserializer
		 * @param defaultValue the returned value if {@link #get(ConfigType)} throws
		 * @return the parsed value or <code>defaultValue</code> if {@link #get(ConfigType)} throws
		 */
		public <T> T get(ConfigType<T> serial, T defaultValue) {
			try {
				return get(serial);
			} catch (ParsingException e) {
				return defaultValue;
			}
		}
		
		/**
		 * Serializes and returns the cached object value or returns the already cached string value if any.
		 * @return the printable serialized value
		 * @throws ParsingException if no string cache exists and the serializer throws
		 * @see ConfigType#serialize(Object)
		 */
		public String getSerialized() throws ParsingException {
			if(stringValue != null)
				return stringValue;
			return stringValue = serial.serialize(value).replaceAll("\n", "");
		}
	}
	
}
