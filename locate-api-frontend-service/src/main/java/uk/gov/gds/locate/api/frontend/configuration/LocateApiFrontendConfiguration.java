package uk.gov.gds.locate.api.frontend.configuration;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.yammer.dropwizard.config.Configuration;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@JsonIgnoreProperties(ignoreUnknown = true)
public class LocateApiFrontendConfiguration extends Configuration {

    @Valid
    @NotNull
    @JsonProperty
    private MongoConfiguration mongoConfiguration = new MongoConfiguration();

    @Valid
    @NotNull
    @JsonProperty
    private String username;

    @Valid
    @NotNull
    @JsonProperty
    private String password;

    public MongoConfiguration getMongoConfiguration() {
        return mongoConfiguration;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
