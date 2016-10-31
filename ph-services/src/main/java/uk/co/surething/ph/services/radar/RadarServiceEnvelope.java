package uk.co.surething.ph.services.radar;

import uk.co.surething.ph.models.radar.Root;
import uk.co.surething.ph.services.AServiceEnvelope;

public class RadarServiceEnvelope extends AServiceEnvelope {

	private Root body;

	public RadarServiceEnvelope() {
		
	}
	
	public RadarServiceEnvelope(Root root) {
		this.body = (Root) body;
	}
	
	@Override
	public void setBody(Object body) {
		this.body = (Root) body;
	}

	@Override
	public Object getBody() {
		return body;
	}

	
}
