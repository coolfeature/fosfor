package uk.co.surething.services.radar;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import uk.co.surething.ph.common.Constants;
import uk.co.surething.ph.models.Samples;
import uk.co.surething.ph.models.radar.Root;
import uk.co.surething.ph.services.ServicesTestBase;
import uk.co.surething.ph.services.radar.RadarService;
import uk.co.surething.ph.services.radar.RadarServiceEnvelope;

public class RadarTest extends ServicesTestBase {

	@Autowired
	@Qualifier(Constants.BEAN_SVC_RADAR)
	private RadarService svcRadar;
	
	
	@Test
	public void testCall() {
		Root root = Samples.getRoot();
		RadarServiceEnvelope params = new RadarServiceEnvelope(root);
		svcRadar.execute(params);
	}

}
