package me.antoniocaccamo.cxf.prime.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.apache.cxf.message.Message;
import org.apache.cxf.ws.policy.AbstractPolicyInterceptor;
import org.apache.cxf.ws.policy.PolicyConstants;
import org.apache.cxf.ws.policy.PolicyException;
import org.apache.neethi.Policy;

/**
 * Created by antonio on 20/06/2019.
 */
@Slf4j
public abstract class CxfPrimeAbstractPolicyInterceptor extends AbstractPolicyInterceptor {

    protected  String policyFile ;

    public CxfPrimeAbstractPolicyInterceptor(String phase, String policyFile) {
        super(phase);
        this.policyFile = policyFile;
    }

    @Override
    protected void handle(Message message) throws PolicyException {
        Policy wsaPolicy = CxfPrimePolicyHelper.parsePolicy(message, policyFile);
        message.put(PolicyConstants.POLICY_OVERRIDE, wsaPolicy);

    }
}
