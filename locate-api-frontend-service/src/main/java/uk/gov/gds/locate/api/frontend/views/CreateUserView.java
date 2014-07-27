package uk.gov.gds.locate.api.frontend.views;

import com.yammer.dropwizard.views.View;

public class CreateUserView extends View {
    public CreateUserView() {
        super("/assets/views/createUser.ftl");
    }
}
