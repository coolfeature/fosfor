package uk.co.surething.ph.ws.metrics;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.codahale.metrics.MetricRegistry;

import uk.co.surething.ph.ws.AEndpoint;

@Controller
@ResponseBody
public class MetricEndpoint extends AEndpoint {

	@Autowired
	private MetricRegistry metricRegistry;
	
	
	/**
	 * 
	 * @param response
	 * @return
	 */
	@RequestMapping(
			path = METRICS
			, method = RequestMethod.GET
			, produces = { MediaType.APPLICATION_JSON_VALUE })
	public MetricRegistry getMetrics(final HttpServletResponse response) {
		response.setHeader("Cache-Control", "must-revalidate,no-cache,no-store");
		return metricRegistry;
	}
}
