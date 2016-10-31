package uk.co.surething.ph.services.core;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import uk.co.surething.ph.common.Constants;
import uk.co.surething.ph.services.ServicesTestBase;

public class CoreTest extends ServicesTestBase {

	@Autowired
	@Qualifier(Constants.BEAN_SVC_CORE)
	private CoreService coreSvc;
	
	
	@Test
	public void test() {
		CoreServiceEnvelope params = new CoreServiceEnvelope();
		coreSvc.execute(params);
	}
}
