package uk.gov.gds.locate.api.frontend;

import com.mongodb.DB;
import com.mongodb.MongoClient;
import com.sun.jersey.api.core.ResourceConfig;
import com.yammer.dropwizard.Service;
import com.yammer.dropwizard.assets.AssetsBundle;
import com.yammer.dropwizard.auth.basic.BasicAuthProvider;
import com.yammer.dropwizard.config.Bootstrap;
import com.yammer.dropwizard.config.Environment;
import com.yammer.dropwizard.views.ViewBundle;
import org.mongojack.JacksonDBCollection;
import uk.gov.gds.locate.api.frontend.authentication.BasicAuthAuthenticator;
import uk.gov.gds.locate.api.frontend.configuration.LocateApiFrontendConfiguration;
import uk.gov.gds.locate.api.frontend.configuration.MongoConfiguration;
import uk.gov.gds.locate.api.frontend.dao.AuthorizationTokenDao;
import uk.gov.gds.locate.api.frontend.healthchecks.MongoHealthCheck;
import uk.gov.gds.locate.api.frontend.managed.ManagedMongo;
import uk.gov.gds.locate.api.frontend.model.AuthorizationToken;
import uk.gov.gds.locate.api.frontend.resources.CreateUserResource;
import uk.gov.gds.locate.api.frontend.services.BearerTokenGenerationService;
import uk.gov.gds.locate.api.frontend.tasks.MongoIndexTask;

import javax.ws.rs.ext.ExceptionMapper;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static uk.gov.gds.locate.api.frontend.formatters.DateTimeFormatters.internalDateFormatter;

public class LocateApiFrontendService extends Service<LocateApiFrontendConfiguration> {

    public static void main(String[] args) throws Exception {
        new LocateApiFrontendService().run(args);
    }

    @Override
    public void initialize(Bootstrap<LocateApiFrontendConfiguration> bootstrap) {
        bootstrap.addBundle(new ViewBundle());
        bootstrap.addBundle(new AssetsBundle("/assets/stylesheets", "/stylesheets"));
        bootstrap.addBundle(new AssetsBundle("/assets/javascripts", "/javascripts"));
        bootstrap.addBundle(new AssetsBundle("/assets/images", "/images"));
    }

    @Override
    public void run(LocateApiFrontendConfiguration configuration, Environment environment) throws Exception {

        /**
         * Mongo set up
         */
        MongoClient mongoClient = configureMongoClient(environment, configuration.getMongoConfiguration());

        DB credentialsDb = setUpDb(configuration.getMongoConfiguration().getCredentialsDatabase(), configuration.getMongoConfiguration(), mongoClient);

        /**
         * Dao layer
         */
        final AuthorizationTokenDao authorizationTokenDao = configureAuthorizationTokenDao(credentialsDb);

        /**
         * Resources
         */
        environment.addResource(new CreateUserResource(authorizationTokenDao, new BearerTokenGenerationService()));

        /**
         * Healthchecks
         */
        environment.addHealthCheck(new MongoHealthCheck(mongoClient));

        /**
         * Exception mapper
         */
        environment.addProvider(new LocateExceptionMapper());

        /**
         * Authentication
         */
        environment.addProvider(new BasicAuthProvider(new BasicAuthAuthenticator(), "create-user"));

        /**
         * Better exception mappings
         */
        removeDefaultExceptionMappers(environment);

        /**
         * Date serialisation
         */
        environment.getObjectMapperFactory().setDateFormat(internalDateFormatter);

        /**
         * Tasks
         */
        environment.addTask(new MongoIndexTask(authorizationTokenDao));

    }

    private MongoClient configureMongoClient(Environment environment, MongoConfiguration config) throws UnknownHostException {
        MongoClient mongoClient = new MongoClient(config.getHosts(), config.getPort());
        environment.manage(new ManagedMongo(mongoClient));

        return mongoClient;
    }


    private DB setUpDb(String database, MongoConfiguration config, MongoClient mongoClient) {
        DB db = mongoClient.getDB(database);

        if (config.requiresAuth()) {
            db.authenticate(config.getUsername(), config.getPassword().toCharArray());
        }

        return db;
    }

    private AuthorizationTokenDao configureAuthorizationTokenDao(DB db) {
        return new AuthorizationTokenDao(JacksonDBCollection.wrap(db.getCollection("authorizationToken"), AuthorizationToken.class, String.class));
    }

    private void removeDefaultExceptionMappers(Environment environment) {
        List<Object> singletonsToRemove = new ArrayList<Object>();
        ResourceConfig jrConfig = environment.getJerseyResourceConfig();
        Set<Object> dwSingletons = jrConfig.getSingletons();

        for (Object s : dwSingletons) {
            if (s instanceof ExceptionMapper && s.getClass().getName().startsWith("com.yammer.dropwizard.jersey.")) {
                singletonsToRemove.add(s);
            }
        }
        for (Object s : singletonsToRemove) {
            jrConfig.getSingletons().remove(s);
        }

    }
}