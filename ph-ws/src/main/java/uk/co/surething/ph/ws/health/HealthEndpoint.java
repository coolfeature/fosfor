package uk.co.surething.ph.ws.health;

import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.codahale.metrics.annotation.Timed;

import uk.co.surething.ph.ws.AEndpoint;

@Controller
@ResponseBody
public class HealthEndpoint extends AEndpoint {

	
	/**
	 * 
	 * @param response
	 * @return
	 */
	@RequestMapping(
			path = HEALTH
			, method = RequestMethod.GET )
	public String healthCheck(final HttpServletResponse response) {
		response.setHeader("Cache-Control", "must-revalidate,no-cache,no-store");
		return "OK";
	}
	
	
	/**
	 * 
	 * @return
	 */
	@Timed
	@RequestMapping("/health/ping")
	public String ping() {
		return "pong";
	}
	
}
