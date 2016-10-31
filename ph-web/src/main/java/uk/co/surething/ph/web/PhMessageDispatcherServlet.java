package uk.co.surething.ph.web;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.ws.transport.http.MessageDispatcherServlet;
import org.springframework.ws.wsdl.WsdlDefinition;

import uk.co.surething.ph.common.Constants;
import uk.co.surething.ph.ws.AEndpoint;

public class PhMessageDispatcherServlet extends MessageDispatcherServlet {


	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public PhMessageDispatcherServlet(WebApplicationContext webApplicationContext) {
		super(webApplicationContext);
	}
	
	@Override
	protected WsdlDefinition getWsdlDefinition(HttpServletRequest request) {
		WsdlDefinition wsdlDefinition = super.getWsdlDefinition(request);
		if (wsdlDefinition == null) {
			String requestUri = request.getRequestURI();
			if (requestUri.contains(AEndpoint.WS_RATE)) {
				String wsdlParam = request.getParameter("wsdl");
				if (wsdlParam != null) {
					WebApplicationContext context  = this.getWebApplicationContext();
					wsdlDefinition = (WsdlDefinition) context.getBean(Constants.BEAN_WSDL);
				}
			}
		}
		return wsdlDefinition;
	}
	
}
