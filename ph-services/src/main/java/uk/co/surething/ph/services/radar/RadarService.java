package uk.co.surething.ph.services.radar;

import java.io.ByteArrayOutputStream;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;

import javax.annotation.PostConstruct;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.Marshaller;
import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.namespace.NamespaceContext;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamWriter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;

import uk.co.surething.ph.common.Constants;
import uk.co.surething.ph.models.radar.ObjectFactory;
import uk.co.surething.ph.models.radar.Root;
import uk.co.surething.ph.models.radar.dpo.PofRequest;
import uk.co.surething.ph.models.radar.dpo.PofResponse;
import uk.co.surething.ph.models.radar.dpo.PofrInformationCollectionDataContract;
import uk.co.surething.ph.models.radar.dpo.PofrInformationDataContract;
import uk.co.surething.ph.services.AService;
import uk.co.surething.ph.services.AServiceEnvelope;
import uk.co.surething.ph.services.AServiceResult;

public class RadarService extends AService {

	
	@Autowired
	@Qualifier(Constants.BEAN_JAXB_MARSHALLER)
	private Jaxb2Marshaller jaxb2Marshaller;
	
	@Autowired
	private RadarServicesSupport radarServicesSupport;
	
	@PostConstruct
	public void init() {
	}
	
	
	@Override
	public AServiceResult execute(AServiceEnvelope params) {
		RadarServiceResult result = new RadarServiceResult();
		RadarServiceEnvelope envelope = (RadarServiceEnvelope) params;
		Root root = (Root) envelope.getBody();
		PofRequest req = getRequest(root);
		PofResponse response = radarServicesSupport.call(req);
		//JAXBElement<PofInformationCollectionDataContract> resp = response.getPofCollection();
		System.out.println("Response error code: " + response.getErrorCode().getValue());
		System.out.println("Response error msg: " + response.getErrorMessage().getValue());
		return result;
	}
	
	



	/**
	 * 
	 * @param root
	 * @return
	 */
	public PofRequest getRequest(Root root) {
		String quoteStr = marshall(root);
		
		PofrInformationDataContract pofr = new PofrInformationDataContract();
		pofr.setPofr(quoteStr);
		uk.co.surething.ph.models.radar.dpo.ObjectFactory o = new uk.co.surething.ph.models.radar.dpo.ObjectFactory();
		PofrInformationCollectionDataContract pofrColl = o.createPofrInformationCollectionDataContract();
		pofrColl.getPofrInformationDataContract().add(pofr);
		JAXBElement<PofrInformationCollectionDataContract> pofrCollElement = o.createPofRequestPofrCollection(pofrColl);
		PofRequest req = new PofRequest();
		req.setPofrCollection(pofrCollElement);
		
		GregorianCalendar c = new GregorianCalendar();
		c.setTime(new Date());
		
		XMLGregorianCalendar cal;
		try {
			cal = DatatypeFactory.newInstance().newXMLGregorianCalendar(c);
			cal.setTimezone(0);

			System.out.println(cal);
			req.setTime(cal);
		} catch (DatatypeConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return req;
	}
	
	/**
	 * 
	 * @param root
	 * @return
	 */
	public String marshall(Root root) {
		JAXBContext context = jaxb2Marshaller.getJaxbContext();
		return marshall(root, context);
	}
	

	/**
	 * 
	 * @param root
	 * @param context
	 * @return
	 */
	public static String marshall(Root root, JAXBContext context) {
		ByteArrayOutputStream os = null;
		Marshaller marshaller = null;
		try {
			os = new ByteArrayOutputStream();
			try {
				marshaller = context.createMarshaller();
				marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
				marshaller.setProperty(Marshaller.JAXB_ENCODING, Constants.UTF_16);
				ObjectFactory o = new uk.co.surething.ph.models.radar.ObjectFactory();
				JAXBElement<Object> rootElement = o.createRoot(root);
				XMLStreamWriter writer = XMLOutputFactory.newInstance().createXMLStreamWriter(os, Constants.UTF_16);
				writer.setNamespaceContext(new NamespaceContext() {
					public Iterator<String> getPrefixes(String namespaceURI) {
					    return null;
					}
					
					public String getPrefix(String namespaceURI) {
					    return "";
					}
					
					public String getNamespaceURI(String prefix) {
					    return null;
					}
		        });

				marshaller.marshal(rootElement, writer);
				byte[] bytes = os.toByteArray();
				return new String(bytes, Constants.UTF_16);
			} finally {
				if (os != null) os.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return null;
	}
}
