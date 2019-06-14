package me.antoniocaccamo.cxf.prime.server.jaxrs;


import org.apache.cxf.Bus;
import org.apache.cxf.endpoint.Server;
import org.apache.cxf.jaxrs.JAXRSServerFactoryBean;
import org.apache.cxf.jaxrs.swagger.Swagger2Feature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import me.antoniocaccamo.cxf.prime.impl.HelloWorldServiceImpl;

import java.util.Arrays;

@SpringBootApplication
public class CxfPrimeServerJaxRsApplication {


    @Autowired
    private Bus bus;

    public static void main(String[] args) {
        SpringApplication.run(CxfPrimeServerJaxRsApplication.class, args);
    }

    @Bean
    public Server rsServer() {
        JAXRSServerFactoryBean endpoint = new JAXRSServerFactoryBean();
        endpoint.setBus(bus);
        endpoint.setAddress("/services");
        // Register 2 JAX-RS root resources supporting "/sayHello/{id}" and "/sayHello2/{id}" relative paths
        endpoint.setServiceBeans(Arrays.<Object>asList(new HelloWorldServiceImpl()));
        endpoint.setFeatures(Arrays.asList(new Swagger2Feature()));
        return endpoint.create();
    }

}
