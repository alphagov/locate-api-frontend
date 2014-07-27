package uk.gov.gds.locate.api.frontend.model;


public enum QueryType {
    RESIDENTIAL("residential"),
    COMMERCIAL("commercial"),
    RESIDENTIAL_AND_COMMERCIAL("residentialAndCommercial"),
    ALL("all");

    private String type;

    private QueryType(String type) {
        this.type = type;
    }

    public static QueryType parse(String value) throws IllegalArgumentException {
        for (QueryType queryType : QueryType.values()) {
            if (queryType.getType().equals(value)) {
                return queryType;
            }
        }
        throw new IllegalArgumentException(String.format("No QueryType with value '%s'", value));
    }

    public String getType() {
        return this.type;
    }
}
