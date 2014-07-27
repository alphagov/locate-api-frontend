package uk.gov.gds.locate.api.frontend.authentication;

import com.google.common.base.Optional;
import com.yammer.dropwizard.auth.AuthenticationException;
import com.yammer.dropwizard.auth.Authenticator;
import com.yammer.dropwizard.auth.basic.BasicCredentials;
import uk.gov.gds.locate.api.frontend.configuration.LocateApiFrontendConfiguration;

public class BasicAuthAuthenticator implements Authenticator<BasicCredentials, String> {

    private final LocateApiFrontendConfiguration configuration;

    public BasicAuthAuthenticator(LocateApiFrontendConfiguration configuration) {
        this.configuration = configuration;
    }

    public Optional<String> authenticate(BasicCredentials creds) throws AuthenticationException {
        if(creds.getUsername().equals(configuration.getUsername()) && creds.getPassword().equals(configuration.getPassword())) {
            return Optional.of(creds.getUsername());
        }
        return Optional.absent();
    }
}
