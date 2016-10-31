package uk.co.surething.ph.models;

import java.io.IOException;

import org.apache.ws.commons.schema.resolver.URIResolver;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.xml.xsd.commons.CommonsXsdSchemaCollection;
import org.xml.sax.InputSource;

import uk.co.surething.ph.common.CommonConfig;
import uk.co.surething.ph.common.Constants;

@Configuration
@Import(CommonConfig.class)
@ComponentScan
public class ModelsConfig {

	@Bean(name = Constants.BEAN_PH_SCHEMA)
	public CommonsXsdSchemaCollection phSchema() {
		String phSchemaPath = "xsd/ph/ph.xsd";
		String cdlInSchemaPath = "xsd/cdl/requestMotor.xsd";
		String cdlOutSchemaPath = "xsd/cdl/responseMotor.xsd";
		Resource[] schemas = new Resource[] { 
				new ClassPathResource(phSchemaPath) 
				, new ClassPathResource(cdlInSchemaPath) 
				, new ClassPathResource(cdlOutSchemaPath) 
		};
		CommonsXsdSchemaCollection ret = new CommonsXsdSchemaCollection(schemas);
		ret.setUriResolver(new URIResolver() {
			@Override
			public InputSource resolveEntity(String targetNamespace, String arg1, String arg2) {
				InputSource is = null;
				try {
					if ("http://www.cdl.co.uk/schemas/ihp/request".equals(targetNamespace)) {
						is = new InputSource(new ClassPathResource(cdlInSchemaPath).getInputStream());
					} else if ("http://www.cdl.co.uk/schemas/ihp/response".equals(targetNamespace)) {
						is = new InputSource(new ClassPathResource(cdlOutSchemaPath).getInputStream());
					} else if ("http://www.surething.co.uk/schemas/ph/radar".equals(targetNamespace)) {
						//is = new InputSource(new ClassPathResource("xsd/radar/radar_v1.xsd").getInputStream());
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
				return is;
			}
		});
		ret.setInline(true);
		return ret;
	}

	
	/**
	 * 
	 * @return
	 */
	@Bean(name = Constants.BEAN_JAXB_MARSHALLER)
	public Jaxb2Marshaller jaxb2Marshaller() {
		Jaxb2Marshaller marshaller = new Jaxb2Marshaller();
		marshaller.setPackagesToScan("uk.co.surething.ph.*");
		return marshaller;
	}
}
