package uk.gov.gds.locate.api.frontend.authentication;

import com.google.common.base.Optional;
import com.yammer.dropwizard.auth.AuthenticationException;
import com.yammer.dropwizard.auth.Authenticator;
import com.yammer.dropwizard.auth.basic.BasicCredentials;

public class BasicAuthAuthenticator implements Authenticator<BasicCredentials, String> {

    public Optional<String> authenticate(BasicCredentials creds) throws AuthenticationException {
        return Optional.of("martyn");
    }
}
