package me.antoniocaccamo.cxf.prime.interceptor;


import org.apache.cxf.phase.Phase;
import org.apache.cxf.ws.policy.PolicyOutInterceptor;

/**
 * Created by antonio on 20/06/2019.
 */
public class DynamicOutPolicyInterceptor extends CxfPrimeAbstractPolicyInterceptor {

    public DynamicOutPolicyInterceptor(String policyFile) {
        super(Phase.SETUP, policyFile);
        getBefore().add(PolicyOutInterceptor.class.getName());
    }

}
