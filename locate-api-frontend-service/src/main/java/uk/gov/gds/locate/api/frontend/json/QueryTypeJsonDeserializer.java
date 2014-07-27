package uk.gov.gds.locate.api.frontend.json;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import uk.gov.gds.locate.api.frontend.model.QueryType;

import java.io.IOException;

public class QueryTypeJsonDeserializer extends JsonDeserializer<QueryType> {

    @Override
    public QueryType deserialize(JsonParser parser, DeserializationContext context) throws IOException {
        String json = parser.getText();
        return QueryType.parse(json);
    }
}