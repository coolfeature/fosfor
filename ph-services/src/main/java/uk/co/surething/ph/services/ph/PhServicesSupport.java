package uk.co.surething.ph.services.ph;

import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.xml.bind.JAXBElement;
import javax.xml.namespace.QName;
import javax.xml.transform.stream.StreamResult;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.ws.WebServiceMessage;
import org.springframework.ws.client.core.WebServiceMessageCallback;
import org.springframework.ws.client.core.WebServiceTemplate;
import org.springframework.ws.client.core.support.WebServiceGatewaySupport;
import org.springframework.ws.soap.SoapHeader;
import org.springframework.ws.soap.SoapHeaderElement;
import org.springframework.ws.soap.SoapMessage;

import uk.co.surething.ph.common.Constants;
import uk.co.surething.ph.models.ph.GetRateRequest;
import uk.co.surething.ph.models.ph.GetRateResponse;

@Configuration
public class PhServicesSupport extends WebServiceGatewaySupport {

	@Autowired
	@Qualifier(Constants.BEAN_JAXB_MARSHALLER)
	private Jaxb2Marshaller jaxb2Marshaller;
	
	private static final String NAMESPACE_URI = Constants.NAMESPACE_PH;
	private static final String URL = "http://localhost:8080/ph-web/ws/rate";
	
	@PostConstruct
	public void init() {
		this.setDefaultUri(NAMESPACE_URI);
		this.setMarshaller(jaxb2Marshaller);
		this.setUnmarshaller(jaxb2Marshaller);
	}
	
	public GetRateResponse rate(GetRateRequest req) {
		GetRateResponse response = (GetRateResponse) this.getWebServiceTemplate()
			.marshalSendAndReceive(URL, req,  
				new WebServiceMessageCallback() {
					public void doWithMessage(WebServiceMessage message) {
					}
				});
		return response;
	}

	public String getSampleRateRequestXML(GetRateRequest rr) {
		java.io.StringWriter sw = new StringWriter();
		Map<String, Object> props = new HashMap<String, Object>();
		props.put(javax.xml.bind.Marshaller.JAXB_FORMATTED_OUTPUT, true);
		jaxb2Marshaller.setMarshallerProperties(props);
		jaxb2Marshaller.marshal(rr, new StreamResult(sw));
		return sw.toString();
	}

}
