package me.antoniocaccamo.cxf.prime.interceptor;


import org.apache.cxf.phase.Phase;
import org.apache.cxf.ws.policy.*;

/**
 * Created by antonio on 20/06/2019.
 */
public class DynamicOutPolicyIntercptor extends CxfPrimeAbstractPolicyInterceptor {

    private final String policyFile;

    public DynamicOutPolicyIntercptor(String policyFile) {
        super(Phase.SETUP);
        getBefore().add(PolicyOutInterceptor.class.getName());
        this.policyFile = policyFile;
    }

}
