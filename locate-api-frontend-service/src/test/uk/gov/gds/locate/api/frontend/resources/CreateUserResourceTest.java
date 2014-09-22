package uk.gov.gds.locate.api.frontend.resources;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Optional;
import com.mongodb.MongoException;
import com.sun.jersey.api.client.UniformInterfaceException;
import com.yammer.dropwizard.auth.AuthenticationException;
import com.yammer.dropwizard.auth.Authenticator;
import com.yammer.dropwizard.auth.basic.BasicAuthProvider;
import com.yammer.dropwizard.auth.basic.BasicCredentials;
import com.yammer.dropwizard.testing.ResourceTest;
import org.apache.commons.codec.binary.Base64;
import org.junit.Test;
import uk.gov.gds.locate.api.frontend.LocateExceptionMapper;
import uk.gov.gds.locate.api.frontend.authentication.BasicAuthAuthenticator;
import uk.gov.gds.locate.api.frontend.configuration.LocateApiFrontendConfiguration;
import uk.gov.gds.locate.api.frontend.dao.AuthorizationTokenDao;
import uk.gov.gds.locate.api.frontend.model.AuthorizationToken;
import uk.gov.gds.locate.api.frontend.model.CreateUserRequest;
import uk.gov.gds.locate.api.frontend.services.BearerTokenGenerationService;

import static org.fest.assertions.api.Assertions.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

public class CreateUserResourceTest extends ResourceTest {

    private LocateApiFrontendConfiguration configuration = mock(LocateApiFrontendConfiguration.class);
    private AuthorizationTokenDao dao = mock(AuthorizationTokenDao.class);
    private BearerTokenGenerationService bearerTokenGenerationService = mock(BearerTokenGenerationService.class);
    private String validBasicAuth = Base64.encodeBase64URLSafeString("username:password".getBytes());
    private String invalidBasicAuth = Base64.encodeBase64URLSafeString("bogus:fake".getBytes());
    private String validBasicAuthHeader = "Basic " + validBasicAuth;
    private String invalidBasicAuthHeader = "Basic " + invalidBasicAuth;

    private ObjectMapper mapper = new ObjectMapper();

    @Test
    public void shouldRejectRequestForCreateUserPageIfNoAuthSet() {
        try {
            client().resource("/locate/create-user").header("Content-type", "application/json").get(Object.class);
        } catch (UniformInterfaceException e) {
            assertThat(e.getResponse().getStatus()).isEqualTo(401);
        }
    }

    @Test
    public void shouldRejectJsonSubmissionToRequestForCreateUserPageIfNoAuthSet() {
        try {
            client().resource("/locate/create-user").header("Content-type", "application/json").post(Object.class);
        } catch (UniformInterfaceException e) {
            assertThat(e.getResponse().getStatus()).isEqualTo(401);
        }
    }

    @Test
    public void shouldRejectFromSubmissionToRequestForCreateUserPageIfNoAuthSet() {
        try {
            client().resource("/locate/create-user").header("Content-type", "application/x-www-form-urlencoded").post(Object.class);
        } catch (UniformInterfaceException e) {
            assertThat(e.getResponse().getStatus()).isEqualTo(401);
        }
    }

    @Test
    public void shouldRejectRequestForCreateUserPageIfInvalidAuthSet() {
        try {
            client().resource("/locate/create-user").header("Authorization", invalidBasicAuthHeader).header("Content-type", "application/json").get(Object.class);
        } catch (UniformInterfaceException e) {
            assertThat(e.getResponse().getStatus()).isEqualTo(401);
        }
    }

    @Test
    public void shouldRejectJsonSubmissionToRequestForCreateUserPageIfInvalidAuthSet() {
        try {
            client().resource("/locate/create-user").header("Authorization", invalidBasicAuthHeader).header("Content-type", "application/json").post(Object.class);
        } catch (UniformInterfaceException e) {
            assertThat(e.getResponse().getStatus()).isEqualTo(401);
        }
    }

    @Test
    public void shouldRejectFromSubmissionToRequestForCreateUserPageIfInvalidAuthSet() {
        try {
            client().resource("/locate/create-user").header("Authorization", invalidBasicAuthHeader).header("Content-type", "application/x-www-form-urlencoded").post(Object.class);
        } catch (UniformInterfaceException e) {
            assertThat(e.getResponse().getStatus()).isEqualTo(401);
        }
    }

    @Test
    public void shouldCreateAUserAccountForAValidRequest() throws JsonProcessingException {
        when(configuration.getUsername()).thenReturn("username");
        when(configuration.getPassword()).thenReturn("password");
        when(bearerTokenGenerationService.newToken()).thenReturn("this is a token");
        when(dao.create(any(AuthorizationToken.class))).thenReturn(true);
        CreateUserRequest request = new CreateUserRequest("name", "real@email.gov.uk", "org");

        String jsonRequest = mapper.writeValueAsString(request);
        String response = client().resource("/locate/create-user").header("Authorization", validBasicAuthHeader).header("Content-type", "application/json").post(String.class, jsonRequest);
        assertThat(response).contains("\"identifier\":\"real@email.gov.uk\"");
        assertThat(response).contains("\"token\":\"this is a token\"");
        verify(dao, times(1)).create(any(AuthorizationToken.class));
    }

    @Test
    public void shouldRejectAnInvalidCreateRequest() throws JsonProcessingException {
        when(dao.create(any(AuthorizationToken.class))).thenReturn(true);
        CreateUserRequest request = new CreateUserRequest("", "real@email.gov.uk", "org");

        String jsonRequest = mapper.writeValueAsString(request);
        try {
            client().resource("/locate/create-user").header("Authorization", validBasicAuthHeader).header("Content-type", "application/json").post(String.class, jsonRequest);
            fail("Should have thrown exception");
        } catch (UniformInterfaceException e) {
            verify(dao, times(0)).create(any(AuthorizationToken.class));
            assertThat(e.getResponse().getStatus()).isEqualTo(422);
            assertThat(e.getResponse().getEntity(String.class)).isEqualTo("[\"Name must be present and shorter than 255 letters\"]");
        }
    }

    @Test
    public void shouldRejectARequestThatHasADuplicateIdentifier() throws JsonProcessingException {
        when(dao.create(any(AuthorizationToken.class))).thenThrow(new MongoException("authorizationToken.$identifier_index  dup key"));
        CreateUserRequest request = new CreateUserRequest("name", "real@email.gov.uk", "org");

        String jsonRequest = mapper.writeValueAsString(request);
        try {
            client().resource("/locate/create-user").header("Authorization", validBasicAuthHeader).header("Content-type", "application/json").post(String.class, jsonRequest);
            fail("Should have thrown exception");
        } catch (UniformInterfaceException e) {
            verify(dao, times(1)).create(any(AuthorizationToken.class));
            assertThat(e.getResponse().getStatus()).isEqualTo(422);
            assertThat(e.getResponse().getEntity(String.class)).isEqualTo("[\"These details have been previously used\"]");
        }
    }


    @Override
    protected void setUpResources() throws Exception {
        addResource(new CreateUserResource(dao, bearerTokenGenerationService));
        addProvider(new BasicAuthProvider(new TestBasicAuthAuthenticator(), "test"));
        addProvider(new LocateExceptionMapper());
    }


    protected class TestBasicAuthAuthenticator implements Authenticator<BasicCredentials, String> {

        public Optional<String> authenticate(BasicCredentials credentials) throws AuthenticationException {

            if ("username".equalsIgnoreCase(credentials.getUsername())) {
                return Optional.of(credentials.getUsername());
            } else {
                return Optional.absent();
            }
        }
    }
}