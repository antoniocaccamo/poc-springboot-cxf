package me.antoniocaccamo.cxf.prime.servlet;

import me.antoniocaccamo.cxf.prime.impl.HelloWorldServiceImpl;
import org.apache.cxf.Bus;
import org.apache.cxf.BusFactory;
import org.apache.cxf.transport.servlet.CXFNonSpringServlet;

import javax.servlet.annotation.WebServlet;
import javax.xml.ws.Endpoint;

@WebServlet(urlPatterns ="/services/*")
public class CxFPrimeServlet extends CXFNonSpringServlet {

    @Override
    protected void loadBus(javax.servlet.ServletConfig servletConfig) {
        super.loadBus(servletConfig);
        Bus bus = getBus();
        BusFactory.setDefaultBus(bus);
        Endpoint.publish("/hello", new HelloWorldServiceImpl());
    }
}
