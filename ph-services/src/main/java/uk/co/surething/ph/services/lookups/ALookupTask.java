package uk.co.surething.ph.services.lookups;

import java.util.concurrent.Callable;

import org.apache.log4j.Logger;

public abstract class ALookupTask implements Callable<LookupResult> {

	protected static Logger LOGGER;
	
	{
		LOGGER = Logger.getLogger(this.getClass());
	}
	
}
