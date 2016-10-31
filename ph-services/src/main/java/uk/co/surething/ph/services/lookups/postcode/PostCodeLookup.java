package uk.co.surething.ph.services.lookups.postcode;


import uk.co.surething.ph.services.lookups.ALookupTask;
import uk.co.surething.ph.services.lookups.LookupResult;

public class PostCodeLookup extends ALookupTask {

	public PostCodeLookup() {
		
	}

	@Override
	public LookupResult call() throws Exception {
		LookupResult result = new LookupResult();
		LOGGER.info("\n\nPerforming lookup\n\n");
		Thread.sleep(5000);
		return result;
	}
	
}
