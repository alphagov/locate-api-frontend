package uk.gov.gds.locate.api.frontend.views;

import com.yammer.dropwizard.views.View;

public class ExplorerView extends View {
    public ExplorerView() {
        super("/assets/views/explorer.ftl");
    }
}
