package uk.gov.gds.locate.api.frontend.validation;

import org.junit.Test;
import uk.gov.gds.locate.api.frontend.model.CreateUserRequest;

import java.util.List;

import static org.fest.assertions.api.Assertions.assertThat;
import static uk.gov.gds.locate.api.frontend.validation.ValidationCreateUserRequest.*;

public class ValidationCreateUserRequestTest {

    private static final String TOO_LONG = "0123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789012345678901234567890123456789123456";

    @Test
    public void shouldReturnNoErrorsIfValid() {
        CreateUserRequest request = new CreateUserRequest("name", "email@something.gov.uk", "org");
        List<String> errors = validateRequest(request);
        assertThat(errors.size()).isEqualTo(0);
    }

    @Test
    public void shouldReturnAnErrorIfNoName() {
        CreateUserRequest request = new CreateUserRequest("", "email@something.gov.uk", "org");
        List<String> errors = validateRequest(request);
        assertThat(errors.size()).isEqualTo(1);
        assertThat(errors.get(0)).isEqualTo("Name must be present and shorter than 255 letters");
    }

    @Test
    public void shouldReturnAnErrorIfNameTooLong() {
        CreateUserRequest request = new CreateUserRequest(TOO_LONG, "email@something.gov.uk", "org");
        List<String> errors = validateRequest(request);
        assertThat(errors.size()).isEqualTo(1);
        assertThat(errors.get(0)).isEqualTo("Name must be present and shorter than 255 letters");
    }


    @Test
    public void shouldReturnAnErrorIfNotValidGvtEmail() {
        CreateUserRequest request = new CreateUserRequest("name", "email@something.co.uk", "org");
        List<String> errors = validateRequest(request);
        assertThat(errors.size()).isEqualTo(1);
        assertThat(errors.get(0)).isEqualTo("Email must be a valid government address");
    }

    @Test
    public void shouldReturnAnErrorIfTooLongValidGvtEmail() {
        CreateUserRequest request = new CreateUserRequest("name", TOO_LONG + "@something.gov.uk", "org");
        List<String> errors = validateRequest(request);
        assertThat(errors.size()).isEqualTo(1);
        assertThat(errors.get(0)).isEqualTo("Email must be a valid government address");
    }

    @Test
    public void shouldReturnAnErrorIfNotValidEmail() {
        CreateUserRequest request = new CreateUserRequest("name", "@.gov.uk", "org");
        List<String> errors = validateRequest(request);
        assertThat(errors.size()).isEqualTo(1);
        assertThat(errors.get(0)).isEqualTo("Email must be a valid government address");
    }

    @Test
    public void shouldReturnAnErrorIfNoOrganisation() {
        CreateUserRequest request = new CreateUserRequest("me", "email@something.gov.uk", "");
        List<String> errors = validateRequest(request);
        assertThat(errors.size()).isEqualTo(1);
        assertThat(errors.get(0)).isEqualTo("Organisation must be present and shorter than 255 letters");
    }

    @Test
    public void shouldReturnAnErrorIfTooLongOrganisation() {
        CreateUserRequest request = new CreateUserRequest("me", "email@something.gov.uk", TOO_LONG);
        List<String> errors = validateRequest(request);
        assertThat(errors.size()).isEqualTo(1);
        assertThat(errors.get(0)).isEqualTo("Organisation must be present and shorter than 255 letters");
    }


}
