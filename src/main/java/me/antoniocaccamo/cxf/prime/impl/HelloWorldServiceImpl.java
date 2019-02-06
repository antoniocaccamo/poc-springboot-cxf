package me.antoniocaccamo.cxf.prime.impl;

import me.antoniocaccamo.cxf.prime.Greeting;
import me.antoniocaccamo.cxf.prime.HelloWorldService;
import me.antoniocaccamo.cxf.prime.Person;

import javax.jws.WebService;

@WebService(endpointInterface = "me.antoniocaccamo.cxf.prime.HelloWorldService")
public class HelloWorldServiceImpl implements HelloWorldService {

    public Greeting sayHello(Person person) {

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
