package uk.co.surething.ph.ws.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.codahale.metrics.annotation.Timed;

import uk.co.surething.ph.common.Constants;
import uk.co.surething.ph.models.Samples;
import uk.co.surething.ph.models.ph.GetRateRequest;
import uk.co.surething.ph.models.radar.Root;
import uk.co.surething.ph.services.radar.RadarService;
import uk.co.surething.ph.services.radar.RadarServiceEnvelope;
import uk.co.surething.ph.services.radar.RadarServiceResult;
import uk.co.surething.ph.ws.AEndpoint;

@Controller
@ResponseBody
public class PhApiEndpoint extends AEndpoint {	

	@Autowired
	@Qualifier(Constants.BEAN_SVC_RADAR)
	private RadarService svcRadar;

	/**
	 * 
	 * @param risk
	 * @return
	 */
	@Timed
	@RequestMapping(
			path = API_RATE
			, method = RequestMethod.POST
			, consumes = { MediaType.APPLICATION_XML_VALUE, MediaType.TEXT_XML_VALUE })
	public String rate(@RequestBody GetRateRequest risk) {
		Root root = Samples.getRoot();
		RadarServiceEnvelope params = new RadarServiceEnvelope(root);
		RadarServiceResult result = (RadarServiceResult) svcRadar.execute(params);
		return "Done";
	}


}
