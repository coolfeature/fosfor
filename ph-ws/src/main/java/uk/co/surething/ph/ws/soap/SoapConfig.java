package uk.co.surething.ph.ws.soap;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.ws.config.annotation.EnableWs;
import org.springframework.ws.config.annotation.WsConfigurerAdapter;
import org.springframework.ws.server.EndpointInterceptor;
import org.springframework.ws.soap.security.wss4j.Wss4jSecurityInterceptor;
import org.springframework.ws.soap.security.wss4j.callback.SimplePasswordValidationCallbackHandler;
import org.springframework.ws.wsdl.wsdl11.DefaultWsdl11Definition;
import org.springframework.xml.xsd.commons.CommonsXsdSchemaCollection;

import uk.co.surething.ph.common.Constants;
import uk.co.surething.ph.ws.AEndpoint;

@Configuration
@EnableWs
public class SoapConfig extends WsConfigurerAdapter {

	protected static final Logger LOGGER = Logger.getLogger(WsConfigurerAdapter.class);
	
	@Autowired
	@Qualifier(Constants.BEAN_PH_SCHEMA)
	private CommonsXsdSchemaCollection phSchema;
	
	@Bean(name = Constants.BEAN_WSDL)
	public DefaultWsdl11Definition defaultWsdl11Definition() {
		DefaultWsdl11Definition wsdl11Definition = new DefaultWsdl11Definition();
		wsdl11Definition.setPortTypeName(Constants.WS_PH_RATE_PORT_TYPE_NAME);
		wsdl11Definition.setLocationUri(AEndpoint.WS_RATE);
		wsdl11Definition.setTargetNamespace(Constants.NAMESPACE_PH);
		wsdl11Definition.setSchemaCollection(phSchema);
		
		Properties soapActions = new Properties();
		soapActions.setProperty("getRate", Constants.NAMESPACE_PH + "/getRate");
		wsdl11Definition.setSoapActions(soapActions);
		
		wsdl11Definition.setCreateSoap12Binding(true);
		LOGGER.info("Setting defaultWsdl11Definition");
		return wsdl11Definition;
	}
	
	@Bean
    public SimplePasswordValidationCallbackHandler callbackHandler() {
        SimplePasswordValidationCallbackHandler callbackHandler = new SimplePasswordValidationCallbackHandler();
        Map<String,String> userMap = new HashMap<String,String>();
        userMap.put("test", "test");
        callbackHandler.setUsersMap(userMap);
        return callbackHandler;
    }

	
	@Bean
	public Wss4jSecurityInterceptor wss4jSecurityInterceptor() throws Exception {
		Wss4jSecurityInterceptor interceptor = new Wss4jSecurityInterceptor();
		interceptor.setValidationActions("UsernameToken");
		interceptor.setValidationCallbackHandler(callbackHandler());
		return interceptor;
	}

	@Bean
	public WsConfigurerAdapter wsConfigurerAdapter() {
		return new WsConfigurerAdapter() {

			@Override
			public void addInterceptors(List<EndpointInterceptor> interceptors) {
				try {
					interceptors.add(wss4jSecurityInterceptor());
				} catch (Exception e) {
					e.printStackTrace();
				}
				super.addInterceptors(interceptors);
			}

		};

	}
	
}
