/**
 * 
 */
package me.antoniocaccamo.cxf.prime;

import me.antoniocaccamo.cxf.prime.impl.HelloWorldServiceImpl;
import org.apache.cxf.common.logging.Slf4jLogger;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.namespace.QName;
import javax.xml.ws.Endpoint;
import javax.xml.ws.Service;
import java.net.MalformedURLException;
import java.net.URL;

import static org.junit.Assert.*;

/**
 * @author conscaccamoantonio
 *
 */
public class CxFPrimeServletTest {

	private static final Logger logger = LoggerFactory.getLogger(CxFPrimeServletTest.class.getName());
	
	private static final String SERVICE_TNS  = "http://antoniocaccamo.me/cxf/prime";


	private static final String SERVICE  = "HelloWorldService";
	
	private static final String SERVICE_NAME  = SERVICE + "ImplService";
	private static final String SERVICE_PORT  = "HelloWorldService";
	
	private static final String SERVICE_URL   = "http://localhost:8080/services/hello";
	private static final String SERVICE_WSDL  = SERVICE_URL + "?wsdl";

	@Test
	public void test() throws MalformedURLException {
	    // Publishes the SOAP Web Service



	    Endpoint endpoint = Endpoint.publish(SERVICE_URL, new HelloWorldServiceImpl());

	    assertTrue(endpoint.isPublished());
	    assertEquals("http://schemas.xmlsoap.org/wsdl/soap/http", endpoint.getBinding().getBindingID());
	 
	    // Data to access the web service
	    URL wsdlDocumentLocation = new URL(SERVICE_WSDL);
	    String namespaceURI = SERVICE_TNS;
	    String servicePart  = SERVICE_NAME;
	    String portName     = SERVICE_PORT;
	    QName serviceQN     = new QName(SERVICE_TNS, SERVICE_NAME);
	    QName portQN        = new QName(SERVICE_TNS, SERVICE_PORT);
	 
	    // Creates a service instance
	    Service service = Service.create(wsdlDocumentLocation, serviceQN);
	    HelloWorldService helloWorldService = service.getPort(HelloWorldService.class);

	    // Invoke the web service
	    Person person = new ObjectFactory().createPerson();
		person.setFirstName("antonio");
		person.setLastName("caccamo");

	 
	    assertEquals(
	    		new StringBuilder("Hello ")
	                .append(person.getFirstName())
	                .append(" ")
	                .append(person.getLastName())
	                .append("!!!")
	                .toString(), 
                helloWorldService.sayHello(person).getText()
        );
	 
	    // Unpublishes the SOAP Web Service
	    endpoint.stop();
	    assertFalse(endpoint.isPublished());

	}

}
