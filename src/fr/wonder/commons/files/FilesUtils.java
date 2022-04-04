package fr.wonder.commons.files;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ServiceLoader;
import java.util.function.Predicate;

public class FilesUtils {

	public static final int SIZE_1K = 1024, SIZE_1M = 1048576, SIZE_1G = 1073741824;
	
	public static File getUserHome() {
		return new File(System.getProperty("user.home"));
	}

	/**
	 * Returns the .jar file the class is located in. If the class was loaded with a
	 * {@link ServiceLoader} the jar file is the loaded jar, not the jar that
	 * instantiated the loader.
	 * 
	 * @param file the execution class
	 * @return the file from which the class was loaded
	 */
	public static File getExecutionFile(Class<?> clazz) {
		try {
			return new File(clazz.getProtectionDomain().getCodeSource().getLocation().toURI());
		} catch (URISyntaxException e) {
			return null;
		}
	}
	
	/**
	 * Returns the .jar file this class is located in.
	 * 
	 * @return the file from which this class was loaded
	 */
	public static File getExecutionFile() {
		try {
			return new File(FilesUtils.class.getProtectionDomain().getCodeSource().getLocation().toURI());
		} catch (URISyntaxException e) {
			return null;
		}
	}
	
	/**
	 * Returns the file name without its extension if it has one
	 * 
	 * @param file the file
	 * @return the file name without its extension
	 */
	public static String getFileName(File file) {
		return getFileName(file.getName());
	}
	
	/**
	 * Returns the file name without its extension if it has one
	 * 
	 * @param name the file name (not a path)
	 * @return the file name without its extension
	 */
	public static String getFileName(String name) {
		int dot = name.lastIndexOf('.');
		if (dot == -1)
			return name;
		return name.substring(0, dot);
	}


	/**
	 * Returns the file's extension if it has one, null otherwise.
	 * 
	 * @param file the file
	 * @return the file extension, or null if it does not have one
	 */
	public static String getFileExtension(File file) {
		return getFileExtension(file.getName());
	}
	
	/**
	 * Returns the file's extension if it has one, null otherwise.
	 * 
	 * @param name the file name (not path)
	 * @return the file extension, or null if it does not have one
	 */
	public static String getFileExtension(String name) {
		int dot = name.lastIndexOf('.');
		if (dot == -1)
			return null;
		return name.substring(dot + 1);
	}

	/**
	 * Returns the relative path from a parent directory to a child file/directory.
	 * If {@code parent} is not a parent of {@code child} the absolute path of {@code child}
	 * is returned.
	 * 
	 * @param parent the parent directory
	 * @param child the child directory
	 * @return the path from the parent to the child
	 */
	public static String getRelativePath(File parent, File child) {
		String ppath = parent.getAbsolutePath();
		String cpath = child.getAbsolutePath();
		if(!cpath.startsWith(ppath))
			return cpath;
		return cpath.substring(ppath.length() + 1);
	}
	
	/**
	 * Returns a predicate matching for files that have the given extension
	 * 
	 * @param ext the file extension without the '.' sign
	 */
	public static Predicate<File> extensionFilter(String ext) {
		return f -> ext.equals(getFileExtension(f));
	}
	
	/**
	 * Creates the file if it does not exist and returns it
	 * 
	 * @param file the file to create
	 * @return the {@code file} argument
	 * @throws IOException if the file does not exist and could not be created
	 */
	public static File create(File file) throws IOException {
		if(!file.isFile()) {
			file.getParentFile().mkdirs();
			file.createNewFile();
		}
		return file;
	}

	/** Calls {@link #create(File)} on a new File with the given path */
	public static File create(String path) throws IOException {
		return create(new File(path));
	}
	
	/** Calls {@link #create(File)} on a new File with the given parent and path */
	public static File create(File parent, String path) throws IOException {
		return create(new File(parent, path));
	}

	/**
	 * Creates the directory if it does not exist and returns it
	 * 
	 * @param dir the directory to create
	 * @return the {@code dir} argument
	 * @throws IOException if the directory does not exist and could not be created
	 */
	public static File createDir(File dir) throws IOException {
		if(!dir.isDirectory() && !dir.mkdir())
			throw new IOException("Unable to create directory");
		return dir;
	}
	
	/** Calls {@link #createDir(File)} on a new File with the given path */
	public static File createDir(String path) throws IOException {
		return createDir(new File(path));
	}
	
	/** Calls {@link #createDir(File)} on a new File with the given parent and path */
	public static File createDir(File parent, String path) throws IOException {
		return createDir(new File(parent, path));
	}

	public static String read(File file) throws IOException {
		try(InputStream is = new FileInputStream(file)) {
			return new String(is.readAllBytes());
		}
	}

	public static byte[] readBytes(File file) throws IOException {
		try (FileInputStream stream = new FileInputStream(file)) {
			return stream.readAllBytes();
		}
	}

	public static void write(File file, String text) throws IOException {
		try (FileOutputStream stream = new FileOutputStream(file)) {
			stream.write(text.getBytes());
		}
	}

	public static void write(File file, byte[] bytes) throws IOException {
		try (FileOutputStream stream = new FileOutputStream(file)) {
			stream.write(bytes);
		}
	}

	/** returns true if dir is a part of the file hierarchy */
	public static boolean isParentDir(File dir, File file) {
		return file.getAbsolutePath().startsWith(dir.getAbsolutePath());
	}

	/** Deletes all children files or directories of {@code rootDir} and the {@code rootDir} directory */
	public static void deleteRecur(File rootDir) throws IOException {
		Files.walk(rootDir.toPath()).sorted(Comparator.reverseOrder()).map(Path::toFile).forEach(File::delete);
	}
	
	/** Deletes all children files or directories of {@code rootDir} but not the {@code rootDir} directory */
	public static void deleteContents(File dir) throws IOException {
		for(File f : dir.listFiles()) {
			if(f.isDirectory())
				deleteRecur(f);
			else
				f.delete();
		}
	}

	public static Map<String, Long> collectTimestamps(File dir, boolean recur) {
		Map<String, Long> timestamps = new HashMap<>();

		File[] files = dir.listFiles();
		if (files != null)
			for (File f : files) {
				if (f.isFile()) {
					timestamps.put(f.getName(), f.lastModified());
				} else if (f.isDirectory() && recur) {
					Map<String, Long> stamps = collectTimestamps(f, true);
					stamps.forEach((sf, ts) -> timestamps.put(f.getName() + File.separatorChar + sf, ts));
				}
			}

		return timestamps;
	}

	
	/**
	 * Collects all files of {@code dir} (including directories) recursively.
	 * The order in which files are collected is deepest first so if some file
	 * must be deleted it will not affect the following.
	 * 
	 * @param dir the directory to collect from (not added to the list)
	 * @param files the list to add to
	 * @return the given list instance with the collected files added to it
	 */
	public static List<File> collectFiles(File dir, List<File> files) {
		File[] dirFiles = dir.listFiles();
		for(File f : dirFiles) {
			if(f.isDirectory())
				collectFiles(f, files);
		}
		for(File f : dirFiles)
			files.add(f);
		return files;
	}
	/**
	 * Returns all files (including directories) of {@code dir} including itself.
	 * The order in which files are collected is deepest first so if some file
	 * must be deleted it will not affect the following, {@code dir} is the last
	 * item collected.
	 * 
	 * @see #collectFiles(File, List)
	 */
	public static List<File> listFiles(File dir) {
		List<File> files = new ArrayList<>();
		collectFiles(dir, files);
		files.add(dir);
		return files;
	}
	
	/**
	 * Returns all files (including directories) of <code>dir</code> matching the
	 * given filter
	 * 
	 * @see #listFiles(File)
	 */
	public static List<File> listFiles(File dir, Predicate<File> filter) {
		List<File> files = listFiles(dir);
		files.removeIf(filter.negate());
		return files;
	}

	/**
	 * Returns all files (excluding directories) of <code>dir</code>
	 * 
	 * @see #listFiles(File)
	 */
	public static List<File> listTrueFiles(File dir) {
		return listFiles(dir, File::isFile);
	}
	
	/**
	 * Returns the first file that exists in the given array, or null if none exist.
	 * 
	 * @param files the files to search in
	 * @return the first file that exists in the given array
	 */
	public static File firstAlternative(File... files) {
		for(File f : files) {
			if(f.exists())
				return f;
		}
		return null;
	}

}
