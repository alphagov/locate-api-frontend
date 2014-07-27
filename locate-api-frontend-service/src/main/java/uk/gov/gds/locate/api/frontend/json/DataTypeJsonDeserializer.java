package uk.gov.gds.locate.api.frontend.json;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import uk.gov.gds.locate.api.frontend.model.DataType;

import java.io.IOException;

public class DataTypeJsonDeserializer extends JsonDeserializer<DataType> {

    @Override
    public DataType deserialize(JsonParser parser, DeserializationContext context) throws IOException {
        String json = parser.getText();
        return DataType.parse(json);
    }
}