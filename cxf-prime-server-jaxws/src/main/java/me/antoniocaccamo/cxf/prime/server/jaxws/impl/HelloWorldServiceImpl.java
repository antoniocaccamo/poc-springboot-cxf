package me.antoniocaccamo.cxf.prime.server.jaxws.impl;

import lombok.extern.slf4j.Slf4j;
import me.antoniocaccamo.cxf.prime.wsdl.HelloWorldService;
import me.antoniocaccamo.cxf.prime.xsd.Greeting;
import me.antoniocaccamo.cxf.prime.xsd.Person;
import org.springframework.ws.server.endpoint.annotation.Endpoint;

import javax.jws.WebService;

@WebService(
        endpointInterface = "me.antoniocaccamo.cxf.prime.wsdl.HelloWorldService" ,
        targetNamespace   = "http://antoniocaccamo.me/cxf/prime/wsdl",
        name = "HelloWorldService"
)  @Slf4j
public class HelloWorldServiceImpl implements HelloWorldService {

    public Greeting sayHello(Person person) {

        log.info("greeting person {}", person);

        Greeting greeting = new Greeting();
        greeting.setText(
                new StringBuilder("Hello ")
                        .append(person.getFirstName())
                        .append(" ")
                        .append(person.getLastName())
                        .append("!!!")
                .toString()
        );
        return greeting;
    }
}
