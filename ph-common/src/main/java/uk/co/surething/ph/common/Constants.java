package uk.co.surething.ph.common;

import java.nio.charset.StandardCharsets;

public class Constants {
	
	static {
		String name = Constants.class.getName();
		String[] tokens = name.split("\\.");
		APP_NAME = tokens[3];
	}
	
	public static final String APP_NAME;
	public static final String BEAN_PROPERTIES = "appProperties";
	
	public static final String UK_TIMEZONE = "Europe/London";
	public static final String DB_DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
	
	public static final String UTF_16 = StandardCharsets.UTF_16.toString();
	
	public static final String PROFILE_TEST = "test";
	public static final String PROFILE_PRD = "prd";
	
	public static final String WS_PH_RATE_PORT_TYPE_NAME = "PHRatePort";
	
	
	public static final String BEAN_ASYNC_CALLBACK_QUEUE = "beanCallbackBlockingQueue";
	public static final String BEAN_ASYNC_CALLBACK_EXECUTOR = "beanThreadPoolTaskExecutor";
	public static final String BEAN_WEB_SERVICE_TEMPLATE = "beanRadarWebServiceTemplate";
	public static final String BEAN_JAXB_RADAR_ROOT = "beanJaxbRadarRoot";
	public static final String BEAN_JAXB_MARSHALLER = "beanJaxbMarshaller";
	public static final String BEAN_RADAR_HTTP_CLIENT = "beanRadarHTTPClient";
	public static final String BEAN_PH_SCHEMA = "beanPHSchema";
	public static final String BEAN_WSDL = "beanWSDL";
	
	public static final String BEAN_SVC_CORE = "beanSvcCore";
	public static final String BEAN_SVC_RADAR = "beanSvcRadar";
	
	public static final String NAMESPACE_PH = "http://www.surething.co.uk/schemas/ph";
	
	
}
