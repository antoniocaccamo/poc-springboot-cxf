package me.antoniocaccamo.cxf.prime.client.jaxws.config;

import me.antoniocaccamo.cxf.prime.xsd.ObjectFactory;
import org.apache.commons.lang3.StringUtils;
import org.apache.cxf.Bus;
import org.apache.cxf.configuration.jsse.TLSClientParameters;
import org.apache.cxf.endpoint.Client;
import org.apache.cxf.frontend.ClientProxy;
import org.apache.cxf.jaxws.EndpointImpl;
import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;
import org.apache.cxf.transport.http.HTTPConduit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import me.antoniocaccamo.cxf.prime.wsdl.HelloWorldService;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.TrustManagerFactory;
import javax.xml.ws.Endpoint;
import java.io.FileInputStream;
import java.security.KeyStore;
import java.security.NoSuchAlgorithmException;

@Configuration
public class CxfPrimeClientJaxWsConfiguration {


    @Value("${client.HelloWorldService.address}")
    private String address;

    @Value("${ssl.trust-store}")
    private String trustStore;

    @Value("${ssl.trust-store-password}")
    private String trustStorePassword;

    @Bean
    public ObjectFactory helloWorldObjectFactory(){
        return new ObjectFactory();
    }

    @Bean
    public HelloWorldService helloWorldService() throws Exception {
        JaxWsProxyFactoryBean jaxWsProxyFactoryBean = new JaxWsProxyFactoryBean();
        jaxWsProxyFactoryBean.setAddress(address);
        jaxWsProxyFactoryBean.setServiceClass(HelloWorldService.class);

        HelloWorldService proxy = (HelloWorldService) jaxWsProxyFactoryBean.create();

        if (StringUtils.startsWithIgnoreCase(address, "https") ) {
            configureSSL(proxy);
        }
        return proxy;
    }

    private void configureSSL(Object proxy) throws Exception {

        KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
        keyStore.load(new FileInputStream(trustStore), trustStorePassword.toCharArray());

        KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
        keyManagerFactory.init(keyStore, trustStorePassword.toCharArray());

        TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        trustManagerFactory.init(keyStore);

        TLSClientParameters tlsClientParameters = new TLSClientParameters();
        tlsClientParameters.setDisableCNCheck(true);
        tlsClientParameters.setKeyManagers(keyManagerFactory.getKeyManagers());
        tlsClientParameters.setTrustManagers(trustManagerFactory.getTrustManagers());

        Client client = ClientProxy.getClient(proxy);
        HTTPConduit httpConduit = (HTTPConduit) client.getConduit();
        httpConduit.setTlsClientParameters(tlsClientParameters);

    }

}
