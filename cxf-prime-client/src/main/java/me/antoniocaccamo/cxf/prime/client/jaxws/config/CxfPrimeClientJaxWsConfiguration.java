package me.antoniocaccamo.cxf.prime.client.jaxws.config;

import me.antoniocaccamo.cxf.prime.xsd.ObjectFactory;
import org.apache.cxf.Bus;
import org.apache.cxf.jaxws.EndpointImpl;
import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import me.antoniocaccamo.cxf.prime.wsdl.HelloWorldService;

import javax.xml.ws.Endpoint;

@Configuration
public class CxfPrimeClientJaxWsConfiguration {


    @Value("${client.HelloWorldService.address}")
    private String address;

    @Bean
    public HelloWorldService helloWorldService() {
        JaxWsProxyFactoryBean jaxWsProxyFactoryBean = new JaxWsProxyFactoryBean();
        jaxWsProxyFactoryBean.setAddress(address);
        jaxWsProxyFactoryBean.setServiceClass(HelloWorldService.class);
        return (HelloWorldService) jaxWsProxyFactoryBean.create();
    }

    @Bean
    public ObjectFactory helloWorldObjectFactory(){
        return new ObjectFactory();
    }



}
