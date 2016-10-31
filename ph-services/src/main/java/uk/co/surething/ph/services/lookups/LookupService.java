package uk.co.surething.ph.services.lookups;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import uk.co.surething.ph.common.Constants;
import uk.co.surething.ph.services.AService;
import uk.co.surething.ph.services.AServiceEnvelope;
import uk.co.surething.ph.services.AServiceResult;
import uk.co.surething.ph.services.lookups.postcode.PostCodeLookup;

/**
 * 
 * Lookup service initiates async lookups and waits some time for the lookup
 * results to arrive. 
 * 
 * @author szymon.czaja
 *
 */
@Service
public class LookupService extends AService {

	
	@Autowired
	@Qualifier(Constants.BEAN_ASYNC_CALLBACK_EXECUTOR) 
	private ThreadPoolTaskExecutor executor; 
	
	
	@Override
	public AServiceResult execute(AServiceEnvelope params) {
		LookupsResult result = performAllLookups();
		
		return result;
	}

	/**
	 * 
	 * @return
	 */
	public LookupsResult performAllLookups() {
		
		long timeout = 2;
		TimeUnit unit = TimeUnit.SECONDS;
		List<Callable<LookupResult>> tasks = new ArrayList<Callable<LookupResult>>();
		
		tasks.add(new PostCodeLookup());
		Assert.notNull(executor);
		try {
			List<Future<LookupResult>> r = executor.getThreadPoolExecutor().invokeAll(tasks, timeout, unit);
			for (Future<LookupResult> f : r) {
				
				if (f.isCancelled()) {
					System.out.println("Task did not finish in time");
				} else {
					System.out.println("Task completed" );
					LookupResult lookupResult = f.get();
				}
				
			}
			
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}



}
