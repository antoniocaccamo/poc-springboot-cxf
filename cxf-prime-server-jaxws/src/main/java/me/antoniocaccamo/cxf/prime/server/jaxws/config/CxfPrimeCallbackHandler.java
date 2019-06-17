package me.antoniocaccamo.cxf.prime.server.jaxws.config;

import org.apache.wss4j.common.ext.WSPasswordCallback;
import org.springframework.ws.soap.security.wss4j2.callback.KeyStoreCallbackHandler;

import javax.security.auth.callback.UnsupportedCallbackException;
import java.io.IOException;
import java.security.KeyPair;
import java.util.List;
import java.util.Map;

/**
 * Created by antonio on 18/06/2019.
 */

public class CxfPrimeCallbackHandler extends KeyStoreCallbackHandler {

    private final Map<String, String> aliasPassords;

    public CxfPrimeCallbackHandler(String s)

    public CxfPrimeCallbackHandler(Map<String, String> aliasPassords;) {
        this.aliasPassords = aliasPassords;
    }


    @Override
    protected void handleSignature(WSPasswordCallback callback) throws IOException, UnsupportedCallbackException {

        super.handleSignature(callback);
    }
}
