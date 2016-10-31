package uk.co.surething.ph.models;

import java.time.ZonedDateTime;

import javax.xml.bind.annotation.adapters.XmlAdapter;

import uk.co.surething.ph.common.Utils;

public class DateTimeSerializer extends XmlAdapter<String, ZonedDateTime> {

	@Override
	public String marshal(ZonedDateTime date) throws Exception {
		return Utils.toString(date);
	}

	@Override
	public ZonedDateTime unmarshal(String date) throws Exception {
		return Utils.getZonedDateTime(date);
	}

}
