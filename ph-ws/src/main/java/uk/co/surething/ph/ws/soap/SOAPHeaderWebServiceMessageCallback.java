package uk.co.surething.ph.ws.soap;

import java.io.IOException;

import javax.xml.soap.Name;
import javax.xml.soap.SOAPElement;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPHeader;
import javax.xml.soap.SOAPHeaderElement;
import javax.xml.soap.SOAPMessage;
import javax.xml.soap.SOAPPart;
import javax.xml.transform.TransformerException;

import org.springframework.ws.WebServiceMessage;
import org.springframework.ws.client.core.WebServiceMessageCallback;
import org.springframework.ws.soap.saaj.SaajSoapMessage;

public class SOAPHeaderWebServiceMessageCallback implements WebServiceMessageCallback {

    public static final String WSS_10_NAMESPACE = "http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-secext-1.0.xsd";

    public final static String WSSE_Security_Elem = "Security";

    public final static String WSSE_Security_prefix = "wsse";

    public final static String WSSE_UsernameToken_Elem = "UsernameToken";

    public final static String WSSE_Username_Elem = "Username";

    public final static String WSSE_Password_Elem = "Password";

    private String user;
    private String pass;


    public SOAPHeaderWebServiceMessageCallback(String user, String pass) {
        this.user = user;
        this.pass = pass;
    }

    /**
     * @see org.springframework.ws.client.core.WebServiceMessageCallback#doWithMessage(org.springframework.ws.WebServiceMessage)
     */
    public void doWithMessage(WebServiceMessage message) throws IOException, TransformerException {

        try {

        	/* to set Action
			SoapMessage soapMessage = ((SoapMessage) message);
			soapMessage.setSoapAction(NAMESPACE_URI);

			SoapHeader soapHeader = soapMessage.getSoapHeader();
			QName wsaToQName = new QName("http://www.w3.org/2005/08/addressing", "To", "wsa");
			SoapHeaderElement wsaTo = soapHeader.addHeaderElement(wsaToQName);
			wsaTo.setText(NAMESPACE_URI + "/rate");

			QName wsaActionQName = new QName("http://www.w3.org/2005/08/addressing", "Action", "wsa");
			SoapHeaderElement wsaAction = soapHeader.addHeaderElement(wsaActionQName);
			wsaAction.setText("getRate");
			*/
        	
            // you have to use the default SAAJWebMessageFactory 
            SaajSoapMessage saajSoapMessage = (SaajSoapMessage) message;
            SOAPMessage soapMessage = saajSoapMessage.getSaajMessage();

            // then grab for the SOAP header, and...
            SOAPPart soapPart = soapMessage.getSOAPPart();
            SOAPEnvelope soapEnvelope = soapPart.getEnvelope();
            SOAPHeader soapHeader = soapEnvelope.getHeader();

            // ... add the WS-Security Header Element
            Name headerElementName = soapEnvelope.createName(WSSE_Security_Elem,
                    WSSE_Security_prefix, WSS_10_NAMESPACE);
            SOAPHeaderElement soapHeaderElement = soapHeader.addHeaderElement(headerElementName);

            // otherwise a RST without appliesTo fails
            soapHeaderElement.setMustUnderstand(true);

            // add the usernameToken to "Security" soapHeaderElement
            SOAPElement usernameTokenSOAPElement = soapHeaderElement
                    .addChildElement(WSSE_UsernameToken_Elem);

            // add the username to usernameToken
            SOAPElement userNameSOAPElement = usernameTokenSOAPElement
                    .addChildElement(WSSE_Username_Elem);
            userNameSOAPElement.addTextNode(user);

            // add the password to usernameToken
            SOAPElement passwordSOAPElement = usernameTokenSOAPElement
                    .addChildElement(WSSE_Password_Elem);
            passwordSOAPElement.addTextNode(pass);

        } catch (SOAPException soapException) {
            throw new RuntimeException("WSSEHeaderWebServiceMessageCallback", soapException);
        }
    }
}