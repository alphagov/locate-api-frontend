package uk.gov.gds.locate.api.frontend.resources;

import com.yammer.dropwizard.auth.Auth;
import com.yammer.metrics.annotation.Timed;
import uk.gov.gds.locate.api.frontend.views.CreateUserView;
import uk.gov.gds.locate.api.frontend.views.ExplorerView;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Path("/locate/explorer")
public class ExplorerResource {

    @GET
    @Timed
    @Produces(MediaType.TEXT_HTML)
    public ExplorerView explore(@Auth String user) {
        return new ExplorerView();
    }

}
