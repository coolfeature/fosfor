package uk.co.surething.ph.services.radar;

import javax.annotation.PostConstruct;
import javax.xml.namespace.QName;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.ws.WebServiceMessage;
import org.springframework.ws.client.core.WebServiceMessageCallback;
import org.springframework.ws.client.core.WebServiceTemplate;
import org.springframework.ws.client.core.support.WebServiceGatewaySupport;
import org.springframework.ws.soap.SoapHeader;
import org.springframework.ws.soap.SoapHeaderElement;
import org.springframework.ws.soap.SoapMessage;

import uk.co.surething.ph.common.Config;
import uk.co.surething.ph.common.Constants;
import uk.co.surething.ph.models.radar.dpo.PofRequest;
import uk.co.surething.ph.models.radar.dpo.PofResponse;

@Configuration
public class RadarServicesSupport extends WebServiceGatewaySupport {

	@Autowired
	@Qualifier(Constants.BEAN_WEB_SERVICE_TEMPLATE)
	private WebServiceTemplate webServiceTemplate;
	
	private static final String NAMESPACE_URI = "http://towerswatson.com/rto/dpo/services/2010/01/DpoServiceContract/GetPof";
	private static final String URL = Config.getStringProperty("ph.radar.service.url");
	
	@PostConstruct
	public void init() {
		this.setWebServiceTemplate(webServiceTemplate);
		this.getWebServiceTemplate().setDefaultUri(NAMESPACE_URI);
	}
	
	/**
	 * 
	 * @param req
	 * @return
	 */
	public PofResponse call(PofRequest req) {;
		return (PofResponse) this.getWebServiceTemplate().marshalSendAndReceive(
				URL, req, new WebServiceMessageCallback() {
					
			public void doWithMessage(WebServiceMessage message) {
				SoapMessage soapMessage = ((SoapMessage) message);
				soapMessage.setSoapAction(NAMESPACE_URI);

	            SoapHeader soapHeader = soapMessage.getSoapHeader();
	            QName wsaToQName = new QName("http://www.w3.org/2005/08/addressing", "To", "wsa");
	            SoapHeaderElement wsaTo =  soapHeader.addHeaderElement(wsaToQName);
	            wsaTo.setText(NAMESPACE_URI);
	             
	            QName wsaActionQName = new QName("http://www.w3.org/2005/08/addressing", "Action", "wsa");
	            SoapHeaderElement wsaAction =  soapHeader.addHeaderElement(wsaActionQName);
	            wsaAction.setText(NAMESPACE_URI);
			}
			
		});
	}

}
