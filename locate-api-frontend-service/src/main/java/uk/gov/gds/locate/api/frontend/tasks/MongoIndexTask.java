package uk.gov.gds.locate.api.frontend.tasks;

import com.google.common.collect.ImmutableMultimap;
import com.yammer.dropwizard.tasks.Task;
import com.yammer.metrics.annotation.Timed;
import uk.gov.gds.locate.api.frontend.dao.AuthorizationTokenDao;

import java.io.PrintWriter;

public class MongoIndexTask extends Task {

    private final AuthorizationTokenDao authorizationTokenDao;

    public MongoIndexTask(AuthorizationTokenDao authorizationTokenDao) {
        super("mongo-index");
        this.authorizationTokenDao = authorizationTokenDao;
    }

    @Override
    @Timed
    public void execute(ImmutableMultimap<String, String> parameters, PrintWriter output) throws Exception {
        authorizationTokenDao.applyIndexes();
    }
}
