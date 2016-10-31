package uk.co.surething.ph.ws;

import org.apache.log4j.Logger;

public abstract class AEndpoint {

	public static final String ROOT = "/";
	public static final String API = "/api";
	
	public static final String API_RATE = API + "/rate";
	
	public static final String WS = "/ws";
	public static final String WS_RATE = WS + "/rate";
	
	public static final String METRICS = "/metrics";
	
	public static final String HEALTH = "/health";
	public static final String HEALTH_PING = HEALTH + "/ping";
	
	protected static Logger LOGGER;
	
	{
		LOGGER = Logger.getLogger(this.getClass());
	}
	
}
