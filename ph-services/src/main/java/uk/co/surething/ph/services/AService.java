package uk.co.surething.ph.services;

import org.apache.log4j.Logger;

public abstract class AService {

	protected static Logger LOGGER;
	
	{
		LOGGER = Logger.getLogger(this.getClass());
	}
	
	public abstract AServiceResult execute(AServiceEnvelope params);
	
}
