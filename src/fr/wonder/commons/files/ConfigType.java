package fr.wonder.commons.files;

import java.io.File;
import java.util.Arrays;

public interface ConfigType<T> {
	
	public String serialize(Object val) throws ParsingException;
	public T unserialize(String val) throws ParsingException;
	
	public static final ConfigType<Integer> INT = new ConfigType<Integer>() {

		@Override
		public String serialize(Object val) throws ParsingException {
			if(!(val instanceof Integer))
				throw new ParsingException(val + " is not an integer");
			return String.valueOf((int)val);
		}

		@Override
		public Integer unserialize(String val) throws ParsingException {
			try {
				return Integer.parseInt(val);
			} catch (NumberFormatException e) {
				throw new ParsingException(e.getMessage());
			}
		}
		
	};
	
	public static final ConfigType<Float> FLOAT = new ConfigType<Float>() {

		@Override
		public String serialize(Object val) throws ParsingException {
			if(!(val instanceof Float))
				throw new ParsingException(val + " is not an integer");
			return String.valueOf((float)val);
		}

		@Override
		public Float unserialize(String val) throws ParsingException {
			try {
				return Float.parseFloat(val);
			} catch (NumberFormatException e) {
				throw new ParsingException(e.getMessage());
			}
		}
		
	};
	
	public static final ConfigType<String> STRING = new ConfigType<String>() {

		@Override
		public String serialize(Object val) throws ParsingException {
			return val.toString();
		}

		@Override
		public String unserialize(String val) throws ParsingException {
			return val;
		}
		
	};
	
	public static final ConfigType<int[]> INTS = new ConfigType<int[]>() {

		@Override
		public String serialize(Object val) throws ParsingException {
			if(!(val instanceof int[]))
				throw new ParsingException(val + " is not an int array");
			String array = Arrays.toString((int[])val);
			return array.substring(1, array.length()-1).replaceAll(",", "");
		}

		@Override
		public int[] unserialize(String val) throws ParsingException {
			String[] splits = val.split(" ");
			int[] ints = new int[splits.length];
			try {
				for(int i = 0; i < ints.length; i++) {
					ints[i] = Integer.parseInt(splits[i]);
				}
				return ints;
			} catch (NumberFormatException e) {
				throw new ParsingException(e.getMessage());
			}
		}
		
	};
	
	public static final ConfigType<File> FILE = new ConfigType<File>() {

		@Override
		public String serialize(Object val) throws ParsingException {
			if(!(val instanceof File))
				throw new ParsingException(val + " is not a file");
			return ((File)val).getAbsolutePath();
		}

		@Override
		public File unserialize(String val) throws ParsingException {
			return new File(val);
		}
		
	};
	
}
