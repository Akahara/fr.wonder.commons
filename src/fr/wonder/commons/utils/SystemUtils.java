package fr.wonder.commons.utils;

import java.io.File;

import fr.wonder.commons.files.FilesUtils;

public class SystemUtils {
	
	public static enum SystemOS {
		
		MAC_OS_X,
		WINDOWS,
		UNIX,
		UNKNOWN;
		
		public static final SystemOS OS;
		
		static {
			String name = getOSName();
			if(name.startsWith("Linux"))
				OS = UNIX;
			else if(name.startsWith("Windows"))
				OS = WINDOWS;
			else if(name.startsWith("Mac OS"))
				OS = MAC_OS_X;
			else
				OS = UNKNOWN;
		}
	}
	
	public static File getUserHomeDir() {
		return FilesUtils.getUserHome();
	}
	
	public static String getOSName() {
		return System.getProperty("os.name");
	}
	
	public static SystemOS getOS() {
		return SystemOS.OS;
	}
	
}
