package uk.gov.gds.locate.api.frontend.validation;

import com.google.common.base.Strings;
import org.apache.commons.validator.routines.EmailValidator;
import uk.gov.gds.locate.api.frontend.model.CreateUserRequest;

import java.util.ArrayList;
import java.util.List;

public abstract class ValidationCreateUserRequest {

    public static final int MAX_LENGTH = 255;
    public static final String NAME_ERROR = "Name must be present and shorter than 255 letters";
    public static final String ORGANISATION_ERROR = "Organisation must be present and shorter than 255 letters";
    public static final String EMAIL_ERROR = "Email must be a valid government address";

    public static List<String> validateRequest(CreateUserRequest request) {
        List<String> errors = new ArrayList<String>();

        if (Strings.isNullOrEmpty(request.getName()) || request.getName().length() > MAX_LENGTH) {
            errors.add(NAME_ERROR);
        }

        if (!EmailValidator.getInstance().isValid(request.getEmail()) || !request.getEmail().endsWith(".gov.uk") || request.getEmail().length() > MAX_LENGTH) {
            errors.add(EMAIL_ERROR);
        }

        if (Strings.isNullOrEmpty(request.getOrganisation()) || request.getOrganisation().length() > MAX_LENGTH) {
            errors.add(ORGANISATION_ERROR);
        }

        return errors;
    }

}
