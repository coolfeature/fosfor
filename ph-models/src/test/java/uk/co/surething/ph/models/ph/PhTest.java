package uk.co.surething.ph.models.ph;

import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.junit.Ignore;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;

import uk.co.surething.ph.common.Constants;
import uk.co.surething.ph.models.ModelsTestBase;
import uk.co.surething.ph.models.Samples;

public class PhTest extends ModelsTestBase {

	private static final String EXAMPLE_XML_PATH = "xml/rateRequest.xml";

	@Autowired
	@Qualifier(Constants.BEAN_JAXB_MARSHALLER)
	private Jaxb2Marshaller jaxb2Marshaller;
	
	@Ignore
	@Test
	public void unmarshall() {
		Resource resource = null;
		Object unmarshalledObject = null;
		try {
			resource = new ClassPathResource(EXAMPLE_XML_PATH);
	        StreamSource source = new StreamSource();
	        source.setInputStream(resource.getInputStream());
	        Map<String, Object> props = new HashMap<String, Object>();
	        props.put(javax.xml.bind.Marshaller.JAXB_FORMATTED_OUTPUT, true);
	        jaxb2Marshaller.setUnmarshallerProperties(props);
			unmarshalledObject = (GetRateRequest) jaxb2Marshaller.unmarshal(source);
		} catch (IOException e) {
			e.printStackTrace();
		}
		System.out.println(unmarshalledObject);
	}

	@Test
	public void marshall() {
		GetRateRequest rr = Samples.getRateRequest();
		java.io.StringWriter sw = new StringWriter();
        Map<String, Object> props = new HashMap<String, Object>();
        props.put(javax.xml.bind.Marshaller.JAXB_FORMATTED_OUTPUT, true);
        jaxb2Marshaller.setMarshallerProperties(props);
		jaxb2Marshaller.marshal(rr, new StreamResult(sw));
		System.out.println("\n\n\n" + sw.toString() + "\n\n\n");
	}
	
}
