package fr.wonder.commons.files;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

public class FileState {
	
	private final File file;
	private boolean saved;
	private int operations;
	
	public FileState(File file, boolean saved) {
		this.file = file;
		this.saved = saved;
	}
	
	public FileState(File file) {
		this(file, true);
	}
	
	public boolean isSaved() {
		return saved;
	}
	
	public void setState(boolean saved, int operations) {
		this.saved = saved;
		this.operations = operations;
	}
	
	public void modify() {
		saved = false;
		operations++;
	}
	
	public void silentModify() {
		saved = false;
	}
	
	public void rollback() {
		if(operations == 0)
			throw new IllegalStateException("The state has not yet been modified");
		operations--;
		saved = operations == 0;
	}
	
	public void updateState(byte[] data) {
		try {
			this.saved = Arrays.equals(data, FilesUtils.readBytes(file));
		} catch (IOException e) {
			this.saved = false;
		}
	}
	
	public void updateState(String text) {
		try {
			this.saved = text != null && text.equals(FilesUtils.read(file));
		} catch (IOException e) {
			this.saved = false;
		}
	}
	
	public void save(byte[] data) throws IOException {
		if(saved)
			return;
		FilesUtils.write(file, data);
		saved = true;
	}
	
	public void save(String text) throws IOException {
		if(saved)
			return;
		FilesUtils.write(file, text);
		saved = true;
	}
	
	public boolean saveUnsafe(byte[] data) {
		return saveUnsafeEx(data) == null;
	}

	public boolean saveUnsafe(String text) {
		return saveUnsafeEx(text) == null;
	}
	
	public IOException saveUnsafeEx(byte[] data) {
		if(saved)
			return null;
		try {
			save(data);
			return null;
		} catch (IOException e) {
			return e;
		}
	}
	
	public IOException saveUnsafeEx(String text) {
		if(saved)
			return null;
		try {
			save(text);
			return null;
		} catch (IOException e) {
			return e;
		}
	}
	
}
