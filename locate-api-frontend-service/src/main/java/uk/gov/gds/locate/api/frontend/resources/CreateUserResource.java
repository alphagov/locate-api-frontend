package uk.gov.gds.locate.api.frontend.resources;

import com.google.common.collect.ImmutableList;
import com.mongodb.MongoException;
import com.yammer.dropwizard.auth.Auth;
import com.yammer.metrics.annotation.Timed;
import uk.gov.gds.locate.api.frontend.dao.AuthorizationTokenDao;
import uk.gov.gds.locate.api.frontend.exceptions.LocateWebException;
import uk.gov.gds.locate.api.frontend.model.AuthorizationToken;
import uk.gov.gds.locate.api.frontend.model.CreateUserRequest;
import uk.gov.gds.locate.api.frontend.model.DataType;
import uk.gov.gds.locate.api.frontend.model.QueryType;
import uk.gov.gds.locate.api.frontend.services.BearerTokenGenerationService;
import uk.gov.gds.locate.api.frontend.views.CompleteView;
import uk.gov.gds.locate.api.frontend.views.CreateUserView;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.List;

import static uk.gov.gds.locate.api.frontend.validation.ValidationCreateUserRequest.validateRequest;

@Path("/locate/create-user")
public class CreateUserResource {

    private final AuthorizationTokenDao authorizationTokenDao;
    private final BearerTokenGenerationService bearerTokenGenerationService;

    public CreateUserResource(AuthorizationTokenDao authorizationTokenDao, BearerTokenGenerationService bearerTokenGenerationService) {
        this.authorizationTokenDao = authorizationTokenDao;
        this.bearerTokenGenerationService = bearerTokenGenerationService;
    }

    @GET
    @Timed
    @Produces(MediaType.TEXT_HTML)
    public CreateUserView createUser(@Auth String user) {
        return new CreateUserView();
    }

    @POST
    @Timed
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public AuthorizationToken createUserFromJson(@Auth String user, CreateUserRequest request) throws Exception {

        List<String> errors = validateRequest(request);

        if (!errors.isEmpty()) {
            throw new LocateWebException(422, errors);
        }

        AuthorizationToken token = new AuthorizationToken(
                org.bson.types.ObjectId.get().toString(),
                request.getName(),
                request.getEmail(),
                request.getOrganisation(),
                bearerTokenGenerationService.newToken(),
                QueryType.parse(request.getQueryType()),
                DataType.parse(request.getDataType())
        );

        createUser(token);
        return token;
    }

    @POST
    @Timed
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    @Produces(MediaType.TEXT_HTML)
    public CompleteView createUserFromForm(
            @Auth String user,
            @FormParam("email") String email,
            @FormParam("name") String name,
            @FormParam("organisation") String organisation,
            @FormParam("queryType") String queryType,
            @FormParam("dataType") String dataType
    ) throws Exception {
        CreateUserRequest request = new CreateUserRequest(name, email, organisation, queryType, dataType);

        List<String> errors = validateRequest(request);

        if (errors.size() == 0) {
            AuthorizationToken token = new AuthorizationToken(
                    org.bson.types.ObjectId.get().toString(),
                    request.getName(),
                    request.getEmail(),
                    request.getOrganisation(),
                    bearerTokenGenerationService.newToken(),
                    QueryType.parse(request.getQueryType()),
                    DataType.parse(request.getDataType())
            );
            createUser(token);
            return new CompleteView(token, errors);
        }
        return new CompleteView(new AuthorizationToken(), errors);
    }

    private void createUser(AuthorizationToken token) throws LocateWebException {
        try {
            authorizationTokenDao.create(token);
        } catch (MongoException exception) {
            if (exception.getMessage().contains("authorizationToken.$identifier_index  dup key"))
                throw new LocateWebException(422, ImmutableList.of("These details have been previously used"), exception);
            else
                throw exception;
        }
    }

}
