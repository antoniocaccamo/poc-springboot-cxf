package me.antoniocaccamo.cxf.prime.impl;

import me.antoniocaccamo.cxf.prime.xsd.Greeting;
import me.antoniocaccamo.cxf.prime.wsdl.HelloWorldService;
import me.antoniocaccamo.cxf.prime.xsd.Person;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.jws.WebService;

@WebService(
        endpointInterface = "import me.antoniocaccamo.cxf.prime.wsdl.HelloWorldService" ,
        targetNamespace   = "http://antoniocaccamo.me/cxf/prime/wsdl",
        name = "HelloWorldService"
)
public class HelloWorldServiceImpl implements HelloWorldService {

    private static final Logger logger = LoggerFactory.getLogger(HelloWorldServiceImpl.class.getName());

    public Greeting sayHello(Person person) {

        logger.info("greeting person {}", person);

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
