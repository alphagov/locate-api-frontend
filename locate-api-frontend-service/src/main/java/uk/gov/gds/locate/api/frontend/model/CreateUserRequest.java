package uk.gov.gds.locate.api.frontend.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class CreateUserRequest {

    @JsonProperty("name")
    private String name;
    @JsonProperty("email")
    private String email;
    @JsonProperty("organisation")
    private String organisation;

    public CreateUserRequest() {
    }

    public CreateUserRequest(String name, String email, String organisation) {
        this.name = name;
        this.email = email;
        this.organisation = organisation;
    }

    public String getName() {
        return name;
    }


    public String getEmail() {
        return email;
    }


    public String getOrganisation() {
        return organisation;
    }

}
