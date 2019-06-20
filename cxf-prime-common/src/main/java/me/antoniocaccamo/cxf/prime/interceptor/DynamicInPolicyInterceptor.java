package me.antoniocaccamo.cxf.prime.interceptor;


import org.apache.cxf.phase.Phase;
import org.apache.cxf.ws.policy.PolicyInInterceptor;

/**
 * Created by antonio on 20/06/2019.
 */
public class DynamicInPolicyInterceptor extends CxfPrimeAbstractPolicyInterceptor{


    public DynamicInPolicyInterceptor(String policyFile) {
        super(Phase.RECEIVE, policyFile);
        getBefore().add(PolicyInInterceptor.class.getName());
    }


}
