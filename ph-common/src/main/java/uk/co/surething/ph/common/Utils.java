package uk.co.surething.ph.common;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Timestamp;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.apache.log4j.Logger;

public class Utils {
	
	private static final Logger LOGGER = Logger.getLogger(Utils.class);
	
	/**
	 * 
	 * @param date
	 * @return
	 */
	public static ZonedDateTime getZonedDateTime(String date) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(Constants.DB_DATETIME_FORMAT);
		formatter = formatter.withZone(ZoneId.of(Constants.UK_TIMEZONE));
		return ZonedDateTime.parse(date,formatter);
	}
	
	/**
	 * 
	 * @param timestamp
	 * @return
	 */
	public static ZonedDateTime toZonedDateTime(Timestamp timestamp){
		return timestamp.toInstant().atZone(ZoneId.of(Constants.UK_TIMEZONE));
	}
	
	/**
	 * 
	 * @param date
	 * @return
	 */
	public static Timestamp toTimestamp(ZonedDateTime date){
		 return Timestamp.from(date.toInstant());
	}
	
	/**
	 * 
	 * @param date
	 * @return
	 */
	public static String toString(ZonedDateTime date){
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(Constants.DB_DATETIME_FORMAT);
		formatter = formatter.withZone(ZoneId.of(Constants.UK_TIMEZONE));
		return formatter.format(date);
	}
	
	
	/**
	 * 
	 * @return
	 */
	public static String determineProfile() {
		String profile = Constants.PROFILE_TEST;
		String hostname = null;
		try {
			hostname = InetAddress.getLocalHost().getHostName();
			if (hostname.toLowerCase().contains("prd")) {
				profile = Constants.PROFILE_PRD;
			}
		} catch (UnknownHostException e) {
			e.printStackTrace();
		}
		LOGGER.info("Setting " + profile + " profile for hostname " + hostname);
		return profile;
	}
	
	
	
	/**
	 * 
	 * @param directoryName
	 * @return
	 */
	public static void listDir(File directory, List<String> results) {
		File[] fList = directory.listFiles();
		for (File file : fList) {
			if (file.isFile()) {
				results.add(file.getPath());
			} else if (file.isDirectory()) {
				listDir(file, results);
			}
		}
	}
	
	
	/**
	 * 
	 * @param dir
	 */
	public static void ensureDirExists(String dir) {
		try {
			Files.createDirectories(Paths.get(dir));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
