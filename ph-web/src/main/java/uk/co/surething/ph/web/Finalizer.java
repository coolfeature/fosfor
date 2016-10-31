package uk.co.surething.ph.web;

import java.util.List;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.context.annotation.Configuration;

import uk.co.surething.ph.common.ClassUtils;
import uk.co.surething.ph.models.cdl.in.AlcoholReadingType;


@Configuration
public class Finalizer implements DisposableBean  {

	private static Logger LOGGER = Logger.getLogger(Finalizer.class);
	
	@Override
	public void destroy() throws Exception {
		
		LOGGER.info("Cleaning up...");
		LogManager.shutdown();
		ClassUtils.disposeEnums("uk.co.surething.ph.models", AlcoholReadingType.class);

	}

	
}
