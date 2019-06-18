package me.antoniocaccamo.cxf.prime.callback;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.wss4j.common.ext.WSPasswordCallback;

import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.UnsupportedCallbackException;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by antonio on 18/06/2019.
 */

@Slf4j
public class CxfPrimeCallbackHandler implements CallbackHandler {

    private final Map<String, String> aliasPassords = new HashMap<>();


    public  static final String KEY_PAIRS_DIVIDER = "|";
    public  static final String KEY_PAIR_DIVIDER = ",";

    public CxfPrimeCallbackHandler(Map<String, String> aliasPassords) {
        this.aliasPassords.putAll(aliasPassords);
    }

    public CxfPrimeCallbackHandler(String keypairs) {

        for ( String keypair : StringUtils.split(keypairs, KEY_PAIRS_DIVIDER)) {
            String[] split =    StringUtils.split(keypair, KEY_PAIR_DIVIDER);
            aliasPassords.put(StringUtils.trim(split[0]), StringUtils.trim(split[1]));
        }
        log.info("password setted for alias {}", aliasPassords.keySet() );
    }

    @Override
    public void handle(Callback[] callbacks) throws IOException, UnsupportedCallbackException {

        for (Callback callback : callbacks) {
            WSPasswordCallback  pc = (WSPasswordCallback) callback;

            String usage = null;

            switch (pc.getUsage()) {
                case WSPasswordCallback.SIGNATURE :
                    usage = "WSPasswordCallback.SIGNATURE";
                    break;
                case WSPasswordCallback.DECRYPT :
                    usage = "WSPasswordCallback.DECRYPT";
            }

            String pass = aliasPassords.get(pc.getIdentifier());
            if (pass != null) {
                pc.setPassword(pass);
                log.warn("password requested for alias {} for usage {} ", pc.getIdentifier() , usage );
                return;
            }
            log.warn("password requested for alias {} for usage {} but not found ..", pc.getIdentifier() , usage );
        }
    }
}
