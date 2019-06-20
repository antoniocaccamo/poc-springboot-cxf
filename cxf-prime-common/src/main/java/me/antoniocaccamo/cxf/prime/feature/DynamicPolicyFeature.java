package me.antoniocaccamo.cxf.prime.feature;

import me.antoniocaccamo.cxf.prime.interceptor.DynamicInPolicyIntercptor;
import me.antoniocaccamo.cxf.prime.interceptor.DynamicOutPolicyIntercptor;
import org.apache.cxf.Bus;
import org.apache.cxf.feature.AbstractFeature;
import org.apache.cxf.interceptor.InterceptorProvider;

/**
 * Created by antonio on 20/06/2019.
 */
public class DynamicPolicyFeature extends AbstractFeature {

    private final DynamicInPolicyIntercptor  in;
    private final DynamicOutPolicyIntercptor out;

    public DynamicPolicyFeature(String policyFile){
        in  = new DynamicInPolicyIntercptor(policyFile);
        out = new DynamicOutPolicyIntercptor(policyFile);
    }

    @Override
    protected void initializeProvider(InterceptorProvider provider, Bus bus) {
        provider.getInInterceptors().add(in);
        provider.getInFaultInterceptors().add(in);

        provider.getOutInterceptors().add(out);
        provider.getOutFaultInterceptors().add(out);
    }
}
