package uk.co.surething.ph.services.core;

import org.springframework.beans.factory.annotation.Autowired;

import uk.co.surething.ph.services.AService;
import uk.co.surething.ph.services.AServiceEnvelope;
import uk.co.surething.ph.services.lookups.LookupService;
import uk.co.surething.ph.services.lookups.LookupsResult;
import uk.co.surething.ph.services.radar.RadarService;
import uk.co.surething.ph.services.radar.RadarServiceEnvelope;
import uk.co.surething.ph.services.radar.RadarServiceResult;

public class CoreService extends AService {

	@Autowired
	private LookupService svcLookup;
	
	@Autowired
	private RadarService svcRadar;
	
	@Override
	public CoreServiceResult execute(AServiceEnvelope params) {
		CoreServiceResult result = new CoreServiceResult();
		LOGGER.info("Core Service execute");
		LookupsResult lookupResult = (LookupsResult) svcLookup.execute(params);
		RadarServiceEnvelope radarParams = new RadarServiceEnvelope();
		RadarServiceResult radarResult = (RadarServiceResult) svcRadar.execute(radarParams);
		LOGGER.info("Core service test completed in " + result.displayElapsedSinceInstantiation());
		return result;
	}
}
