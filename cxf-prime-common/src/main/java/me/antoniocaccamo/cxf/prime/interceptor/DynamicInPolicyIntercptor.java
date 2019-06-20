package me.antoniocaccamo.cxf.prime.interceptor;


import org.apache.cxf.phase.Phase;
import org.apache.cxf.ws.policy.PolicyInInterceptor;

/**
 * Created by antonio on 20/06/2019.
 */
public class DynamicInPolicyIntercptor  extends CxfPrimeAbstractPolicyInterceptor{


    public DynamicInPolicyIntercptor(String policyFile) {
        super(Phase.RECEIVE);
        getBefore().add(PolicyInInterceptor.class.getName());
        this.policyFile = policyFile;
    }


}
