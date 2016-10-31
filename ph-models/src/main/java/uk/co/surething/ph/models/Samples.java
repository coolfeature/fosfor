package uk.co.surething.ph.models;

import java.math.BigInteger;
import java.util.List;

import uk.co.surething.ph.models.cdl.in.PolDataType;
import uk.co.surething.ph.models.cdl.in.PolMessageType;
import uk.co.surething.ph.models.ph.GetRateRequest;
import uk.co.surething.ph.models.radar.Alarm;
import uk.co.surething.ph.models.radar.Quote;
import uk.co.surething.ph.models.radar.Root;

public class Samples {
	
	/**
	 * 
	 * @return
	 */
	public static Root getRoot() {
		Root root = new Root();
		Quote quote = new Quote();
		Alarm alarm = new Alarm();
		alarm.setValue("asdf");
		quote.setAlarm(alarm);
		root.setQuote(quote);
		return root;
	}
	
	/**
	 * 
	 * @return
	 */
	public static GetRateRequest getRateRequest() {
		GetRateRequest rr = new GetRateRequest();
		PolMessageType polMessageType = new PolMessageType();
		BigInteger version = new BigInteger("1");
		polMessageType.setVersion(version);
		List<PolDataType> polData = polMessageType.getPolData();
		PolDataType polDataType = new PolDataType();
		polData.add(polDataType);
		rr.setPolMessage(polMessageType);
		return rr;
	}

	
}
