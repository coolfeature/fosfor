package uk.co.surething.ph.common;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.log4j.PropertyConfigurator;

/**
 * GOALS: + Upon initialisation export all resources specified in a resource
 * directory to a file system location without overwriting any files + Load
 * application and logging configuration from the file system on application
 * startup + Provide a consistent way of reading exported properties from the
 * file system configuration files + Provide a consistent way of reading
 * exported resources from the file system folders
 * 
 * + Realise the above goals both for exploded artifacts as well as JUnit tests
 * 
 * @author szymon.czaja
 *
 */
public class Config {

	protected static final String EXPORTED_RESOURCE_FOLDER = "exported";
	private static final String PH_DIR_FALLBACK = "/tmp/apps";
	private static final String PH_DIR = "PH_DIR";
	private static final String KEY_LOG4J = "log4j.config";
	private static final String JDNI_RESOURCE_EXPORT_DIR = "java:comp/env/" + PH_DIR;
	private static String RESOURCE_DIR = null;
	protected static Properties PROPERTIES;

	
	/**
	 *
	 */
	static {
		init();
		exportResources();
		loadProperties();
		configureLogger();
	}


	/**
	 * Determines the file of the RESOURCE_DIR variable which stores the path to
	 * the file system location of application configuration files and
	 * resources.
	 * 
	 * 1) Looks for PH_DIR in application context 2) Looks for PH_DIR in system
	 * env 3) Fallbacks onto PH_DIR_FALLBACK
	 */
	private static void init() {
		log("Initializing configuration");
		Context initCtx;
		try {
			initCtx = new InitialContext();
			RESOURCE_DIR = (String) initCtx.lookup(JDNI_RESOURCE_EXPORT_DIR);
		} catch (NamingException e) {
			String sysPhDir = System.getProperty(PH_DIR);
			if (sysPhDir != null) {
				RESOURCE_DIR = sysPhDir;
			} else {
				try {
					Files.createDirectories(Paths.get(PH_DIR_FALLBACK));
				} catch (IOException e1) {
					e1.printStackTrace();
				}
				RESOURCE_DIR = PH_DIR_FALLBACK + File.separator + Constants.APP_NAME;
				log("No JDNI or system ENV entry found, defaulting to: " + RESOURCE_DIR);
			}
		}
		log("PH dir: " + RESOURCE_DIR);
	}

	
	/**
	 * Export the resource folder specified by EXPORTED_RESOURCE_FOLDER to file
	 * system destination as pointed by RESOURCE_DIR.
	 * 
	 * The export of resources should work regardless if the application is
	 * exploded and this code is executed from within a JAR or when running as a
	 * JUnit test.
	 */
	private static void exportResources() {
		log("Exporting JAR resources to " + RESOURCE_DIR);
		try {
			Files.createDirectories(Paths.get(RESOURCE_DIR));
			boolean isJarred = ClassUtils.isJarred();
			List<String> resources = ClassUtils.listDomainMembers(Config.class);
			List<String> filtered = filterResources(resources, isJarred);
			log("Export candidates: " + filtered);
			exportResources(filtered);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @param resources
	 * @param areJarred
	 * @return
	 */
	private static List<String> filterResources(List<String> resources, boolean areJarred) {
		List<String> filtered = new ArrayList<String>();
		if (areJarred) {
			for (String resource : resources) {
				if (resource.startsWith(EXPORTED_RESOURCE_FOLDER)) {
					String pattern = EXPORTED_RESOURCE_FOLDER + "/";
					resource = resource.replace(pattern, "");
					if (!resource.equals("")) {
						filtered.add(resource);
					}
				}
			}
		} else {
			String classPath = Config.class.getProtectionDomain().getCodeSource().getLocation().getPath();
			String exportedPath = classPath + File.separator + EXPORTED_RESOURCE_FOLDER;
			Path basePath = new File(exportedPath).toPath();
			String exportedPathNormalized = basePath.normalize().toString();
			for (String resource : resources) {
				if (resource.startsWith(exportedPathNormalized)) {
					Path relative = basePath.relativize(Paths.get(resource));
					filtered.add(relative.toString());
				}
			}
		}
		return filtered;
	}

	/**
	 * 
	 * @param resources
	 * @param jarred
	 */
	public static void exportResources(List<String> resources) {
		try {
			for (String resource : resources) {
				Path target = Paths.get(RESOURCE_DIR + File.separator + resource);
				InputStream is = null;
				try {
					String src = EXPORTED_RESOURCE_FOLDER + File.separator + resource;
					is = Thread.currentThread().getContextClassLoader().getResourceAsStream(src);
					Path targetParent = target.getParent();
					Files.createDirectories(targetParent);
					System.out.println("Copying " + src + " to " + target.toString());
					if (!Files.exists(target)) {
						Files.copy(is, target);
					}
				} finally {
					if (is != null)
						is.close();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	
	/**
	 * 
	 * @param resource
	 */
	private static void loadProperties() {
		String properties = Constants.APP_NAME + ".properties";
		log("Loading: " + properties);
		PROPERTIES = new Properties();
		InputStream is = null;
		try {
			try {
				is = getResourceAsStream(properties);
				PROPERTIES.load(is);
			} finally {
				if (is != null)
					is.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	
	/**
	 * 
	 */
	private static void configureLogger() {
		String log4jPath = getStringProperty(KEY_LOG4J);
		try {
			InputStream is = null;
			try {
				is = getResourceAsStream(log4jPath);
				Properties props = new Properties();
				props.load(is);
				PropertyConfigurator.configure(props);
			} finally {
				if (is != null)
					is.close();
			}
		} catch (IOException ioe) {

		}
	}


	/**
	 * 
	 * @param key
	 * @return
	 */
	public static String getStringProperty(String key) {
		return PROPERTIES.getProperty(key);
	}

	
	/**
	 * 
	 * @param key
	 * @return
	 */
	public static int getIntProperty(String key) {
		String val = PROPERTIES.getProperty(key);
		return Integer.parseInt(val);
	}

	
	/**
	 * 
	 * @return
	 * @throws FileNotFoundException
	 */
	public static InputStream getResourceAsStream(String resource) throws FileNotFoundException {
		String path = null;
		if (resource.startsWith("/")) {
			path = resource;
		} else {
			path = RESOURCE_DIR + File.separator + resource;
		}
		log("Loading system resource: " + path);
		return new FileInputStream(path);
	}

	
	/**
	 * 
	 * @param resource
	 * @return
	 * @throws FileNotFoundException
	 */
	public static Path getResourceAsPath(String resource) throws FileNotFoundException {
		String path = null;
		if (resource.startsWith("/")) {
			path = resource;
		} else {
			path = RESOURCE_DIR + File.separator + resource;
		}
		log("Loading system resource: " + path);
		return Paths.get(path);
	}

	
	/**
	 * 
	 * @param msg
	 */
	private static void log(String msg) {
		String formatted = String.format("*** %s", msg);
		System.out.println(formatted);
	}
}
