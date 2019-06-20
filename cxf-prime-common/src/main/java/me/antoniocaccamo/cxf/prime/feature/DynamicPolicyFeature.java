package me.antoniocaccamo.cxf.prime.feature;

import me.antoniocaccamo.cxf.prime.interceptor.DynamicInPolicyInterceptor;
import me.antoniocaccamo.cxf.prime.interceptor.DynamicOutPolicyInterceptor;
import org.apache.cxf.Bus;
import org.apache.cxf.feature.AbstractFeature;
import org.apache.cxf.interceptor.InterceptorProvider;

/**
 * Created by antonio on 20/06/2019.
 */
public class DynamicPolicyFeature extends AbstractFeature {

    private final DynamicInPolicyInterceptor in;
    private final DynamicOutPolicyInterceptor out;

    public DynamicPolicyFeature(String policyFile){
        in  = new DynamicInPolicyInterceptor(policyFile);
        out = new DynamicOutPolicyInterceptor(policyFile);
    }

    @Override
    protected void initializeProvider(InterceptorProvider provider, Bus bus) {
        provider.getInInterceptors().add(in);
        provider.getInFaultInterceptors().add(in);

        provider.getOutInterceptors().add(out);
        provider.getOutFaultInterceptors().add(out);
    }
}
