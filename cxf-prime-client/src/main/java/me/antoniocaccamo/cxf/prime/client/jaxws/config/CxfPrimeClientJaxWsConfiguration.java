package me.antoniocaccamo.cxf.prime.client.jaxws.config;

import lombok.extern.slf4j.Slf4j;
import me.antoniocaccamo.cxf.prime.callback.CxfPrimeCallbackHandler;
import me.antoniocaccamo.cxf.prime.wsdl.HelloWorldService;
import me.antoniocaccamo.cxf.prime.xsd.ObjectFactory;
import org.apache.commons.lang3.StringUtils;
import org.apache.cxf.configuration.jsse.TLSClientParameters;
import org.apache.cxf.endpoint.Client;
import org.apache.cxf.frontend.ClientProxy;
import org.apache.cxf.interceptor.LoggingInInterceptor;
import org.apache.cxf.interceptor.LoggingOutInterceptor;
import org.apache.cxf.jaxws.JaxWsProxyFactoryBean;
import org.apache.cxf.transport.http.HTTPConduit;
import org.apache.cxf.ws.security.SecurityConstants;
import org.apache.cxf.ws.security.wss4j.WSS4JInInterceptor;
import org.apache.cxf.ws.security.wss4j.WSS4JOutInterceptor;
import org.apache.wss4j.dom.handler.WSHandlerConstants;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.TrustManagerFactory;
import javax.xml.ws.BindingProvider;
import java.io.FileInputStream;
import java.security.KeyStore;
import java.util.HashMap;
import java.util.Map;

@Configuration @Slf4j
public class CxfPrimeClientJaxWsConfiguration {


    @Value("${client.HelloWorldService.address}")
    private String address;

    @Value("${ssl.trust-store}")
    private String trustStore;

    @Value("${ssl.trust-store-password}")
    private String trustStorePassword;

    @Value("${wss4j.enabled}")
    private Boolean wss4jEnabled;

    @Value("${wss4j.signature.file}")
    private String wss4jSignaturePropsFile;

    @Value("${wss4j.signature.username}")
    private String wss4jSignatureUsername;

    @Value("${wss4j.encrypt.file}")
    private String wss4jEncryptPropsFile;

    @Value("${wss4j.encrypt.username}")
    private String wss4jEncryptUsername;

    @Value("${wss4j.callback.keypairs}")
    private String keypairs;


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


        Client client = ClientProxy.getClient(proxy);

        if (StringUtils.startsWithIgnoreCase(address, "https") ) {
            configureSSL(client);
        }

        if ( wss4jEnabled ) {
            Map<String, Object> wss4jMap = new HashMap<>();

            wss4jMap.put(WSHandlerConstants.ACTION,
                            WSHandlerConstants.TIMESTAMP + " " +
                            WSHandlerConstants.SIGNATURE + " " +
                            WSHandlerConstants.ENCRYPT
            );
            wss4jMap.put(WSHandlerConstants.PW_CALLBACK_REF, new CxfPrimeCallbackHandler(keypairs));
            wss4jMap.put(WSHandlerConstants.SIGNATURE_USER  , wss4jSignatureUsername);
            wss4jMap.put(WSHandlerConstants.SIG_PROP_FILE, StringUtils.replace(wss4jSignaturePropsFile, "\\","/"));
            wss4jMap.put(WSHandlerConstants.SIGNATURE_PARTS, "{}{http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd}Timestamp;{}{}Body;");

            wss4jMap.put(WSHandlerConstants.ENCRYPTION_USER , wss4jEncryptUsername);
            wss4jMap.put(WSHandlerConstants.ENC_PROP_FILE, StringUtils.replace(wss4jEncryptPropsFile, "\\","/"));
            wss4jMap.put(WSHandlerConstants.DEC_PROP_FILE, StringUtils.replace(wss4jEncryptPropsFile, "\\","/"));
            wss4jMap.put(WSHandlerConstants.ENCRYPTION_PARTS, "{}{}Body;");



            client.getOutInterceptors().add(new WSS4JOutInterceptor(wss4jMap));
            client.getInInterceptors().add( new WSS4JInInterceptor(wss4jMap));



            client.getEndpoint().getInInterceptors().add(  new LoggingInInterceptor());
            client.getEndpoint().getOutInterceptors().add( new LoggingOutInterceptor());

            log.info("wss4jMap : {}", wss4jMap);
        }
        return proxy;
    }

    private void configureSSL(Client client) throws Exception {

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


        HTTPConduit httpConduit = (HTTPConduit) client.getConduit();
        httpConduit.setTlsClientParameters(tlsClientParameters);

    }

}
