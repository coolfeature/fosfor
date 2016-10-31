package uk.co.surething.ph.common;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;


/**
 * 
 * @author szymon.czaja
 *
 */
public class ClassUtils {

	private static final String CLASS_FILE_SUFFIX = ".class";

	
	/**
	 * 
	 * @return
	 */
	public static boolean isJarred() {
		String classPath = ClassUtils.class.getProtectionDomain().getCodeSource().getLocation().getPath();
		File jarFile = new File(classPath);
		return jarFile.isFile();
	}
	
	
	/**
	 * 
	 * @param clazz
	 * @return
	 */
	public static List<String> listDomainMembers(Class<?> clazz) {
		List<String> filenames = null;
		File jarFile = null;
		JarFile jar = null;
		String classPath = clazz.getProtectionDomain().getCodeSource().getLocation().getPath();
		try {
			jarFile = new File(classPath);
			try {
				if (jarFile.isFile()) {
					jar = new JarFile(jarFile);
					Enumeration<JarEntry> entries = jar.entries();
					while (entries.hasMoreElements()) {
						if (filenames == null) {
							filenames = new ArrayList<String>();
						}
						JarEntry jarEntry = entries.nextElement();
						String name = jarEntry.getName();
						filenames.add(name);
					}
				} else {
					if (filenames == null) {
						filenames = new ArrayList<String>();
					}
					Utils.listDir(jarFile, filenames);
				}
			} finally {
				if (jar != null) {
					jar.close();
				}
			}
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
		return filenames;
	}

	
	/**
	 * 
	 * @param scannedPackage
	 * @param packageClass
	 * @return
	 */
	public static List<Class<?>> listPackageClasses(String scannedPackage, Class<?> packageClass) {
		List<Class<?>> classes = new ArrayList<Class<?>>();
		List<String> members = listDomainMembers(packageClass);
		String scannedPackagePath = scannedPackage.replaceAll("\\.", "/");
		for (String member : members) {
			if (member.endsWith(CLASS_FILE_SUFFIX) && member.contains(scannedPackagePath)) {
				int endIndex = member.length() - CLASS_FILE_SUFFIX.length();
				String className = member.substring(0, endIndex);
				className = className.replaceAll("/", ".");
				try {
					classes.add(Class.forName(className));
				} catch (ClassNotFoundException ignore) {
				}
			}
		}
		return classes;
	}
	
	/**
	 * 
	 * @param scannedPackage
	 * @param packageClass
	 * @return
	 */
	public static List<Class<?>> listEnums(String scannedPackage, Class<?> packageClass) {
		List<Class<?>> enums = new ArrayList<Class<?>>();
		List<Class<?>> classes = listPackageClasses(scannedPackage, packageClass);
		for (Class<?> clazz : classes) {
			if (clazz.isEnum()) {
				enums.add(clazz);
			}
		}
		return enums;
	}
	
	
	/**
	 * 
	 * @param scannedPackage
	 * @param packageClass
	 */
	public static void disposeEnums(String scannedPackage, Class<?> packageClass) {
		List<Class<?>> enums = listEnums(scannedPackage, packageClass);
		disposeEnums(enums);
	}
	
	
	/**
	 * 
	 * @param classes
	 */
	public static void disposeEnums(List<Class<?>> classes) {
		String[] specialFields = { "$VALUES" };
		for (Class<?> clazz : classes) {
			List<String> fieldNames = new ArrayList<String>();

			for (String specialField : specialFields) {
				fieldNames.add(specialField);
			}
			Field[] fields = clazz.getFields();
			for (Field field : fields) {
				fieldNames.add(field.getName());
			}
			for (String fieldName : fieldNames) {
				try {
					Field field = clazz.getDeclaredField(fieldName);
					field.setAccessible(true);
					
					// in case private static final
					Field modifiersField = Field.class.getDeclaredField("modifiers");
					modifiersField.setAccessible(true);
					modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);
					
					field.set(null, null);
					
				} catch (NoSuchFieldException e) {

				} catch (SecurityException e) {
					e.printStackTrace();
				} catch (IllegalArgumentException e) {
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				}
			}

		}
	}
}
