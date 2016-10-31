package uk.co.surething.ph.services;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.util.Assert;
import org.springframework.ws.client.core.WebServiceTemplate;
import org.springframework.ws.soap.SoapVersion;
import org.springframework.ws.soap.saaj.SaajSoapMessageFactory;
import org.springframework.ws.transport.http.HttpComponentsMessageSender;

import uk.co.surething.ph.common.Config;
import uk.co.surething.ph.common.Constants;
import uk.co.surething.ph.dao.DaoConfig;
import uk.co.surething.ph.services.core.CoreService;
import uk.co.surething.ph.services.radar.RadarService;

@Configuration
@Import(DaoConfig.class)
@ComponentScan
public class ServicesConfig {
	
	/**
	 * http://docs.spring.io/spring-framework/docs/current/javadoc-api/org/springframework/core/task/TaskExecutor.html
	 * @param queue
	 * @return
	 */
	@Bean(name = Constants.BEAN_ASYNC_CALLBACK_EXECUTOR)
	public ThreadPoolTaskExecutor beanThreadPoolCallbackTaskExecutor() {

		int queueCapacity = Config.getIntProperty("ph.lookupPool.queueCapacity");
		int corePoolSize = Config.getIntProperty("ph.lookupPool.corePoolSize");
		int maxPoolSize = Config.getIntProperty("ph.lookupPool.maxPoolSize"); 
		int keepAliveTime = Config.getIntProperty("ph.lookupPool.keepAliveTime"); 
		
		ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
		executor.setCorePoolSize(corePoolSize);
        executor.setMaxPoolSize(maxPoolSize);
        executor.setQueueCapacity(queueCapacity);
        executor.setKeepAliveSeconds(keepAliveTime);
		Assert.notNull(executor);
	    return executor;
	}
	
	
	/**
	 * 
	 * @return
	 */
	@Bean(name = Constants.BEAN_RADAR_HTTP_CLIENT)
	public HttpComponentsMessageSender getHttpComponentsMessageSender() {
		HttpComponentsMessageSender sender = new HttpComponentsMessageSender();
		int connTimeout = Config.getIntProperty("ph.radar.connection.timeout");
		sender.setConnectionTimeout(connTimeout);
		int socTimeout = Config.getIntProperty("ph.radar.socket.timeout");
		sender.setConnectionTimeout(socTimeout);
		return sender;
	}
	
	/**
	 * 
	 * @return
	 */
	@Bean(name = Constants.BEAN_WEB_SERVICE_TEMPLATE)
	public WebServiceTemplate webServiceTemplate(
			@Qualifier(Constants.BEAN_JAXB_MARSHALLER) Jaxb2Marshaller marshaller
			, @Qualifier(Constants.BEAN_RADAR_HTTP_CLIENT) HttpComponentsMessageSender httpSender
			) {
		SaajSoapMessageFactory messageFactory = new SaajSoapMessageFactory();
		messageFactory.setSoapVersion(SoapVersion.SOAP_12);
		messageFactory.afterPropertiesSet();
		
		WebServiceTemplate webServiceTemplate = new WebServiceTemplate();
		webServiceTemplate.setMessageFactory(messageFactory);
		webServiceTemplate.setMarshaller(marshaller);
		webServiceTemplate.setUnmarshaller(marshaller);
		webServiceTemplate.setMessageSender(httpSender);
		webServiceTemplate.afterPropertiesSet();
		
		return webServiceTemplate;
	}
	
	
	/**
	 * 
	 * @return
	 */
	@Bean(name = Constants.BEAN_SVC_CORE)
	public CoreService beanSvcCore() {
		CoreService svc = new CoreService();
	    return svc;
	}
	
	
	/**
	 * 
	 * @return
	 */
	@Bean(name = Constants.BEAN_SVC_RADAR)
	public RadarService beanSvcRadar() {
		RadarService svc = new RadarService();
	    return svc;
	}
}
