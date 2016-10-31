package uk.co.surething.ph.ws.soap;

import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

import uk.co.surething.ph.common.Config;
import uk.co.surething.ph.common.Constants;
import uk.co.surething.ph.models.ph.GetRateRequest;
import uk.co.surething.ph.models.ph.GetRateResponse;

@Endpoint
public class PhWsEndpoint {

	@PayloadRoot(namespace = Constants.NAMESPACE_PH, localPart = "getRateRequest")
	@ResponsePayload
	public GetRateResponse getRate(@RequestPayload GetRateRequest request) {
		System.out.println("RATE REQUEST:: " + request.toString());
		GetRateResponse response = new GetRateResponse();
		response.setMessage("RATED");
		long sleep = Config.getIntProperty("test.ph.keepThreadTime");
		try {
			Thread.sleep(sleep);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return response;
	}
}
