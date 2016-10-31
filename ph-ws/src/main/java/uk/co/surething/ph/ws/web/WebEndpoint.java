package uk.co.surething.ph.ws.web;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.codahale.metrics.MetricRegistry;

import uk.co.surething.ph.ws.AEndpoint;

@Controller
@ResponseBody
public class WebEndpoint extends AEndpoint {

	@Autowired
	private ServletContext servletContext;
	
	@Autowired
	private MetricRegistry metricRegistry;
	
	/**
	 * 
	 * @return
	 */
	@RequestMapping(ROOT)
	public ModelAndView index() {
		LOGGER.info("Rendering index... ");
		Map<String, Object> map = new HashMap<String, Object>();
		ModelAndView model = new ModelAndView("index");
		
		String contextPath = servletContext.getContextPath();
		
		//String metrics = metricRegistry.
		//System.out.println("metrics: " + metrics);
		Map<String, String> restPaths = new HashMap<String, String>();
		restPaths.put("RATE", contextPath + API_RATE);
		restPaths.put("METRICS", contextPath + METRICS);
		restPaths.put("PING", contextPath + HEALTH_PING);
		map.put("restPaths", restPaths);
		
		Map<String, String> soapPaths = new HashMap<String, String>();
		soapPaths.put("RATE", contextPath + WS_RATE + "?wsdl");
		map.put("soapPaths", soapPaths);
		
		model.addObject("data", map);
		//model.addObject("metrics", metrics);
		return model;
	}
	
	/**
	 * 
	 * https://github.com/dropwizard/metrics/blob/9cd8f5b03285515cab6e98d49bb314cb3dd952f3/metrics-core/src/main/java/com/codahale/metrics/ConsoleReporter.java
	 * @return
	 */
	private void formatMetrics() {

	}
	
}
