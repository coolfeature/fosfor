package uk.co.surething.ph.ws.soap;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.test.client.MockWebServiceServer;
import org.springframework.ws.test.client.RequestMatchers;
import org.springframework.ws.test.client.ResponseCreators;
import org.springframework.xml.transform.StringSource;

import uk.co.surething.ph.models.Samples;
import uk.co.surething.ph.models.ph.GetRateRequest;
import uk.co.surething.ph.models.ph.GetRateResponse;
import uk.co.surething.ph.services.ph.PhServicesSupport;
import uk.co.surething.ph.ws.WsTestBase;

/**
 * phServicesSupport client behaviour integration test.
 * @author szymon.czaja
 *
 */
public class SoapTest extends WsTestBase {

	@Autowired
	private PhServicesSupport phServicesSupport;

	private MockWebServiceServer mockServer;

	@Before
	public void createServer() throws Exception {
		mockServer = MockWebServiceServer.createServer(phServicesSupport);
	}

	@Test
	public void test() {
		GetRateRequest rr = Samples.getRateRequest();
		String request = phServicesSupport.getSampleRateRequestXML(rr);
		String response = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"yes\"?>\n"
				+ "<ns2:getRateResponse xmlns:ns2=\"http://www.surething.co.uk/schemas/ph\">\n"
				+ "<ns2:message>RATED</ns2:message>\n</ns2:getRateResponse>\n";
		mockServer
			.expect(RequestMatchers.payload(new StringSource(request)))
			.andRespond(ResponseCreators.withPayload(new StringSource(response)));
		GetRateResponse rateResponse = phServicesSupport.rate(rr);
		Assert.assertEquals(rateResponse.getMessage(), "RATED");
		mockServer.verify();
	}

}
