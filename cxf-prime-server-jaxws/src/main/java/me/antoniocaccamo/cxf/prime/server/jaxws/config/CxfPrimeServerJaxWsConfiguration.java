package me.antoniocaccamo.cxf.prime.server.jaxws.config;

import me.antoniocaccamo.cxf.prime.impl.HelloWorldServiceImpl;
import org.apache.cxf.Bus;
import org.apache.cxf.jaxws.EndpointImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import me.antoniocaccamo.cxf.prime.wsdl.HelloWorldService;

import javax.xml.ws.Endpoint;

@Configuration
public class CxfPrimeServerJaxWsConfiguration {



    @Autowired
    private Bus bus;




    @Bean
    public Endpoint endpoint() {
        EndpointImpl endpoint = new EndpointImpl(bus, helloWorldService());
        endpoint.publish("/hello");
        return endpoint;
    }

    @Bean
    public HelloWorldService helloWorldService() {

        return new HelloWorldServiceImpl();
    }
}
