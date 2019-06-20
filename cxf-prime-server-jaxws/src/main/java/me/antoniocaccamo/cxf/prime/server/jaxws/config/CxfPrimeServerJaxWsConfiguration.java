package me.antoniocaccamo.cxf.prime.server.jaxws.config;

import lombok.extern.slf4j.Slf4j;
import me.antoniocaccamo.cxf.prime.callback.CxfPrimeCallbackHandler;
import me.antoniocaccamo.cxf.prime.feature.DynamicPolicyFeature;
import me.antoniocaccamo.cxf.prime.interceptor.DynamicInPolicyIntercptor;
import me.antoniocaccamo.cxf.prime.interceptor.DynamicOutPolicyIntercptor;
import me.antoniocaccamo.cxf.prime.server.jaxws.impl.HelloWorldServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.apache.cxf.Bus;
import org.apache.cxf.bus.spring.SpringBus;
import org.apache.cxf.endpoint.Endpoint;
import org.apache.cxf.interceptor.LoggingInInterceptor;
import org.apache.cxf.interceptor.LoggingOutInterceptor;
import org.apache.cxf.jaxws.EndpointImpl;
import org.apache.cxf.transport.servlet.CXFServlet;
import org.apache.cxf.ws.security.SecurityConstants;
import org.apache.cxf.ws.security.wss4j.DefaultCryptoCoverageChecker;
import org.apache.cxf.ws.security.wss4j.WSS4JInInterceptor;
import org.apache.cxf.ws.security.wss4j.WSS4JOutInterceptor;
import org.apache.wss4j.dom.handler.WSHandlerConstants;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration  @Slf4j
public class CxfPrimeServerJaxWsConfiguration     {


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

    @Value("${wss4j.policy.enabled}")
    private Boolean wss4jPolicyEnabled;

    @Value("${wss4j.policy.file}")
    private String wss4jPolicyFile;

    @Bean
    public ServletRegistrationBean dispachServlet() {
        return new ServletRegistrationBean(new CXFServlet(), "/services/*");
    }

    @Bean(name = Bus.DEFAULT_BUS_ID)
    public SpringBus springBus(){
        return new SpringBus();
    }

    @Bean
    public DynamicPolicyFeature dynamicPolicyFeature(){
        return new DynamicPolicyFeature(wss4jPolicyFile);
    }

    @Bean
    public EndpointImpl helloWorldServiceImpl() throws Exception {
        HelloWorldServiceImpl helloWorldService = new HelloWorldServiceImpl();

        EndpointImpl endpoint = new EndpointImpl(springBus(), helloWorldService);
        endpoint.publish("/hello");
        Endpoint cxfEndPoint = endpoint.getServer().getEndpoint();


        Map<String, Object> wss4jMap = new HashMap<>();

        if ( wss4jPolicyEnabled) {
            wss4jMap.put(SecurityConstants.CALLBACK_HANDLER    , new CxfPrimeCallbackHandler(keypairs));
            wss4jMap.put(SecurityConstants.ENCRYPT_USERNAME    , wss4jEncryptUsername);
            wss4jMap.put(SecurityConstants.ENCRYPT_PROPERTIES  , StringUtils.replace(wss4jEncryptPropsFile, "\\", "/"));
            wss4jMap.put(SecurityConstants.SIGNATURE_USERNAME  , wss4jSignatureUsername);
            wss4jMap.put(SecurityConstants.SIGNATURE_PROPERTIES, StringUtils.replace(wss4jSignaturePropsFile, "\\", "/"));

            cxfEndPoint.putAll(wss4jMap);
            cxfEndPoint.getInInterceptors().add(new DynamicInPolicyIntercptor(wss4jPolicyFile));
            cxfEndPoint.getOutInterceptors().add(new DynamicOutPolicyIntercptor(wss4jPolicyFile));


        } else {

            wss4jMap.put(WSHandlerConstants.ACTION,
                    WSHandlerConstants.TIMESTAMP + " " +
                            WSHandlerConstants.SIGNATURE + " " +
                            WSHandlerConstants.ENCRYPT
            );
            wss4jMap.put(WSHandlerConstants.PW_CALLBACK_REF, new CxfPrimeCallbackHandler(keypairs));
            wss4jMap.put(WSHandlerConstants.SIGNATURE_USER, wss4jSignatureUsername);
            wss4jMap.put(WSHandlerConstants.SIG_PROP_FILE, StringUtils.replace(wss4jSignaturePropsFile, "\\", "/"));
            wss4jMap.put(WSHandlerConstants.SIGNATURE_PARTS, "{}{http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-wssecurity-utility-1.0.xsd}Timestamp;{}{}Body;");

            wss4jMap.put(WSHandlerConstants.ENCRYPTION_USER, wss4jEncryptUsername);
            wss4jMap.put(WSHandlerConstants.ENC_PROP_FILE, StringUtils.replace(wss4jEncryptPropsFile, "\\", "/"));
            wss4jMap.put(WSHandlerConstants.DEC_PROP_FILE, StringUtils.replace(wss4jEncryptPropsFile, "\\", "/"));
            wss4jMap.put(WSHandlerConstants.ENCRYPTION_PARTS, "{}{}Body;");


            cxfEndPoint.getInInterceptors().add(new WSS4JInInterceptor(wss4jMap));
            cxfEndPoint.getInInterceptors().add(new DefaultCryptoCoverageChecker());
            cxfEndPoint.getOutInterceptors().add(new WSS4JOutInterceptor(wss4jMap));
        }

        log.info("wss4jMap {}", wss4jMap);

        cxfEndPoint.getInInterceptors().add(  new LoggingInInterceptor());
        cxfEndPoint.getOutInterceptors().add( new LoggingOutInterceptor());

        return endpoint;
    }



//    @Bean
//    public HelloWorldService helloWorldService() {
//
//        return new HelloWorldServiceImpl();
//    }

//    @Bean
//    public Wss4jSecurityInterceptor securityInterceptor() throws Exception {
//
//        Wss4jSecurityInterceptor securityInterceptor = new Wss4jSecurityInterceptor();
//
//        // validate incoming request
//        securityInterceptor.setValidationActions("Timestamp Signature Encrypt");
//        securityInterceptor.setValidationSignatureCrypto(getCryptoFactoryBean().getObject());
//        securityInterceptor.setValidationDecryptionCrypto(getCryptoFactoryBean().getObject());
//        securityInterceptor.setValidationCallbackHandler(cxfPrimeCallbackHandler());
//
//        // encrypt the response
//        securityInterceptor.setSecurementEncryptionUser("client-public");
//        securityInterceptor.setSecurementEncryptionParts("{Content}{https://memorynotfound.com/beer}getBeerResponse");
//        securityInterceptor.setSecurementEncryptionCrypto(getCryptoFactoryBean().getObject());
//
//        // sign the response
//        securityInterceptor.setSecurementActions("Signature Encrypt");
//        securityInterceptor.setSecurementUsername("server");
//        securityInterceptor.setSecurementPassword("Passw0rd!");
//        securityInterceptor.setSecurementSignatureCrypto(getCryptoFactoryBean().getObject());
//
//        return securityInterceptor;
//    }
//
//
//    @Bean
//    public CryptoFactoryBean getCryptoFactoryBean() throws IOException {
//        CryptoFactoryBean cryptoFactoryBean = new CryptoFactoryBean();
//        cryptoFactoryBean.setKeyStorePassword("Passw0rd!");
//        cryptoFactoryBean.setKeyStoreLocation(new FileSystemResource("D:\\development\\workspaces\\antoniocaccamo\\cxf-prime\\certificates\\server\\cxf-prime-server.keystore"));
//        return cryptoFactoryBean;
//    }
//
//    @Bean
//    public KeyStoreCallbackHandler cxfPrimeCallbackHandler() {
//
//        KeyStoreCallbackHandler callbackHandler = new KeyStoreCallbackHandler();
//        callbackHandler.setPrivateKeyPassword("Passw0rd!");
//        return callbackHandler;
//    }

//    private void wss4j(Endpoint endpoint) throws Exception {
//        if ( wss4jEnabled ) {
//
//
//
//            CxfPrimeCallbackHandler callbackHandler = new CxfPrimeCallbackHandler(keypairs);
//
//            Map<String, Object> wss4jMap = new HashMap<>();
//
//            endpoint.getServer().getEndpoint().put(SecurityConstants.CALLBACK_HANDLER, callbackHandler);
//
//            endpoint.getServer().getEndpoint().put(SecurityConstants.SIGNATURE_USERNAME  , wss4jSignatureUsername);
//
//            Properties signatureProps = new Properties();
//            signatureProps.load( Thread.currentThread().getContextClassLoader().getResourceAsStream(wss4jSignaturePropsFile));
//
//
//            endpoint.getServer().getEndpoint().put(SecurityConstants.ENCRYPT_USERNAME    , wss4jEncryptUsername);
//
//            Properties encryptProps = new Properties();
//            encryptProps.load( Thread.currentThread().getContextClassLoader().getResourceAsStream(wss4jEncryptPropsFile));
//
//
//            endpoint.getI.put("ws-security.signature.properties", signatureProps);
//            endpoint.getServer().getEndpoint().put("ws-security.encryption.properties", encryptProps);
//
//
//        }
//
//        log.info("endpoint.getProperties() : {}", endpoint.getProperties());
//    }
}
