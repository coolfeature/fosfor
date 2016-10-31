package uk.co.surething.ph.models;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.namespace.QName;
import javax.xml.transform.Source;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamSource;

import org.apache.ws.commons.schema.XmlSchema;
import org.apache.ws.commons.schema.XmlSchemaCollection;
import org.apache.ws.commons.schema.XmlSchemaSerializer;
import org.springframework.beans.BeanInstantiationException;
import org.springframework.beans.BeanUtils;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.util.Assert;
import org.springframework.xml.validation.XmlValidator;
import org.springframework.xml.validation.XmlValidatorFactory;
import org.springframework.xml.xsd.XsdSchema;
import org.springframework.xml.xsd.commons.CommonsXsdSchemaException;
import org.w3c.dom.Document;

public class PhSchema implements XsdSchema {

	private final XmlSchema schema;

	private final XmlSchemaCollection collection;

	public PhSchema(XmlSchema schema) {
		this(schema, null);
	}

	public PhSchema(XmlSchema schema, XmlSchemaCollection collection) {
		Assert.notNull(schema, "'schema' must not be null");
		this.schema = schema;
		this.collection = collection;
	}

	@Override
	public String getTargetNamespace() {
		return schema.getTargetNamespace();
	}

	public QName[] getElementNames() {
		List<QName> result = new ArrayList<QName>(schema.getElements().keySet());
		return result.toArray(new QName[result.size()]);
	}

	@Override
	public Source getSource() {
		// try to use the the package-friendly XmlSchemaSerializer first, fall
		// back to slower stream-based version
		try {
			XmlSchemaSerializer serializer = BeanUtils.instantiateClass(XmlSchemaSerializer.class);
			if (collection != null) {
				serializer.setExtReg(collection.getExtReg());
			}
			Document[] serializedSchemas = serializer.serializeSchema(schema, false);
			return new DOMSource(serializedSchemas[0]);
		} catch (BeanInstantiationException ex) {
			// ignore
		} catch (XmlSchemaSerializer.XmlSchemaSerializerException ex) {
			// ignore
		}
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		try {
			schema.write(bos);
		} catch (UnsupportedEncodingException ex) {
			throw new CommonsXsdSchemaException(ex.getMessage(), ex);
		}
		ByteArrayInputStream bis = new ByteArrayInputStream(bos.toByteArray());
		return new StreamSource(bis);
	}

	@Override
	public XmlValidator createValidator() {
		try {
			Resource resource = new UrlResource(schema.getSourceURI());
			return XmlValidatorFactory.createValidator(resource, XmlValidatorFactory.SCHEMA_W3C_XML);
		} catch (IOException ex) {
			throw new CommonsXsdSchemaException(ex.getMessage(), ex);
		}
	}

	/** Returns the wrapped Commons {@code XmlSchema} object. */
	public XmlSchema getSchema() {
		return schema;
	}

	public String toString() {
		StringBuilder builder = new StringBuilder("CommonsXsdSchema");
		builder.append('{');
		builder.append(getTargetNamespace());
		builder.append('}');
		return builder.toString();
	}
}
