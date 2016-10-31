package uk.co.surething.ph.web;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;

import org.apache.log4j.Logger;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;

import uk.co.surething.ph.common.Utils;
import uk.co.surething.ph.ws.AEndpoint;
import uk.co.surething.ph.ws.WsConfig;

@Configuration
@Import(WsConfig.class)
@ComponentScan
public class WebConfig implements WebApplicationInitializer {

	private static final Logger LOGGER = Logger.getRootLogger();
	
	@Override
	public void onStartup(ServletContext servletContext) throws ServletException {

		servletContext.setInitParameter("spring.profiles.active", Utils.determineProfile());
		
		AnnotationConfigWebApplicationContext context = new AnnotationConfigWebApplicationContext();
		context.setConfigLocation(WebConfig.class.getName());

		LOGGER.info("Registering DispatcherServlet");
		DispatcherServlet dispatcherServlet = new DispatcherServlet(context);
		final ServletRegistration.Dynamic dispatcher = servletContext
				.addServlet("dispatcherServlet", dispatcherServlet);
		dispatcher.setLoadOnStartup(1);
		dispatcher.addMapping(AEndpoint.API + "/**", "/static/**", AEndpoint.METRICS + "/**", AEndpoint.HEALTH + "/**", "/");

		LOGGER.info("Registering MessageDispatcherServlet");
		PhMessageDispatcherServlet messageDispatcherServlet = new PhMessageDispatcherServlet(context);
		messageDispatcherServlet.setTransformWsdlLocations(true);
		final ServletRegistration.Dynamic messageDispatcher = servletContext
				.addServlet("messageDispatcherServlet", messageDispatcherServlet);
		messageDispatcher.setLoadOnStartup(2);
		messageDispatcher.addMapping(AEndpoint.WS + "/*");
		
		servletContext.addListener(new ContextLoaderListener(context));
		
	}

}
