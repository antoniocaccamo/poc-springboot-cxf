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




	}

}
