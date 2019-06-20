package me.antoniocaccamo.cxf.prime.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.apache.cxf.message.Message;
import org.apache.neethi.Policy;
import org.apache.neethi.PolicyBuilder;

import java.io.InputStream;

/**
 * Created by antonio on 20/06/2019.
 */
@Slf4j
public class CxfPrimePolicyHelper {

    public static Policy parsePolicy(Message message,  String policyFile) {

        Policy policy = null;

        try {
            InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream(policyFile);

            org.apache.cxf.ws.policy.PolicyBuilder builder = message.getExchange()
                    .getBus()
                    .getExtension(org.apache.cxf.ws.policy.PolicyBuilder.class);
            policy = builder.getPolicy(is);
            return policy;
        } catch (Throwable t ){
            log.error("error occurred ", t);
            throw new RuntimeException(t);
        }

    }
}
