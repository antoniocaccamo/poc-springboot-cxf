package me.antoniocaccamo.cxf.prime.client.jaxws;


import lombok.extern.slf4j.Slf4j;
import me.antoniocaccamo.cxf.prime.xsd.Greeting;
import me.antoniocaccamo.cxf.prime.xsd.ObjectFactory;
import me.antoniocaccamo.cxf.prime.xsd.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import me.antoniocaccamo.cxf.prime.wsdl.HelloWorldService;

@SpringBootApplication @Slf4j
public class CxfPrimeClientJaxWsApplication implements ApplicationRunner {

    @Autowired
    private HelloWorldService helloWorldService;

    @Autowired
    private ObjectFactory helloWorldObjectFactory;


    public static void main(String[] args) {
        SpringApplication.run(CxfPrimeClientJaxWsApplication.class, args);
    }


    @Override
    public void run(ApplicationArguments args) throws Exception {
        log.info("args {} ", args);

        Person person = helloWorldObjectFactory.createPerson();
        person.setFirstName("me");
        person.setLastName("my self");

        Greeting greeting = helloWorldService.sayHello(person);

        log.info("greeting : {}", greeting);

    }
}
